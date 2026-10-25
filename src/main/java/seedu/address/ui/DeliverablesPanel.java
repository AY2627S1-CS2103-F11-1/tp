package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seedu.address.logic.commands.StudentDeliverablesView;
import seedu.address.model.course.Deliverable;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;

/** Displays one student's deliverables, grouped by course and semester. */
public class DeliverablesPanel extends UiPart<Region> {
    private static final String FXML = "DeliverablesPanel.fxml";

    @FXML
    private VBox content;
    @FXML
    private Label emptyState;
    @FXML
    private Label studentName;
    @FXML
    private Label studentId;
    @FXML
    private VBox courses;

    /** Creates an initially empty deliverables panel. */
    public DeliverablesPanel() {
        super(FXML);
        showEmptyState();
    }

    /** Updates this panel to display {@code view}. */
    public void showDeliverables(StudentDeliverablesView view) {
        studentName.setText(view.getStudent().getName().fullName);
        studentId.setText(view.getStudent().getId().toString());
        courses.getChildren().clear();

        for (StudentCourseProgress progress : view.getCourseProgresses()) {
            courses.getChildren().add(createCourseGroup(progress));
        }

        emptyState.setVisible(false);
        emptyState.setManaged(false);
        content.setVisible(true);
        content.setManaged(true);
    }

    private void showEmptyState() {
        emptyState.setVisible(true);
        emptyState.setManaged(true);
        content.setVisible(false);
        content.setManaged(false);
    }

    private VBox createCourseGroup(StudentCourseProgress progress) {
        Label courseHeading = new Label(formatCourseHeading(progress));
        courseHeading.getStyleClass().add("deliverables-course-heading");
        courseHeading.setWrapText(true);

        VBox taskList = new VBox(6);
        taskList.getStyleClass().add("deliverables-task-list");
        progress.getDeliverableStatuses().forEach((deliverable, status) ->
                taskList.getChildren().add(createTaskRow(deliverable, status)));

        VBox courseGroup = new VBox(8, courseHeading, taskList);
        courseGroup.getStyleClass().add("deliverables-course-group");
        return courseGroup;
    }

    static String formatCourseHeading(StudentCourseProgress progress) {
        long completedCount = progress.getDeliverableStatuses().values().stream()
                .filter(status -> status == CompletionStatus.DONE)
                .count();
        int totalCount = progress.getDeliverableStatuses().size();
        return String.format("%s · %s (%d/%d complete)", progress.getCourseCode(), progress.getSemester(),
                completedCount, totalCount);
    }

    private HBox createTaskRow(Deliverable deliverable, CompletionStatus status) {
        boolean isComplete = status == CompletionStatus.DONE;

        Region boxBackground = new Region();
        boxBackground.getStyleClass().add(isComplete
                ? "deliverable-status-box-complete"
                : "deliverable-status-box-incomplete");
        StackPane statusBox = new StackPane(boxBackground);
        statusBox.getStyleClass().add("deliverable-status-container");
        statusBox.setAccessibleRole(AccessibleRole.TEXT);
        statusBox.setAccessibleText(isComplete ? "Complete" : "Incomplete");
        if (isComplete) {
            Label checkMark = new Label("✓");
            checkMark.getStyleClass().add("deliverable-status-check");
            statusBox.getChildren().add(checkMark);
        }

        Label taskName = new Label(deliverable.toString());
        taskName.getStyleClass().add("deliverable-task-name");
        taskName.setWrapText(true);
        HBox.setHgrow(taskName, javafx.scene.layout.Priority.ALWAYS);

        HBox taskRow = new HBox(10, statusBox, taskName);
        taskRow.setAlignment(Pos.CENTER_LEFT);
        taskRow.getStyleClass().add("deliverable-task-row");
        return taskRow;
    }
}
