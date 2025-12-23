# 📋 ALL TASKS — Task Roulette (Полный список задач проекта)

## Full Todo List (TL;DR)
- **MVP v1**: Task CRUD → Wheel → Spin → Result + Confetti → History + Replay → Settings → Local Stats

## TASKS PRIORITIES

### 0. 0-day BLOCKERS (DO IT NOW)

- [x] **Базовая тех-подготовка под PRD (Compose / MVVM / Local-first)** ✅ Done _(branch=`251223_feature_taskroulette_foundation`)_
  - Включить Jetpack Compose + Material 3 и перевести UI на Single-Activity Compose setup
  - Подключить DI (Hilt — предпочтительно) и каркас Clean Architecture (UI ↔ VM ↔ UseCase ↔ Repo ↔ Data)
  - Подключить Room (tasks + sessions) и DataStore (settings)
  - Критерии приёмки:
    - Приложение запускается и навигация между экранами (Home/Editor/History/Settings) работает
    - Нет runtime permissions и нет сетевых зависимостей для работы MVP

- [x] **Контракт доменной логики: SpinEngine + детерминированные тесты** ✅ Done
  - Описать правила: weighted выбор + точная остановка на секторе + seedable RNG (для unit tests)
  - Критерии приёмки:
    - Unit tests покрывают weighted выбор и математику секторов
    - 100% попадание “pointer → выбранный сектор” (контракт зафиксирован тестами)

- [x] **Схема хранения Game Session + Replay** ✅ Done
  - Решение: хранить snapshot списка задач на момент спина (title/color/weight) + выбранный элемент
  - Критерии приёмки:
    - История неизменяема: изменения текущих задач не меняют прошлые сессии
    - Replay использует snapshot, делает “fresh spin” и создаёт новую сессию

### 1. Core MVP tasks (Ship v1)

- [x] **Task CRUD (Create / Edit / Delete)** ✅ Done _(branch=`251224_feature_coremvp_ship_v1`)_
  - Task поля: Title (required), Color (auto + editable), Weight (optional, default=1)
  - Критерии приёмки:
    - Задачи создаются/редактируются/удаляются в UI
    - После перезапуска приложения список задач сохраняется
    - Изменения задач мгновенно отражаются на колесе

- [x] **Automatic color harmony generation (база)** ✅ Done
  - Генерация устойчивых различимых цветов (например, golden-angle по Hue) + ручная правка
  - Критерии приёмки:
    - Новые задачи получают разные цвета, читаемые на светлой/тёмной теме

- [x] **RouletteWheel composable** ✅ Done
  - Canvas-отрисовка секторов, подписи, фиксированный pointer
  - Сектор = пропорционален weight: \(a_i = 360° \cdot w_i / \sum w\)
  - Критерии приёмки:
    - Для 1..50 задач колесо корректно рисуется без артефактов
    - Узкие сектора не ломают UI (ellipsis/уменьшение шрифта)

- [x] **Spin physics & animation** ✅ Done
  - Плавный “physical-style” спин + рандомизированное замедление
  - Обязательное условие: колесо всегда останавливается точно на выбранной задаче
  - Критерии приёмки:
    - 60fps на типичных устройствах (без заметных просадок)
    - На каждом спине итог совпадает с выбранным task (без “между секторами”)

- [x] **Sound + Haptics** ✅ Done
  - Tick во время спина + stop sound; лёгкая вибрация на ticks + “success” на остановке
  - Критерии приёмки:
    - Настройки Sound/Haptics выключают эффекты полностью
    - Нет конфликтов/зацикливаний при быстрых повторных спинах

- [x] **Result screen + Celebration** ✅ Done
  - Экран результата: “Your task for now”, конфетти, success haptic, выделение выбранного task
  - Установка Active Task (persisted)
  - Критерии приёмки:
    - Конфетти/вибрация срабатывают ровно 1 раз на спин
    - Active Task сохраняется при рестарте

- [x] **Game Session persistence (History)** ✅ Done
  - Создавать сессию на каждый спин: timestamp + tasks snapshot + selected
  - Экран History: список → детали → replay
  - Критерии приёмки:
    - История отображается после перезапуска
    - Открытие деталей показывает выбранную задачу

- [x] **Replay mechanism** ✅ Done
  - Replay запускает колесо на snapshot задач и делает новый спин (fresh random)
  - Критерии приёмки:
    - Replay не зависит от текущего списка задач
    - Replay создаёт новую запись в истории

- [x] **Statistics (Local)** ✅ Done
  - Total spins, most frequently selected task
  - “Quick Spin” на последнем наборе задач
  - Критерии приёмки:
    - Статы считаются только по локальным данным (без сети)
    - Результат “most frequent” детерминирован при равенстве (tie-break rule)

- [x] **Settings** ✅ Done
  - Sound on/off, Haptics on/off, базовый выбор темы (sound/confetti/wheel) + (optional) reduced motion
  - Критерии приёмки:
    - Настройки хранятся в DataStore и применяются к следующему спину

### 2. UX polish tasks (Delight & perceived quality)

- [ ] **Multiple spin sound themes (2–3)** _(In Work → branch=`251224_feature_uxpolish_v1`)_
  - Критерии приёмки: тема меняется без крашей, корректно применена на спине

- [ ] **Confetti style variations (2–3)**
  - Критерии приёмки: стили отличаются визуально и не просаживают fps

- [ ] **Subtle UI animations (scale / glow / blur)**
  - Критерии приёмки: анимации не мешают чтению и не ломают accessibility

- [ ] **Improved label readability**
  - Разумное поведение при большом количестве задач (ellipsis, размер шрифта, контраст)
  - Критерии приёмки: нет “перекрытий” текста и плохого контраста

- [ ] **Accessibility & localization readiness**
  - ContentDescription, TalkBack, large font scaling, RTL smoke-check
  - Критерии приёмки: критические элементы читаемы и доступны

### 3. Technical debt (future)

- [ ] **Performance validation**
  - Macrobenchmark (cold start) + профилирование анимаций

- [ ] **Room schema hardening**
  - Экспорт схемы, миграции, тесты миграций (по мере изменений)

- [ ] **Codebase modularization (по мере роста)**
  - Разделение по фичам (wheel/history/settings), чтобы не разрастались файлы

- [ ] **Testing discipline**
  - Расширить покрытие UseCases & Repositories (happy + edge), UI tests для критического флоу

### 4. Nice-to-have features (Optional)

- [ ] **Completion tracking + streaks**
  - Mark task as done + longest streak calculation

- [ ] **Task sets / presets**
  - Наборы задач (например: “Morning”, “Work”, “Weekend”)

- [ ] **Import / Export (offline)**
  - JSON экспорт/импорт задач (без аккаунтов/облака)

- [ ] **Gesture spin (fling)**
  - Доп. способ запуска спина (кроме кнопки)

- [ ] **Cosmetic IAP (one-time purchase)**
  - Unlock pack’и: themes/confetti/sounds (без подписок)
  - Примечание: требует отдельного согласования и обновления PRD по биллингу

---

## ✅ Already Developed Features
- Android project scaffold (app module, launcher icon, базовые темы)
- Single-Activity entry point (`MainActivity`)
