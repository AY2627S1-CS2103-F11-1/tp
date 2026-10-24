package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * Contains utility methods for populating AddressBook with sample data.
 */
public class SampleDataUtil {

    public static Student[] getSampleStudents() {
        return new Student[] {
            new Student(new Name("Alex Yeoh"), new Id("A1234567B"), new Email("alexyeoh@u.nus.edu")),
            new Student(new Name("Bernice Yu"), new Id("A2345678C"), new Email("berniceyu@u.nus.edu")),
            new Student(new Name("Charlotte Oliveiro"), new Id("A3456789D"),
                    new Email("charlotte@u.nus.edu")),
            new Student(new Name("David Li"), new Id("A4567890E"), new Email("davidli@u.nus.edu")),
            new Student(new Name("Irfan Ibrahim"), new Id("A5678901F"), new Email("irfanibrahim@u.nus.edu")),
            new Student(new Name("Roy Balakrishnan"), new Id("A6789012G"), new Email("royb@u.nus.edu"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAddressBook = new AddressBook();
        Course cs2103 = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                java.util.List.of(new Deliverable("MVP"), new Deliverable("Final")));
        Course is2108 = new Course(new CourseCode("IS2108"), new Semester("AY26/27-S1"),
                java.util.List.of(new Deliverable("Proposal"), new Deliverable("Presentation")));
        sampleAddressBook.addCourse(cs2103);
        sampleAddressBook.addCourse(is2108);
        Student[] students = getSampleStudents();
        sampleAddressBook.addStudentWithEnrolment(students[0], enrolment(students[0], cs2103, "T07",
                "Needs help with understanding inheritance."));
        sampleAddressBook.addStudentWithEnrolment(students[1], enrolment(students[1], cs2103, "F10",
                "Has been consistent with weekly deliverables."));
        sampleAddressBook.addStudentWithEnrolment(students[2], enrolment(students[2], is2108, "L01", ""));
        sampleAddressBook.addStudentWithEnrolment(students[3], enrolment(students[3], cs2103, "T1B",
                "Previously discussed project scope."));
        sampleAddressBook.addStudentWithEnrolment(students[4], enrolment(students[4], is2108, "T02",
                "Attendance has improved recently."));
        sampleAddressBook.addStudentWithEnrolment(students[5], enrolment(students[5], is2108, "F05", ""));

        return sampleAddressBook;
    }

    private static StudentCourseEnrolment enrolment(Student student, Course course,
            String tutorialClass, String notes) {
        NotesList enrolmentNotes = notes.isEmpty()
                ? new NotesList()
                : new NotesList(new Notes(notes, "MVP"));
        return new StudentCourseEnrolment(student.getId(), course.getCourseCode(), course.getSemester(),
                new TutorialClass(tutorialClass), enrolmentNotes);
    }
}
