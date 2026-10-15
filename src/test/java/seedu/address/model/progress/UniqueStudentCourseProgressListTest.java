package seedu.address.model.progress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.exceptions.DuplicateStudentCourseProgressException;
import seedu.address.model.student.Id;

/** Tests for {@code UniqueStudentCourseProgressList}. */
public class UniqueStudentCourseProgressListTest {

    private static final Course COURSE = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("MVP")));
    private static final StudentCourseProgress ALICE_PROGRESS =
            new StudentCourseProgress(new Id("A1234567Z"), COURSE);

    private final UniqueStudentCourseProgressList progressList = new UniqueStudentCourseProgressList();

    @Test
    public void add_duplicateProgress_throwsDuplicateStudentCourseProgressException() {
        progressList.add(ALICE_PROGRESS);

        assertThrows(DuplicateStudentCourseProgressException.class, () -> progressList.add(ALICE_PROGRESS));
    }

    @Test
    public void removeForStudent_removesOnlyMatchingProgress() {
        StudentCourseProgress bobProgress = new StudentCourseProgress(new Id("A7654321Z"), COURSE);
        progressList.add(ALICE_PROGRESS);
        progressList.add(bobProgress);

        progressList.removeForStudent(new Id("A1234567Z"));

        assertEquals(List.of(bobProgress), progressList.asUnmodifiableObservableList());
    }
}
