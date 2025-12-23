# Task Roulette - Product Requirements Document (PRD)

## 1. Product Overview

**Product Name:** Task Roulette  
**Product Type:** Android Productivity / Gamification App  
**Target Platform:** Android (API Level 24+)  

### Product Vision
Task Roulette helps users overcome procrastination by turning task selection into a visually engaging game. Instead of deciding what to do next, users spin a roulette wheel that randomly selects a task—creating urgency, excitement, and commitment.

### Core Value Proposition
- **Eliminate decision fatigue:** task selection is handled by the wheel
- **Turn tasks into a game:** anticipation, surprise, celebration
- **Strong visual & emotional engagement:** animation, sound, haptics, confetti
- **Extremely simple interaction model:** create tasks → spin → do the task
- **Offline-first & privacy-friendly:** no accounts, no cloud sync, no personal data collection

### Target Users / Primary Scenarios
- Users with a small-to-medium list of “things I should do” who struggle to start
- Students choosing between assignments
- Work-from-home users needing a quick “what now?” decision

### MVP Goals
- **Fast time-to-action:** from app launch to a selected task in <10 seconds (with existing tasks)
- **Delightful, physical-feeling spin:** consistent 60fps animations and synced feedback
- **Reliable offline persistence:** tasks and history survive process death / device reboot
- **Zero permission friction:** no runtime permissions required for MVP

### Non-goals (MVP)
- Accounts/login, cloud sync, or cross-device sharing
- Notifications/reminders
- “Project management” features (tags, due dates, subtasks, dependencies)
- Social features (leaderboards, sharing, invites)

## 2. Key Product Features

### 2.1 Task Management (Local)
Users manage a list of tasks used to build the roulette wheel.

Each task has:
- **Title** (required)
- **Color** (auto-generated but editable)
- **Weight** (optional chance modifier; default = 1)

**Acceptance criteria**
- Create, edit, delete tasks from UI; changes reflect on wheel immediately.
- Tasks persist locally and are available after app restart.
- Deleting a task must not corrupt old history sessions (history uses snapshots).

### 2.2 Roulette Wheel Visualization
The Home screen shows a circular wheel divided into labeled, colored sectors.

**Sector sizing**
- Let tasks have weights \(w_i \ge 1\). Sector proportion is \(p_i = w_i / \sum w\).
- Sector angle is \(a_i = 360° \cdot p_i\).

**Constraints**
- Must handle 1..50 tasks. When labels become too dense, degrade gracefully (ellipsis/smaller text) rather than overlapping or crashing.

**Acceptance criteria**
- Sector angles visibly reflect weights (higher weight → larger sector).
- Wheel always renders without layout/jank regressions on typical devices.
- Selected sector can be highlighted (stroke/glow) without changing business logic.

### 2.3 Roulette Spin Mechanics
Spinning selects a task and animates the wheel with a physical-style deceleration.

**Selection**
- Weighted random selection over the current task set (default weight = 1).
- The wheel must stop **exactly** on the selected task (no “between sectors” ambiguity).

**Animation**
- Smooth rotation with randomized deceleration profile to feel non-deterministic.
- Add a randomized number of full rotations for anticipation.

**Feedback**
- Sound theme (tick during spin + final stop sound).
- Haptic feedback (light ticks during spin; “success” vibration on stop).

**Acceptance criteria**
- For every spin, the final wheel position aligns with the selected sector under the fixed pointer.
- Spin feels non-repetitive (variation in duration/turn count), but remains stable and predictable enough to avoid motion sickness.
- Spin logic supports deterministic testing (seedable RNG in domain layer).

### 2.4 Result & Celebration
When the wheel stops:
- Selected task is highlighted.
- A Result screen is shown with:
  - Clear text: **“Your task for now”**
  - Task title (and color accent)
  - Fullscreen confetti animation
  - Success haptic
- The selected task becomes the **Active Task** (persisted).

**Acceptance criteria**
- Confetti and success haptic trigger exactly once per completed spin.
- Active Task persists across app restarts.
- Result screen is readable and accessible (TalkBack labels, adequate contrast).

### 2.5 Game Session & History (Local)
Each spin creates a **Game Session** stored locally.

Session data (minimum):
- Timestamp
- Snapshot of tasks at spin time (title, color, weight)
- Selected task within that snapshot

History screen:
- List of sessions (newest first)
- Tap session → session details (shows selected task)
- **Replay session:** uses the same snapshot task list but performs a fresh spin

