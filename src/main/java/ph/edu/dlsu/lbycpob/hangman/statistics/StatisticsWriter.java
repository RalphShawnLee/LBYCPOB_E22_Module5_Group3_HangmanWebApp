package ph.edu.dlsu.lbycpob.hangman.statistics;

// AI-CHECK: Transcribed by Claude (Anthropic) from the LBYCPOB Module 5A manual.
// Verify: hangman_statistics.txt is written relative to the JVM's working
// directory (wherever IntelliJ launches the app from) - after you run the app,
// locate that file on disk and confirm its contents match what stats.html
// displayed on screen, since that cross-check is exactly what the manual's
// "Statistics have been saved to..." message on the stats page implies.

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes Hangman session statistics to a flat file.
 */
// UNDERSTAND: Purpose - appends a formatted block of session totals to a flat
//             text file every time GameController.stats() is called.
// DECISION: @Component (not @Service) because this class is infrastructure
//           (file I/O), not business logic - keeping the two stereotypes
//           distinct documents which classes GameService could theoretically
//           be unit-tested without touching the filesystem.
@Component
public class StatisticsWriter {

    private static final Logger log = LoggerFactory.getLogger(StatisticsWriter.class);

    private static final String FILENAME = "hangman_statistics.txt";
    private static final String SEPARATOR = "=".repeat(60);

    // UNDERSTAND: Purpose - public entry point; ensures the file exists, appends
    //             one session's stats, and never lets an I/O failure bubble up
    //             to the controller.
    // DECISION: IOException is caught and logged here (not rethrown) since a
    //           failed statistics write should never prevent the player from
    //           seeing their stats on screen - the file write is a side effect,
    //           not the primary purpose of the /game/stats request.
    public void writeStats(int gamesPlayed, int gamesWon, int gamesLost,
                           double winPercentage, int bestScore) {
        try {
            ensureFileExists();
            appendStatsToFile(gamesPlayed, gamesWon, gamesLost, winPercentage, bestScore);
            log.info("Session statistics saved to {}", FILENAME);
        } catch (IOException e) {
            log.error("Error writing statistics to file: {}", e.getMessage(), e);
        }
    }

    private void appendStatsToFile(int gamesPlayed, int gamesWon, int gamesLost,
                                   double winPercentage, int bestScore) throws IOException {
        try (FileWriter fw = new FileWriter(FILENAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(SEPARATOR);
            bw.newLine();
            bw.write("Hangman Game Session - " + getCurrentTimestamp());
            bw.newLine();
            bw.write(SEPARATOR);
            bw.newLine();
            bw.write(String.format("Total Games Played: %d%n", gamesPlayed));
            bw.write(String.format("Games Won:          %d%n", gamesWon));
            bw.write(String.format("Games Lost:         %d%n", gamesLost));
            bw.write(String.format("Win Percentage:     %.1f%%%n", winPercentage));
            bw.write(String.format("Best Score:         %d guess(es) remaining%n", bestScore));
            bw.newLine();
        }
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void ensureFileExists() throws IOException {
        File file = new File(FILENAME);
        if (!file.exists()) {
            try (FileWriter fw = new FileWriter(FILENAME)) {
                fw.write("HANGMAN GAME STATISTICS LOG\n");
                fw.write("Session records appended below\n\n");
            }
        }
    }
}
