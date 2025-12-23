# 2_WORKFLOW_RULES.md – Chat-Driven Development Protocol

# Important
- Always respect the basic rules from .cursor/rules folder
- When calculating YYMMDD => ALWAYS get first the current system date

## Purpose
Define the iterative workflow between human-developer and LLM assistant so tasks stay small, traceable and always have the right good-enough context for current task execution

---
## Artefact Glossary
 - **1_ALL_TASKS.md** – Entire project backlog (Level-1 themes & Level-2 tasks) which corresponds to the project context from folder **../context**.  
 - **2_CURRENT_TASK.md** – The *active* high-level task, copied from 1_ALL_TASKS.md and decomposed into detailed sub-steps. This task must be in a separate feature-branch so when fully finished it will be merged into development branch "develop"
 - **3_DOING_NOW.md** – The *current slice* being executed; a logically complete subset of CURRENT_TASK.  

---
## Flow
1. **Pick Task & Create Branch**  
 - Pirck next task from *1_ALL_TASKS.md* according to priorities
 - Create new feature-branch `YYMMDD_feature_shortname` (follow git-rules.mdc)
 - Mark task inside *1_ALL_TASKS.md* as **«In Work → branch=... »** (for history and sync).

2. **Create CURRENT_TASK**  
 - inside new branch copy chosen task from *1_ALL_TASKS.md* into **2_CURRENT_TASK.md** and decompose it into the clear TODO-list and plan
 - clearly define the task scope, specification, business logic, tests and acceptance criteria

3. **Create Slice**  
 - Based on finalized *2_CURRENT_TASK.md* take first logically correct and fulfilled subtask and create new **3_DOING_NOW.md** with more detailed plan and detalization on how this slice should be implemented

4. **Execution Cycle** 
 - Assistant implements the code and puts syncs changes for 3_DOING_NOW.  
 - Human reviews
 - If approved → mark slice done.
 - LLM Assistant NEVER goes to implementation of the next phase until current is approved

5. **After Approval**
Assistant must:
- Update check-boxes / status inside 3_DOING_NOW & 2_CURRENT_TASK docs
- Take next slice of tasks from 2_CURRENT_TASK and create new 3_DOING_NOW document (according to current flow)
- Generate new **3_DOING_NOW.md** summarising what was achieved and the next slice to tackle. IMPORTANT! This file must always contain very detailed piece of information and very detailed instructions what needs to be done for the currently chosen focus

6. **CURRENT_TASK Completion**
When all tasks inside 2_CURRENT_TASK.md done, then:
- Check all tests are green and the acceptance criteria met
- No code left with TODO, STOPSHIP flags inside
- Update check-boxes / status inside **1_ALL_TASKS.md** if the high-level task is finished
- Clean-up current working documents: 3_DOING_NOW & 2_CURRENT_TASK
- Merge current branch changes into branch 'develop'
- Add changes into the "NEXT" session of the **RELEASE_NOTES.md** document (version will be assigned later). 
- The NEXT section inside RELEASE_NOTES MUST be always on TOP (Keep it conscise and straight to the point from the business and product perspective, don't overflood it with too much technical details)
- Propose the next task based on priorities from the 1_ALL_TASKS document and get approval by the human
---

## Release Process

1. **Trigger** – Human asks «Build next release» and the LLM agent gives the list of tasks for the full execution (see below)
2. **Branching**  
   ```bash
   git checkout release
   git merge --no-ff develop -m "Release vX.Y.Z"
   ```
3. **Version bump** – Update `package.json` (`version`, `versionCode`) и `app.config.ts`.
4. **RELEASE_NOTES.md** – Move section **NEXT** into `## vX.Y.Z – …` with the today date inside.
5. **Tag** – `git tag vX.Y.Z` & push tag
6. **Build** the AAB file
7. **Post-release** – clean up **NEXT** (leave empty for the next release).

## Rules & Best-Practices
• Keep 3_DOING_NOW.md ≤ ~200 lines to stay within chat context. 
• Every code change must reference the checklist item it fulfils.  
• Always sync documentation after edits.  
• Never start a new slice until prior slice merged and approved by human
• Ask clarifying questions if any ambiguity before coding;

## Mandatory Regression Testing Protocol
After ANY code changes affecting core engine, decoders, encoders, or effects, the following **complete test checklist** must be executed and pass:

### Build & Run command executions
NEVER run yourself until human is asked; all app build & run will be proceed by human and if you want help - only provide commands, not execute them
