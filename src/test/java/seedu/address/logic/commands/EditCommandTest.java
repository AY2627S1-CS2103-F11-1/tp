package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.StringJoiner;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;
import seedu.address.testutil.EditStudentDescriptorBuilder;
import seedu.address.testutil.StudentBuilder;

/** Contains integration tests and unit tests for {@link EditCommand}. */
public class EditCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecified_success() {
        Id targetId = new Id("A1234567Y");
        Student studentToEdit = findStudent(targetId);
        Student editedStudent = new StudentBuilder(studentToEdit).withName(VALID_NAME_BOB)
                .withId(VALID_ID_BOB).withEmail(VALID_EMAIL_BOB).withTutorialClass("F10").build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB)
                .withId(VALID_ID_BOB).withEmail(VALID_EMAIL_BOB).withTutorialClass("F10").build();

        Model expectedModel = copyModel();
        expectedModel.setStudent(findStudent(expectedModel, targetId), editedStudent);
        expectedModel.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);

        assertEditSuccess(new EditCommand(targetId, descriptor), successMessage(studentToEdit, descriptor),
                expectedModel, editedStudent);
    }

    @Test
    public void execute_someFieldsSpecified_success() {
        Id targetId = new Id("A1234567Y");
        Student studentToEdit = findStudent(targetId);
        Student editedStudent = new StudentBuilder(studentToEdit).withName(VALID_NAME_BOB).build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();

        Model expectedModel = copyModel();
        expectedModel.setStudent(findStudent(expectedModel, targetId), editedStudent);
        expectedModel.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);

        assertEditSuccess(new EditCommand(targetId, descriptor), successMessage(editedStudent, descriptor),
                expectedModel, editedStudent);
    }

    @Test
    public void execute_unknownStudent_failure() {
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();

        assertCommandFailure(new EditCommand(new Id("A9999999Z"), descriptor), model,
                "Student with ID A9999999Z was not found.");
    }

    @Test
    public void execute_duplicateStudent_failure() {
        Student studentToEdit = findStudent(new Id("A1234567Y"));
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName("Benson Meier").withId("A2345678X")
                .withEmail("benson@u.nus.edu").withTutorialClass("F10").build();

        assertCommandFailure(new EditCommand(studentToEdit.getId(), descriptor), model,
                EditCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void equals() {
        EditCommand standardCommand = new EditCommand(new Id(VALID_ID_AMY), DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(new Id(VALID_ID_AMY),
                new EditStudentDescriptor(DESC_AMY));

        assertTrue(standardCommand.equals(commandWithSameValues));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(new EditCommand(new Id(VALID_ID_BOB), DESC_AMY)));
        assertFalse(standardCommand.equals(new EditCommand(new Id(VALID_ID_AMY), DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Id studentId = new Id(VALID_ID_AMY);
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        EditCommand editCommand = new EditCommand(studentId, descriptor);
        String expected = EditCommand.class.getCanonicalName() + "{studentId=" + studentId
                + ", descriptor=" + descriptor + "}";

        assertEquals(expected, editCommand.toString());
    }

    private Student findStudent(Id studentId) {
        return findStudent(model, studentId);
    }

    private Student findStudent(Model sourceModel, Id studentId) {
        return sourceModel.getAddressBook().getStudentList().stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElseThrow();
    }

    private Model copyModel() {
        return new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
    }

    private String successMessage(Student student, EditStudentDescriptor descriptor) {
        StringJoiner changes = new StringJoiner("\n");
        descriptor.getName().ifPresent(name -> changes.add("Name: " + name));
        descriptor.getId().ifPresent(id -> changes.add("Student ID: " + id));
        descriptor.getEmail().ifPresent(email -> changes.add("Email: " + email));
        descriptor.getTutorialClass().ifPresent(tutorialClass ->
                changes.add("Tutorial class: " + tutorialClass));

        return String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, student.getId(), changes);
    }

    private void assertEditSuccess(EditCommand command, String expectedMessage,
            Model expectedModel, Student expectedStudent) {
        try {
            CommandResult result = command.execute(model);
            assertEquals(new CommandResult(expectedMessage, false, false,
                    new Id("A1234567Y")), result);
            assertEquals(expectedModel.getAddressBook(), model.getAddressBook());
            assertTrue(model.getAddressBook().getStudentList().contains(expectedStudent));
        } catch (CommandException exception) {
            throw new AssertionError("Execution of command should not fail.", exception);
        }
    }
}
