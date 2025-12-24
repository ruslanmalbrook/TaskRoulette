## CURRENT TASK / Текущая задача

### Task Roulette — Nice-to-have features (Optional)

**Branch (required before code changes):** `251224_feature_nicetohave_v1`  
**Status:** In Progress  
**Source:** `docs/workflow/1_ALL_TASKS.md` → `4. Nice-to-have features (Optional)`  

---

## Goal / Цель
Complete the full **Nice-to-have** block (optional features):
- Completion tracking + streaks
- Task sets / presets
- Import / Export (offline) for tasks
- Gesture spin (fling)
- Cosmetic IAP (one-time purchase) — requires separate approval (see ALL_TASKS note)

## Scope / В рамках задачи
Add optional features without redesigning the app:
- Task sets with an active set selection and a few built-in presets
- Offline JSON import/export via Storage Access Framework (no storage permission)
- Completion tracking (mark done) and streak/longest streak calculation (local-only)
- Gesture spin on wheel area (additional input method)

## Out of scope / Вне рамок
- Monetization
- Completion/streaks

## Acceptance Criteria / Критерии приёмки
- Completion can be marked on Result and streaks are updated deterministically.
- Task sets can be created/selected; presets can be added quickly.
- Import/export works offline using SAF (create/open document) and handles invalid JSON gracefully.
- Gesture spin triggers a spin without breaking existing button behavior.
- Build remains green.

---

## Work Breakdown (Slices) / Декомпозиция
- [ ] **Completion tracking + streaks**
- [ ] **Task sets / presets**
- [ ] **Import / Export (offline)**
- [ ] **Gesture spin (fling)**
- [ ] **Cosmetic IAP (one-time purchase)** — requires separate approval

## Git note / Примечание по git (workflow)
Work happens on feature branch `251224_feature_nicetohave_v1` and will be merged into `develop` when approved.

