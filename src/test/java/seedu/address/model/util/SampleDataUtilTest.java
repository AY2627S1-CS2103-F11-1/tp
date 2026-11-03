package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.course.Course;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.progress.StudentCourseProgress;
import seedu.address.model.student.Student;

/** Tests the invariants of the address book used for first-launch sample data. */
public class SampleDataUtilTest {

    @Test
    public void getSampleStudents_returnsExpectedStudents() {
        Student[] students = SampleDataUtil.getSampleStudents();

        assertEquals(6, students.length);
        Set<String> studentIds = java.util.Arrays.stream(students)
                .map(student -> student.getId().toString())
                .collect(Collectors.toSet());
        assertEquals(students.length, studentIds.size());
    }

    @Test
    public void getSampleAddressBook_containsConsistentCourseData() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();

        assertEquals(2, addressBook.getCourseList().size());
        assertEquals(6, addressBook.getStudentList().size());
        assertEquals(6, addressBook.getEnrolmentList().size());
        assertEquals(6, addressBook.getStudentCourseProgressList().size());

        for (StudentCourseEnrolment enrolment : addressBook.getEnrolmentList()) {
            assertTrue(addressBook.getStudentList().stream()
                    .anyMatch(student -> student.getId().equals(enrolment.getStudentId())));
            assertTrue(addressBook.getCourseList().stream()
                    .anyMatch(course -> course.getCourseCode().equals(enrolment.getCourseCode())
                            && course.getSemester().equals(enrolment.getSemester())));
        }

        for (StudentCourseProgress progress : addressBook.getStudentCourseProgressList()) {
            assertFalse(progress.getDeliverableStatuses().isEmpty());
            assertTrue(addressBook.getEnrolmentList().stream()
                    .anyMatch(enrolment -> enrolment.getStudentId().equals(progress.getStudentId())
                            && enrolment.getCourseCode().equals(progress.getCourseCode())
                            && enrolment.getSemester().equals(progress.getSemester())));
        }
    }

    @Test
    public void getSampleAddressBook_containsOnlyCourseDeliverablesInProgress() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();

        for (StudentCourseProgress progress : addressBook.getStudentCourseProgressList()) {
            Course course = addressBook.getCourseList().stream()
                    .filter(candidate -> candidate.getCourseCode().equals(progress.getCourseCode())
                            && candidate.getSemester().equals(progress.getSemester()))
                    .findFirst()
                    .orElseThrow();
            assertTrue(course.getDeliverables()
                    .containsAll(progress.getDeliverableStatuses().keySet()));
        }
    }
}
