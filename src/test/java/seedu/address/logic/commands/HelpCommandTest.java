package seedu.address.logic.commands;

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

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void helpContent_containsSupportedDeliverableSyntax() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/view/HelpContent.txt")) {
            assertNotNull(input);
            String content = new String(input.readAllBytes(), StandardCharsets.UTF_8);

            assertTrue(content.contains("/deliverable DELIVERABLE"));
            assertTrue(content.contains("search /name NAME[, NAME]..."));
            assertFalse(content.contains("/task"));
        }
    }

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }
}
