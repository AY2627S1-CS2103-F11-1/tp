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

/** Tests for {@code UnmarkCommand}. */
public class UnmarkCommandTest {

    private static final Course COURSE = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("MVP")));
    private static final Deliverable MVP = new Deliverable("MVP");

    @Test
    public void execute_completedDeliverable_success() {
        Model model = modelWithCompletedMvp();
        UnmarkCommand command = new UnmarkCommand(ALICE.getId(), COURSE.getCourseCode(), COURSE.getSemester(),
                List.of(MVP));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        StudentCourseProgress originalProgress = findAliceProgress(expectedModel);
        expectedModel.setStudentCourseProgress(originalProgress,
                originalProgress.withStatus(MVP, CompletionStatus.NOT_DONE));

        assertCommandSuccess(command, model, "Marked MVP as incomplete for Alice Pauline.", expectedModel);
    }

    @Test
    public void execute_alreadyIncomplete_throwsCommandException() {
        AddressBook addressBook = getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());
        UnmarkCommand command = new UnmarkCommand(ALICE.getId(), COURSE.getCourseCode(), COURSE.getSemester(),
                List.of(MVP));

        assertCommandFailure(command, model, "MVP is already marked as incomplete.");
    }

    private Model modelWithCompletedMvp() {
        AddressBook addressBook = getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());
        StudentCourseProgress originalProgress = findAliceProgress(model);
        model.setStudentCourseProgress(originalProgress, originalProgress.withStatus(MVP, CompletionStatus.DONE));
        return model;
    }

    private StudentCourseProgress findAliceProgress(Model model) {
        return model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.getStudentId().equals(ALICE.getId()))
                .findFirst()
                .orElseThrow();
    }
}
