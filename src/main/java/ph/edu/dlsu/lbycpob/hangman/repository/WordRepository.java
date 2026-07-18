package ph.edu.dlsu.lbycpob.hangman.repository;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: ClasspathWordRepository is the only implementation registered as a
// @Bean in HangmanSpringBootApplication - if you add a FileWordRepository (reading
// from the real filesystem, like your Module 4 version had) it must be wired there
// too, not just added to this package.

import java.io.IOException;

/**
 * Something that can hand back a random secret word.
 */
// UNDERSTAND: Purpose - abstracts "where words come from" away from
//             HangmanService, matching the same Dependency Inversion approach
//             used for HangmanRenderer.
// DECISION: One method only (getRandomWord) - the interface stays minimal
//           because that is the only operation HangmanService ever needs from it.
public interface WordRepository {

    /**
     * Returns one random, upper-cased word read from {@code filename}.
     *
     * @throws IOException if the file does not exist, cannot be read, or
     *                      contains no usable words
     */
    String getRandomWord(String filename) throws IOException;
}
