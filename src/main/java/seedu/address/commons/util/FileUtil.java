package seedu.address.commons.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Writes and reads files
 */
public class FileUtil {

    private static final String CHARSET = "UTF-8";

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createIfMissing(Path file) throws IOException {
        if (Files.exists(file)) {
            return;
        }

        createParentDirsOfFile(file);

        Files.createFile(file);
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    private static void createParentDirsOfFile(Path file) throws IOException {
        Path parentDir = file.getParent();

        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
    }

    /**
     * Assumes file exists
     */
    public static String readFromFile(Path file) throws IOException {
        return new String(Files.readAllBytes(file), CHARSET);
    }

    /**
     * Writes given string to a file.
     * Will create the file if it does not exist yet.
     * The contents are first written to a temporary file in the same directory,
     * then moved into place to avoid leaving a partially written target file.
     * If atomic moves are unsupported by the file system, a regular replacement
     * move is used as a portability fallback and may not provide the same guarantee.
     */
    public static void writeToFile(Path file, String content) throws IOException {
        Path absoluteFile = file.toAbsolutePath();
        Path parentDirectory = absoluteFile.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        Path temporaryFile = Files.createTempFile(parentDirectory,
                absoluteFile.getFileName().toString(), ".tmp");
        try {
            Files.write(temporaryFile, content.getBytes(StandardCharsets.UTF_8));
            try {
                Files.move(temporaryFile, absoluteFile,
                        StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, absoluteFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

}
