package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.student.Id;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    public static final String NOT_STUDENT_DELIVERABLES_VIEW_MESSAGE =
            "Current View is not a StudentDeliverablesListView";
    public static final String NOT_STUDENT_NOTES_LIST_VIEW_MESSAGE =
            "Current View is not a StudentNotesListView";
    public static final String NO_STUDENT_ITERABLE_VIEW_MESSAGE =
            "Does not contain a StudentIterableView";

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    private final Id studentIdToFocus;
    private final StudentIterableView studentIterableView;

    /**
     * Constructs a CommandResult with an optional added student ID.
     */
    public CommandResult(String feedbackToUser, boolean showHelp,
            boolean exit, Id studentIdToFocus) {
        this(feedbackToUser, showHelp, exit, studentIdToFocus, null);
    }

    /**
     * Constructs a CommandResult with an optional added student ID and studentIterableView
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit,
            Id studentIdToFocus, StudentIterableView studentIterableView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.studentIdToFocus = studentIdToFocus;
        this.studentIterableView = studentIterableView;
    }

    /**
     * Constructs a CommandResult without an added student ID.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, null);
    }

    /**
     * Constructs a CommandResult with only feedback.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    /** Constructs a result containing the deliverables view for a student. */
    public CommandResult(String feedbackToUser, StudentIterableView studentIterableView) {
        this(feedbackToUser, false, false,
                requireNonNull(studentIterableView).getStudent().getId(), studentIterableView);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public Optional<Id> getStudentIdToFocus() {
        return Optional.ofNullable(studentIdToFocus);
    }

    /** Returns the deliverables view produced by the command. */
    public StudentDeliverablesView getStudentDeliverablesView() {
        assert containsStudentDeliverablesView()
                : NOT_STUDENT_DELIVERABLES_VIEW_MESSAGE;

        return (StudentDeliverablesView) studentIterableView;
    }

    /**
     * Returns the StudentNotesListView produced by the command.
     * Assumes CommandResult contains a StudentNotesListView.
     * Use {@code containsStudentNotesListView} to check before using.
     */
    public StudentNotesListView getStudentNotesListView() {
        assert containsStudentNotesListView()
                : NOT_STUDENT_NOTES_LIST_VIEW_MESSAGE;

        return (StudentNotesListView) studentIterableView;
    }

    /**
     * Returns the StudentIterableView produced by the command.
     * Assumes CommandResult contains a StudentIterableView.
     * Use {@code containsStudentIterableView} to check before using.
     */
    public StudentIterableView getStudentIterableView() {
        assert containsStudentIterableView()
                : NO_STUDENT_ITERABLE_VIEW_MESSAGE;

        return studentIterableView;
    }

    /** Checks if CommandResult contains a StudentIterableView */
    public boolean containsStudentIterableView() {
        return studentIterableView != null;
    }

    /** Checks if CommandResult contains a StudentNotesListView */
    public boolean containsStudentDeliverablesView() {
        return studentIterableView instanceof StudentDeliverablesView;
    }

    /** Checks if CommandResult contains a StudentNotesListView */
    public boolean containsStudentNotesListView() {
        return studentIterableView instanceof StudentNotesListView;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult otherCommandResult)) {
            return false;
        }

        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && Objects.equals(studentIdToFocus, otherCommandResult.studentIdToFocus)
                && Objects.equals(studentIterableView, otherCommandResult.studentIterableView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, studentIdToFocus, studentIterableView);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("studentIdToFocus", studentIdToFocus)
                .add("studentIterableView", studentIterableView)
                .toString();
    }

}
