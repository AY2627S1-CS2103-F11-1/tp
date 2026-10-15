package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;

import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Id;

/** Parses arguments for a {@code NoteCommand}. */
public class NoteCommandParser implements Parser<NoteCommand> {

    /** Parses a command in the format {@code notes /id ID /notes NOTES}. */
    public NoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NOTES);

        try {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NOTES);
            if (!argMultimap.getPreamble().isEmpty()
                    || argMultimap.getValue(PREFIX_ID).isEmpty()
                    || argMultimap.getValue(PREFIX_NOTES).isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
            }
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE), pe);
        }

        String notesString = argMultimap.getValue(PREFIX_NOTES).orElseThrow();
        if (notesString.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NoteCommand.INVALID_NOTES + '\n' + NoteCommand.MESSAGE_USAGE));
        }

        Id studentId = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).orElseThrow());
        return new NoteCommand(studentId, ParserUtil.parseNotes(notesString));
    }
}
