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

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    private final Id studentIdToFocus;

    /** Deliverables that should be displayed in the details panel, if any. */
    private final StudentDeliverablesView studentDeliverablesView;

    /**
     * Constructs a CommandResult with an optional added student ID.
     */
    public CommandResult(String feedbackToUser, boolean showHelp,
            boolean exit, Id studentIdToFocus) {
        this(feedbackToUser, showHelp, exit, studentIdToFocus, null);
    }

    /**
     * Constructs a CommandResult with optional UI display data.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Id studentIdToFocus,
                         StudentDeliverablesView studentDeliverablesView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.studentIdToFocus = studentIdToFocus;
        this.studentDeliverablesView = studentDeliverablesView;
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

    /**
     * Constructs a CommandResult that displays one student's deliverables.
     */
    public CommandResult(String feedbackToUser, StudentDeliverablesView studentDeliverablesView) {
        this(feedbackToUser, false, false, requireNonNull(studentDeliverablesView).getStudent().getId(),
                studentDeliverablesView);
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

    public Optional<Id> getstudentIdToFocus() {
        return Optional.ofNullable(studentIdToFocus);
    }

    public Optional<StudentDeliverablesView> getStudentDeliverablesView() {
        return Optional.ofNullable(studentDeliverablesView);
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
                && Objects.equals(studentDeliverablesView, otherCommandResult.studentDeliverablesView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, studentIdToFocus, studentDeliverablesView);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("studentIdToFocus", studentIdToFocus)
                .add("studentDeliverablesView", studentDeliverablesView)
                .toString();
    }

}
