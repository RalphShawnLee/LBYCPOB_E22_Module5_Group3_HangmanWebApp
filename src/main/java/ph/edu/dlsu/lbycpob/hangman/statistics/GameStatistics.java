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


}
