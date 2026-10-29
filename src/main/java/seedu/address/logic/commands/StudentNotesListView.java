package seedu.address.logic.commands;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.student.Student;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

public class StudentNotesListView extends StudentIterableView {
    private final NotesList notesList;

    /**
     * Creates a UI view for {@code student} using the supplied course-progress records.
     */
    public StudentNotesListView(Student student, NotesList notesList) {
        requireAllNonNull(student, notesList);

        super(student);
        this.notesList = notesList;
    }

    public NotesList getNotesList() {
        return notesList;
    }

    public String viewNotesList() {
        return notesList.viewNotes();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StudentNotesListView otherView
                && getStudent().equals(otherView.getStudent())
                && notesList.equals(otherView.notesList));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudent(), notesList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("student", getStudent())
                .add("notesList", notesList)
                .toString();
    }
}
