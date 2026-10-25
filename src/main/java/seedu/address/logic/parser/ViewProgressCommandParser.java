package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ViewProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@code ViewProgressCommand}. */
public class ViewProgressCommandParser implements Parser<ViewProgressCommand> {

    @Override
    public ViewProgressCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.rejectUnknownParameters(
                args, PREFIX_ID, PREFIX_NAME);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(
                args, PREFIX_ID, PREFIX_NAME);

        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(Messages.MESSAGE_UNEXPECTED_TEXT);
        }

        arguments.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NAME);

        ParserUtil.validateExactlyOneOf(arguments, PREFIX_ID, PREFIX_NAME);

        if (arguments.getValue(PREFIX_ID).isPresent()) {
            ParserUtil.validateRequiredParameters(arguments, PREFIX_ID);

            return new ViewProgressCommand(ParserUtil.parseId(
                    arguments.getValue(PREFIX_ID).orElseThrow()));
        }

        ParserUtil.validateRequiredParameters(arguments, PREFIX_NAME);

        return new ViewProgressCommand(arguments.getValue(PREFIX_NAME).orElseThrow());
    }

}
