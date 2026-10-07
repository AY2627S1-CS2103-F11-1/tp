package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Id;
import seedu.address.model.student.Notes;
import seedu.address.model.student.Student;
import seedu.address.model.student.TutorialClass;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * 
     * @throws ParseException if the user input does not conform to the expected
     *                        format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL,
                PREFIX_CLASS, PREFIX_NOTES);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL, PREFIX_CLASS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ID, PREFIX_EMAIL, PREFIX_CLASS, PREFIX_NOTES);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Id id = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        TutorialClass tutorialClass = ParserUtil.parseTutorialClass(
                argMultimap.getValue(PREFIX_CLASS).get());
        Notes notes = argMultimap.getValue(PREFIX_NOTES).isPresent()
                ? ParserUtil.parseNotes(argMultimap.getValue(PREFIX_NOTES).get()) : null;

        Student student = notes == null
                ? new Student(name, id, email, tutorialClass)
                : new Student(name, id, email, tutorialClass, notes);

        return new AddCommand(student);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
