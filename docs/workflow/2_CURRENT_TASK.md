## CURRENT TASK / Текущая задача

### Task Roulette — Technical debt (future)

**Branch (required before code changes):** `251224_feature_techdebt_v1`  
**Status:** In Progress  
**Source:** `docs/workflow/1_ALL_TASKS.md` → `3. Technical debt (future)`  

---

## Goal / Цель
Complete the full **Technical debt** block:
- Performance validation scaffold (Macrobenchmark + profiling notes)
- Room schema hardening (schema export + migration test scaffold)
- Codebase modularization by feature (packages)
- Testing discipline improvements (unit + a basic UI smoke test)

## Scope / В рамках задачи
This block MUST NOT change product behavior. Focus on maintainability, safety and tooling:
- Room schema export enabled and committed to VCS.
- Add migration testing scaffolding (so future schema changes are safer).
- Restructure packages by feature (wheel/history/settings/tasks) while keeping APIs stable.
- Improve test coverage with targeted unit tests and a minimal UI smoke test.
- Add a Macrobenchmark module scaffold and instructions (run on device).

## Out of scope / Вне рамок
- Monetization
- Completion/streaks

## Acceptance Criteria / Критерии приёмки
- Build remains green (`:app:assembleDebug`, `:app:testDebugUnitTest`).
- Room schema export generates files and they are committed.
- Migration test scaffolding compiles and is runnable.
- Package structure is cleaner (feature-oriented) without breaking behavior.
- At least one UI test exists for critical flow smoke (can be run in androidTest).
- Macrobenchmark module scaffold exists and builds; docs explain how to run it.

---

## Work Breakdown (Slices) / Декомпозиция
- [ ] **Room schema hardening**
- [ ] **Codebase modularization**
- [ ] **Testing discipline**
- [ ] **Performance validation**

## Git note / Примечание по git (workflow)
Work happens on feature branch `251224_feature_uxpolish_v1` and will be merged into `develop` when approved.

