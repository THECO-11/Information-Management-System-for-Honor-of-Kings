# Prompt Records

Project: AI-Assisted Information Management System for Honor of Kings

This document records the important AI prompts used during the project. It focuses on prompts that affected planning, architecture, implementation, testing, and review decisions. Minor troubleshooting conversations are not included because they did not meaningfully change the project design or implementation.

## Prompt 01: Architecture Review

Time: 2026-06-04

Tool/Model: Codex / GPT-5

Agent Role: AI-Architect

Related Commit: `fed9512`

### My Prompt

Please carefully read the coursework requirement document and review all existing files and source code in my project folder. Act as an AI-Architect. Evaluate the current project foundation, identify architecture-level problems, and give clear advice about how I should continue the task.

### AI Response Summary

The AI reviewed the requirement document, `docs/plan.md`, UML draft, Java package structure, model classes, service classes, utility classes, and enums. It found that the project direction matched the coursework requirement, but the implementation was still at the skeleton stage. `Main`, `GameDataManager`, and most service classes did not contain real behavior yet.

The AI recommended a console-first Java OOP architecture with these layers:

- `model` for domain objects such as `Person`, `Player`, `Admin`, `Hero`, `Equipment`, `Team`, and `MatchRecord`.
- `service` for business operations such as search, ranking, authentication, persistence, and data management.
- `util` for input handling and initial data creation.
- `Main` as a thin application entry point, not a large class containing all business logic.

### My Decision

I accepted the architecture direction and continued using the planned package structure. I also decided to implement the project in small stages so that each stage could be tested, committed, and explained.

## Prompt 02: Basic Architecture Placeholder Code

Time: 2026-06-05

Tool/Model: Codex / GPT-5

Agent Role: AI-Architect / AI-Implementation

Related Commit: `0b00489`

### My Prompt

According to the coursework requirement and my `plan.md`, please create only the initial architecture placeholder code for this project. Keep the implementation minimal. Add the basic class structure, constructors, fields, accessors, and method boundaries, but do not implement full features and do not introduce interfaces yet.

### AI Response Summary

The AI improved the basic code architecture by completing the model and service placeholders. It added constructors, getters, setters, role polymorphism, model relationships, and service method boundaries. Service methods that were not ready for implementation were left as placeholders.

### My Decision

I accepted this as the baseline architecture stage. I kept the design simple because the project needed a stable foundation before implementing search, ranking, authentication, file storage, and menu features.

## Prompt 03: Implementation Roadmap

Time: 2026-06-08

Tool/Model: Codex / GPT-5

Agent Role: AI-Implementation

Related Commit: Planning conversation, no direct code commit

### My Prompt

I plan to use AI-Implementation for the next coding stages. Based on the requirement, how many implementation steps should I divide the work into, and what should each step implement?

### AI Response Summary

The AI suggested implementing the project in six stages:

1. Complete model classes and `DataInitializer`.
2. Implement `GameDataManager`.
3. Implement `SearchService`.
4. Implement `RankingService` and match history support.
5. Implement `AuthenticationService` and role-based menus.
6. Implement `FileStorageService` and expand testing documentation.

### My Decision

I accepted the staged roadmap. This approach makes the development process easier to test and produces clearer AI evidence and Git history.

## Prompt 04: Model And Initial Dataset

Time: 2026-06-08

Tool/Model: Codex / GPT-5

Agent Role: AI-Implementation

Related Commit: `18e1fed`

### My Prompt

Following the staged implementation plan, complete the first step: Model + DataInitializer. Implement only the model-level helper behavior and initial dataset needed by the requirement. Do not implement search, ranking, authentication, file storage, or menu functions yet.

### AI Response Summary

The AI implemented lightweight relationship methods in model classes, such as `addHero`, `addEquipment`, `addMember`, `addMatchRecord`, and `addHeroUsed`. It also implemented `DataInitializer` with a requirement-ready initial dataset:

- 1 admin account
- 15 players
- 15 heroes
- 20 equipment items
- 3 teams
- 10 match records

Each player was given at least 3 heroes, each hero was given at least 2 equipment items, and each team was given 5 members.

### My Decision

I accepted the implementation. I kept 15 players instead of only 10 because the requirement also asks for 3 teams with at least 5 players each.

## Prompt 05: Core Data Management

Time: 2026-06-08

Tool/Model: Codex / GPT-5

Agent Role: AI-Implementation

Related Commit: `15e22e7`

### My Prompt

Continue with the second implementation step. Implement the core `GameDataManager` methods needed to manage the system data. Focus on add, delete, update, lookup, duplicate ID checking, and basic reference cleanup. Do not implement UI menus or unrelated services yet.

### AI Response Summary

The AI implemented `GameDataManager` methods for adding, deleting, updating, and looking up players, admins, heroes, equipment, teams, match records, and users. It added duplicate ID validation and basic exception handling using `IllegalArgumentException`.

The AI also added cleanup behavior so that deleting or updating related objects does not leave obvious stale references in teams, player hero lists, match records, or hero equipment lists.

### My Decision

I accepted the implementation because it created the central data-management foundation needed by later search, ranking, authentication, and file-storage features.

## Prompt 06: Staged Test Runner

Time: 2026-06-08

Tool/Model: Codex / GPT-5

Agent Role: Testing/Reviewer Agent

Related Commit: `5823b34`

### My Prompt

According to the coursework requirement, write and run staged test code for the currently completed implementation. Focus on testing the implemented model, initial data, and `GameDataManager` behavior. Do not claim unfinished features are complete. I will use the test output to write the final test documentation myself.

### AI Response Summary

The AI implemented staged test code and added a temporary `Main`-based test runner so the current implementation stage could be compiled and executed directly. The tests covered:

- Initial dataset counts
- Team member counts
- Player hero ownership
- Hero equipment compatibility
- ID lookup methods
- Duplicate player ID rejection
- Adding a player
- Deleting a player and cleaning references
- Updating a hero and replacing references
- Deleting equipment and cleaning hero references
- Current blocked status for search, ranking, authentication, and file storage

### My Decision

I accepted the staged test runner and used the results to prepare the test documentation myself. I understand that using `Main` as a test runner is temporary and that the final project should later replace it with a menu-driven application entry point.
