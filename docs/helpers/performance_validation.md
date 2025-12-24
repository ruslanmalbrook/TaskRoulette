## Performance validation (Macrobenchmark)

### Goal
Provide a repeatable way to measure **cold startup** and spot animation regressions.

### What we added
- `:app` has a dedicated **benchmark** build type (same as `release`, signed with debug for local install).
- `:benchmark` module contains a minimal Macrobenchmark test: `StartupBenchmark.coldStartup()`.
- `app/src/benchmark/AndroidManifest.xml` enables `<profileable android:shell="true" />` only for the benchmark build.

### How to run (local device)
- **Prerequisites**:
  - Connect a physical device (recommended) or an emulator.
  - Disable system animations for more stable results (optional): Window/Transition/Animator = 0.0x.

- **Run macrobenchmark**:

```bash
./gradlew :benchmark:connectedBenchmarkAndroidTest --no-daemon
```

- **Outputs**:
  - See the Gradle console output for measured timings.
  - Additional outputs are written under `benchmark/build/outputs/`.

---

## Проверка производительности (Macrobenchmark)

### Цель
Иметь воспроизводимый способ измерять **cold start** и отслеживать регрессии анимаций.

### Что добавлено
- В `:app` добавлен build type **benchmark** (как `release`, но подписан debug‑ключом для локальной установки).
- Модуль `:benchmark` содержит минимальный Macrobenchmark тест: `StartupBenchmark.coldStartup()`.
- `app/src/benchmark/AndroidManifest.xml` включает `<profileable android:shell="true" />` только для benchmark build.

### Как запускать (локально на устройстве)
- **Требования**:
  - Подключи физическое устройство (лучше) или эмулятор.
  - Для стабильности результатов можно выключить анимации системы (опционально): 0.0x.

- **Запуск macrobenchmark**:

```bash
./gradlew :benchmark:connectedBenchmarkAndroidTest --no-daemon
```

- **Результаты**:
  - Время запуска смотри в выводе Gradle.
  - Доп. артефакты лежат в `benchmark/build/outputs/`.



