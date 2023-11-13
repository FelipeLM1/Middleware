package ufrn.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MiddlewareBanner {
    private MiddlewareBanner() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareBanner.class);

    public static void printBanner() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/banner.txt"))) {
            StringBuilder banner = new StringBuilder();
            String line;
            banner.append("\n");
            while ((line = br.readLine()) != null) {
                banner.append(line).append("\n");
            }
            logger.info(banner.toString());
        } catch (IOException e) {
            logger.error("Erro ao ler o banner: {}", e.getMessage());
        }
    }
}

