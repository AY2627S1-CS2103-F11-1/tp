package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_COURSE;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.ARGUMENT_DELIVERABLE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_COURSE;
import static seedu.address.logic.parser.CliSyntax.EXAMPLE_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELIVERABLE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.course.Course;

/**
 * Initialises a course and its project deliverables for one semester.
 */
public class InitCourseCommand extends Command {

    public static final String COMMAND_WORD = "init";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " course: Initialises a course and its project "
            + "deliverables. Parameters: " + PREFIX_COURSE + " " + ARGUMENT_COURSE
            + " " + PREFIX_SEMESTER + " " + ARGUMENT_SEMESTER
            + " " + PREFIX_DELIVERABLE + " " + ARGUMENT_DELIVERABLE + "[, " + ARGUMENT_DELIVERABLE + "]...\n"
            + "Example: " + COMMAND_WORD + " course " + PREFIX_COURSE + " " + EXAMPLE_COURSE
            + " " + PREFIX_SEMESTER + " " + EXAMPLE_SEMESTER
            + " " + PREFIX_DELIVERABLE + " v1.1, v1.2, MVP";
    public static final String MESSAGE_SUCCESS = "Course initialised: %1$s (%2$s) with %3$d deliverable(s).";
    public static final String MESSAGE_DUPLICATE_COURSE = "This course is already initialised for that semester.";

    private final Course toAdd;

    /**
     * Creates an {@code InitCourseCommand} to add the specified course.
     */
    public InitCourseCommand(Course course) {
        requireNonNull(course);
        toAdd = course;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasCourse(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_COURSE);
        }
        model.addCourse(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getCourseCode(), toAdd.getSemester(),
                toAdd.getDeliverables().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof InitCourseCommand otherCommand)) {
            return false;
        }
        return toAdd.equals(otherCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", toAdd).toString();
    }
}
