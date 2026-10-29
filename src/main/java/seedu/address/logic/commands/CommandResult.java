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
            "Does not contain a StudentSplitView";

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    private final Id studentIdToFocus;
    private final StudentSplitView studentSplitView;

    /**
     * Constructs a CommandResult with an optional added student ID.
     */
    public CommandResult(String feedbackToUser, boolean showHelp,
            boolean exit, Id studentIdToFocus) {
        this(feedbackToUser, showHelp, exit, studentIdToFocus, null);
    }

    /**
     * Constructs a CommandResult with an optional added student ID and studentSplitView
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit,
            Id studentIdToFocus, StudentSplitView studentSplitView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.studentIdToFocus = studentIdToFocus;
        this.studentSplitView = studentSplitView;
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
    public CommandResult(String feedbackToUser, StudentSplitView studentSplitView) {
        this(feedbackToUser, false, false,
                requireNonNull(studentSplitView).getStudent().getId(), studentSplitView);
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

        return (StudentDeliverablesView) studentSplitView;
    }

    /**
     * Returns the StudentNotesListView produced by the command.
     * Assumes CommandResult contains a StudentNotesListView.
     * Use {@code containsStudentNotesListView} to check before using.
     */
    public StudentNotesListView getStudentNotesListView() {
        assert containsStudentNotesListView()
                : NOT_STUDENT_NOTES_LIST_VIEW_MESSAGE;

        return (StudentNotesListView) studentSplitView;
    }

    /**
     * Returns the StudentSplitView produced by the command.
     * Assumes CommandResult contains a StudentSplitView.
     * Use {@code containsStudentSplitView} to check before using.
     */
    public StudentSplitView getStudentSplitView() {
        assert containsStudentSplitView()
                : NO_STUDENT_ITERABLE_VIEW_MESSAGE;

        return studentSplitView;
    }

    /** Checks if CommandResult contains a StudentSplitView */
    public boolean containsStudentSplitView() {
        return studentSplitView != null;
    }

    /** Checks if CommandResult contains a StudentNotesListView */
    public boolean containsStudentDeliverablesView() {
        return studentSplitView instanceof StudentDeliverablesView;
    }

    /** Checks if CommandResult contains a StudentNotesListView */
    public boolean containsStudentNotesListView() {
        return studentSplitView instanceof StudentNotesListView;
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
                && Objects.equals(studentSplitView, otherCommandResult.studentSplitView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, studentIdToFocus, studentSplitView);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("studentIdToFocus", studentIdToFocus)
                .add("studentSplitView", studentSplitView)
                .toString();
    }

}
