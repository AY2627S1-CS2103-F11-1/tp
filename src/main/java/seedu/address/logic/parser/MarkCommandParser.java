package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.MarkCommand;
import seedu.address.model.course.CourseCode;
import seedu.address.model.course.Deliverable;
import seedu.address.model.course.Semester;
import seedu.address.model.student.Id;

/** Parses arguments for {@code MarkCommand}. */
public class MarkCommandParser extends StudentProgressCommandParser<MarkCommand> {

    @Override
    MarkCommand createCommand(Id studentId, CourseCode courseCode, Semester semester,
                              List<Deliverable> deliverables) {
        return new MarkCommand(studentId, courseCode, semester, deliverables);
    }

    @Override
    MarkCommand createCommand(String studentName, CourseCode courseCode, Semester semester,
                              List<Deliverable> deliverables) {
        return new MarkCommand(studentName, courseCode, semester, deliverables);
    }

}
