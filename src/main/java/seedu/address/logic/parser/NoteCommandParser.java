package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Notes;

/**
 * Parses arguments for a {@code NoteCommand}.
 */
public class NoteCommandParser implements Parser<NoteCommand> {

    /**
     * Parses Note command.
     * Accepted format: notes INDEX /notes NOTES
     * @param args String containing index and notes.
     * @return NoteCommand.
     * @throws ParseException If
     */
    public NoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NOTES);

        Index index;
        try {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NOTES);
            index = ParserUtil.parseIndex(argMultimap.getPreamble());

        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NoteCommand.MESSAGE_USAGE), pe);
        }

        String notesString = argMultimap.getValue(PREFIX_NOTES).orElse("");
        if (notesString.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NoteCommand.INVALID_NOTES + '\n' + NoteCommand.MESSAGE_USAGE));
        }
        
        return new NoteCommand(index, new Notes(notesString));
    }
}
