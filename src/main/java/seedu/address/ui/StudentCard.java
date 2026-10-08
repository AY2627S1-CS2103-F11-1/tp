package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.student.Notes;
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
    private Label displayedIndex;
    @FXML
    private Label studentId;
    @FXML
    private Label email;
    @FXML
    private Label tutorialClass;
    @FXML
    private Label notes;

    /**
     * Creates a {@code StudentCard} with the given {@code Student} and index to
     * display.
     */
    public StudentCard(Student student, int displayedIndex) {
        super(FXML);
        this.student = student;
        this.displayedIndex.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        studentId.setText(student.getId().id);
        email.setText(student.getEmail().email);
        tutorialClass.setText(student.getTutorialClass().tutorialClass);
        notes.setText(student.getNotes().map(Notes::toString).orElse(""));
    }
}