**Acceptance criteria**
- Sessions persist locally (Room / DataStore as implementation detail) and survive process death.
- Replay uses the snapshot list even if the current task list has changed.
- Replay creates a new session entry (audit trail stays intact).

### 2.6 Statistics (Local)
The app provides local statistics derived from stored sessions:
- Total spins
- Most frequently selected task
- “Quick Spin” from last-used task set (last current list or last replay snapshot)

Optional (post-MVP or gated):
- Longest streak of completed tasks (requires completion tracking)

**Acceptance criteria**
- Stats are computed from local data only; no network required.
- Most-frequent task calculation is deterministic (ties handled consistently).

### 2.7 Settings & Customization
Settings must be simple and privacy-safe:
- Sound on/off
- Haptics on/off
- Reduced motion (optional)
- Cosmetic preferences:
  - Multiple spin sound themes
  - Confetti style variations
  - Wheel themes

**Acceptance criteria**
- Settings persist locally via DataStore.
- Toggling sound/haptics affects the next spin immediately.

## 3. Monetization (Optional, Light)
Cosmetic unlocks only (no subscriptions):
- Confetti styles
- Wheel themes
- Sound themes

Monetization principles:
- App is fully usable for core function without paying.
- No ads in MVP.

**Acceptance criteria**
- Purchases (if enabled) are one-time and restore correctly on the same Google account.
- No paywall blocks basic task CRUD, spinning, result, or history.

## 4. User Experience Flow

### 4.1 Onboarding
Minimal onboarding (2–3 screens):
- Visual explanation of the concept
- Privacy statement: no login, no permissions, offline-first
- Option to skip

**Acceptance criteria**
- Onboarding shows on first launch only and is always skippable.
- No permission prompts during onboarding (MVP).

### 4.2 Main Screens
- **Home:** roulette wheel + primary Spin button + quick access to Task Editor + Active Task summary
- **Task Editor:** create/edit/delete task (title, color, weight)
- **Result:** “Your task for now” + confetti + set Active Task
- **History:** sessions list + details + replay
- **Settings:** toggles + themes

## 5. Analytics & Performance Tracking

### 5.1 Analytics (Privacy-First)
- MVP: **no third-party analytics**.
- Local-only stats shown to the user.
- Crash reporting is optional; if added, it must be opt-in and must not contain PII.

### 5.2 Performance Requirements
- **60fps** during wheel animation on mid-range devices
- **Zero ANRs**
- **Fast cold start:** <2 seconds target on typical devices
- No heavy work on main thread; all IO offloaded

**Acceptance criteria**
- Spin animation remains smooth under profiler/macrobenchmark inspection.
- App is fully functional in airplane mode.

## 6. Technical Requirements & Constraints

### 6.1 Android Version Support
- **Minimum SDK:** Android 7.0 (API Level 24)
- **Target SDK:** Latest stable Android version
- Dark mode supported

### 6.2 Architecture & Tech Stack
- Kotlin
- Jetpack Compose + Material 3
- Single-Activity architecture
- MVVM + Clean Architecture data flow: UI ↔ ViewModel ↔ UseCase ↔ Repository ↔ DataSource
- Local persistence only:
  - Room for tasks + sessions
  - DataStore for preferences

**Acceptance criteria**
- Business rules (selection, stats, replay contract) live in UseCases, not UI.
- All persistence APIs are suspend and run on Dispatchers.IO.

### 6.3 Privacy & Permissions
- No login, no cloud sync
- No runtime permissions required for MVP
- Provide “Clear data” action in Settings (tasks + sessions + preferences)

**Acceptance criteria**
- App never requests runtime permissions in MVP.
- Clear data results in a clean first-run-like state.

### 6.4 Quality & Testing
- Unit tests for domain logic:
  - Weighted selection
  - Sector sizing math
  - Stats aggregation
- UI tests for critical flow:
  - Task CRUD → Spin → Result → History → Replay

**Acceptance criteria**
- Domain logic is deterministic under test (injectable RNG).
- New core logic meets repository quality standards (coverage target per project rules).

## 7. Risks, Edge Cases, and Open Questions
- **Many tasks / tiny sectors:** label readability and tap targets; potential cap or alternative list view for selection.
- **Extreme weights:** very small sectors; consider minimum angle thresholds vs strict proportionality.
- **Replay semantics:** replay uses same snapshot list but creates a new session entry (recommended).
- **Completion tracking:** whether to ship completion + streak in MVP or after initial validation.
