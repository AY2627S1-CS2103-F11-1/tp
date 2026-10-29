---
layout: page
title: User Guide
---

TAssist is a **desktop application for university teaching assistants who manage
students across project-based computing courses**. It combines a command-line
workflow for fast data entry with a graphical overview of student enrolments,
notes, and project-deliverable progress.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download `TAssist.jar` from the
   [latest TAssist release](https://github.com/AY2627S1-CS2103-F11-1/tp/releases/latest).

1. Copy the file to the folder you want to use as the _home folder_ for TAssist.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar TAssist.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all students.

   * `add /name John Doe /id A1234567Z /email johndoe@u.nus.edu /course CS2103T /sem AY26/27-S1 /class T07` : Adds a student with an initial course enrolment.

   * `delete /id A1234567Z` : Deletes the student with the specified ID.

   * `clear` : Deletes all students, courses, enrolments, notes, and progress records.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add /name NAME`, replace `NAME` with a value such as `John Doe`.

* Items in square brackets are optional.<br>
  For example, `edit /id ID [/email EMAIL]` can be used as
  `edit /id A1234567Z /email john@u.nus.edu`.

* Items followed by `...` accept multiple comma-separated values.<br>
  For example, `/deliverable DELIVERABLE[, DELIVERABLE]...` can be
  `/deliverable v1.4` or `/deliverable v1.4, v1.5`.

* Parameters can be in any order unless a command explicitly documents an ordering requirement.<br>
  The `/notes` parameter for the `notes` command must be the final parameter because its value is free text.

* Commands reject unsupported or extraneous parameters with an error message. Commands such as `help`, `exit`, and
  `clear` do not take parameters, while `list` also rejects unexpected text or parameters.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Opens a read-only command reference containing the available commands, syntax,
short descriptions, and examples. For detailed explanations, refer to this User
Guide.

![help message](images/helpMessage.png)

Format: `help`


### Adding a student: `add`

Adds a student to TAssist with at least one course enrolment.

Format: `add /name NAME /id ID /email EMAIL /course COURSE[, COURSE]... /sem SEMESTER[, SEMESTER]... /class CLASS[, CLASS]...`

The course, semester and tutorial-class values are matched by position. At least
one complete enrolment is required. The referenced course-semester must already
have been initialised with `init course`.


Examples:
* `add /name John Doe /id A1234567Z /email johndoe@u.nus.edu /course CS2103T /sem AY26/27-S1 /class T07`

Multiple enrolments can be added in one command:

* `add /name John Doe /id A1234567Z /email johndoe@u.nus.edu /course CS2103T, IS2108 /sem AY26/27-S1, AY26/27-S1 /class T07, T02`

### Initialising a course: `init course`

Initialises one course for a semester and records its project deliverables. A course can be initialised only once
per semester, regardless of letter case. Provide at least one unique deliverable and separate deliverable names with
commas.

Format: `init course /course COURSE_CODE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...`

Example: `init course /course CS2103T /sem AY26/27-S1 /deliverable v1.2, MVP, PE-D`

### Listing students: `list`

Shows all students, or only students enrolled in a selected course-semester.
The filtered list can be narrowed further by tutorial class or by a deliverable
that is still incomplete.

Format: `list`

Filtered formats:

* `list /course COURSE /sem SEMESTER`
* `list /course COURSE /sem SEMESTER [/class CLASS] [/deliverable DELIVERABLE]`

Examples:

* `list /course CS2103T /sem AY26/27-S1` shows students enrolled in CS2103T for the selected semester.
* `list /course CS2103T /sem AY26/27-S1 /class T07` shows only students from tutorial T07 in that course-semester.
* `list /course CS2103T /sem AY26/27-S1 /deliverable MVP` shows enrolled students whose MVP is still incomplete.
* `list /course CS2103T /sem AY26/27-S1 /class T07 /deliverable MVP` combines the tutorial and incomplete-deliverable filters.

`/course` and `/sem` must be supplied together. `/class` and `/deliverable` can be used
only with both of them. `/deliverable` accepts exactly one deliverable tracked by that
course-semester. Students appear once even if they have other enrolments.

### Viewing a course progress overview: `overview`

Shows every student enrolled in a course-semester together with the number of
completed deliverables out of the total tracked deliverables. Students are
shown in the same order as the main student list.

Format: `overview /course COURSE /sem SEMESTER`

Example: `overview /course CS2103T /sem AY26/27-S1`

Example output:

```text
Progress overview for CS2103T (AY26/27-S1):
Alice Pauline (A1234567Y): 2/5 completed
Benson Meier (A2345678X): 4/5 completed
```

The selected course must already be initialised. This command reports progress
only and does not change students, enrolments, or completion statuses.

### Editing a student: `edit`

Edits the details of an existing student identified by their current student ID.

Format: `edit /id CURRENT_ID [/name NEW_NAME] [/newid NEW_ID] [/email NEW_EMAIL]`

* `/id CURRENT_ID` is **required** and identifies the student whose details are to be edited.

* At least one of the optional fields (`/name`, `/newid`, `/email`) must be provided.

* Only the specified fields will be updated. All unspecified fields, including existing project notes, will remain unchanged.

* `/newid` allows the student's ID to be updated, while `/id` identifies the existing student.

* The updated student ID and email address must not conflict with those of another student.

* If all provided values are identical to the student's existing details, no changes will be made and the user will be notified.

* If some provided values are unchanged, the remaining valid changes will still be applied. The application will indicate which fields were changed and which were already up to date.

Examples:
* `edit /id A1234567Z /email johndoe@u.nus.edu` Updates the email address of the student with ID `A1234567Z` to `johndoe@u.nus.edu`.

* `edit /id A1234567Z /name John Tan /email johntan@u.nus.edu`
Updates the student's name and email address.

* `edit /id A1234567Z /newid A7654321B`
Changes the student's ID from `A1234567Z` to `A7654321B`.

* `edit /id A1234567Z /newid A7654321B /email johntan@u.nus.edu`
Updates both the student's ID and email address.

### Managing course enrolments: `enrol`, `unenrol`

Use `enrol` to add an existing student to another course-semester. The course
must have been initialised first.

Format: `enrol /id ID /course COURSE /sem SEMESTER /class CLASS`

Example:

* `enrol /id A1234567Z /course IS2108 /sem AY26/27-S1 /class T02`

Use `unenrol` to remove one course-semester enrolment. Its deliverable progress
is removed as well. If it was the student's final enrolment, the student record
is removed too.

Format: `unenrol /id ID /course COURSE /sem SEMESTER`

Example:

* `unenrol /id A1234567Z /course IS2108 /sem AY26/27-S1`

### Searching students by name or ID: `search`

Searches student records by one or more names or student IDs. Name queries use
case-insensitive partial-substring matching with OR semantics: a student is
returned when their name contains at least one comma-separated query. Each
name query may contain spaces.

Format:

* `search /name NAME[, NAME]...`
* `search /id ID[, ID]...`

Examples:

* `search /name John Doe, Alex` shows students whose names contain either `John Doe` or `Alex`.
* `search /id A1234567Z, A2345678X` shows students with either ID.

Only one of `/name` and `/id` may be supplied. The command rejects missing, repeated, or unknown parameters.
Student ID matching is exact after the supplied IDs are trimmed and normalized to uppercase.

Command words and parameter prefixes are case-sensitive. Use the documented
lowercase syntax, such as `search` and `/name`.

### Managing student notes: `notes`, `view notes`, `delete notes`

Add, view, or delete the notes recorded for a student. Use the student's unique ID to avoid ambiguity.

Formats:

* `notes /id ID /course COURSE /sem SEMESTER /deliverable DELIVERABLE /notes NOTES`
* `view notes /id ID /course COURSE /sem SEMESTER`
* `delete notes /id ID /course COURSE /sem SEMESTER`

The `/notes` value consumes the remaining text. Standalone slash-prefixed parameter-like
tokens after `/notes` are rejected to prevent malformed commands from being treated as note text.

Examples:

* `notes /id A1234567Z /course CS2103T /sem AY26/27-S1 /deliverable MVP /notes Struggled with inheritance during consultation` adds a note to the selected enrolment.
* `view notes /id A1234567Z /course CS2103T /sem AY26/27-S1` displays notes for the selected enrolment.
* `delete notes /id A1234567Z /course CS2103T /sem AY26/27-S1` deletes all notes for the selected enrolment.

### Marking deliverables as complete: `mark`

Marks one or more deliverables as complete for a student in a course-semester.

Format: `mark /id ID /course COURSE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...` or
`mark /name NAME /course COURSE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...`

Examples:

* `mark /id A1234567Z /course CS2103T /sem AY26/27-S1 /deliverable MVP`
* `mark /name John Doe /course CS2103T /sem AY26/27-S1 /deliverable v1.1, MVP`

Use `/id` when several students have the same name. Every deliverable must belong to
the specified course-semester and must currently be Not Done.

### Marking deliverables as incomplete: `unmark`

Marks one or more completed deliverables as incomplete for a student in a
course-semester.

Format: `unmark /id ID /course COURSE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...` or
`unmark /name NAME /course COURSE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...`

Examples:

* `unmark /id A1234567Z /course CS2103T /sem AY26/27-S1 /deliverable MVP`
* `unmark /name John Doe /course CS2103T /sem AY26/27-S1 /deliverable v1.1, MVP`

### Viewing a student's deliverable progress: `view`

Shows every deliverable and its completion status for one student across all
their course-semester enrolments. The deliverables panel expands to display
the grouped progress details.

Format: `view /id ID` or `view /name NAME`

Examples:

* `view /id A1234567Z`
* `view /name John Doe`

Use `/id` when several students have the same name. The result lists each
deliverable as `Done` or `Not Done` in the order it was initialised.

### Deleting a student: `delete`

Deletes a student from TAssist.

Format: `delete /id ID` or `delete /name NAME`

* Use `/id` to delete a student by their unique student ID.
* Use `/name` only when the name identifies one student. If more than one student
  has that name, use the student's ID instead.

Examples:
* `delete /id A1234567Z` deletes the student with ID `A1234567Z`.
* `delete /name John Doe` deletes John Doe when there is exactly one such student.

### Clearing all entries: `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

TAssist automatically saves data after every successful command. You do not need to save manually.

### Editing the data file

TAssist data is saved automatically as `[JAR file location]/data/addressbook.json`.
Advanced users may update the JSON file directly, but should make a backup first.

**Legacy save files:** The enrolment-based data format is not compatible with older save files that stored tutorial classes or notes directly on students. If TAssist reports that an old save format was detected, first back up the file, then delete or replace `data/addressbook.json` with a compatible backup before restarting. This avoids silently losing course-specific data.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes make the data file invalid, TAssist starts with sample data at
the next run. The invalid file remains on disk until a command is run, at which
point TAssist saves its current data. Edit the file only if you are confident
that the resulting values follow the documented formats.<br>
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install TAssist on the other computer and replace its generated data file
with the `data/addressbook.json` file from your previous TAssist home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add /name NAME /id ID /email EMAIL /course COURSE[, COURSE]... /sem SEMESTER[, SEMESTER]... /class CLASS[, CLASS]...`
**Clear** | `clear`
**Delete** | `delete /id ID` or `delete /name NAME`<br> e.g., `delete /id A1234567Z`
**Delete notes** | `delete notes /id ID /course COURSE /sem SEMESTER`<br> e.g., `delete notes /id A1234567Z /course CS2103T /sem AY26/27-S1`
**Edit** | `edit /id CURRENT_ID [/name NEW_NAME] [/newid NEW_ID] [/email NEW_EMAIL]`
**Exit** | `exit`
**Search** | `search /name NAME[, NAME]...` or `search /id ID[, ID]...`<br> e.g., `search /name Alex Yeoh, Bernice Yu`
**Notes** | `notes /id ID /course COURSE /sem SEMESTER /deliverable DELIVERABLE /notes NOTES`<br> e.g., `notes /id A1234567Z /course CS2103T /sem AY26/27-S1 /deliverable MVP /notes Discussed inheritance`
**Mark** | `mark /id ID /course COURSE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...`<br> e.g., `mark /id A1234567Z /course CS2103T /sem AY26/27-S1 /deliverable MVP`
**Unmark** | `unmark /id ID /course COURSE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...`<br> e.g., `unmark /id A1234567Z /course CS2103T /sem AY26/27-S1 /deliverable MVP`
**View progress** | `view /id ID` or `view /name NAME`<br> e.g., `view /id A1234567Z`
**Course progress overview** | `overview /course COURSE /sem SEMESTER`<br> e.g., `overview /course CS2103T /sem AY26/27-S1`
**View notes** | `view notes /id ID /course COURSE /sem SEMESTER`<br> e.g., `view notes /id A1234567Z /course CS2103T /sem AY26/27-S1`
**List** | `list` or `list /course COURSE /sem SEMESTER [/class CLASS] [/deliverable DELIVERABLE]`<br> e.g., `list /course CS2103T /sem AY26/27-S1 /deliverable MVP`
**Initialise course** | `init course /course COURSE_CODE /sem SEMESTER /deliverable DELIVERABLE[, DELIVERABLE]...`<br> e.g., `init course /course CS2103T /sem AY26/27-S1 /deliverable v1.2, MVP`
**Enrol** | `enrol /id ID /course COURSE /sem SEMESTER /class CLASS`<br> e.g., `enrol /id A1234567Z /course IS2108 /sem AY26/27-S1 /class T02`
**Unenrol** | `unenrol /id ID /course COURSE /sem SEMESTER`<br> e.g., `unenrol /id A1234567Z /course IS2108 /sem AY26/27-S1`
**Help** | `help`
