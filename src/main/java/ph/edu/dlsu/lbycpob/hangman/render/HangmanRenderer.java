package ph.edu.dlsu.lbycpob.hangman.render;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: this interface has exactly one implementation (AsciiArtRenderer) in this
// project - if you ever add a second one (e.g. an emoji-based renderer), confirm it
// is wired into HangmanSpringBootApplication's @Bean method, not hardcoded elsewhere.

import java.io.IOException;
import java.util.List;

/**
 * Produces the hangman drawing for a given number of guesses remaining.
 */
// UNDERSTAND: Purpose - abstracts "how the gallows is drawn" away from
//             HangmanService, so the service depends on a contract, not a
//             concrete file-reading implementation.
// DECISION: Declared as an interface (not a concrete class) so the renderer is
//           swappable/mockable, matching the Dependency Inversion Principle used
//           for WordRepository below.
public interface HangmanRenderer {

    /**
     * Returns the lines of hangman art for {@code guessesRemaining} (0–8).
     *
     * @throws IOException if the art resource cannot be located or read
     */
    List<String> render(int guessesRemaining) throws IOException;
}
