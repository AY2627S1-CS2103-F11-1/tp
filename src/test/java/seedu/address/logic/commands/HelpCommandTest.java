package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.ui.HelpWindow;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void helpWindowContent_matchesCurrentCommandContract() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/view/HelpWindow.fxml")) {
            assertNotNull(input);
            String content = new String(input.readAllBytes(), StandardCharsets.UTF_8);

            String[] commands = {"list", "add /name", "edit /id", "delete /id", "search /name",
                "init course", "enrol /id", "unenrol /id", "mark /id", "unmark /id", "view /id",
                "notes /id", "view notes /id", "delete notes /id", "help", "clear", "exit"};
            for (String command : commands) {
                assertTrue(content.contains(command), "Missing help entry: " + command);
            }
            assertTrue(content.contains("/deliverable DELIVERABLE"));
            assertFalse(content.contains("/task"));
            assertEquals("https://ay2627s1-cs2103-f11-1.github.io/tp/UserGuide.html",
                    HelpWindow.USERGUIDE_URL);
        }
    }
}
