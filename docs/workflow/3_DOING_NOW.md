## DOING NOW / Делаем сейчас

### ✅ Completed (Approved)
- Foundations: Compose + Navigation, Hilt DI, Room/DataStore persistence, core domain contracts + unit tests

---

## Nice-to-have execution — continuous

## Goal / Цель
Finish the entire block `4. Nice-to-have features (Optional)` **without waiting for per-slice approvals**, but with frequent **checkpoints + commits**.

## Checkpoints (commit plan)
- Checkpoint A: Task sets/presets + migration + active set selection
- Checkpoint B: Import/Export JSON (SAF) + gesture spin
- Checkpoint C: Completion tracking + streaks + final verification

---

## Checkpoint A — Task sets / presets

**Branch precondition:** `251224_feature_nicetohave_v1`  
**Source checklist item:** `docs/workflow/1_ALL_TASKS.md` → `4. Nice-to-have` → “Task sets / presets”  

## Goal / Цель
Add task sets with a few presets and the ability to select an active set.

## Acceptance Criteria / Критерии приёмки
- User can create/select task set and tasks are scoped to it.
- Existing tasks are migrated into a default set (no data loss).

## Implementation Checklist
- [ ] Add `TaskSet` model + Room tables/DAO + repositories/usecases
- [ ] Add migration for existing tasks → default set
- [ ] Add Settings UI to create/select active set + add presets
- [ ] Build `:app:assembleDebug`

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

