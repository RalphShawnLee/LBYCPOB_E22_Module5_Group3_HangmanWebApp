package ph.edu.dlsu.lbycpob.hangman;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual's
// reference implementation (Section 1, "Hangman Web App"). Verify: (1) this compiles
// against the exact Spring Boot 4.1.0 / Java 25 setup shown in your IntelliJ "New
// Project" dialog, (2) the three @Bean methods below are the ones GameController's
// constructor chain (via HangmanService) actually needs, (3) you can explain, in your
// own words, why @Bean definitions live here instead of a separate @Configuration class.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ph.edu.dlsu.lbycpob.hangman.render.AsciiArtRenderer;
import ph.edu.dlsu.lbycpob.hangman.render.HangmanRenderer;
import ph.edu.dlsu.lbycpob.hangman.repository.ClasspathWordRepository;
import ph.edu.dlsu.lbycpob.hangman.repository.WordRepository;

import java.util.Random;

// UNDERSTAND: Purpose - entry point of the Spring Boot app; also hosts the @Bean
//             definitions that supply HangmanService with its collaborators.
// DECISION: For a small app like this one, keeping @Bean methods in the main class
//           (instead of a separate @Configuration class) keeps startup wiring in one
//           place, which matters here because ClasspathWordRepository and
//           AsciiArtRenderer both need a constructor argument (a classpath base path)
//           that Spring cannot infer automatically - mirrors the composition-root
//           pattern used in the constructor of the original console Hangman class.
@SpringBootApplication
public class HangmanSpringBootApplication {

    /** Shared classpath root – mirrors the original {@code GAME_ASSETS_BASE_PATH}. */
    private static final String GAME_ASSETS_BASE_PATH = "/game-assets";

    static void main(String[] args) {
        SpringApplication.run(HangmanSpringBootApplication.class, args);
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public WordRepository wordRepository(Random random) {
        return new ClasspathWordRepository(GAME_ASSETS_BASE_PATH + "/words", random);
    }

    @Bean
    public HangmanRenderer hangmanRenderer() {
        return new AsciiArtRenderer(GAME_ASSETS_BASE_PATH + "/hangman-art");
    }
}
