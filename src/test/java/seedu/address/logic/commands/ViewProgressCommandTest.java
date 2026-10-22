package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import seedu.address.model.student.Id;
import seedu.address.model.student.Student;
import seedu.address.testutil.StudentBuilder;

/** Tests for {@code ViewProgressCommand}. */
public class ViewProgressCommandTest {

    private static final Course CS2103T = new Course(new CourseCode("CS2103T"), new Semester("AY26/27-S1"),
            List.of(new Deliverable("v1.1"), new Deliverable("MVP")));
    private static final Course CS2030S = new Course(new CourseCode("CS2030S"), new Semester("AY26/27-S2"),
            List.of(new Deliverable("Lab 1"), new Deliverable("PE")));
    private static final Deliverable MVP = new Deliverable("MVP");

    @Test
    public void execute_validId_displaysAllCourseProgress() {
        Model model = modelWithCourses();
        markMvpDone(model);
        ViewProgressCommand command = new ViewProgressCommand(ALICE.getId());

        String expectedMessage = String.format("Progress for Alice Pauline (%s):\n"
                + "CS2103T AY26/27-S1:\n[Not Done] v1.1\n[Done] MVP\n\n"
                + "CS2030S AY26/27-S2:\n[Not Done] Lab 1\n[Not Done] PE", ALICE.getId());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(command, model, new CommandResult(expectedMessage,
                createDeliverablesView(model)), expectedModel);
    }

    @Test
    public void execute_validUniqueNameIgnoringCase_displaysAllCourseProgress() {
        Model model = modelWithCourses();
        ViewProgressCommand command = new ViewProgressCommand("alice pauline");

        String expectedMessage = String.format("Progress for Alice Pauline (%s):\n"
                + "CS2103T AY26/27-S1:\n[Not Done] v1.1\n[Not Done] MVP\n\n"
                + "CS2030S AY26/27-S2:\n[Not Done] Lab 1\n[Not Done] PE", ALICE.getId());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(command, model, new CommandResult(expectedMessage,
                createDeliverablesView(model)), expectedModel);
    }

    @Test
    public void execute_studentWithNoCourses_displaysNoProgressMessage() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        ViewProgressCommand command = new ViewProgressCommand(ALICE.getId());

        String expectedMessage = String.format(ViewProgressCommand.MESSAGE_NO_PROGRESS,
                ALICE.getName(), ALICE.getId());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(command, model, new CommandResult(expectedMessage,
                createDeliverablesView(model)), expectedModel);
    }

    @Test
    public void execute_studentWithMultipleCourses_deliverablesViewContainsEveryCourse() throws Exception {
        Model model = modelWithCourses();
        ViewProgressCommand command = new ViewProgressCommand(ALICE.getId());

        StudentDeliverablesView view = command.execute(model).getStudentDeliverablesView().orElseThrow();

        assertEquals(List.of(CS2103T.getCourseCode(), CS2030S.getCourseCode()),
                view.getCourseProgresses().stream().map(StudentCourseProgress::getCourseCode).toList());
    }

    @Test
    public void execute_studentExcludedByActiveFilter_restoresStudentList() throws Exception {
        Model model = modelWithCourses();
        model.updateFilteredStudentList(student -> !student.getId().equals(ALICE.getId()));
        ViewProgressCommand command = new ViewProgressCommand(ALICE.getId());

        command.execute(model);

        assertEquals(model.getAddressBook().getStudentList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_unknownId_throwsCommandException() {
        Model model = modelWithCourses();
        ViewProgressCommand command = new ViewProgressCommand(new Id("A9999999Z"));

        assertCommandFailure(command, model,
                String.format(StudentProgressCommand.MESSAGE_STUDENT_ID_NOT_FOUND, "A9999999Z"));
    }

    @Test
    public void execute_unknownName_throwsCommandException() {
        Model model = modelWithCourses();
        ViewProgressCommand command = new ViewProgressCommand("Unknown Student");

        assertCommandFailure(command, model,
                String.format(StudentProgressCommand.MESSAGE_STUDENT_NAME_NOT_FOUND, "Unknown Student"));
    }

    @Test
    public void execute_duplicateName_throwsCommandException() {
        Model model = modelWithCourses();
        Student anotherAlice = new StudentBuilder(ALICE)
                .withId("A7654321Z")
                .withEmail("alice2@u.nus.edu")
                .build();
        model.addStudent(anotherAlice);
        ViewProgressCommand command = new ViewProgressCommand(ALICE.getName().fullName);

        assertCommandFailure(command, model, StudentProgressCommand.MESSAGE_MULTIPLE_STUDENTS_WITH_NAME);
    }

    private Model modelWithCourses() {
        AddressBook addressBook = getTypicalAddressBook();
        addressBook.addCourse(CS2103T);
        addressBook.addCourse(CS2030S);
        return new ModelManager(addressBook, new UserPrefs());
    }

    private void markMvpDone(Model model) {
        StudentCourseProgress originalProgress = model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.isFor(ALICE.getId(), CS2103T.getCourseCode(), CS2103T.getSemester()))
                .findFirst()
                .orElseThrow();
        model.setStudentCourseProgress(originalProgress, originalProgress.withStatus(MVP, CompletionStatus.DONE));
    }

    private StudentDeliverablesView createDeliverablesView(Model model) {
        List<StudentCourseProgress> progresses = model.getStudentCourseProgressList().stream()
                .filter(progress -> progress.getStudentId().equals(ALICE.getId()))
                .toList();
        return new StudentDeliverablesView(ALICE, progresses);
    }
}
