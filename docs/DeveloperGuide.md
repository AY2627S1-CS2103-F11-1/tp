# Developer Guide

## Target Users

University teaching assistants (TAs) who supervise computing modules with at least one multi-week coding project.

## Value Proposition

The platform helps TAs efficiently track students’ attendance, participation, weaknesses, stress levels and past interactions. It also helps TAs monitor students’ project progress and record their design decisions for programming assignments.

By centralising this information, the platform allows TAs to recall previous interactions, monitor project progress and provide more contextualised support to students.

## User Stories

The user stories are prioritised using the MoSCoW method:

- **Must-have**: Essential for the core product.
- **Should-have**: Important, but the product can function without it.
- **Could-have**: Useful additional functionality.
- **Won’t-have**: Not planned for the current product scope.

### Student Management

#### Must-have

- As a TA, I can view all students under my supervision, including their name, student ID, email address and tutorial class, so that I can keep track of the students I manage.

- As a TA, I can add a student with their name, student ID, email address and tutorial class, so that I can maintain accurate student records.

- As a TA, I can search for a student by name or student ID, so that I can quickly retrieve the relevant student record.

- As a TA, I can update a student’s name, student ID, email address or tutorial class, so that I can keep the student’s information up to date.

- As a TA, I can delete a student who is no longer in my tutorial class, so that I only keep track of students I currently supervise.

### Course and Deliverable Management

#### Must-have

- As a TA, I can initialise a course and its project deliverables for a particular semester, so that I can track students’ progress for that course.

- As a TA, I can view students’ project deliverables for a selected course and semester, together with their completion statuses, so that I can understand and compare their progress across the project.

- As a TA, I can mark a project deliverable as completed for a student, so that I can record the student’s progress.

- As a TA, I can unmark a project deliverable as completed for a student, so that I can correct the student’s progress record when necessary.

### Project-Work Notes

#### Should-have

- As a TA, I can add a note about a student’s project work, so that I can record observations about the student’s project direction, implementation or progress.

- As a TA, I can view a student’s project-work notes, so that I can recall previous observations and interactions with the student.

- As a TA, I can edit a student’s project-work note, so that I can correct or update information when necessary.

- As a TA, I can delete an outdated or incorrect project-work note, so that the student’s record remains accurate.

### Student Support Tracking

#### Could-have

- As a TA, I can record a student’s attendance, so that I can identify students who may require additional support.

- As a TA, I can record a student’s participation, so that I can monitor the student’s engagement during tutorials.

- As a TA, I can record a student’s weaknesses, so that I can provide more targeted guidance.

- As a TA, I can record a student’s stress level, so that I can identify students who may need additional support.

- As a TA, I can record past interactions with a student, so that I can provide support based on the student’s previous circumstances and discussions.

### Project Context

#### Should-have

- As a TA, I can record a student’s important project design decisions, so that I can understand the reasoning behind the student’s implementation.

- As a TA, I can view a student’s project history, so that I can review how the student’s project has developed over time.

### Future Scope

#### Won’t-have for the current version

- As a student, I can log in and view my own project progress.

- As a student, I can update my own personal information.

- As a TA, I can send notifications directly to students through the application.

- As a TA, I can generate formal reports of student performance.


## Use Case: Add a Student

**Actor:** TA

**Precondition:**
- The application is running.
- The TA is on the main screen.

**Main Success Scenario:**
1. The TA enters the `add student` command.
2. The system prompts the TA for the student’s name, ID, email address, tutorial class and optional notes.
3. The TA enters the requested information.
4. The system validates the student ID and email address.
5. The system checks that the student ID is not already in use.
6. The system creates and stores the student record.
7. The system displays a confirmation message.
8. The new student appears in the student list.

**Extensions:**
- If the student ID already exists, the system rejects the record and informs the TA.
- If the email address is invalid, the system asks the TA to enter it again.
- If a required field is missing, the system informs the TA which field must be completed.

## Non-Functional Requirements

### Compatibility

- The application should run on Windows, macOS and Linux operating systems.

### Technical Environment

- The application should be developed using Java 25.

- The application should use a JavaFX version compatible with Java 25.

### Performance

- The application should display student search results within two seconds under normal operating conditions.

- The application should display the project deliverables and completion statuses for a selected course and semester within two seconds under normal operating conditions.

### Data Integrity

- The system should prevent duplicate student IDs from being stored.

- The system should validate required student information before saving a student record.

### Usability

- The application should provide a graphical user interface that allows a TA to manage student records and project deliverables without requiring knowledge of the underlying database or source code.

### Reliability

- The application should preserve student records, project-work notes and deliverable completion statuses after the application is closed and reopened.

## Glossary

| Term | Definition |
|---|---|
| **TA** | A teaching assistant who manages student information and monitors project progress in the system. |
| **Student** | An individual enrolled in the course and supervised by the TA. |
| **Student record** | The collection of information stored about a student, including their name, student ID, email address, tutorial class and notes. |
| **Student ID** | The unique identifier assigned to each student. No two students should have the same student ID. |
| **Tutorial class** | The tutorial group to which a student belongs. |
| **Course** | An academic subject for which the TA supervises students and monitors project deliverables. |
| **Semester** | A specific academic period in which a course is conducted. |
| **Project** | The overall piece of work completed by students for a course. |
| **Project deliverable** | A specific item or milestone that a student is expected to complete as part of the project. |
| **Completion status** | An indication of whether a student has completed a particular project deliverable. |
| **Mark as completed** | The action of recording that a student has completed a project deliverable. |
| **Unmark as completed** | The action of changing a deliverable’s status from completed to not completed. |
| **Initialise** | The action of creating a course and setting up its project deliverables for a particular semester. |
| **Project-work notes** | Comments recorded by the TA about a student’s project direction, implementation or progress. |
| **Search** | The process of finding a student record using the student’s name or student ID. |
| **Authorised user** | A user who has permission to access and modify the system’s student and project information. |
| **Active student** | A student who is currently supervised by the TA and whose record is maintained in the system. |
