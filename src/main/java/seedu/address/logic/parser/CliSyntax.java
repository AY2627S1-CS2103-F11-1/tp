package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions for Students*/
    public static final Prefix PREFIX_NAME = new Prefix("/name");
    public static final Prefix PREFIX_ID = new Prefix("/id");
    public static final Prefix PREFIX_EMAIL = new Prefix("/email");
    public static final Prefix PREFIX_CLASS = new Prefix("/class");
    public static final Prefix PREFIX_NOTES = new Prefix("/notes");
    /* Prefix definitions for Courses*/
    public static final Prefix PREFIX_COURSE = new Prefix("/course");
    public static final Prefix PREFIX_SEMESTER = new Prefix("/sem");
    public static final Prefix PREFIX_TASK = new Prefix("/task");

}
