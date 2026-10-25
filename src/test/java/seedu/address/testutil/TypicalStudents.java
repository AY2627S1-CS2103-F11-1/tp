package seedu.address.testutil;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/** A utility class containing typical {@link Student} objects for tests. */
public final class TypicalStudents {

    public static final Student ALICE = new StudentBuilder().withName("Alice Pauline")
            .withId("A1234567Y").withEmail("alice@u.nus.edu").build();
    public static final Student BENSON = new StudentBuilder().withName("Benson Meier")
            .withId("A2345678X").withEmail("benson@u.nus.edu").build();
    public static final Student CARL = new StudentBuilder().withName("Carl Kurz")
            .withId("A3456789W").withEmail("carl@u.nus.edu").build();
    public static final Student DANIEL = new StudentBuilder().withName("Daniel Meier")
            .withId("A4567890V").withEmail("daniel@u.nus.edu").build();
    public static final Student ELLE = new StudentBuilder().withName("Elle Meyer")
            .withId("A5678901U").withEmail("elle@u.nus.edu").build();
    public static final Student FIONA = new StudentBuilder().withName("Fiona Kunz")
            .withId("A6789012T").withEmail("fiona@u.nus.edu").build();
    public static final Student HOON = new StudentBuilder().withName("Hoon Meier")
            .withId("A7890123S").withEmail("hoon@u.nus.edu").build();
    public static final Student IDA = new StudentBuilder().withName("Ida Mueller")
            .withId("A8901234R").withEmail("ida@u.nus.edu").build();
    public static final Student AMY = new StudentBuilder().withName("Amy Bee")
            .withId("A9012345Q").withEmail("amy@u.nus.edu").build();
    public static final Student BOB = new StudentBuilder().withName("Bob Choo")
            .withId("A0123456P").withEmail("bob@u.nus.edu").build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier";

    private TypicalStudents() {
    }

    /** Returns an address book containing the typical students and enrolments. */
    public static AddressBook getTypicalAddressBook() {
        AddressBook addressBook = new AddressBook();
        Course course = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                List.of(new Deliverable("MVP"), new Deliverable("Final")));
        addressBook.addCourse(course);
        addEnrolment(addressBook, ALICE, course, "T07", "Needs help with testing");
        addEnrolment(addressBook, BENSON, course, "F10", "");
        addEnrolment(addressBook, CARL, course, "L01", "");
        addEnrolment(addressBook, DANIEL, course, "T02", "");
        return addressBook;
    }

    private static void addEnrolment(AddressBook addressBook, Student student, Course course,
            String tutorialClass, String notes) {
        addressBook.addStudentWithEnrolment(student, new StudentCourseEnrolment(student.getId(),
                course.getCourseCode(), course.getSemester(), new TutorialClass(tutorialClass),
                notes.isEmpty() ? new NotesList()
                        : new NotesList(new Notes(notes, "MVP"))));
    }

    /** Returns the standard list of students used by tests. */
    public static List<Student> getTypicalStudents() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL));
    }
}
