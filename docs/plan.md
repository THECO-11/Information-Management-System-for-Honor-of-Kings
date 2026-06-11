# Plan:AI-Assisted Information Management System for Honor of Kings

> Author:Wang Xinfu
> 
> Date:2026-6-4
> 

---
## 1.Project Goal

This system stores and manages information about heroes, equipment, teams, and match records.

The system supports two roles:
- Admin: can create, edit, and delete data
- Player: can view and search public information

---
## 2. Requirement Analysis


1. **Player Lookup**  
   Users can search for a player by ID and view player information.

2. **Team Overview**  
   Users can search for a team by ID and view team details and members.

3. **Hero Details**  
   Users can search for a hero by ID and view hero information and equipment.

4. **Equipment Ranking**  
   The system generates equipment rankings based on equipment score. The current design sorts equipment by `score` in descending order. If two equipment items have the same score, the system uses equipment name and equipment ID as tie-breakers.

5. **Match History**  
   Users can view match records for players and teams.

6. **Leaderboard**  
   The system generates player rankings based on win rate, level, number of matches, and a composite score. The current composite score formula is:

   `compositeScore = winRate * 0.7 + level * 0.3`

   For player ranking, ties are handled in a stable order. For example, win-rate ranking uses win rate first, then level, then match count, then player name, and finally player ID.

7. **Data Management**  
   Admins can add, edit, and delete system data.

8. **Authentication**  
   The system supports login for Admin and Player roles.


---
## 3. Java Concepts Used

### Inheritance
Person is an abstract superclass.
Player and Admin extend Person.

### Interface
Searchable:
Used by SearchService.

Persistable:
Used by FileStorageService.

Authenticatable:
Used by AuthenticationService for login verification.

### Polymorphism
All users can be stored in:

```java
List<Person> users;
```

The system can process Player and Admin objects through Person references.

### Encapsulation
All fields are private.

Getter and Setter methods are used to control access.

### Collections
ArrayList<Player> for team members.

ArrayList<Hero> for player heroes.

ArrayList<Equipment> for hero equipment.

HashMap<String, Player> for fast player lookup.

### Exception Handling
Handle:

- invalid menu input
- duplicate IDs
- player not found
- file loading failure

### File I/O
FileStorageService saves and loads:

- players
- heroes
- teams
- match records

using CSV files.

### Enums
HeroType:
TANK, MAGE, ASSASSIN, MARKSMAN, SUPPORT

Role:
ADMIN, PLAYER

MatchResult:
WIN, LOSS, DRAW

EquipmentType:
ATTACK, DEFENSE, MAGIC, MOVEMENT

---
## 4. Class Design

### Person (Abstract)
Base class for all system users.

Attributes:
- id
- name
- username
- password

### Player
Extends Person.

Responsibilities:
- manage hero collection
- view match history
- update personal profile

### Admin
Extends Person.

Responsibilities:
- manage system data
- add/edit/delete records

### Hero
Stores hero information.

Attributes:
- heroId
- heroName
- heroType
- attack
- defense
- equipmentList

### Equipment
Stores equipment information.

Attributes:
- equipmentId
- equipmentName
- equipmentType
- score

### Team
Stores team information.

Attributes:
- teamId
- teamName
- members

### MatchRecord
Stores match data.

Attributes:
- matchId
- date
- result
- players
- heroesUsed

### SearchService
Provides search functions.

### RankingService
Generates leaderboard and equipment rankings.

Current ranking design:
- player win-rate ranking
- player level ranking
- player match-count ranking
- player composite-score ranking
- equipment score ranking

### AuthenticationService
Handles login and role verification.

### FileStorageService
Handles file persistence.

### GameDataManager
Central manager for all system data.

---
## 5. UML Draft

![UML Class Diagram](HonorOfKings_UML.png)

---
## 6. Data Design

### Initial Dataset

The system will contain:

