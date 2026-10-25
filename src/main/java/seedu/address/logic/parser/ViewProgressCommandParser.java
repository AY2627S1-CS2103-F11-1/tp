package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.ViewProgressCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@code ViewProgressCommand}. */
public class ViewProgressCommandParser implements Parser<ViewProgressCommand> {

    public static final String MESSAGE_MISSING_STUDENT = "Missing required parameter: " + PREFIX_ID + " or "
            + PREFIX_NAME;
    public static final String MESSAGE_BOTH_IDENTIFIERS = "Please specify either /id or /name, not both.";
    public static final String MESSAGE_UNEXPECTED_TEXT = "Unexpected text found before parameter.";

    @Override
    public ViewProgressCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.rejectUnknownParameters(
                args, PREFIX_ID, PREFIX_NAME);

        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(
                args, PREFIX_ID, PREFIX_NAME);

        if (!arguments.getPreamble().isEmpty()) {
            throw new ParseException(MESSAGE_UNEXPECTED_TEXT);
        }

        arguments.verifyNoDuplicatePrefixesFor(
                PREFIX_ID, PREFIX_NAME);

        boolean hasId = arguments.getValue(PREFIX_ID).isPresent();
        boolean hasName = arguments.getValue(PREFIX_NAME).isPresent();

        if (!hasId && !hasName) {
            throw new ParseException(MESSAGE_MISSING_STUDENT);
        }

        if (hasId && hasName) {
            throw new ParseException(MESSAGE_BOTH_IDENTIFIERS);
        }

        if (hasId) {
            ParserUtil.validateRequiredParameters(arguments, PREFIX_ID);

            return new ViewProgressCommand(ParserUtil.parseId(
                    arguments.getValue(PREFIX_ID).orElseThrow()));
        }

        ParserUtil.validateRequiredParameters(arguments, PREFIX_NAME);

        return new ViewProgressCommand(arguments.getValue(PREFIX_NAME).orElseThrow());
    }

}
