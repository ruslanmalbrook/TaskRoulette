## DOING NOW / Делаем сейчас

### ✅ Completed (Approved)
- Foundations: Compose + Navigation, Hilt DI, Room/DataStore persistence, core domain contracts + unit tests

---

## Core MVP execution (Ship v1) — continuous

## Goal / Цель
Finish the entire block `1. Core MVP tasks (Ship v1)` **without waiting for per-slice approvals**, but with frequent **checkpoints + commits**.

## Checkpoints (commit plan)
- Checkpoint A: Task CRUD + strings (already done)
- Checkpoint B: Auto color harmony + Task Editor polish
- Checkpoint C: Home wheel + spin + session creation + Result
- Checkpoint D: History + Replay
- Checkpoint E: Settings + Stats + final build verification

---

## Checkpoint A — Task Editor UI + ViewModel + repository integration ✅ Done

**Branch precondition:** `251224_feature_taskcrud_mvp`  
**Source checklist item:** `docs/workflow/1_ALL_TASKS.md` → `1. Core MVP tasks` → “Task CRUD”  

## Goal / Цель
Implement Task CRUD in UI, backed by `TaskRepository` (Room), with immediate updates.

## Acceptance Criteria / Критерии приёмки
- Task Editor screen shows a list of tasks (from DB).
- User can:
  - Add task (title required, weight default=1, auto-color)
  - Edit task
  - Delete task
- After app restart tasks persist.
- UI updates immediately after save/delete (Flow/State).

## Implementation Checklist
- [x] Add ViewModel layer for Task Editor (MVVM):
  - `TaskEditorViewModel` with `StateFlow` state
  - Uses domain/repository APIs (no direct DB calls)
- [x] Implement Task Editor UI:
  - List (`LazyColumn`) + “Add” action
  - Add/Edit dialog (title + weight + color palette)
  - Delete action per item
- [x] Minimal validation:
  - Disallow saving blank title
  - Clamp weight to >= 1
- [x] Build `:app:assembleDebug` and fix errors

## Files to touch (expected)
- `app/src/main/java/tech/taskroulette/app/presentation/screens/TaskEditorScreen.kt`
- `app/src/main/java/tech/taskroulette/app/presentation/taskeditor/...` (new)
- `app/src/main/java/tech/taskroulette/app/domain/usecase/...` (if needed for CRUD helpers)
- `app/src/main/res/values/strings.xml` (move UI strings from code to resources)

---

## Checkpoint B — Automatic color harmony generation (base)

## Goal / Цель
Implement auto color generation for new tasks (distinct, harmonious) and keep color editable.

## Implementation Checklist
- [x] Add `GenerateTaskColorUseCase` (golden-angle hue / HSL → ARGB)
- [x] Use it for new task default color
- [x] Keep a small palette for manual selection (generated)

---

## Checkpoint C — Home wheel + spin + session creation + Result ✅ Done

## Implementation Checklist
- [x] Implement `RouletteWheel` composable (Canvas sectors + labels + highlight)
- [x] Implement spin planning (`PlanSpinUseCase`) + animation (stop exactly on selected task)
- [x] Persist `GameSession` on spin completion
- [x] Result screen loads session by id + shows confetti + success feedback

---

## Checkpoint D — History + Replay ✅ Done

## Implementation Checklist
- [x] History screen lists sessions (latest first)
- [x] Tap session → Result
- [x] Replay uses session snapshot tasks and triggers fresh spin (creates new session)

---

## Checkpoint E — Settings + Stats + final verification ✅ Done

## Implementation Checklist
- [x] Settings toggles (sound/haptics) persisted in DataStore
- [x] Stats (total spins, most frequent task) computed from local sessions
- [x] Build `:app:assembleDebug`
- [x] Run unit tests `:app:testDebugUnitTest`

