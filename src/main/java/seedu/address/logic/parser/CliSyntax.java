package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple
 * commands
 */
public class CliSyntax {

    /* Prefix definitions for Students */
    public static final Prefix PREFIX_NAME = new Prefix("/name");
    public static final Prefix PREFIX_ID = new Prefix("/id");
    public static final Prefix PREFIX_NEW_ID = new Prefix("/newid");
    public static final Prefix PREFIX_EMAIL = new Prefix("/email");
    public static final Prefix PREFIX_CLASS = new Prefix("/class");
    public static final Prefix PREFIX_NOTES = new Prefix("/notes");
    /* Prefix definitions for Courses */
    public static final Prefix PREFIX_COURSE = new Prefix("/course");
    public static final Prefix PREFIX_SEMESTER = new Prefix("/sem");
    public static final Prefix PREFIX_DELIVERABLE = new Prefix("/deliverable");

    /** Common argument labels used in command usage messages. */
    public static final String ARGUMENT_ID = "ID";
    public static final String ARGUMENT_NEW_ID = "NEW_ID";
    public static final String ARGUMENT_NAME = "NAME";
    public static final String ARGUMENT_EMAIL = "EMAIL";
    public static final String ARGUMENT_CLASS = "CLASS";
    public static final String ARGUMENT_NOTES = "NOTES";
    public static final String ARGUMENT_COURSE = "COURSE";
    public static final String ARGUMENT_SEMESTER = "SEMESTER";
    public static final String ARGUMENT_DELIVERABLE = "DELIVERABLE";

    /** Common example values used in command usage messages. */
    public static final String EXAMPLE_ID = "A1234567Z";
    public static final String EXAMPLE_NAME = "John Doe";
    public static final String EXAMPLE_EMAIL = "johndoe@u.nus.edu";
    public static final String EXAMPLE_COURSE = "CS2103T";
    public static final String EXAMPLE_COURSES = "CS2103T, CS1231";
    public static final String EXAMPLE_SEMESTER = "AY26/27-S1";
    public static final String EXAMPLE_SEMESTERS = "AY26/27-S1, AY26/27-S1";
    public static final String EXAMPLE_CLASS = "T07";
    public static final String EXAMPLE_CLASSES = "T07, T02";
    public static final String EXAMPLE_DELIVERABLE = "MVP";
    public static final String EXAMPLE_DELIVERABLES = "v1.1, v1.2, MVP";

}