- 3 Teams
- 10 Players
- 15 Heroes
- 20 Equipment items
- 10 Match Records

### Relationships

- Each Team contains multiple Players.
- Each Player owns multiple Heroes.
- Each Hero can use multiple Equipment items.
- Each MatchRecord contains participating Players and Heroes used.
---
## 7. AI Usage Plan
**Architect Agent**: Suggest class structure, interface names, UML relationships

**Implementation Agent**: Write code to modify, finish the class struct.

**Testing/Reviewer Agent**: Find bugs, suggest test cases, give me the suggestion.

All AI suggestions will be reviewed and verified before being used in the project.

---
## 8. Prompt Strategy
**Design prompts** — ask for suggestions only, never full code

**Implementation prompts** — always paste existing class context

**Debugging prompts** — describe the exact symptom

**Review prompts** — ask for specific checks


---
## 9. Development Timeline

- **step 1**    
Read requirements, create Git repository, write this **plan.md**.
- **step 2**    
Ask **Architect Agent** for class design feedback; revise structure and **plan.md**.
- **step 3**    
Write the basic classes and use **Implementation Agent** to modify it, revise **plan.md**.
- **step 4**    
Use **Implementation prompts** to finish classes one by one, and check by myself every time.
- **step 5**   
Use **Testing/Reviewer Agent** to find bugs and give me some suggestion and test cases, revise **plan.md**.
- **step 6**    
Compile and run frequently. 
- **step 7**    
Use test cases to test the system, and record what happened.
- **step 8**    
Finish **prompts.md**, **agent-log.md**, **reflection.md**, **git-history.txt**, **README.md**. 


---
## 10. Testing Plan

The project will use staged testing during development, not only one final test at the end. My current testing strategy is:

1. After every two functional modules are completed, run one staged test round.
2. Record the result of each staged test round in `docs/test-cases.md`.
3. After all required modules are implemented, run one final full-system test round.

### Current module grouping

The project will be tested in these development groups:

- Group 1: `Model` + `DataInitializer`
- Group 2: `GameDataManager` + `SearchService`
- Group 3: `RankingService` + `AuthenticationService`
- Group 4: `FileStorageService` + `Main` menu flow

### Staged testing method

For each group, I will:

- compile the project after implementation
- test the new module functions
- retest related old functions to check for regressions
- record actual output and pass/fail result
- note any bug found and fix it before moving on

### Planned staged test rounds

**Round 1: Data foundation test**  
After finishing `Model` and `DataInitializer`, test:

- initial dataset count
- team member count
- player-hero relationship
- hero-equipment relationship

**Round 2: Data management and search test**  
After finishing `GameDataManager` and `SearchService`, test:

- add, update, delete, and lookup operations
- duplicate ID handling
- player lookup
- team lookup
- hero lookup
- player match history lookup
- team match history lookup

**Round 3: Ranking and authentication test**  
After finishing `RankingService` and `AuthenticationService`, test:

- win-rate leaderboard
- level leaderboard
- match-count leaderboard
- composite-score leaderboard
- equipment ranking
- login and logout
- admin/player role restriction

**Round 4: Persistence and menu flow test**  
After finishing `FileStorageService` and `Main`, test:

- file save and load
- menu navigation
- invalid menu input handling
- admin data management flow
- player view flow

### Final full-system test

After all required modules are finished, I will run one final overall test round covering:

- player lookup
- team overview
- hero details
- equipment statistics
- match history
- leaderboard
- authentication
- admin data management
- persistence
- full menu flow

### Test case record format

Each test case record will include:

- test ID
- function tested
- input
- expected output
- actual output
- pass/fail result
- bug found, if any

### Current status

I have already completed an early staged test round for the implemented data foundation and data-management stage. Future testing will continue in the "every two modules, then one test round" pattern, followed by one final full-system test.

---
## 11. Risk Analysis

### 11.1 Data Consistency Risk

