package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
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

/** Tests for {@code MarkCommand}. */
public class MarkCommandTest {

    private static final Course COURSE = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("MVP"), new Deliverable("Final")));
    private static final Deliverable MVP = new Deliverable("MVP");

    @Test
    public void execute_validId_success() {
        Model model = modelWithCourse();
        MarkCommand command = new MarkCommand(ALICE.getId(), COURSE.getCourseCode(), COURSE.getSemester(),
                List.of(MVP));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        StudentCourseProgress originalProgress = findAliceProgress(expectedModel);
        expectedModel.setStudentCourseProgress(originalProgress,
                originalProgress.withStatus(MVP, CompletionStatus.DONE));

        CommandResult expectedResult = new CommandResult("Marked MVP as completed for Alice Pauline.",
                new StudentDeliverablesView(ALICE, expectedModel.getStudentCourseProgressList().stream()
                        .filter(progress -> progress.getStudentId().equals(ALICE.getId())).toList()));
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_validUniqueName_success() {
        Model model = modelWithCourse();
        MarkCommand command = new MarkCommand("alice pauline", COURSE.getCourseCode(), COURSE.getSemester(),
                List.of(MVP));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        StudentCourseProgress originalProgress = findAliceProgress(expectedModel);
        expectedModel.setStudentCourseProgress(originalProgress,
                originalProgress.withStatus(MVP, CompletionStatus.DONE));

        CommandResult expectedResult = new CommandResult("Marked MVP as completed for Alice Pauline.",
                new StudentDeliverablesView(ALICE, expectedModel.getStudentCourseProgressList().stream()
                        .filter(progress -> progress.getStudentId().equals(ALICE.getId())).toList()));
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_unknownTask_throwsCommandException() {
        Model model = modelWithCourse();
        MarkCommand command = new MarkCommand(ALICE.getId(), COURSE.getCourseCode(), COURSE.getSemester(),
                List.of(new Deliverable("PE-D")));

        assertCommandFailure(command, model, "Task 'PE-D' does not exist in CS2103T AY26/27-S1.");
    }

    @Test
    public void execute_alreadyComplete_throwsCommandException() {
        Model model = modelWithCourse();
        StudentCourseProgress originalProgress = findAliceProgress(model);
        model.setStudentCourseProgress(originalProgress, originalProgress.withStatus(MVP, CompletionStatus.DONE));
        MarkCommand command = new MarkCommand(ALICE.getId(), COURSE.getCourseCode(), COURSE.getSemester(),
                List.of(MVP));

        assertCommandFailure(command, model, "MVP is already marked as complete.");
    }

    private Model modelWithCourse() {
        AddressBook addressBook = getTypicalAddressBook();
        return new ModelManager(addressBook, new UserPrefs());
    }

    private StudentCourseProgress findAliceProgress(Model model) {
        return model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.getStudentId().equals(ALICE.getId()))
                .findFirst()
                .orElseThrow();
    }
}
