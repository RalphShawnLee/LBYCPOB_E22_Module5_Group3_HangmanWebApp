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

