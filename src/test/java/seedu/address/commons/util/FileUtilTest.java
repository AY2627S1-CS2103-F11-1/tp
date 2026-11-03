package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests safe file writing behaviour. */
public class FileUtilTest {
    @TempDir
    public Path temporaryFolder;

    @Test
    public void writeToFile_createsParentDirectoriesAndWritesContent() throws IOException {
        Path file = temporaryFolder.resolve("nested").resolve("data.json");

        FileUtil.writeToFile(file, "content");

        assertEquals("content", Files.readString(file));
    }
}
