package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.model.student.Student;

/** Utility methods for creating student command strings in tests. */
public final class StudentUtil {

    private StudentUtil() {
    }

    /** Returns an add command string for {@code student}. */
    public static String getAddCommand(Student student) {
        return AddCommand.COMMAND_WORD + " " + getStudentDetails(student);
    }

    /** Returns the command arguments for {@code student}. */
    public static String getStudentDetails(Student student) {
        StringBuilder builder = new StringBuilder();
        builder.append(PREFIX_NAME).append(student.getName()).append(" ");
        builder.append(PREFIX_ID).append(student.getId()).append(" ");
        builder.append(PREFIX_EMAIL).append(student.getEmail()).append(" ");
        builder.append(PREFIX_CLASS).append(student.getTutorialClass()).append(" ");
        student.getNotes().ifPresent(notes -> builder.append(PREFIX_NOTES).append(notes).append(" "));
        return builder.toString();
    }

    /** Returns the command arguments represented by an edit descriptor. */
    public static String getEditStudentDescriptorDetails(EditStudentDescriptor descriptor) {
        StringBuilder builder = new StringBuilder();
        descriptor.getName().ifPresent(name -> builder.append(PREFIX_NAME).append(name).append(" "));
        descriptor.getId().ifPresent(id -> builder.append(PREFIX_ID).append(id).append(" "));
        descriptor.getEmail().ifPresent(email -> builder.append(PREFIX_EMAIL).append(email).append(" "));
        descriptor.getTutorialClass()
                .ifPresent(tutorialClass -> builder.append(PREFIX_CLASS).append(tutorialClass).append(" "));
        descriptor.getNotes().ifPresent(notes -> builder.append(PREFIX_NOTES).append(notes).append(" "));
        return builder.toString();
    }
}
