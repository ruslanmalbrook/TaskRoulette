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
- [ ] Enable `exportSchema = true` and configure schema location
- [ ] Commit generated schemas
- [ ] Add migration test scaffold (androidTest)
- [ ] Build `:app:assembleDebug`

---

## Checkpoint B — Confetti styles (2–3) ✅ Done

## Implementation Checklist
- [x] Add `ConfettiStyle` to Settings (persist in DataStore)
- [x] Add 3 distinct confetti styles
- [x] Apply selected style on Result screen
- [x] Build `:app:assembleDebug`

---

## Checkpoint C — Labels + subtle animations + accessibility ✅ Done

## Implementation Checklist
- [x] Improve wheel label truncation and dynamic sizing (no garbled text)
- [x] Subtle highlight pulse for selected sector
- [x] Add basic semantics (wheel content description)
- [x] Remove hard-coded UI placeholders from Kotlin (use resources)
- [x] Build `:app:assembleDebug`
- [x] Unit tests `:app:testDebugUnitTest`

## Files to touch (expected)
-- `app/src/main/java/tech/taskroulette/app/data/local/db/TaskRouletteDatabase.kt`
-- `app/build.gradle.kts`
-- `app/schemas/` (new folder committed)
-- `app/src/androidTest/...` (migration test)

