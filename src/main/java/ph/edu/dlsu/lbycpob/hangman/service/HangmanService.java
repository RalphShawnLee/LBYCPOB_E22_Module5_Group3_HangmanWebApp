package ph.edu.dlsu.lbycpob.hangman.service;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: createHint() below produces the exact same output as your Module 4
// console app's Hangman.createHint() for the same (secretWord, guessedLetters)
// pair - the manual states the algorithm is unchanged, so this is a good first
// thing to hand-trace for guide question 2.10.

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.lbycpob.hangman.render.HangmanRenderer;
import ph.edu.dlsu.lbycpob.hangman.repository.WordRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Pure, stateless game-logic service.
 */
// UNDERSTAND: Purpose - holds every rule of the game (word selection, hint
//             building, art lookup) that has nothing to do with HTTP.
// DECISION: Declared @Service (not @Component) to signal, by name, that this
//           class belongs to the business-logic layer - functionally identical
//           to @Component, but the more specific stereotype documents intent for
//           anyone reading the class list.
@Service
public class HangmanService {

    private static final Logger log = LoggerFactory.getLogger(HangmanService.class);

    /** Maximum incorrect guesses before the player loses. */
    public static final int MAX_GUESSES = 8;

    private static final String[] FALLBACK_WORDS = {
            "JAVA", "HANGMAN", "COMPUTER", "KEYBOARD", "PROGRAM", "ALGORITHM"
    };

    private final WordRepository wordRepository;
    private final HangmanRenderer renderer;
    private final Random random;

    public HangmanService(WordRepository wordRepository,
                           HangmanRenderer renderer,
                           Random random) {
        this.wordRepository = Objects.requireNonNull(wordRepository);
        this.renderer = Objects.requireNonNull(renderer);
        this.random = Objects.requireNonNull(random);
    }

    // ------------------------------------------------------------------ //
    // Word selection                                                     //
    // ------------------------------------------------------------------ //

    /**
     * Returns a random upper-cased word from {@code filename}, falling back
     * to a built-in word list if the file cannot be read.
     *
     * <p>Mirrors {@code Hangman.getRandomWord} but logs the error instead
     * of printing to stdout.
     */
    // UNDERSTAND: Purpose - never lets a missing/misspelled word-list filename
    //             crash the request; falls back to FALLBACK_WORDS instead.
    // DECISION: try/catch around the repository call (rather than letting the
    //           IOException propagate to the controller) so GameController never
    //           has to think about word-list I/O failures at all.
    public String getRandomWord(String filename) {
        Objects.requireNonNull(filename, "filename must not be null");
        try {
            return wordRepository.getRandomWord(filename);
        } catch (IOException e) {
            log.warn("Could not load words from \"{}\": {}. Using built-in fallback.",
                    filename, e.getMessage());
            return FALLBACK_WORDS[random.nextInt(FALLBACK_WORDS.length)];
        }
    }

