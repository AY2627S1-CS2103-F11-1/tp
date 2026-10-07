package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;

public class InitCourseCommandTest {
    private static final Course CS2103 = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("v1.2"), new Deliverable("MVP")));

    @Test
    public void execute_courseAcceptedByModel_addSuccessful() throws Exception {
        ModelManager model = new ModelManager();

        CommandResult result = new InitCourseCommand(CS2103).execute(model);

        assertEquals("Course initialised: CS2103T (AY26/27-S1) with 2 deliverable(s).",
                result.getFeedbackToUser());
        assertEquals(List.of(CS2103), model.getAddressBook().getCourseList());
    }

    @Test
    public void execute_duplicateCourse_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addCourse(CS2103);

        assertThrows(CommandException.class, InitCourseCommand.MESSAGE_DUPLICATE_COURSE, () ->
                new InitCourseCommand(CS2103).execute(model));
    }

    @Test
    public void constructor_nullCourse_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new InitCourseCommand(null));
    }
}
