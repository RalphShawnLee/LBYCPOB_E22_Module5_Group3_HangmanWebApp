package ph.edu.dlsu.lbycpob.hangman.model;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual's
// reference implementation. Verify: every field here is actually read by
// GameController and by play.html/stats.html (a stray unused field is a common
// tell that a class was pasted in without being read) - cross-check the field
// list below against play.html's th:text/th:each usages once you write that
// template.

import ph.edu.dlsu.lbycpob.hangman.statistics.GameStatistics;
import java.io.Serializable;

/**
 * Mutable snapshot of one player's session: the current game plus the
 * running statistics across all games played so far.
 *
 * <p>Implements {@link Serializable} so the servlet container can persist
 * the session to disk if it needs to (required by the spec even if the
 * container never actually serialises it in development).
 *
 * <p>UNDERSTAND: every field below is simple state with an obvious purpose
 * (documented per-field), and every getter/setter is a plain accessor with
 * no logic - per the lab's "previously annotated trivial constructs may be
 * omitted" rule, they are not individually re-tagged with UNDERSTAND/DECISION
 * below.
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The word list file currently in use (e.g. {@code "words.txt"}).
     */
    private String filename = "";

    /**
     * Upper-cased secret word for the current round.
     */
    private String secretWord = "";

    /**
     * Concatenated string of every letter the player has guessed so far,
     * in the order they were guessed – mirrors the {@code guessedLetters}
     * local variable from the original {@code playOneGame}.
     */
    private String guessedLetters = "";

    /**
     * How many incorrect guesses the player still has available.
     */
    private int guessesRemaining = 8;

    /**
     * Accumulated statistics across all games in this browser session –
     * the immutable {@link GameStatistics} record is replaced on each
     * game completion via {@link GameStatistics#withGame}.
     */
    private GameStatistics statistics = GameStatistics.empty();

    /**
     * The feedback message shown at the top of the play page.
     */
    private String message = "";

    /**
     * {@code true} once the round has been won or lost.
     */
    private boolean gameOver = false;

    /**
     * {@code true} if the player guessed all letters before running out.
     */
    private boolean won = false;

    // ------------------------------------------------------------------ //
    // Getters and setters                                                //
    // ------------------------------------------------------------------ //

