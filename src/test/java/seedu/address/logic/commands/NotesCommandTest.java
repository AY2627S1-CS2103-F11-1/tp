package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.model.student.NotesList;
import seedu.address.testutil.StudentBuilder;

/** Tests for {@code NoteCommand}. */
public class NotesCommandTest {

    private final Model model = new ModelManager(loadAddressBook(), new UserPrefs());

    /**
     * Loads new AddressBook with Student to test adding Notes.
     * Prevents adding Notes to the TypicalAddressBook.
     * @return addressBook Loaded AddressBook.
     */
    public static AddressBook loadAddressBook() {
        AddressBook addressBook = new AddressBook();
        addressBook.addStudent(new StudentBuilder().withName("Alice Pauline")
                .withId("A1234567Y").withEmail("alice@u.nus.edu").withTutorialClass("T07")
                .withNotes("Needs help with testing", "Code Testing").build());
        return addressBook;
    }
    @Test
    public void execute_validId_success() {
        Notes newNotes = new Notes("Added more Unit tests", "Code Testing");

        NotesCommand command = new NotesCommand(ALICE.getId(), newNotes);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        NotesList expectedNotesList = new NotesList(new ArrayList<Notes>(
                List.of(new Notes("Needs help with testing", "Code Testing"),
                        new Notes("Added more Unit tests", "Code Testing"))));
        expectedModel.setStudent(ALICE, new StudentBuilder(ALICE).withNotes(expectedNotesList).build());

        assertCommandSuccess(command, model, new CommandResult(String.format(
                NotesCommand.MESSAGE_UPDATE_NOTES_SUCCESS, ALICE.getName(), ALICE.getId()),
                false, false, ALICE.getId()),
                expectedModel);
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        NotesCommand command = new NotesCommand(new Id("A9999999Z"), new Notes("Any note", "Any Task"));

        assertCommandFailure(command, model,
                String.format(StudentNotesCommandUtil.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }
}
