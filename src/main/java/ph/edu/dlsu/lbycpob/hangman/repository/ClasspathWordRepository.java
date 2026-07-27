package ph.edu.dlsu.lbycpob.hangman.repository;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: the three files this reads from (test.txt, words.txt, large.txt under
// game-assets/words/) are the same ones the "Choose a word list" dropdown in
// index.html offers - if you add a new word list file, add a matching <option>
// in index.html or it can never actually be selected.

import ph.edu.dlsu.lbycpob.hangman.render.AsciiArtRenderer;
import ph.edu.dlsu.lbycpob.hangman.utils.ClasspathResources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * {@link WordRepository} that reads one of the word lists bundled inside
 * the application itself - packaged as a classpath resource, the same way
 * {@link AsciiArtRenderer} reads its art - rather than a file on the
 * user's real, separate filesystem.
 */
public final class ClasspathWordRepository implements WordRepository {

    private final String resourceBasePath;
    private final Random random;

    /**
     * @param resourceBasePath classpath folder containing the bundled word
     *                         list files, e.g. {@code "/game-assets/words"}
     * @param random           source of randomness for picking a word
     */
    public ClasspathWordRepository(String resourceBasePath, Random random) {
        Objects.requireNonNull(resourceBasePath, "resourceBasePath must not be null");
        this.random = Objects.requireNonNull(random, "random must not be null");
        if (resourceBasePath.isBlank()) {
            throw new IllegalArgumentException("resourceBasePath must not be blank");
        }
        this.resourceBasePath = resourceBasePath.endsWith("/")
                ? resourceBasePath.substring(0, resourceBasePath.length() - 1)
                : resourceBasePath;
    }

    // UNDERSTAND: Purpose - reads filename's lines, cleans/uppercases each one,
    //             and returns a single random pick.
    // DECISION: Blank lines are filtered out with the for-loop below (instead of
    //           trusting the file to be perfectly formatted) since a stray blank
    //           line at the end of a word list is an easy, easy-to-miss mistake
    //           that would otherwise let the game pick an empty "word".

}
