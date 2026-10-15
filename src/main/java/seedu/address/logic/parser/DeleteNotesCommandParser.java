package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import seedu.address.logic.commands.DeleteNotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for a {@code DeleteNotesCommand}. */
public class DeleteNotesCommandParser implements Parser<DeleteNotesCommand> {

    /** Parses a command in the format {@code deletenotes /id ID}. */
    public DeleteNotesCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID);
        try {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID);
            if (!argMultimap.getPreamble().isEmpty()
                    || argMultimap.getValue(PREFIX_ID).isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeleteNotesCommand.MESSAGE_USAGE));
            }
            return new DeleteNotesCommand(ParserUtil.parseId(
                    argMultimap.getValue(PREFIX_ID).orElseThrow()));
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteNotesCommand.MESSAGE_USAGE), pe);
        }
    }
}
