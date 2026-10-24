package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * Panel containing the list of students.
 */
public class StudentListPanel extends UiPart<Region> {
    private static final String FXML = "StudentListPanel.fxml";

    @FXML
    private ListView<Student> studentListView;

    private final ObservableList<StudentCourseEnrolment> enrolments;
    private Id focusedStudentId;

    /**
     * Creates a {@code StudentListPanel} with students and their enrolments.
     *
     * @param studentList students to display
     * @param enrolments enrolments used to populate each student's course summary
     */
    public StudentListPanel(ObservableList<Student> studentList,
            ObservableList<StudentCourseEnrolment> enrolments) {
        super(FXML);
        this.enrolments = enrolments;
        studentListView.setItems(studentList);
        studentListView.setCellFactory(listView -> new StudentListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Student} using
     * a {@code StudentCard}.
     */
    class StudentListViewCell extends ListCell<Student> {
        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);

            if (empty || student == null) {
                setGraphic(null);
                setText(null);
            } else {
                StudentCard studentCard = new StudentCard(student, enrolments);
                if (student.getId().equals(focusedStudentId)) {
                    studentCard.getRoot().getStyleClass().add("student-card-focused");
                }
                setGraphic(studentCard.getRoot());
            }
        }
    }

    /**
     * Scrolls to and selects the student with the specified ID.
     */
    public void scrollToStudentId(Id id) {
        focusedStudentId = id;
        ObservableList<Student> students = studentListView.getItems();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                studentListView.scrollTo(i);
                studentListView.getSelectionModel().select(i);
                studentListView.refresh();
                return;
            }
        }
    }
}
