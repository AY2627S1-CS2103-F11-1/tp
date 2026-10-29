package seedu.address.ui;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Student;

/**
 * A UI component that displays information of a {@code Student}.
 */
public class StudentCard extends UiPart<Region> {

    private static final String FXML = "StudentListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
     *      issue on AddressBook level 4</a>
     */

    public final Student student;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label studentId;
    @FXML
    private Label email;
    @FXML
    private Label courseCount;
    @FXML
    private VBox enrolledCourses;

    /**
     * Creates a {@code StudentCard} with the given student's identity and enrolments.
     *
     * @param student student to display
     * @param enrolments enrolments used to populate the course summary
     */
    public StudentCard(Student student, ObservableList<StudentCourseEnrolment> enrolments) {
        super(FXML);
        this.student = student;
        name.setText(student.getName().fullName);
        studentId.setText(student.getId().id);
        email.setText(student.getEmail().email);
        List<StudentCourseEnrolment> studentEnrolments = enrolments.stream()
                .filter(enrolment -> enrolment.getStudentId().equals(student.getId()))
                .toList();
        courseCount.setText(studentEnrolments.size() + " course"
                + (studentEnrolments.size() == 1 ? "" : "s"));
        studentEnrolments.forEach(enrolment -> enrolledCourses.getChildren().add(createCourseRow(enrolment)));
    }

    private HBox createCourseRow(StudentCourseEnrolment enrolment) {
        Label course = new Label(enrolment.getCourseCode().toString());
        course.getStyleClass().add("student-course-code");

        Label details = new Label(enrolment.getSemester() + " · Tutorial "
                + enrolment.getTutorialClass());
        details.getStyleClass().add("student-course-details");

        VBox courseDetails = new VBox(course, details);
        courseDetails.setSpacing(2);

        Label notes = new Label(enrolment.getNotes().size() + " note"
                + (enrolment.getNotes().size() == 1 ? "" : "s"));
        notes.getStyleClass().add("student-notes-count");

        HBox row = new HBox(courseDetails, notes);
        row.setSpacing(8);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox.setHgrow(courseDetails, javafx.scene.layout.Priority.ALWAYS);
        row.getStyleClass().add("student-course-row");
        return row;
    }
}
