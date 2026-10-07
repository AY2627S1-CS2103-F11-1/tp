package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * Contains utility methods for populating AddressBook with sample data.
 */
public class SampleDataUtil {

    public static Student[] getSampleStudents() {
        return new Student[] {
            new Student(
                    new Name("Alex Yeoh"),
                    new Id("A1234567B"),
                    new Email("alexyeoh@u.nus.edu"),
                    new TutorialClass("T07"),
                    new Notes("Needs help with understanding inheritance.")),

            new Student(
                    new Name("Bernice Yu"),
                    new Id("A2345678C"),
                    new Email("berniceyu@u.nus.edu"),
                    new TutorialClass("F10"),
                    new Notes("Has been consistent with weekly deliverables.")),

            new Student(
                    new Name("Charlotte Oliveiro"),
                    new Id("A3456789D"),
                    new Email("charlotte@u.nus.edu"),
                    new TutorialClass("L01")),

            new Student(
                    new Name("David Li"),
                    new Id("A4567890E"),
                    new Email("davidli@u.nus.edu"),
                    new TutorialClass("T1B"),
                    new Notes("Previously discussed project scope.")),

            new Student(
                    new Name("Irfan Ibrahim"),
                    new Id("A5678901F"),
                    new Email("irfanibrahim@u.nus.edu"),
                    new TutorialClass("T02"),
                    new Notes("Attendance has improved recently.")),

            new Student(
                    new Name("Roy Balakrishnan"),
                    new Id("A6789012G"),
                    new Email("royb@u.nus.edu"),
                    new TutorialClass("F05"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAddressBook = new AddressBook();

        for (Student sampleStudent : getSampleStudents()) {
            sampleAddressBook.addStudent(sampleStudent);
        }

        return sampleAddressBook;
    }
}