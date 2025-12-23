## CURRENT TASK / Текущая задача

### Task Roulette — UX polish tasks (Delight & perceived quality)

**Branch (required before code changes):** `251224_feature_uxpolish_v1`  
**Status:** In Progress  
**Source:** `docs/workflow/1_ALL_TASKS.md` → `2. UX polish tasks (Delight & perceived quality)`  

---

## Goal / Цель
Complete the full **UX polish** block to improve delight and perceived quality:
- Multiple spin sound themes (2–3)
- Confetti style variations (2–3)
- Subtle UI animations (scale/glow/blur-like effects without performance regression)
- Improved wheel label readability
- Accessibility & localization readiness (strings, semantics, font scaling)

## Scope / В рамках задачи
- Add **sound theme selection** in Settings and apply it in spin ticks + stop sound.
- Add **confetti style selection** in Settings and apply it on Result screen.
- Add subtle animations:
  - Selected sector highlight pulse
  - Small scale/alpha transitions where appropriate
- Improve wheel labels:
  - Dynamic truncation by available arc length
  - Dynamic text sizing for dense wheels
  - Avoid overlap/readability issues
- Accessibility & localization:
  - Ensure all user-visible strings are in `strings.xml`
  - Add semantics/content descriptions for key UI elements (wheel, buttons)
  - Verify no hard-coded English strings remain in UI layer

## Out of scope / Вне рамок
- Monetization
- Completion/streaks

## Acceptance Criteria / Критерии приёмки
- User can select between **2–3 sound themes**; theme affects spin tick/stop feedback immediately.
- User can select between **2–3 confetti styles**; styles are visually distinct.
- Wheel label readability improved for large task counts (no overlapping/garbled text; graceful truncation).
- Subtle animations do not introduce jank; build remains green.
- Accessibility basics: TalkBack descriptions for wheel + primary controls; all strings in resources.

---

## Work Breakdown (Slices) / Декомпозиция
- [ ] **Sound themes (2–3)**
- [ ] **Confetti styles (2–3)**
- [ ] **Subtle animations**
- [ ] **Wheel label readability**
- [ ] **Accessibility & localization readiness**

## Git note / Примечание по git (workflow)
Work happens on feature branch `251224_feature_uxpolish_v1` and will be merged into `develop` when approved.

