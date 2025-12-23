## DOING NOW / Делаем сейчас

### ✅ Completed (Approved)
- Foundations: Compose + Navigation, Hilt DI, Room/DataStore persistence, core domain contracts + unit tests

---

## Technical debt execution — continuous

## Goal / Цель
Finish the entire block `3. Technical debt (future)` **without waiting for per-slice approvals**, but with frequent **checkpoints + commits**.

## Checkpoints (commit plan)
- Checkpoint A: Room schema export + migration test scaffold
- Checkpoint B: Package modularization by feature + import cleanup
- Checkpoint C: Test improvements + Macrobenchmark scaffold + final verification

---

## Checkpoint A — Room schema hardening

**Branch precondition:** `251224_feature_techdebt_v1`  
**Source checklist item:** `docs/workflow/1_ALL_TASKS.md` → `3. Technical debt` → “Room schema hardening”  

## Goal / Цель
Harden Room database maintenance: schema export + migration testing scaffold.

## Acceptance Criteria / Критерии приёмки
- Room exports schema JSON into versioned folder committed to VCS.
- Migration test scaffolding compiles and can be run.

## Implementation Checklist
- [x] Enable `exportSchema = true` and configure schema location
- [x] Commit generated schemas
- [x] Add migration test scaffold (androidTest)
- [x] Build `:app:assembleDebug`

---

## Checkpoint B — Codebase modularization ✅ Done

## Implementation Checklist
- [x] Move Compose screens next to their feature ViewModels (feature-oriented packages)
- [x] Update navigation imports accordingly
- [x] Build `:app:assembleDebug`

---

## Checkpoint C — Tests + Macrobenchmark scaffold + final verification ✅ Done

## Implementation Checklist
- [x] Add extra unit tests (stats + spin plan invariants)
- [x] Add repository instrumentation test (TaskRepositoryImpl)
- [x] Add Compose UI smoke test for critical flow
- [x] Add Macrobenchmark module scaffold `:benchmark` + benchmark build type for `:app`
- [x] Add run docs for Macrobenchmark
- [x] Build `:app:assembleDebug`
- [x] Unit tests `:app:testDebugUnitTest`
- [x] Build `:benchmark:assembleBenchmark`

## Files to touch (expected)
- `app/src/main/java/tech/taskroulette/app/data/local/db/TaskRouletteDatabase.kt`
- `app/build.gradle.kts`
- `app/schemas/` (new folder committed)
- `app/src/androidTest/...` (migration test)
- `benchmark/` (new module)
- `docs/helpers/performance_validation.md`

