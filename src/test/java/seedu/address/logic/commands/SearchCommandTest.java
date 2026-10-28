package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;
import static seedu.address.testutil.TypicalStudents.CARL;
import static seedu.address.testutil.TypicalStudents.DANIEL;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Id;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.Student;
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
    public void execute_multipleNameKeywords_filtersStudentsByOrMatch() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(List.of("Alice", "Carl"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 2 matching student(s).", expectedModel);
        assertEquals(List.of(ALICE, CARL), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleNameQueries_usesOrSemantics() {
        StudentMatchesQueryPredicate predicate =
                new StudentMatchesQueryPredicate(List.of("Alice", "Benson"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 2 matching student(s).", expectedModel);
        assertEquals(List.of(ALICE, BENSON), model.getFilteredStudentList());
    }

    @Test
    public void execute_multiWordNameQueries_preservesEachQuery() {
        StudentMatchesQueryPredicate predicate =
                new StudentMatchesQueryPredicate(List.of("Alice Pauline", "Benson Meier"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 2 matching student(s).", expectedModel);
        assertEquals(List.of(ALICE, BENSON), model.getFilteredStudentList());
    }

    @Test
    public void execute_overlappingNameQueries_doesNotDuplicateStudents() {
        StudentMatchesQueryPredicate predicate =
                new StudentMatchesQueryPredicate(List.of("Ali", "Alice"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 1 matching student(s).", expectedModel);
        assertEquals(List.of(ALICE), model.getFilteredStudentList());
    }

    @Test
    public void execute_duplicateIdQueries_doesNotDuplicateStudents() {
        StudentMatchesQueryPredicate predicate = StudentMatchesQueryPredicate.forIds(
                List.of(new Id("A1234567Y"), new Id("a1234567y")));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 1 matching student(s).", expectedModel);
        assertEquals(List.of(ALICE), model.getFilteredStudentList());
    }

    @Test
    public void execute_queryOrder_doesNotChangeResultOrder() throws CommandException {
        SearchCommand firstSearch = new SearchCommand(
                new StudentMatchesQueryPredicate(List.of("Benson", "Alice")));
        SearchCommand secondSearch = new SearchCommand(new StudentMatchesQueryPredicate(List.of("Alice", "Benson")));

        firstSearch.execute(model);
        List<Student> firstResults = List.copyOf(model.getFilteredStudentList());
        secondSearch.execute(model);

        assertEquals(firstResults, List.copyOf(model.getFilteredStudentList()));
        assertEquals(List.of(ALICE, BENSON), firstResults);
    }

    @Test
    public void execute_duplicateNames_returnsAllMatchingStudentsInListOrder() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate("Meier");
        SearchCommand command = new SearchCommand(predicate);

        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 2 matching student(s).", expectedModel);
        assertEquals(List.of(BENSON, DANIEL), model.getFilteredStudentList());
    }

    @Test
    public void execute_noMatchingQuery_returnsEmptyFilteredList() {
        StudentMatchesQueryPredicate predicate = new StudentMatchesQueryPredicate("NoSuchStudent");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 0 matching student(s).", expectedModel);
        assertTrue(model.getFilteredStudentList().isEmpty());
    }

    @Test
    public void execute_mixedMatchingAndNonMatchingQueries_returnsMatchingStudents() {
        StudentMatchesQueryPredicate predicate =
                new StudentMatchesQueryPredicate(List.of("Zzzzz", "Alice"));
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredStudentList(predicate);

        assertCommandSuccess(command, model, "Found 1 matching student(s).", expectedModel);
        assertEquals(List.of(ALICE), model.getFilteredStudentList());
    }

    @Test
    public void execute_repeatedSearches_replacePreviousFilter() throws CommandException {
        SearchCommand firstSearch = new SearchCommand(new StudentMatchesQueryPredicate("Alice"));
        SearchCommand secondSearch = new SearchCommand(new StudentMatchesQueryPredicate("Carl"));

        firstSearch.execute(model);
        assertEquals(List.of(ALICE), model.getFilteredStudentList());

        secondSearch.execute(model);
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
        assertCommandFailure(new SearchCommand(StudentMatchesQueryPredicate.forIds(
                List.of(new Id("A1234567Y")))), new ModelManager(), SearchCommand.MESSAGE_NO_STUDENTS);
    }

    @Test
    public void equals() {
        SearchCommand nameCommand = new SearchCommand(new StudentMatchesQueryPredicate("Alice"));
        SearchCommand idCommand = new SearchCommand(new StudentMatchesQueryPredicate(new Id("A1234567Y")));

        assertTrue(nameCommand.equals(nameCommand));
        assertTrue(nameCommand.equals(new SearchCommand(new StudentMatchesQueryPredicate("Alice"))));
        assertEquals(nameCommand.hashCode(),
                new SearchCommand(new StudentMatchesQueryPredicate("Alice")).hashCode());
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
