package ph.edu.dlsu.lbycpob.hangman.statistics;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: this is a Java record (not the mutable class your Module 4 console
// GameStatistics.java used) - confirm you understand why an immutable snapshot
// plus withGame() fits an HttpSession-stored object better than a class with a
// recordGame() method that mutates in place; this is good guide-question material.

import java.util.Locale;

/**
 * Holds the running statistics for one program session: games played,
 * games won, and the best (highest) number of guesses remaining at the end
 * of any single game.
 */
// UNDERSTAND: Purpose - immutable snapshot of session totals; every "update"
//             returns a brand-new instance instead of mutating this one.
// DECISION: Implemented as a record (not a class with setters) because its
//           entire job is to hold three related values with validation - a
//           record gives that for free (constructor, equals/hashCode, toString,
//           accessors) without hand-writing boilerplate.
public record GameStatistics(int gamesPlayed, int gamesWon, int bestGuessesRemaining) {

    /** Runs automatically every time a GameStatistics is created - checks the numbers make sense. */
    // UNDERSTAND: Purpose - a compact canonical constructor; runs on every
    //             construction path (including withGame() below) to reject
    //             impossible combinations before they are ever stored.
    // DECISION: Three separate if-checks (not one combined boolean) so each
    //           failure produces a specific, readable error message.
    public GameStatistics {
        if (gamesPlayed < 0) {
            throw new IllegalArgumentException("gamesPlayed must be >= 0, got " + gamesPlayed);
        }
        if (gamesWon < 0 || gamesWon > gamesPlayed) {
            throw new IllegalArgumentException(
                    "gamesWon must be between 0 and gamesPlayed (" + gamesPlayed + "), got " + gamesWon);
        }
        if (bestGuessesRemaining < 0) {
            throw new IllegalArgumentException(
                    "bestGuessesRemaining must be >= 0, got " + bestGuessesRemaining);
        }
    }

    /** The statistics for a session in which no games have been played yet. */
    public static GameStatistics empty() {
        return new GameStatistics(0, 0, 0);
    }

    /**
     * Returns a <em>new</em> {@code GameStatistics} reflecting one more
     * completed game. This instance is left unchanged.
     */
    // UNDERSTAND: Purpose - folds one finished game's result into a new totals
    //             snapshot without mutating "this".
    // DECISION: The ternary for newBest treats gamesPlayed == 0 as a special
    //           case (take guessesRemaining as-is) rather than starting
    //           bestGuessesRemaining at 0, since 0 would incorrectly look like
    //           "a game was lost with no guesses left" before any game exists.
    public GameStatistics withGame(boolean won, int guessesRemaining) {
        if (guessesRemaining < 0) {
            throw new IllegalArgumentException("guessesRemaining must be >= 0, got " + guessesRemaining);
        }
        int newBest = (gamesPlayed == 0) ? guessesRemaining : Math.max(bestGuessesRemaining, guessesRemaining);
        return new GameStatistics(gamesPlayed + 1, gamesWon + (won ? 1 : 0), newBest);
    }

    /** Percentage of played games that were won, as a value in [0.0, 100.0]. */
    public double winPercentage() {
        return (gamesPlayed == 0) ? 0.0 : (gamesWon * 100.0) / gamesPlayed;
    }

    /** One-decimal-place formatted win percentage, e.g. {@code "50.0%"}. */
    public String formattedWinPercentage() {
        return String.format(Locale.ROOT, "%.2f%%", winPercentage());
    }
}
