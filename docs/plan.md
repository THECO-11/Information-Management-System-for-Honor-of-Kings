# Plan:AI-Assisted Information Management System for Honor of Kings

> Author:Wang Xinfu
> 
> Date:2026-6-4
> 

---
## 1.Project Goal

This system could store and look up data about heroes, equipments, teams and so on.  

This system support two roles:  
- Admin: can create and deleted all data
- users: can look up public data 

---
## 2. Requirement Analysis

1. **player look up**: users can loop up players' name, team, level and so on by searching player's ID.
2. **team overview**: users can loop up teams' name, all team members, games and so on by searching team's ID.
3. **hero details**: users can find a hero's detailed information by searching hero's ID.
4. **equipments**: Rank equipment will be decided by factor like usage count, average rating and so on.
5. **match history**: users can search the match for a player or a team, and they can find details about the game.
6. **leaderboard**: leaderboard will be decided by win rate, level and so on.
7. **data management**: admin can add, delete, and edit all data in the system.
8. **authentication**: the System support two roles login.

---
## 3. Java Concepts Used

### Inheritance
Person is an abstract superclass.
Player and Admin extend Person.

### Interface
Searchable:
Used by SearchService for player, hero and team search.

Persistable:
Used by FileStorageService for saving and loading data.

### Polymorphism
All users can be stored in:

List<Person> users;

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
- own heroes
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

### AuthenticationService
Handles login and role verification.

### FileStorageService
Handles file persistence.

### GameDataManager
Central manager for all system data.

---
## 5. UML Draft

---
## 6. Data Design

### Initial Dataset

The system will contain:

- 3 Teams
- 10 Players
- 15 Heroes
- 20 Equipment items
- 10 Match Records


---
## 7. AI Usage Plan
**Architect Agent**: Suggest class structure, interface names, UML relationships

**Implementation Agent**: Write code to modify, finish the class struct.

**Testing/Reviewer Agent**: Find bugs, suggest test cases, give me the suggestion.

---
## 8. Prompt Strategy
**Design prompts** — ask for suggestions only, never full code

**Implementation prompts** — always paste existing class context

**Debugging prompts** — describe the exact symptom

**Review prompts** — ask for specific checks


---
## 9. Development Timeline

- **step 1**  Read requirements, create Git repository, write this **plan.md**.
- **step 2**  Ask **Architect Agent** for class design feedback; revise structure and **plan.md**.
- **step 3**  Write the basic classes and use **Implementation Agent** to modify it, revise **plan.md**.
- **step 4**  Use **Implementation prompts** to finish classes one by one, and check by myself every time.
- **step 5**  Use **Testing/Reviewer Agent** to find bugs and give me some suggestion and test cases, revise **plan.md**.
- **step 6**  Compile and run frequently. 
- **step 7**  Use test cases to test the system, and record what happened.
- **step 8**  Finish ** prompts.md**, **agent-log.md**, **reflection.md**, **git-history.txt**, **README.md**. 
- 

---
## 10. Testing Plan

---
## 11. Risk Analysis

---
## 12. Final Reflection Placeholder

---
 