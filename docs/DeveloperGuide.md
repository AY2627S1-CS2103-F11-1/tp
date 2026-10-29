
---
layout: page
title: Developer Guide
---
* Table of Contents
1. [Acknowledgements](#acknowledgements)
2. [Setting up and getting started](#setting-up-and-getting-started)
3. [Product Scope](#product-scope)
   - [Target User Profile](#target-user-profile)
   - [Value Proposition](#value-proposition)
   - [User Stories](#user-stories)
   - [Use Cases](#use-cases)
   - [Non-Functional Requirements](#non-functional-requirements)
   - [Glossary](#glossary)
4. [Design](#design)
   - [Architecture](#architecture)
   - [UI Component](#ui-component)
   - [Logic Component](#logic-component)
   - [Model Component](#model-component)
   - [Storage Component](#storage-component)
5. [Implementation](#implementation)
6. [Testing](#testing)
7. [DevOps](#devops)
8. [Documentation, logging and team contributions](#documentation-logging-and-team-contributions)
9. [Appendix](#appendix)


--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* _{List the sources of reused or adapted ideas, code, documentation, and third-party libraries here, with links to the originals.}_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in `docs/diagrams`. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete /id A1234567B`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2627S1-CS2103-F11-1/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `StudentListPanel`, `DeliverablesPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/AY2627S1-CS2103-F11-1/tp/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/AY2627S1-CS2103-F11-1/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Student` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2627S1-CS2103-F11-1/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the high-level interactions in the `Logic` component and its interaction with `Model`, using the `execute("delete /id A1234567B")` API call as an example.

![High-level interactions for deleting a student by ID](images/DeleteSequenceDiagram.png)

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a student).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2627S1-CS2103-F11-1/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="1000" />


The `Model` component,

* stores students, course-semester offerings, student-course enrolments, and deliverable-progress records.
* keeps those objects in `UniqueStudentList`, `UniqueCourseList`, `UniqueStudentCourseEnrolment`, and `UniqueStudentCourseProgressList` respectively.
* stores the students selected by the current filter, such as search results, in a separate filtered list. It exposes this list as an unmodifiable `ObservableList<Student>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

The object diagram below gives a concrete snapshot of the stored model. Alex has one global student record and separate progress records for two courses. The progress records refer to the student and course-semester using their identifying values, allowing one student to be tracked across multiple courses without duplicating the student object.

![Example objects for one student's multi-course progress](images/StudentCourseProgressObjectDiagram.png)


### Storage component

**API** : [`Storage.java`](https://github.com/AY2627S1-CS2103-F11-1/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="800" />

The `Storage` component,
* can save students, courses, deliverable-progress records, and user preferences in JSON format, and read them back into corresponding model objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* uses separate JSON adapter classes for `Student`, `Course`, and `StudentCourseProgress`.
* depends on some classes in the `Model` component because its responsibility is to save and retrieve model objects.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Course initialisation feature

The `init course` command creates the course and deliverable foundation used by later progress-tracking features.
`InitCourseCommandParser` parses the course code, semester, and comma-separated deliverables, then creates an
`InitCourseCommand`. Invalid, missing, or duplicate deliverable names are rejected before the command changes the
model.

`Course` is identified by its `CourseCode` and `Semester`, while its `Deliverable` objects represent the project
milestones for that course. `UniqueCourseList` prevents duplicate course-semester pairs. The `Model` exposes
`hasCourse` and `addCourse` so that `InitCourseCommand` can reject a duplicate without depending on
`ModelManager`'s concrete implementation.

Courses are kept alongside students in `AddressBook`. `JsonSerializableAddressBook` and `JsonAdaptedCourse` save and
restore them with the existing JSON data file, so a successfully initialised course remains available after a normal
application restart.

### Updating deliverable progress

`MarkCommand` and `UnmarkCommand` share the workflow implemented by `StudentProgressCommand`. The command first
identifies one student and one course-semester, verifies that every requested deliverable exists, and retrieves the
matching `StudentCourseProgress` record. Because progress records are immutable, the command creates updated copies
and replaces the original record in the model only after all validation succeeds.

The following activity diagram summarizes that workflow. Any failed lookup or validation returns the corresponding
`CommandException` without modifying the stored progress.

![Workflow for marking or unmarking deliverables](images/UpdateProgressActivityDiagram.png)

### Viewing deliverables feature

The `view` command displays all tracked deliverables for one student. The command accepts either a student ID or a
name. `ViewProgressCommand` first identifies exactly one student, then filters the model's
`StudentCourseProgress` records by that student's ID. This lets one command display progress from every course and
semester associated with the student.

The command returns two forms of output in one `CommandResult`:

* a textual summary for `ResultDisplay`; and
* an immutable `StudentDeliverablesView` containing the selected `Student` and their progress records.

Using a structured view object keeps the UI independent of the wording and formatting of the textual feedback.
`ViewProgressCommand` restores the full student list before returning. `MainWindow` can then focus the selected
student's card and pass the view object to `DeliverablesPanel`. The panel creates one group per course-semester, shows
the completed/total count, and renders each deliverable with a filled or empty status box.

The following sequence diagram shows the successful `view /id A1234567B` flow. Parser interactions are omitted so
the diagram can focus on how the command result reaches both sides of the split-screen UI.

![Interactions for viewing a student's deliverables](images/ViewDeliverablesSequenceDiagram.png)

#### Design consideration: passing UI data

**Alternative 1: Parse the feedback string in the UI.** This avoids adding another result type, but couples
`DeliverablesPanel` to presentation text and makes wording changes likely to break the panel.

**Alternative 2 (current choice): Attach a structured payload to `CommandResult`.** This requires the small
`StudentDeliverablesView` class, but preserves component boundaries and gives the UI strongly typed, immutable data.
This approach was chosen because it is safer to extend and easier to test.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

The target users of TAssist are university teaching assistants who:

* supervise a significant number of students across one or more tutorial classes
* oversee at least one multi-week programming project
* need to track student information and project progress over an extended period
* frequently search for and update student records
* prefer using a desktop application to manage their records
* are comfortable typing commands and using keyboard-based interactions

**Value proposition**:

The platform helps TAs efficiently track students’ attendance, participation, weaknesses, stress levels and past interactions. It also helps TAs monitor students’ project progress and record their design decisions for programming assignments.

By centralising this information, the platform allows TAs to recall previous interactions, monitor project progress and provide more contextualised support to students.


### User stories

Priorities: High (must have) - `***`, Medium (nice to have) - `**`,
Low (unlikely to have) - `*`

| Priority | As a … | I want to … | So that I can … |
| -------- | ------ | ------------ | --------------- |
| `***` | TA | view all students under my supervision | keep track of the students I manage |
| `***` | TA | add a student with their name, student ID, email address and tutorial class | maintain accurate student records |
| `***` | TA | search for a student by name or student ID | quickly retrieve the relevant student’s information |
| `***` | TA | update a student’s details | keep student information up to date |
| `***` | TA | delete a student who is no longer under my supervision | maintain an up-to-date student list |
| `***` | TA | initialise a course and its project deliverables for a semester | begin tracking students’ progress for that course |
| `***` | TA | view students’ project deliverables and completion statuses | understand and compare their progress |
| `***` | TA | mark a student’s project deliverable as completed | record the student’s progress |
| `***` | TA | unmark a completed project deliverable | correct the student’s progress record when necessary |
| `**` | TA | add a note about a student’s project work | record observations about the student’s progress or implementation |
| `**` | TA | view a student’s project-work notes | recall previous observations and interactions |
| `**` | TA | edit a student’s project-work note | correct or update information when necessary |
| `**` | TA | delete an outdated or incorrect project-work note | keep the student’s record accurate |
| `**` | TA | record a student’s attendance | identify students who may require additional support |
| `**` | TA | record a student’s participation | monitor the student’s engagement during tutorials |
| `**` | TA | record a student’s weaknesses | provide more targeted guidance |
| `**` | TA | record a student’s technical strengths | build on what the student already understands when offering guidance |
| `**` | TA | record a student’s stress level | identify students who may need additional support |
| `**` | TA | record my past interactions with a student | provide support based on previous discussions |
| `**` | TA | view students in a selected tutorial class | prepare for a specific tutorial session |
| `**` | TA | view a student’s attendance history | identify participation patterns over time |
| `**` | TA | categorise interaction notes by programming topic | identify a student’s recurring technical difficulties more quickly |
| `**` | TA | view all students flagged for follow-up | prioritise students who need additional support |
| `**` | TA | mark a follow-up as completed | know which support actions no longer need attention |
| `*` | TA | record a student’s important project design decisions | understand the reasoning behind the student’s implementation |
| `*` | TA | view a student’s project history | understand how the student’s project has developed over time |
| `*` | student | view my own project progress | understand which deliverables I have completed |
| `*` | student | update my own personal information | keep my information up to date |
| `*` | TA | generate a report of student progress | review or share an overview of student performance |
| `*` | TA | send notifications to students through the application | communicate important information conveniently |

### Use cases

**Actor** is the `TA`, unless specified otherwise.

---

**Use case: Add a student**

**MSS**

1. TA requests to add a student.
2. TAssist prompts the TA for the student's name, student ID, email address, course, semester, and tutorial class.
3. TA enters the required student information.
4. TAssist validates the entered information.
5. TAssist adds the student record.
6. TAssist displays a confirmation message and the updated student list.

   Use case ends.

**Extensions**

* 3a. One or more required fields are missing.

  * 3a1. TAssist displays an error message.

  * 3a2. TA enters the missing information.

    Use case resumes at step 4.

* 4a. The student ID is already in use.

  * 4a1. TAssist displays an error message.

    Use case resumes at step 3.

* 4b. The email address is invalid.

  * 4b1. TAssist displays an error message.

    Use case resumes at step 3.

* 4c. The course for the given semester does not exist.

  * 4c1. TAssist displays error message asking to initialise course for the given semester first.
  
    Use case resumes at step 3.

---

**Use case: Search for a student**

**MSS**

1. TA requests to search for a student.
2. TAssist prompts the TA for a student name or student ID.
3. TA enters a search term.
4. TAssist searches the student records.
5. TAssist displays the matching student records.

   Use case ends.

**Extensions**

* 4a. No matching student is found.

  * 4a1. TAssist displays an appropriate message.

    Use case ends.

* 4b. Multiple students have the same name.

  * 4b1. TAssist displays all matching students and their student IDs.

    Use case ends.

---

**Use case: Initialise a course and its project deliverables**

**MSS**

1. TA requests to initialise a course for a specified semester with a list of project deliverables.
2. TAssist validates the course, semester and deliverable details.
3. TAssist creates the course record and its project deliverables.
4. TAssist displays a confirmation.

   Use case ends.

**Extensions**

* 2a. One or more supplied details are invalid.

  * 2a1. TAssist displays an error message.

    Use case resumes at step 1.

* 3a. A record for the same course and semester already exists.

  * 3a1. TAssist displays an error message.

    Use case ends.

---

**Use case: Record a project-work note**

**Preconditions**: The student is recorded in TAssist.

**MSS**

1. TA requests to add a project-work note for a specific student.
2. TAssist validates the student and note details.
3. TAssist saves the note in the student's record.
4. TAssist displays a confirmation.

   Use case ends.

**Extensions**

* 2a. The student cannot be found or the note is invalid.

  * 2a1. TAssist displays an error message.

    Use case resumes at step 1.

---

**Use case: Update a student's deliverable completion status**

**Preconditions**: The student, course, semester, and specified deliverables are
recorded in TAssist.

**MSS**

1. TA requests to mark or unmark specified project deliverables for a student in
   a course and semester.
2. TAssist validates the student, course, semester, and deliverable details.
3. TAssist finds the student's deliverable-progress record for the specified
   course and semester.
4. TAssist updates the deliverables' completion statuses.
5. TAssist displays the updated completion statuses.

   Use case ends.

**Extensions**

* 2a. The student, course, semester, or deliverable cannot be found.

  * 2a1. TAssist displays an error message.

    Use case resumes at step 1.

* 3a. No deliverable-progress record exists for the student, course, and semester.

  * 3a1. TAssist displays an error message.

    Use case resumes at step 1.

* 4a. A requested completion status is already recorded.

  * 4a1. TAssist informs the TA that no change was made.

    Use case ends.

### Non-Functional Requirements

1.  TAssist should work on Windows, macOS and Linux, as long as Java `25` or above is installed.
2.  TAssist should support up to 1,000 student records without noticeable sluggishness during typical usage.
3.  For up to 1,000 student records, TAssist should display student search results and a selected course's deliverable completion statuses within two seconds.
4.  A TA with above-average typing speed for regular English text should be able to accomplish frequent deliverables faster using commands than using the mouse.
5.  An invalid command should not modify existing student records, project-work notes or deliverable completion statuses.
6.  Student records, project-work notes and deliverable completion statuses should be preserved after a normal application exit and restart.

### Glossary

* **Attendance record**: A record of whether a student attended a
  particular tutorial session.

* **Completion status**: Whether a student has completed a particular
  project deliverable.

* **Course**: A university course tracked in TAssist for a specified
  semester.

* **Follow-up**: A support action a TA intends to take for a student,
  which can be marked as completed after it is addressed.

* **Interaction note**: A record of a past discussion or interaction
  between a TA and a student.

* **Participation record**: A record of a student's engagement during a
  tutorial session.

* **Project design decision**: A recorded decision about how a student
  chose to design or implement part of their project.

* **Project deliverable**: A required part or milestone of a course
  project that a student is expected to complete.

* **Project history**: A chronological record of a student's progress,
  notes, and relevant changes made during a course project.

* **Project-work note**: A note recorded by a TA about a student's
  project progress, implementation, or difficulties.

* **Stress level**: A TA's recorded assessment of a student's apparent
  level of stress. The team should later define the allowed levels.

* **Student record**: The information TAssist stores for one student,
  including their personal details, tutorial class, and tracked progress.

* **TA**: A university teaching assistant who uses TAssist to manage
  students and monitor their project progress.

* **Technical strength**: A programming-related area in which a student
  demonstrates competence.

* **Technical weakness**: A programming-related area in which a student
  needs additional support.

* **Tutorial class**: A class group supervised by one or more TAs.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. _{ more test cases …​ }_

### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command, with multiple students in the list.

   1. Test case: `delete /id A1234567B`<br>
      Expected: Alex Yeoh is deleted from the list. The status message shows the deleted student's details.

   1. Test case: `delete /name Bernice Yu`<br>
      Expected: Bernice Yu is deleted from the list. The status message shows the deleted student's details.

   1. Other incorrect delete commands to try: `delete`, `delete /id`, and `delete /unknown value`.<br>
      Expected: No student is deleted. The status message shows the relevant error details.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with corrupted data files

   1. Prerequisites: Place `TAssist.jar` in an empty folder. Ensure there is no data folder inside this folder.
   2. Open a terminal and navigate to the folder containing `TAssist.jar`.
   3. Run `java -jar TAssist.jar` to launch the app.
   4. Add a project-work notes using
   `notes /id A1234567B /course CS2103T /sem AY26/27-S1 /deliverable MVP /notes Implemented add and delete but not edit`. Alex Yeoh's display should now say `2 notes`.
   5. In the folder with `TAssist.jar`, a new data folder is created. Open `addressbook.json` located in the data folder.
   6. Scroll down to enrolments and edit `"studentId" : "A1234567B",` to `"studentId" : ,`
   7. Rerun `java -jar TAssist.jar` in your terminal to launch the app.
   8. Expected: The app will launch with the sample data loaded instead of the corrupted data. Alex Yeoh's display will now show `1 notes` instead.
   9. Restore the data file by editing `"studentId" : ,` back to `"studentId" : "A1234567B",`.
   10. Relaunching the app using `java -jar TAssist.jar` in your terminal will load the correct data. You can verify this by running `view notes /id A1234567B /course CS2103T /sem AY26/27-S1`