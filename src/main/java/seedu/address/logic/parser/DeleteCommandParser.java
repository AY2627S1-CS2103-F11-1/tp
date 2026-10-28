package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses arguments for a {@code DeleteCommand}.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        ParserUtil.rejectUnknownParameters(args, PREFIX_ID, PREFIX_NAME);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME);
        boolean hasId = argMultimap.getValue(PREFIX_ID).isPresent();
        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!hasId && !hasName) {
            if (args.trim().isEmpty()) {
                throw new ParseException(String.format(Messages.MESSAGE_MISSING_IDENTIFIER, PREFIX_ID, PREFIX_NAME));
            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NAME);
        ParserUtil.validateExactlyOneOf(argMultimap, PREFIX_ID, PREFIX_NAME);

        if (hasId) {
            ParserUtil.validateRequiredParameters(argMultimap, PREFIX_ID);
            return new DeleteCommand(ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).orElseThrow()));
        }

        ParserUtil.validateRequiredParameters(argMultimap, PREFIX_NAME);
        return new DeleteCommand(argMultimap.getValue(PREFIX_NAME).orElseThrow());
    }

}
