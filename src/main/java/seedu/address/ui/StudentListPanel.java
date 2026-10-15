package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;

/**
 * Panel containing the list of students.
 */
public class StudentListPanel extends UiPart<Region> {
    private static final String FXML = "StudentListPanel.fxml";

    @FXML
    private ListView<Student> studentListView;

    /**
     * Creates a {@code StudentListPanel} with the given {@code ObservableList}.
     */
    public StudentListPanel(ObservableList<Student> studentList) {
        super(FXML);
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
                setGraphic(new StudentCard(student).getRoot());
            }
        }
    }

    /**
     * Scrolls to and selects the student with the specified ID.
     */
    public void scrollToStudentId(Id id) {
        ObservableList<Student> students = studentListView.getItems();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                studentListView.scrollTo(i);
                studentListView.getSelectionModel().select(i);
                return;
            }
        }
    }
}
