package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import seedu.address.logic.commands.NotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Id;

/** Parses arguments for a {@code NotesCommand}. */
public class NotesCommandParser implements Parser<NotesCommand> {

    /** Parses a command in the format {@code notes /id ID /notes NOTES}. */
    public NotesCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NOTES, PREFIX_TASK);

        try {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NOTES, PREFIX_TASK);
            if (!argMultimap.getPreamble().isEmpty()
                    || argMultimap.getValue(PREFIX_ID).isEmpty()
                    || argMultimap.getValue(PREFIX_NOTES).isEmpty()
                    || argMultimap.getValue(PREFIX_TASK).isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NotesCommand.MESSAGE_USAGE));
            }
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NotesCommand.MESSAGE_USAGE), pe);
        }

        String notesString = argMultimap.getValue(PREFIX_NOTES).orElseThrow();
        if (notesString.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NotesCommand.INVALID_NOTES + '\n' + NotesCommand.MESSAGE_USAGE));
        }
        String taskString = argMultimap.getValue(PREFIX_TASK).orElseThrow();
        if (taskString.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NotesCommand.INVALID_NOTES_TASK + '\n' + NotesCommand.MESSAGE_USAGE));
        }

        Id studentId = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).orElseThrow());
        return new NotesCommand(studentId, ParserUtil.parseNotes(notesString, taskString));
    }
}
