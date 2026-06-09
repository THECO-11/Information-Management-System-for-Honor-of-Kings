# Test Cases

Project: AI-Assisted Information Management System for Honor of Kings

Test date: 2026-06-09

Test scope: Current implementation stage. The project currently includes model classes, initial data creation, `GameDataManager`, `SearchService`, and `RankingService`. `AuthenticationService`, `FileStorageService`, and the final menu-driven application flow are still placeholders.

Test environment:

- Java: 21.0.10
- Test method: temporary Java test runner executed from `Main`
- Git operations: not used during this test

## Summary

Implemented-stage tests passed: 19

Placeholder checks passed: 2

Requirement features still blocked: AuthenticationService, FileStorageService, final console menu

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

## Test 12: Search Player By ID Or Name

Function tested: `SearchService.searchPlayer`

Input: Search keywords `P001`, `ming`, and `unknown`.

Expected output: `P001` and `ming` should return a player result; `unknown` should return empty.

Actual output: `P001` and `ming` were found successfully, and `unknown` returned no result.

Result: Pass

Bug found: None

## Test 13: Search Team By ID Or Name

Function tested: `SearchService.searchTeam`

Input: Search keywords `T001`, `moonlight`, and `unknown`.

Expected output: `T001` and `moonlight` should return a team result; `unknown` should return empty.

Actual output: `T001` and `moonlight` were found successfully, and `unknown` returned no result.

Result: Pass

Bug found: None

## Test 14: Search Hero By ID Or Name

Function tested: `SearchService.searchHero`

Input: Search keywords `H001`, `li bai`, and `unknown`.

Expected output: `H001` and `li bai` should return a hero result; `unknown` should return empty.

Actual output: `H001` and `li bai` were found successfully, and `unknown` returned no result.

Result: Pass

Bug found: None

## Test 15: Player Match History Lookup

Function tested: `SearchService.findMatchesForPlayer`

Input: Player IDs `P001` and `UNKNOWN`, with limits `20` and `3`.

Expected output: Existing player returns non-empty match history sorted by date descending and limited by the requested number. Unknown player returns empty list.

Actual output: `P001` returned match history, the result respected the limit and date order, and `UNKNOWN` returned an empty list.

Result: Pass

Bug found: None

## Test 16: Team Match History Lookup

Function tested: `SearchService.findMatchesForTeam`

Input: Team IDs `T001` and `UNKNOWN`, with limit `3`.

Expected output: Existing team returns recent matches sorted by date descending. Unknown team returns empty list.

Actual output: `T001` returned 3 recent matches in descending date order, and `UNKNOWN` returned an empty list.

Result: Pass

Bug found: None

## Test 17: Player Ranking By Win Rate And Level

Function tested: `RankingService.rankPlayersByWinRate` and `RankingService.rankPlayersByLevel`

Input: Limit `5`

Expected output: Both rankings return 5 players in descending order. The top player should match the highest current dataset values.

Actual output: Both rankings returned 5 players. `P006` ranked first in both win-rate ranking and level ranking. Ordering was verified as descending.

Result: Pass

Bug found: None

## Test 18: Equipment Ranking By Score

Function tested: `RankingService.rankEquipmentByScore`

Input: Limit `5`

Expected output: Ranking returns 5 equipment items sorted by score descending.

Actual output: The ranking returned 5 equipment items. `E009` ranked first and `E001` ranked second. Ordering was verified as descending by score.

Result: Pass

Bug found: None

## Test 19: Match Count And Composite Ranking

Function tested: `RankingService.rankPlayersByMatchCount` and `RankingService.rankPlayersByCompositeScore`

Input: Limit `5`

Expected output: Both rankings return 5 players with correct descending order. Composite ranking should follow the documented formula.

Actual output: Both rankings returned 5 players. Match-count ranking order was verified as descending. Composite ranking order was verified using `compositeScore = winRate * 0.7 + level * 0.3`, and `P006` ranked first.

Result: Pass

Bug found: None

## Test 20: Authentication Feature Status

Function tested: `AuthenticationService.login`

Input: Username `admin`, password `admin123`

Expected output: Later implementation should log in the admin user and return the current user.

Actual output: `UnsupportedOperationException` was thrown because `AuthenticationService` is still a placeholder.

Result: Blocked

Bug found: Feature not implemented yet.

## Test 21: File Storage Feature Status

Function tested: `FileStorageService.save`

Input: Save current initialized data.

Expected output: Later implementation should save system data to a documented file format.

Actual output: `UnsupportedOperationException` was thrown because `FileStorageService` is still a placeholder.

Result: Blocked

Bug found: Feature not implemented yet.

---

## Notes For Next Implementation Stage

The next implementation stage should focus on `AuthenticationService` and then the final menu-driven `Main` flow. After that, persistence can be connected through `FileStorageService`, followed by one larger full-system test round.
