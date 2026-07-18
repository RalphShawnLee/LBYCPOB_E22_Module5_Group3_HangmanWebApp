package ph.edu.dlsu.lbycpob.hangman.controller;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual's
// reference implementation. This is the file the manual calls "the heart of the web
// migration" - verify: (1) each route below matches a link/form/button that actually
// exists in play.html/index.html/stats.html, (2) you can trace, by hand, what happens
// to the HttpSession across one full POST /game/guess -> redirect -> GET /game/play
// cycle (this is exactly what guide question 2.10 and the sequence diagram ask you
// to explain), (3) the PRG (Post-Redirect-Get) pattern used here - every POST ends
// in "redirect:..." rather than returning a view name directly.

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ph.edu.dlsu.lbycpob.hangman.model.GameState;
import ph.edu.dlsu.lbycpob.hangman.service.HangmanService;
import ph.edu.dlsu.lbycpob.hangman.statistics.GameStatistics;
import ph.edu.dlsu.lbycpob.hangman.statistics.StatisticsWriter;

/**
 * HTTP controller – the web equivalent of the {@code Hangman.run()} game
 * loop.
 *
 * <p><b>Request flow</b>
 * <ol>
 * <li>{@code GET  /}               – welcome / word-list selection page</li>
 * <li>{@code POST /game/start}     – initialise session; pick first secret word</li>
 * <li>{@code GET  /game/play}      – render current game state (art, hint, keyboard)</li>
 * <li>{@code POST /game/guess}     – process one letter; PRG-redirect back to play</li>
 * <li>{@code POST /game/again}     – keep statistics, start a fresh round</li>
 * <li>{@code GET  /game/stats}     – display session statistics, write to file,
 *                                    invalidate session</li>
 * <li>{@code GET  /game/reset}     – abandon session, return to welcome page</li>
 * </ol>
 */
@Controller
public class GameController {

    private static final String SESSION_KEY = "gameState";

    private final HangmanService hangmanService;
    private final StatisticsWriter statisticsWriter;

    public GameController(HangmanService hangmanService,
                           StatisticsWriter statisticsWriter) {
        this.hangmanService = hangmanService;
        this.statisticsWriter = statisticsWriter;
    }

    // ------------------------------------------------------------------ //
    // Welcome page                                                       //
    // ------------------------------------------------------------------ //

    // UNDERSTAND: Purpose - shows the word-list selection page; the game has not
    //             started yet so no session state exists and nothing needs to be
    //             added to the Model.
    // DECISION: Kept as a one-liner rather than folding it into another method,
    //           since "/" has exactly one job: render a static-ish landing page.
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ------------------------------------------------------------------ //
    // Start a new session                                                //
    // ------------------------------------------------------------------ //

    /**
     * Initializes the session with a fresh {@link GameState} and picks the
     * first secret word. Mirrors the filename-prompt and first
     * {@code getRandomWord} call that opened {@code Hangman.run()}.
     */
    // UNDERSTAND: Purpose - equivalent of the "Enter the word list filename" prompt
    //             plus the first getRandomWord() call at the top of the old run().
    // DECISION: A brand-new GameState is created here (not reused from any prior
    //           session) so every "Play Now" click always starts a clean round,
    //           matching the do-while in the console version always playing at
    //           least one game.
    @PostMapping("/game/start")
    public String startGame(@RequestParam("filename") String filename,
                             HttpSession session) {
        GameState state = new GameState();
        state.setFilename(filename.trim());

        String word = hangmanService.getRandomWord(state.getFilename());
        state.setSecretWord(word);
        state.setGuessesRemaining(HangmanService.MAX_GUESSES);
        state.setMessage("A new word has been chosen. It has "
                + word.length() + " letter(s). Good luck!");

        session.setAttribute(SESSION_KEY, state);
        return "redirect:/game/play";
    }

    // ------------------------------------------------------------------ //
    // Display the current game state                                    //
    // ------------------------------------------------------------------ //

