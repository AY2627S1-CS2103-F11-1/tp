package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteNotesCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.InitCourseCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.commands.NotesCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.ViewNotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.Notes;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Id;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentMatchesQueryPredicate;
import seedu.address.model.student.TutorialClass;
import seedu.address.testutil.EditStudentDescriptorBuilder;
import seedu.address.testutil.StudentBuilder;
import seedu.address.testutil.StudentUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Student student = new StudentBuilder().build();
        String input = StudentUtil.getAddCommand(student)
                + " /course CS2103T /sem AY26/27-S1 /class T07";
        AddCommand command = (AddCommand) parser.parseCommand(input);
        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(student.getId(),
                new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                new TutorialClass("T07"), new NotesList());
        assertEquals(new AddCommand(student, List.of(enrolment)), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " /id A1234567Z");
        assertEquals(new DeleteCommand(new Id("A1234567Z")), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Student student = new StudentBuilder().build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(student).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + "/id " + student.getId() + " " + StudentUtil.getEditStudentDescriptorDetails(descriptor));
        assertEquals(new EditCommand(student.getId(), descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_initCourse() throws Exception {
        assertTrue(parser.parseCommand("init course /course CS2103T /sem AY26/27-S1 /deliverable v1.2, MVP")
                instanceof InitCourseCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertThrows(ParseException.class, () -> parser.parseCommand(ListCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_mark() throws Exception {
        assertTrue(parser.parseCommand("mark /id A1234567Z /course CS2103T /sem AY26/27-S1 /deliverable MVP")
                instanceof MarkCommand);
    }

    @Test
    public void parseCommand_notes() throws Exception {
        assertEquals(new NotesCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                new Semester("AY26/27-S1"), new Notes("Discussed inheritance", "MVP")), parser.parseCommand(
                NotesCommand.COMMAND_WORD + " /id A1234567Z /course CS2103T /sem AY26/27-S1"
                        + " /deliverable MVP /notes Discussed inheritance"));
        assertEquals(new ViewNotesCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                new Semester("AY26/27-S1")), parser.parseCommand(
                ViewNotesCommand.COMMAND_WORD + " /id A1234567Z /course CS2103T /sem AY26/27-S1"));
        assertEquals(new DeleteNotesCommand(new Id("A1234567Z"), new CourseCode("CS2103T"),
                new Semester("AY26/27-S1")), parser.parseCommand(
                DeleteNotesCommand.COMMAND_WORD + " /id A1234567Z /course CS2103T /sem AY26/27-S1"));
    }

    @Test
    public void parseCommand_search() throws Exception {
        SearchCommand command = (SearchCommand) parser.parseCommand("search /id A1234567Z");
        assertEquals(new SearchCommand(new StudentMatchesQueryPredicate(new Id("A1234567Z"))), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
