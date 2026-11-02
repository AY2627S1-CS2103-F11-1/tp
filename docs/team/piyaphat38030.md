---
layout: page
title: Piyaphat Klanprayoon's Project Portfolio Page
---

### Project: TAssist

TAssist is a desktop application for university teaching assistants who manage students across project-based computing courses. It combines a command-line workflow for day-to-day updates with a graphical overview of enrolments, deliverable progress, and project-work notes. The project is written in Java with JavaFX, and is based on AddressBook-Level3.

Given below are my contributions to the project.

* **Code contributed**: My [RepoSense contribution dashboard](https://nus-cs2103-ay2627-s1.github.io/tp-dashboard/?search=CS2103-F11-1%20KLAN..PHAT&sort=groupTitle%20dsc&sortWithin=title&since=2026-09-18T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=false#/), together with the linked pull requests below, shows my functional code and test contributions.

* **Enhancements implemented**:
  * Added course initialisation with per-course deliverables, providing the prerequisite data model for enrolment and progress workflows ([PR #50](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/50)).
  * Implemented student search by partial name and student ID ([PR #63](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/63)), then followed up on post-merge review feedback ([PR #74](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/74)).
  * Completed the student-notes workflow, including adding, viewing, deleting, and persisting deliverable-specific notes ([PR #90](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/90), [PR #89](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/89)).
  * Built a compact, split-screen deliverables view grouped by course ([PR #102](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/102)).
  * Added course progress overview and filtering for enrolled students with incomplete deliverables ([PR #140](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/140), [PR #136](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/136)). These support the core TAssist use case: identifying which students need follow-up in a particular course and semester.
  * Fixed the empty progress state for students without recorded completion data, so the overview remains understandable rather than appearing broken ([PR #145](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/145)).

* **Testing and quality work**:
  * Added end-to-end multi-course workflow regression tests that cover interactions among course initialisation, enrolment, marking, viewing, and persistence ([PR #135](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/135)).
  * Reported and triaged alpha issues, including data- and UI-facing edge cases, to help the team prioritise release-candidate fixes ([issues #141–#144](https://github.com/AY2627S1-CS2103-F11-1/tp/issues?q=author%3Apiyaphat38030+label%3Aalpha)).

* **Documentation**:
  * Audited and completed the User Guide’s quick-start and feature documentation for the v1.5 release candidate ([PR #137](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/137)).
  * Contributed to the Developer Guide, including the requirements and implementation documentation needed to explain the project’s multi-course model ([PR #45](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/45)).

* **Review and collaboration**:
  * Reviewed teammates’ pull requests across feature implementation, refactoring, parser tests, and documentation. Examples include [PR #155](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/155), [PR #151](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/151), and [PR #146](https://github.com/AY2627S1-CS2103-F11-1/tp/pull/146).
  * Kept work reviewable through focused branches and follow-up PRs, rather than bundling unrelated changes into a single large merge.
