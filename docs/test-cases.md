# Test Cases

Project: AI-Assisted Information Management System for Honor of Kings

Test date: 2026-06-08

Test scope: Current implementation stage. The project currently includes model classes, initial data creation, and basic `GameDataManager` data operations. Search, ranking, authentication, file storage, and menu functions are still implementation placeholders.

Test environment:

- Java: 21.0.10
- Test method: temporary Java test runner compiled with project source files
- Git operations: not used during this test

## Summary

Implemented-stage tests passed: 11

Placeholder checks passed: 4

Requirement features still blocked: SearchService, RankingService, AuthenticationService, FileStorageService, console menu

---

## Test 01: Initial Dataset Counts

Function tested: Initial data creation

Input: `new DataInitializer().createInitialData()`

Expected output: The system creates at least 1 admin, 10 players, 15 heroes, 20 equipment items, 3 teams, and 10 match records.

Actual output: Created 1 admin, 15 players, 15 heroes, 20 equipment items, 3 teams, and 10 match records.

Result: Pass

Bug found: None

## Test 02: Team Member Counts

Function tested: Team data design

Input: Check all teams from initial data.

Expected output: Each team has at least 5 players.

Actual output: All 3 teams have exactly 5 players.

Result: Pass

Bug found: None

## Test 03: Player Hero Ownership

Function tested: Player to Hero association

Input: Check every player from initial data.

Expected output: Each player owns at least 3 heroes.

Actual output: Every player owns 3 heroes.

Result: Pass

Bug found: None

## Test 04: Hero Equipment Compatibility

Function tested: Hero to Equipment association

Input: Check every hero from initial data.

Expected output: Each hero has at least 2 compatible equipment items.

Actual output: Every hero has 2 equipment items.

Result: Pass

Bug found: None

## Test 05: Lookup By ID

Function tested: `GameDataManager` ID lookup methods

Input: Search IDs `P001`, `h001`, `E001`, `T001`, and `M001`.

Expected output: Matching player, hero, equipment, team, and match record are found. Hero ID lookup should be case-insensitive.

Actual output: All records were found successfully.

Result: Pass

Bug found: None

## Test 06: Team Lookup By Player ID

Function tested: `GameDataManager.getTeamByPlayerId`

Input: Player ID `P001`

Expected output: The system returns team `T001`.

Actual output: Team `T001` was returned.

Result: Pass

Bug found: None

## Test 07: Duplicate Player ID Rejection

Function tested: Duplicate ID exception handling

Input: Add a new player using existing ID `P001`.

Expected output: The system rejects the duplicate ID and throws `IllegalArgumentException`.

Actual output: `IllegalArgumentException` was thrown.

Result: Pass

Bug found: None

## Test 08: Add Player

Function tested: `GameDataManager.addPlayer`

Input: Add player `P099`.

Expected output: Player count increases by 1, user list includes the new player, and the player can be found by ID.

Actual output: Player count increased from 15 to 16, `P099` was added to users, and `P099` was found by ID.

Result: Pass

Bug found: None

## Test 09: Delete Player Reference Cleanup

Function tested: `GameDataManager.deletePlayer`

Input: Delete player `P001`.

Expected output: Player is removed from the player list, user list, team member lists, and match participant lists.

Actual output: `P001` was removed from all checked locations.

Result: Pass

Bug found: None

## Test 10: Update Hero Reference Cleanup

Function tested: `GameDataManager.updateHero`

Input: Replace hero `H001` with updated hero name `Arthur Prime`.

Expected output: Main hero list is updated, and player/match references no longer point to the old hero object.

Actual output: Hero name was updated and old hero references were removed from player and match lists.

Result: Pass

Bug found: None

## Test 11: Delete Equipment Reference Cleanup

Function tested: `GameDataManager.deleteEquipment`

Input: Delete equipment `E001`.

Expected output: Equipment is removed from the equipment list and from all hero equipment lists.

Actual output: `E001` was removed from the main equipment list and all hero equipment lists.

Result: Pass

Bug found: None

## Test 12: Player Lookup Feature Status

Function tested: `SearchService.searchPlayer`

Input: Search keyword `P001`.

Expected output: Later implementation should return player information including team, level, win rate, owned heroes, and equipment.

Actual output: `UnsupportedOperationException` was thrown because `SearchService` is still a placeholder.

Result: Blocked

Bug found: Feature not implemented yet.

## Test 13: Leaderboard Feature Status

Function tested: `RankingService.rankPlayersByWinRate`

Input: Limit `5`.

Expected output: Later implementation should return top players sorted by win rate with documented tie handling.

Actual output: `UnsupportedOperationException` was thrown because `RankingService` is still a placeholder.

Result: Blocked

Bug found: Feature not implemented yet.

## Test 14: Authentication Feature Status

Function tested: `AuthenticationService.login`

Input: Username `admin`, password `admin123`.

Expected output: Later implementation should log in the admin user and return the current user.

Actual output: `UnsupportedOperationException` was thrown because `AuthenticationService` is still a placeholder.

Result: Blocked

Bug found: Feature not implemented yet.

## Test 15: File Storage Feature Status

Function tested: `FileStorageService.save`

Input: Save current initialized data.

Expected output: Later implementation should save system data to a documented file format.

Actual output: `UnsupportedOperationException` was thrown because `FileStorageService` is still a placeholder.

Result: Blocked

Bug found: Feature not implemented yet.

---

## Notes For Next Implementation Stage

The next implementation stage should focus on `SearchService`, because it directly supports Player Lookup, Team Overview, and Hero Details. After that, `RankingService` should be implemented for Equipment Statistics and Leaderboard.