The project contains many object relationships:

- team -> players
- player -> heroes
- player -> match history
- match record -> players
- match record -> heroes used

If one object is added, updated, deleted, saved, or loaded incorrectly, related references may become inconsistent.

Current attention points:

- admin menu operations already modify multiple related collections
- file loading must rebuild object relationships correctly
- update and delete logic can easily miss one reference path

Mitigation:

- keep all relationship update logic centralized in `GameDataManager`
- retest reference cleanup after each CRUD-related change
- include save/load regression checks in the final full-system test

### 11.2 Partial Update Risk In Menu Flow

Some admin operations in `Main` currently perform multiple steps in sequence, such as:

- create or update a player
- then assign the player to a team

If the second step fails after the first step already changed the data, the system may end in a partially updated state.

Impact:

- the console may report failure even though part of the data was already changed
- player-team relationships may become temporarily inconsistent

Mitigation:

- review multi-step admin operations before final submission
- validate all dependent input before writing changes
- if needed, refactor these flows into safer service-layer methods

### 11.3 Persistence Risk

The project now uses CSV files for persistence. This is simple and suitable for the assignment, but it has some risks:

- manual CSV parsing may fail on malformed data
- missing files may produce incomplete loaded data
- interrupted save operations may leave partially written files

Impact:

- data loss
- incomplete reload after save/load
- hard-to-debug relationship errors after loading

Mitigation:

- keep the CSV format stable and documented
- test save/load after meaningful CRUD operations
- verify loaded counts and loaded relationships, not only file existence

### 11.4 Authentication And Account Risk

The current authentication design is intentionally lightweight, but it still has project-level risks:

- passwords are stored in plain text
- username uniqueness is not strongly enforced in the current data rules
- duplicate usernames could make login behavior ambiguous

For this course project, the simple approach is acceptable, but it should be clearly understood as a simplified design rather than a production-safe solution.

Mitigation:

- keep default accounts limited to demo use
- avoid adding duplicate usernames during manual testing
- mention the simplified security model in the final reflection or README

### 11.5 Requirement Coverage Risk

The project already covers the main required modules, but there is still a risk that the final submission looks complete while some edge cases are not fully aligned with the requirement wording.

Examples:

- admin data management works, but some flows still need stronger validation
- persistence works, but abnormal file states are not deeply handled
- the console UI works, but usability and error recovery can still be improved

Mitigation:

- use one final full-system test round based directly on the requirement list
- review each requirement item one by one before final submission
- compare actual implemented behavior with both `requirement.pdf` and `plan.md`

### 11.6 Time And Scope Risk

Because this project has already gone through several AI-assisted iterations, there is a risk of spending too much time polishing lower-priority details while delaying the final integration and submission materials.

Mitigation:

- prioritize correctness and requirement coverage over extra refactoring
- finish the final full-system test before cosmetic cleanup
- complete remaining documentation only after implementation and testing are stable

### 11.7 Current Overall Risk Judgment

At the current stage, the project risk is **medium**:

- the architecture is clear enough for a course project
- core modules are implemented and staged-tested
- the main remaining risk is not missing architecture, but inconsistent edge-case behavior during multi-step admin operations and persistence-related failure scenarios

So the next development priority should be:

1. final code review and small bug fixes
2. final full-system test
3. final submission documents

### 11.8 Current Mitigation Status

After the latest implementation revision, several high-value mitigations have already been applied in code:

- username uniqueness is now enforced for system users
- player add/update flow validates the target team before changing stored player data
- team management now keeps one-player-one-team behavior more consistent by normalizing team membership
- match record update no longer depends on a delete-then-add menu flow
- file loading now rejects missing required CSV files instead of silently loading partial data
- file saving now writes through temporary files before replacing the target CSV files

Residual risk still exists, but the most important consistency and delivery risks identified in the previous review have been reduced.

---
## 12. Final Reflection Placeholder

---
 
