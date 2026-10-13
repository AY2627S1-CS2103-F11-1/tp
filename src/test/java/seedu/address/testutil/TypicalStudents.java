package seedu.address.testutil;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.student.Student;

/** A utility class containing typical {@link Student} objects for tests. */
public final class TypicalStudents {

    public static final Student ALICE = new StudentBuilder().withName("Alice Pauline")
            .withId("A1234567Y").withEmail("alice@u.nus.edu").withTutorialClass("T07")
            .withNotes("Needs help with testing").build();
    public static final Student BENSON = new StudentBuilder().withName("Benson Meier")
            .withId("A2345678X").withEmail("benson@u.nus.edu").withTutorialClass("F10").withNotes().build();
    public static final Student CARL = new StudentBuilder().withName("Carl Kurz")
            .withId("A3456789W").withEmail("carl@u.nus.edu").withTutorialClass("L01").withNotes().build();
    public static final Student DANIEL = new StudentBuilder().withName("Daniel Meier")
            .withId("A4567890V").withEmail("daniel@u.nus.edu").withTutorialClass("T02").withNotes().build();
    public static final Student ELLE = new StudentBuilder().withName("Elle Meyer")
            .withId("A5678901U").withEmail("elle@u.nus.edu").withTutorialClass("T03").build();
    public static final Student FIONA = new StudentBuilder().withName("Fiona Kunz")
            .withId("A6789012T").withEmail("fiona@u.nus.edu").withTutorialClass("F04").build();
    public static final Student HOON = new StudentBuilder().withName("Hoon Meier")
            .withId("A7890123S").withEmail("hoon@u.nus.edu").withTutorialClass("T05").build();
    public static final Student IDA = new StudentBuilder().withName("Ida Mueller")
            .withId("A8901234R").withEmail("ida@u.nus.edu").withTutorialClass("F06").build();
    public static final Student AMY = new StudentBuilder().withName("Amy Bee")
            .withId("A9012345Q").withEmail("amy@u.nus.edu").withTutorialClass("T07").build();
    public static final Student BOB = new StudentBuilder().withName("Bob Choo")
            .withId("A0123456P").withEmail("bob@u.nus.edu").withTutorialClass("F10").build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier";

    private TypicalStudents() {
    }

    /** Returns an tutorialClass book containing the typical students. */
    public static AddressBook getTypicalAddressBook() {
        AddressBook addressBook = new AddressBook();
        for (Student student : getTypicalStudents()) {
            addressBook.addStudent(student);
        }
        return addressBook;
    }

    /** Returns the standard list of students used by tests. */
    public static List<Student> getTypicalStudents() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL));
    }
}
