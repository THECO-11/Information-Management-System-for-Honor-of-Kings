# Test Cases

Project: AI-Assisted Information Management System for Honor of Kings

Test date: 2026-06-11

Test scope: Combined staged test record up to the current implementation stage. The project currently includes model classes, initial data creation, `GameDataManager`, `SearchService`, `RankingService`, `AuthenticationService`, `FileStorageService`, and the current console `Main` flow.

Test environment:

- Java: 21.0.10
- Test method:
  - previous staged checks for data foundation, data management, search, and ranking
  - temporary Java service test runner compiled outside the repository for current-stage service verification
  - interactive console test runs executed from `Main`
- Git operations: not used during this test

## Summary

Implemented-stage tests passed: 30

Blocked tests: 0

Current requirement features covered in staged tests:

- player lookup
- team overview
- hero details
- equipment ranking
- match history
- leaderboard
- authentication
- admin data management flow
- file save/load
- console menu flow

Remaining work outside this staged round:

- one final full-system test round after all polishing is done
- final user-written overall test report if needed

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

## Test 20: Authentication Login And Role Detection

Function tested: `AuthenticationService.login`, `logout`, and role checks

Input: Login with `admin / admin123`, then `ming / p123`, then `ming / wrong`.

Expected output: Admin login succeeds with role `ADMIN`, player login succeeds with role `PLAYER`, failed login returns empty result and does not keep a logged-in state.

Actual output: Admin login succeeded with role `ADMIN`, player login succeeded with role `PLAYER`, and failed login returned empty result with `isLoggedIn = false`.

Result: Pass

Bug found: None

## Test 21: File Save Creates CSV Files

Function tested: `FileStorageService.save`

Input: Save initialized data to a temporary test data directory.

Expected output: The service creates CSV files for admins, players, equipment, heroes, teams, and matches.

Actual output: `admins.csv`, `players.csv`, `equipment.csv`, `heroes.csv`, `teams.csv`, and `matches.csv` were all created successfully.

Result: Pass

Bug found: None

## Test 22: File Load Restores Counts And Relations

Function tested: `FileStorageService.load`

Input: Load data from the saved temporary CSV files.

Expected output: Loaded data keeps the same counts and restores player-hero, player-match, team-member, and user-list relationships.

Actual output: Loaded data returned 1 admin, 15 players, 15 heroes, 20 equipment items, 3 teams, and 10 match records. `P001` kept 3 heroes and non-empty match history, `T001` kept 5 members, and the user list count was 16.

Result: Pass

Bug found: None

## Test 23: Main Menu Invalid Input Handling

Function tested: `Main` welcome menu input validation

Input: Enter `abc`, then `0`.

Expected output: The system rejects the invalid menu input, prompts again, and then exits cleanly.

Actual output: The console printed `Invalid number. Please enter an integer.` and then accepted `0` and exited with `Goodbye.`

Result: Pass

Bug found: None

## Test 24: Admin Add Player Flow

Function tested: `Main` admin player-management menu

Input: Login as admin, add player `P099`, assign heroes `H001,H002`, assign team `T001`, then list players and search `P099`.

Expected output: The player is added successfully, appears in the player list, and search shows the new player details with assigned team and heroes.

Actual output: `Player added successfully.` was shown, `P099 | TestUser | Level: 10 | Team: Dragon Vanguard` appeared in the list, and searching `P099` displayed the correct profile, team, and hero details.

Result: Pass

Bug found: None

## Test 25: Admin Update Player Flow

Function tested: `Main` admin player update flow

Input: Update `P099` to `TestUserUpdated`, change username, level, win rate, hero list to `H003`, and team to `T002`, then search `P099`.

Expected output: Updated player data is shown correctly after the edit.

Actual output: `Player updated successfully.` was shown, and searching `P099` displayed name `TestUserUpdated`, username `testuser2`, level `11`, win rate `55.0`, team `Moonlight Guard`, and hero `H003`.

Result: Pass

Bug found: None

## Test 26: Admin Delete Player Flow

Function tested: `Main` admin player delete flow

Input: Delete `P099`, list players, then search `P099`.

Expected output: The player is removed from the list and cannot be found anymore.

Actual output: `Player deleted successfully.` was shown, `P099` no longer appeared in the player list, and searching `P099` returned `Player not found.`

Result: Pass

Bug found: None

## Test 27: Admin Submenu Navigation And Listing

Function tested: `Main` admin menu navigation for heroes, equipment, teams, and match records

Input: Login as admin, open each management submenu, use `List` in each one, and return to the admin menu.

Expected output: Each submenu opens correctly and lists the current dataset.

Actual output:

- Hero list showed all 15 heroes.
- Equipment list showed all 20 equipment items.
- Team list showed all 3 teams.
- Match record list showed all 10 match records.

Result: Pass

Bug found: None

## Test 28: Menu Save And Load Flow

Function tested: `Main` admin save/load menu options

Input: Login as admin, choose `Save Data`, then choose `Load Data`.

Expected output: Save reports the output directory, and load completes successfully and returns to the welcome flow with a fresh login required.

Actual output: The console printed `Data saved to ...\\out\\data.` and then `Data loaded successfully. Please login again.` before returning to the main menu.

Result: Pass

Bug found: None

## Test 29: Player Profile And Match History Flow

Function tested: `Main` player menu basic view flow

Input: Login as `ming / p123`, view profile, then view 2 recent matches with limit `2`.

Expected output: Profile information is shown correctly, and recent match history is displayed without admin permissions.

Actual output: The player profile for `P001` was displayed correctly. Match history was shown successfully, and with the entered limit the console displayed the recent match records available to that player.

Result: Pass

Bug found: None

## Test 30: Player Search And Ranking Flow

Function tested: `Main` player-side search and ranking features

Input: Login as `ming / p123`, search team `T001`, search hero `H001`, view win-rate leaderboard with limit `3`, and view equipment ranking with limit `5`.

Expected output: Player role can access public search and ranking features normally.

Actual output: Team details, hero details, top 3 win-rate leaderboard, and top 5 equipment ranking were all displayed successfully from the player menu.

Result: Pass

Bug found: None

---

## Notes For Next Test Stage

This staged round confirms that `AuthenticationService`, `FileStorageService`, and the current `Main` menu flow are working at a basic implementation level. The next test stage should be the final full-system round, with broader regression coverage and any polish fixes found during manual use.
