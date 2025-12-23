## CURRENT TASK / Текущая задача

### Task Roulette — Core MVP (Ship v1)

**Branch (required before code changes):** `251224_feature_coremvp_ship_v1`  
**Status:** Completed ✅  
**Source:** `docs/workflow/1_ALL_TASKS.md` → `1. Core MVP tasks (Ship v1)`  

---

## Goal / Цель
Complete the full **Core MVP (Ship v1)** block:
- Task CRUD (Create/Edit/Delete)
- Roulette wheel composable + spin mechanics
- Result + celebration (confetti + success haptic)
- Game session persistence + History + Replay
- Settings (sound/haptics toggles)
- Local stats (total spins, most frequent task)

## Scope / В рамках задачи
- **Tasks**
  - Create/Edit/Delete in UI
  - Auto-generated color harmony (editable)
  - Weight affects probability and sector size
- **Wheel & Spin**
  - Canvas wheel with weighted sectors + labels
  - Smooth spin animation, randomized feel
  - Wheel stops exactly on selected task
  - Sound + haptics (toggleable)
- **Result**
  - “Your task for now” + selected task
  - Confetti + success haptic
- **History & Replay**
  - Store every spin as a session (snapshot tasks + selected)
  - History list + details
  - Replay uses same snapshot list, fresh spin, creates new session
- **Settings & Stats**
  - Sound/haptics toggles (DataStore)
  - Total spins + most frequently selected task

## Out of scope / Вне рамок
- Monetization
- Completion/streaks

## Acceptance Criteria / Критерии приёмки
- Core MVP screens work end-to-end offline:
  - Task Editor → Home (wheel) → Spin → Result → History → Replay → Result
- Each spin creates a stored session (snapshot tasks + selected).
- Wheel always stops on the selected task; selection respects weights.
- Settings toggles apply immediately (sound/haptics).
- Local stats are correct (total spins, most frequent task).

---

## Work Breakdown (Slices) / Декомпозиция
- [x] **Task CRUD (UI + persistence)** ✅
- [x] **Color harmony generation (auto color)** ✅
- [x] **Wheel composable + spin + session creation** ✅
- [x] **Result screen + confetti** ✅
- [x] **History + replay** ✅
- [x] **Settings + stats** ✅

## Git note / Примечание по git (workflow)
This repo looks like a fresh/empty git repo (unborn refs). Before the **first commit**, please create base branches and the feature branch:

```bash
git checkout -b main
git checkout -b develop
git checkout -b 251224_feature_coremvp_ship_v1
```

# Reason: workflow assumes feature branches are merged into `develop`.

