---
layout: page
title: User Guide
---

AddressBook Level 3 (AB3) is a **desktop application for managing contacts, optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, AB3 can help you manage contacts faster than traditional GUI applications.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar addressbook.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@u.nus.edu a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add n/NAME`, replace `NAME` with a value such as `John Doe`.

* Items in square brackets are optional.<br>
  For example, `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items followed by `…`​ can appear zero or more times.<br>
  For example, `[t/TAG]…​` may be omitted, or written as `t/friend` or `t/friend t/family`.

* Parameters can be in any order.<br>
  For example, if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that take no parameters, such as `help`, `list`, `exit`, and `clear`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a student: `add`

Adds a student to the address book.

Format: `add /name NAME /id ID /email EMAIL /class CLASS​`


Examples:
* `add /name John Doe /id A1234567Z /email johndoe@u.nus.edu /class T07`

### Initialising a course: `init course`

Initialises one course for a semester and records its project deliverables. A course can be initialised only once
per semester, regardless of letter case. Provide at least one unique deliverable and separate deliverable names with
commas.

Format: `init course /course COURSE_CODE /sem SEMESTER /task DELIVERABLE[, DELIVERABLE]...`

Example: `init course /course CS2103T /sem AY26/27-S1 /task v1.2, MVP, PE-D`

### Listing all students: `list`

Shows a list of all students in the address book.

Format: `list`

### Editing a student: `edit`

Edits the details of an existing student identified by their current student ID.

Format: `edit /id CURRENT_ID [/name NEW_NAME] [/newid NEW_ID] [/email NEW_EMAIL] [/class NEW_CLASS]​`

* `/id CURRENT_ID` is **required** and identifies the student whose details are to be edited.

* At least one of the optional fields (`/name`, `/newid`, `/email`, `/class`) must be provided.

* Only the specified fields will be updated. All unspecified fields, including existing project notes, will remain unchanged.

* `/newid` allows the student's ID to be updated, while `/id` identifies the existing student.

* The updated student ID and email address must not conflict with those of another student.

* If all provided values are identical to the student's existing details, no changes will be made and the user will be notified.

* If some provided values are unchanged, the remaining valid changes will still be applied. The application will indicate which fields were changed and which were already up to date.

Examples:
* `edit /id A1234567Z /email johndoe@u.nus.edu` Updates the email address of the student with ID `A1234567Z` to `johndoe@u.nus.edu`.

* `edit /id A1234567Z /name John Tan /class T07`
Updates the student's name to `John Tan` and tutorial class to `T07`.

* `edit /id A1234567Z /newid A7654321B`
Changes the student's ID from `A1234567Z` to `A7654321B`.

* `edit /id A1234567Z /newid A7654321B /email johntan@u.nus.edu`
Updates both the student's ID and email address.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive; for example, `hans` matches `Hans`.
* Keyword order does not matter; for example, `Hans Bo` matches `Bo Hans`.
* The search considers only names.
* Only full words match; for example, `Han` does not match `Hans`.
* Persons matching at least one keyword are returned (an `OR` search); for example, `Hans Bo` returns `Hans Gruber` and `Bo Yang`.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Searching students by name or ID: `search`

Searches student records by a case-insensitive name fragment or an exact student ID.

Format: `search /name NAME` or `search /id ID`

Examples:

* `search /name John Doe` shows students whose names contain `John Doe`.
* `search /id A1234567Z` shows the student with ID `A1234567Z`.

Only one of `/name` and `/id` may be supplied. The command rejects missing, repeated, or unknown parameters.
Student ID matching is case-insensitive.

### Managing student notes: `notes`, `viewnotes`, `deletenotes`

Add or replace, view, or delete the notes recorded for a student. Use the student's unique ID to avoid ambiguity.

Formats:

* `notes /id ID /notes NOTES`
* `viewnotes /id ID`
* `deletenotes /id ID`

Examples:

* `notes /id A1234567Z /notes Struggled with inheritance during consultation` adds or replaces the student's notes.
* `viewnotes /id A1234567Z` displays the notes currently recorded for the student.
* `deletenotes /id A1234567Z` deletes all notes recorded for the student.

### Marking deliverables as complete: `mark`

Marks one or more deliverables as complete for a student in a course-semester.

Format: `mark /id ID /course COURSE /sem SEMESTER /task TASK[, TASK]...` or
`mark /name NAME /course COURSE /sem SEMESTER /task TASK[, TASK]...`

Examples:

* `mark /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP`
* `mark /name John Doe /course CS2103T /sem AY26/27-S1 /task v1.1, MVP`

Use `/id` when several students have the same name. Every task must belong to
the specified course-semester and must currently be Not Done.

### Marking deliverables as incomplete: `unmark`

Marks one or more completed deliverables as incomplete for a student in a
course-semester.

Format: `unmark /id ID /course COURSE /sem SEMESTER /task TASK[, TASK]...` or
`unmark /name NAME /course COURSE /sem SEMESTER /task TASK[, TASK]...`

Examples:

* `unmark /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP`
* `unmark /name John Doe /course CS2103T /sem AY26/27-S1 /task v1.1, MVP`

### Viewing a student's deliverable progress: `view`

Shows every deliverable and its completion status for one student in a
course-semester. After a successful command, TAssist highlights the selected
student in the student list and displays their tracked courses in a compact
panel on the right. Each course heading shows the number of completed
deliverables, while a filled box marks a completed deliverable and an empty box
marks an incomplete deliverable.

Format: `view /id ID /course COURSE /sem SEMESTER` or
`view /name NAME /course COURSE /sem SEMESTER`

Examples:

* `view /id A1234567Z /course CS2103T /sem AY26/27-S1`
* `view /name John Doe /course CS2103T /sem AY26/27-S1`

Use `/id` when several students have the same name. The result lists each
deliverable in the order it was initialised. The right-hand panel groups the
student's tracked deliverables by course and semester so that their progress can
be reviewed without hiding the student list.

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

AddressBook automatically saves data after every command. You do not need to save manually.

### Editing the data file

AddressBook data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes make the data file invalid, AddressBook starts with an empty address book at the next run. The invalid file remains on disk until you run a command (AddressBook saves after every command). Still, we recommend backing up the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the data file it creates with the data file from your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add /name NAME /id ID /email EMAIL /class CLASS​` 
**Clear** | `clear`
**Delete** | `delete /id ID` or `delete /name NAME`<br> e.g., `delete /id A1234567Z`
**Edit** | `edit /id CURRENT_ID [/name NEW_NAME] [/newid NEW_ID] [/email NEW_EMAIL] [/class NEW_CLASS]​`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Search** | `search /name NAME` or `search /id ID`<br> e.g., `search /id A1234567Z`
**Mark** | `mark /id ID /course COURSE /sem SEMESTER /task TASK[, TASK]...`<br> e.g., `mark /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP`
**Unmark** | `unmark /id ID /course COURSE /sem SEMESTER /task TASK[, TASK]...`<br> e.g., `unmark /id A1234567Z /course CS2103T /sem AY26/27-S1 /task MVP`
**View progress** | `view /id ID /course COURSE /sem SEMESTER`<br> e.g., `view /id A1234567Z /course CS2103T /sem AY26/27-S1`
**List** | `list`
**Initialise course** | `init course /course COURSE_CODE /sem SEMESTER /task DELIVERABLE[, DELIVERABLE]...`<br> e.g., `init course /course CS2103T /sem AY26/27-S1 /task v1.2, MVP`
**Help** | `help`