    /**
     * Populates the model for the play template. No state is mutated here
     * (GET should be idempotent); all mutation happens in the POST handlers.
     */
    // UNDERSTAND: Purpose - re-derives everything play.html needs (hint, art,
    //             keyboard alphabet) from the session's GameState on every load.
    // DECISION: Nothing here writes back to the session - GET requests must stay
    //           idempotent (safe to refresh/bookmark), so all state changes are
    //           confined to the POST handlers below.
    @GetMapping("/game/play")
    public String play(HttpSession session, Model model) {
        GameState state = (GameState) session.getAttribute(SESSION_KEY);
        if (state == null) {
            // Session expired or player navigated here directly – send them home.
            return "redirect:/";
        }

        String hint = hangmanService.createHint(state.getSecretWord(), state.getGuessedLetters());
        String displayHint = hangmanService.formatHintForDisplay(hint);
        String art = hangmanService.getHangmanArtAsString(state.getGuessesRemaining());

        model.addAttribute("state", state);
        model.addAttribute("hint", hint);
        model.addAttribute("displayHint", displayHint);
        model.addAttribute("hangmanArt", art);
        model.addAttribute("alphabet", hangmanService.getAlphabet());
        return "play";
    }

    // ------------------------------------------------------------------ //
    // Process one letter guess                                          //
    // ------------------------------------------------------------------ //

    /**
     * The heart of the web migration: everything that used to happen inside
     * the {@code while(true)} loop of {@code Hangman.playOneGame()} now
     * happens in this single POST handler, then the player is redirected
     * back to the GET so the browser's history contains a safe, refreshable
     * URL.
     */
    // UNDERSTAND: Purpose - validates one submitted letter, records it, and updates
    //             guessesRemaining / gameOver / won / statistics accordingly.
    // DECISION: if-else chain (not switch) chosen because the branches are guard
    //           clauses over string/char state, not a single value being matched -
    //           each early "return redirect:/game/play" keeps the rest of the
    //           method free of deep nesting.
    @PostMapping("/game/guess")
    public String guess(@RequestParam("letter") String letterInput,
                         HttpSession session) {

        GameState state = (GameState) session.getAttribute(SESSION_KEY);
        if (state == null || state.isGameOver()) {
            return "redirect:/game/play";
        }

        // --- Input validation (replaces Hangman.readGuess validation) ---
        String cleaned = letterInput.trim().toUpperCase();
        if (cleaned.length() != 1
                || cleaned.charAt(0) < 'A'
                || cleaned.charAt(0) > 'Z') {
            state.setMessage("Please enter a single letter from A to Z.");
            session.setAttribute(SESSION_KEY, state);
            return "redirect:/game/play";
        }

        char letter = cleaned.charAt(0);
        if (state.getGuessedLetters().indexOf(letter) >= 0) {
            state.setMessage("You already guessed \"" + letter
                    + "\". Choose a different letter.");
            session.setAttribute(SESSION_KEY, state);
            return "redirect:/game/play";
        }

        // --- Record the guess ---
        state.setGuessedLetters(state.getGuessedLetters() + letter);

        // --- Evaluate correctness ---
        if (state.getSecretWord().indexOf(letter) >= 0) {
            // Correct guess
            String hint = hangmanService.createHint(
                    state.getSecretWord(), state.getGuessedLetters());

            if (!hint.contains("-")) {
                // All letters revealed – player wins
                state.setGameOver(true);
                state.setWon(true);
                state.setStatistics(
                        state.getStatistics().withGame(true, state.getGuessesRemaining()));
                state.setMessage("You win! The word was \""
                        + state.getSecretWord() + "\". "
                        + state.getGuessesRemaining() + " guess(es) remaining.");
            } else {
                state.setMessage("Correct! \"" + letter + "\" is in the word.");
            }
        } else {
            // Incorrect guess
            state.setGuessesRemaining(state.getGuessesRemaining() - 1);

            if (state.getGuessesRemaining() == 0) {
                // No guesses left – player loses
                state.setGameOver(true);
                state.setWon(false);
                state.setStatistics(state.getStatistics().withGame(false, 0));
                state.setMessage("You lose. The word was \""
                        + state.getSecretWord() + "\".");
            } else {
                state.setMessage("Incorrect! \"" + letter
                        + "\" is not in the word. "
                        + state.getGuessesRemaining() + " guess(es) left.");
            }
        }

        session.setAttribute(SESSION_KEY, state);
        return "redirect:/game/play";
    }

