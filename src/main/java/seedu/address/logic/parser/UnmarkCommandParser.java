package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Parses arguments for {@code UnmarkCommand}. */
public class UnmarkCommandParser extends StudentProgressCommandParser<UnmarkCommand> {

    @Override
    UnmarkCommand createCommand(Id studentId, CourseCode courseCode, Semester semester,
                                List<Deliverable> deliverables) {
        return new UnmarkCommand(studentId, courseCode, semester, deliverables);
    }

    @Override
    UnmarkCommand createCommand(String studentName, CourseCode courseCode, Semester semester,
                                List<Deliverable> deliverables) {
        return new UnmarkCommand(studentName, courseCode, semester, deliverables);
    }

}
