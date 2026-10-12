package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.CARL;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Id;
import seedu.address.model.student.StudentMatchesQueryPredicate;

public class SearchCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_nameQuery_filtersStudents() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate("arl");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 1 matching student(s).", expectedModel);
        assertEquals(List.of(CARL), model.getFilteredStudentList());
    }

    @Test
    public void execute_idQuery_filtersStudents() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate(new Id("A1234567Y"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 1 matching student(s).", expectedModel);
        assertEquals(List.of(ALICE), model.getFilteredStudentList());
    }

    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        assertCommandFailure(new SearchCommand(new StudentMatchesQueryPredicate("Alice")), new ModelManager(),
                SearchCommand.MESSAGE_NO_STUDENTS);
    }

    @Test
    public void equals() {
        SearchCommand nameCommand = new SearchCommand(new StudentMatchesQueryPredicate("Alice"));
        SearchCommand idCommand = new SearchCommand(new StudentMatchesQueryPredicate(new Id("A1234567Y")));

        assertTrue(nameCommand.equals(nameCommand));
        assertTrue(nameCommand.equals(new SearchCommand(new StudentMatchesQueryPredicate("Alice"))));
        assertFalse(nameCommand.equals(idCommand));
        assertFalse(nameCommand.equals(null));
    }

    @Test
    public void toStringMethod() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate("Alice");
        SearchCommand command = new SearchCommand(predicate);
        assertEquals(SearchCommand.class.getCanonicalName() + "{predicate=" + predicate + "}", command.toString());
    }
}
