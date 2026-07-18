package ph.edu.dlsu.lbycpob.hangman.utils;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: getResourceAsStream resolves paths relative to the classpath root
// (hence the leading "/" in every call site), not relative to the calling
// class's package - a missing leading slash is the most common reason this
// kind of resource lookup silently returns null.

import ph.edu.dlsu.lbycpob.hangman.render.AsciiArtRenderer;
import ph.edu.dlsu.lbycpob.hangman.repository.ClasspathWordRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * One small, focused job: read every line of a text file that is bundled
 * inside the application as a classpath resource - i.e. packaged inside the
 * jar and found with {@code getResourceAsStream}, as opposed to a real file
 * sitting on the user's filesystem.
 */
public final class ClasspathResources {

    private ClasspathResources() {
        // Utility class - never instantiated.
    }

    /**
     * Reads every line of the classpath resource at {@code resourcePath}
     * (e.g. {@code "/game-assets/words/test.txt"}).
     *
     * @throws IOException if the resource does not exist or cannot be read
     */
    // UNDERSTAND: Purpose - single shared implementation of "read a bundled text
    //             file line by line", reused by both AsciiArtRenderer and
    //             ClasspathWordRepository so the same logic is not duplicated.
    // DECISION: Nested try-with-resources (InputStream, then BufferedReader)
    //           guarantees both streams close even if readLine() throws partway
    //           through, without needing an explicit finally block.
    public static List<String> readLines(String resourcePath) throws IOException {
        Objects.requireNonNull(resourcePath, "resourcePath must not be null");

        // try-with-resources: both streams are closed automatically, even
        // if readLine() throws partway through.
        try (InputStream input = ClasspathResources.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Resource not found on the classpath: " + resourcePath);
            }
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            return lines;
        }
    }
}
