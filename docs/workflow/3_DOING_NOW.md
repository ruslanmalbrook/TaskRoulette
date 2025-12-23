## DOING NOW / Делаем сейчас

### ✅ Completed (Approved)
- Foundations: Compose + Navigation, Hilt DI, Room/DataStore persistence, core domain contracts + unit tests

---

## UX polish execution — continuous

## Goal / Цель
Finish the entire block `2. UX polish tasks (Delight & perceived quality)` **without waiting for per-slice approvals**, but with frequent **checkpoints + commits**.

## Checkpoints (commit plan)
- Checkpoint A: Sound themes (2–3) + Settings selection + apply in spin/result
- Checkpoint B: Confetti styles (2–3) + Settings selection + apply
- Checkpoint C: Wheel label readability + subtle animations + accessibility cleanup + final verification

---

## Checkpoint A — Sound themes (2–3)

**Branch precondition:** `251224_feature_uxpolish_v1`  
**Source checklist item:** `docs/workflow/1_ALL_TASKS.md` → `2. UX polish tasks` → “Multiple spin sound themes”  

## Goal / Цель
Add 2–3 distinct sound themes and allow selection in Settings; apply during spin tick + stop sound.

## Acceptance Criteria / Критерии приёмки
- At least 3 sound themes are available.
- Theme can be selected in Settings and takes effect immediately.
- No crashes when toggling sound off/on and switching themes.

## Implementation Checklist
- [x] Add `SpinSoundTheme` to Settings (persist in DataStore)
- [x] Update `ToneSoundPlayer` to support themes
- [x] Update Settings screen to pick theme
- [x] Ensure Home/Result use selected theme
- [x] Build `:app:assembleDebug`

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
- `app/src/main/java/tech/taskroulette/app/domain/model/Settings.kt`
- `app/src/main/java/tech/taskroulette/app/data/repository/SettingsRepositoryImpl.kt`
- `app/src/main/java/tech/taskroulette/app/presentation/sound/ToneSoundPlayer.kt`
- `app/src/main/java/tech/taskroulette/app/presentation/screens/SettingsScreen.kt`
- `app/src/main/res/values/strings.xml`

