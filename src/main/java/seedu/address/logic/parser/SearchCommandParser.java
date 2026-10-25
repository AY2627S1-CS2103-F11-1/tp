package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Id;
import seedu.address.model.student.StudentMatchesQueryPredicate;

/** Parses input arguments and creates a {@code SearchCommand}. */
public class SearchCommandParser implements Parser<SearchCommand> {
    public static final String MESSAGE_BOTH_IDENTIFIERS = "Please search using either "
            + PREFIX_NAME + " or " + PREFIX_ID + ", not both.";

    @Override
    public SearchCommand parse(String args) throws ParseException {
        ParserUtil.rejectUnknownParameters(args, PREFIX_ID, PREFIX_NAME);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID);
        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasId = argMultimap.getValue(PREFIX_ID).isPresent();

        if (!hasName && !hasId) {
            if (args.trim().isEmpty()) {
                throw new ParseException(String.format(Messages.MESSAGE_MISSING_IDENTIFIER, PREFIX_NAME, PREFIX_ID));
            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }
        ParserUtil.rejectDuplicateParameters(argMultimap, PREFIX_NAME, PREFIX_ID);
        ParserUtil.validateExactlyOneOf(argMultimap,
                String.format(Messages.MESSAGE_MISSING_IDENTIFIER, PREFIX_NAME, PREFIX_ID), MESSAGE_BOTH_IDENTIFIERS,
                PREFIX_NAME, PREFIX_ID);

        if (hasName) {
            ParserUtil.validateRequiredParameters(argMultimap, PREFIX_NAME);
            return new SearchCommand(new StudentMatchesQueryPredicate(
                    argMultimap.getValue(PREFIX_NAME).orElseThrow()));
        }

        ParserUtil.validateRequiredParameters(argMultimap, PREFIX_ID);
        Id parsedId = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).orElseThrow());
        return new SearchCommand(new StudentMatchesQueryPredicate(parsedId));
    }

}
