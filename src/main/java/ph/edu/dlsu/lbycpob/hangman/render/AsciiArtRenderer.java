package ph.edu.dlsu.lbycpob.hangman.render;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: resourceBasePath + "/display" + guessesRemaining + ".txt" actually
// resolves to files that exist under src/main/resources/game-assets/hangman-art/
// (display0.txt ... display8.txt) - these were copied over unchanged from your
// Module 4 console project's resources.

import ph.edu.dlsu.lbycpob.hangman.utils.ClasspathResources;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * {@link HangmanRenderer} that reads pre-drawn ASCII art files bundled as
 * classpath resources and <em>returns</em> their lines.
 *
 * <p>The only change from the original: the for-loop that called
 * {@code IO.println(line)} is gone. Returning the lines and letting the
 * caller decide how to display them is the correct separation of concerns
 * for a server-side web component.
 */
public final class AsciiArtRenderer implements HangmanRenderer {
    private static final int MIN_GUESSES_REMAINING = 0;
    private static final int MAX_GUESSES_REMAINING = 8;

    private final String resourceBasePath;


}
