package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.progress.CompletionStatus;
import seedu.address.model.progress.StudentCourseProgress;

/** Tests for {@code ViewProgressCommand}. */
public class ViewProgressCommandTest {

    private static final Course COURSE = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("MVP"), new Deliverable("Final")));
    private static final Deliverable MVP = new Deliverable("MVP");

    @Test
    public void execute_validId_displaysProgress() {
        Model model = modelWithCourse();
        markMvpDone(model);
        ViewProgressCommand command = new ViewProgressCommand(ALICE.getId());

        String expectedMessage = String.format("Progress for Alice Pauline (%s):\nCS2103T AY26/27-S1:\n"
                + "[Done] MVP\n[Not Done] Final", ALICE.getId());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        StudentCourseProgress progress = model.getStudentCourseProgressList().stream()
                .filter(candidate -> candidate.getStudentId().equals(ALICE.getId())).findFirst().orElseThrow();
        CommandResult expectedResult = new CommandResult(expectedMessage,
                new StudentDeliverablesView(ALICE, List.of(progress)));
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_validUniqueName_displaysProgress() {
        Model model = modelWithCourse();
        ViewProgressCommand command = new ViewProgressCommand("alice pauline");

        String expectedMessage = String.format("Progress for Alice Pauline (%s):\nCS2103T AY26/27-S1:\n"
                + "[Not Done] MVP\n[Not Done] Final", ALICE.getId());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        StudentCourseProgress progress = model.getStudentCourseProgressList().stream()
                .filter(candidate -> candidate.getStudentId().equals(ALICE.getId())).findFirst().orElseThrow();
        CommandResult expectedResult = new CommandResult(expectedMessage,
                new StudentDeliverablesView(ALICE, List.of(progress)));
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_studentWithNoProgress_displaysEmptyState() {
        Model model = modelWithCourse();
        model.setStudentCourseProgresses(List.of());
        ViewProgressCommand command = new ViewProgressCommand(ALICE.getId());
        assertCommandSuccess(command, model,
                new CommandResult("No deliverable progress found for Alice Pauline (A1234567Y)."),
                new ModelManager(model.getAddressBook(), new UserPrefs()));
    }

    private Model modelWithCourse() {
        AddressBook addressBook = getTypicalAddressBook();
        return new ModelManager(addressBook, new UserPrefs());
    }

    private void markMvpDone(Model model) {
        StudentCourseProgress originalProgress = model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.getStudentId().equals(ALICE.getId()))
                .findFirst()
                .orElseThrow();
        model.setStudentCourseProgress(originalProgress, originalProgress.withStatus(MVP, CompletionStatus.DONE));
    }
}
