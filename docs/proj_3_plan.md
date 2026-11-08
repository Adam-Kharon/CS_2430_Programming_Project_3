# CS 2430 — Project 3 Plan

Reminder\* Update at least twice per project; for each “Done” task, include an evidence pointer.

## 1\) Project Information

* Team Name: Team 2  
* Team Members: Adam Sarraj, Danielle Major, Mustafa Alrubaye  
* Course / Section: CS 2430 / 001  
* Project Name: Programming Project 3, Optimal selection \- Fall 2026

* Start Date: October 25th 2026  
* Checkpoint Date (mid-project): November 8th 2026  
* Due Date: November 15th 2026

## 2\) Team & Roles (assigned for this project)

| Team Member | Role  | Primary Responsibilities  | General Availability / Notes |
| :---- | :---- | :---- | :---- |
| Danielle Major | Implementation Lead | Owns core algorithm implementation; integrates modules; maintains main branch health. | Monday - Thursday after 3pm, Friday/Saturday/Sunday most of the day|
| Adam Sarraj  | Verification Lead | Creates test plan; edge cases; validates outputs; documents verification evidence. | Monday \- Thursday: After 2 pm <br> Friday \- Saturday: Before 6pm |
| Mustafa Alrubaye | Communications Lead | Assembles report/video; ensures run instructions; tracks deliverables; merges documentation. |  |

## 3\) Repository & Access

* Platform: GitHub   
* Repository URL: https\://github.com/Adam-Kharon/CS\_2430\_Programming\_Project\_3.git  
* Instructor invited as viewer by checkpoint?: Yes — invited date: November 3rd 2026  
* Repo Structure Summary: Included in README.md.

## 4\) Milestones (Timeline)

| Milestone | Target Date | Owner | Definition of Done | Status (NS/IP/D) | Evidence Link / Pointer |
| :---- | :---- | :---- | :---- | :---- | :---- |
| Skeleton running | November 8th | Danielle | sample I/O runs. | D | Commit db8586b |
| Core implementation complete | November 15th | Danielle | All required features implemented; compiles/runs end-to-end. | IP |  |
| Verification complete | November 13th | Adam | Test suite covers edge cases; expected vs actual documented. | NS |  |
| Report/video draft complete | November 15th | Mustafa | Draft report sections complete; video outline \+ script notes. |  |  |
| Final integration \+ submission | November 15th | Team | All deliverables finalized; tags release; submission package ready. |  | Final Submission |

## 5\) Task Backlog (Assignments \+ Status)

| ID | Task | Owner | Target/Due Date | Status (NS/IP/D) | Evidence Link / Pointer | Notes / Dependencies |
| :---: | ----- | :---: | ----- | :---: | :---: | ----- |
| 1 | Create repository \+ baseline README/run steps | Adam | - | D | Commit fcb735e | - |
| 2 | Draft scope document in /docs | Danielle | November 8th | D | Commit 4b16137 | - |
| 3 | Draft UML diagram in /docs | Team | November 8th | D |  | - |
| 4 | Implement core data structures and parsing | Danielle | November 8th | IP |  |  |
| 5 | Define verification plan (tests \+ edge cases) | Adam | November 8th  | D |  |  |
| 6 | Implement edge-case tests \+ expected outputs | Adam | November 13th | NS |  |  |
| 7 | Checkpoint package: update plan \+ docs \+ invite verification | Team | November 8th | D | Checkpoint Submission | - |
| 8 | Report draft (problem/design/verification/results) | Mustafa | November 15th | IP |  |  |
| 9 | Final polish: run instructions \+ release tag | Team | November 15th | NS | Final Submission | Before Submission |

## 6\) Verification Plan

* Correctness checks/tests to run

* Unit tests for core functions.  
* Golden-file tests: compare outputs to expected results on provided sample inputs.  
* Negative tests: invalid inputs produce clear error messages.

* Edge cases to verify

* Empty/near-empty input (minimum size).  
* Maximum size input within assignment constraints.  
* Duplicate or conflicting entries (as applicable to the project spec).

* Expected vs. actual outputs (where applicable)

* Document expected outputs for at least 3 representative test cases in docs/test-results.md.  
* Include screenshots or terminal output snippets if tooling is minimal.

* Performance checks (if required by the project)

* Run on increasing input sizes and record runtime (basic timing).  
* Confirm no unnecessary O(n^2) loops on hot paths where avoidable.

## 7\) Risks / Blockers

| Risk / Blocker | Impact | Mitigation Plan | Owner | Status |
| :---- | :---- | :---- | :---- | :---- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

