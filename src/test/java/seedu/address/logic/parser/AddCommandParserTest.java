package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalStudents.BOB;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Semester;
import seedu.address.model.enrolment.NotesList;
import seedu.address.model.enrolment.StudentCourseEnrolment;
import seedu.address.model.student.Email;
import seedu.address.model.student.Id;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;
import seedu.address.testutil.StudentBuilder;

/** Unit tests for {@link AddCommandParser}. */
public class AddCommandParserTest {

    private static final String COURSE_DESC = " /course CS2103T";
    private static final String SEMESTER_DESC = " /sem AY26/27-S1";
    private static final String VALID_ENROLMENT = COURSE_DESC + SEMESTER_DESC + CLASS_DESC_BOB;

    private final AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expectedStudent = new StudentBuilder(BOB).build();
        StudentCourseEnrolment enrolment = new StudentCourseEnrolment(expectedStudent.getId(),
                new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
                new TutorialClass("F10"), new NotesList());
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + VALID_ENROLMENT,
                new AddCommand(expectedStudent, List.of(enrolment)));
    }

    @Test
    public void parse_duplicateParameter_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + NAME_DESC_BOB + ID_DESC_BOB
                        + EMAIL_DESC_BOB + VALID_ENROLMENT,
                "Multiple values specified for the following single-valued field(s): /name");
    }

    @Test
    public void parse_missingRequiredParameter_failure() {
        assertParseFailure(parser, ID_DESC_BOB + EMAIL_DESC_BOB + VALID_ENROLMENT,
                "Missing required parameter(s): /name");
        assertParseFailure(parser, NAME_DESC_BOB + EMAIL_DESC_BOB + VALID_ENROLMENT,
                "Missing required parameter(s): /id");
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + VALID_ENROLMENT,
                "Missing required parameter(s): /email");
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB + CLASS_DESC_BOB,
                "Missing required parameter(s): /course, /sem");
    }

    @Test
    public void parse_emptyParameter_failure() {
        assertParseFailure(parser, " /name /id A0123456P" + EMAIL_DESC_BOB + VALID_ENROLMENT,
                "Empty parameter value(s): /name");
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + " /email" + VALID_ENROLMENT,
                "Empty parameter value(s): /email");
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB + EMAIL_DESC_BOB + VALID_ENROLMENT,
                Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_ID_DESC + EMAIL_DESC_BOB + VALID_ENROLMENT,
                Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + INVALID_EMAIL_DESC + VALID_ENROLMENT,
                Email.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_unknownParameter_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                        + VALID_ENROLMENT + " /phone 12345678",
                "Unknown parameter: /phone");
    }

    @Test
    public void parse_mismatchedEnrolmentValues_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + EMAIL_DESC_BOB
                        + " /course CS2103T,CS2106 /sem AY26/27-S1" + CLASS_DESC_BOB,
                AddCommandParser.MESSAGE_MISMATCHED_ENROLMENTS);
    }
}
