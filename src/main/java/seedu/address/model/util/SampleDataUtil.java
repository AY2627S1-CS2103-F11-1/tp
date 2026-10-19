package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.student.*;

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
                    new NotesList(new Notes("Used inheritance to extend Customer classes."))),

            new Student(
                    new Name("Bernice Yu"),
                    new Id("A2345678C"),
                    new Email("berniceyu@u.nus.edu"),
                    new TutorialClass("F10"),
                    new NotesList(new Notes("Implemented Customer queue as an ArrayList."))),

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
                    new NotesList(new Notes("Created classes for each individual command."))),

            new Student(
                    new Name("Irfan Ibrahim"),
                    new Id("A5678901F"),
                    new Email("irfanibrahim@u.nus.edu"),
                    new TutorialClass("T02"),
                    new NotesList(new Notes("Added a view available products feature."))),

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
