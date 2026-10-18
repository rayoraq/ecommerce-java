**1. Какие события активируют пайплайн?**
Блок `on:` в `ci.yml`: **push** в ветку `main` и **pull_request** в `main`. Push в другие ветки пайплайн сам по себе не запускает; он стартует, когда на эту ветку открыт pull request в `main`.

**2. Будет ли пайплайн запускаться заново, если изменен только README.md или документация?**
В текущей конфигурации **да**: у триггера нет фильтра по путям, поэтому любой push в `main` запускает пайплайн. Чтобы не запускать его на изменениях документации, добавляют `paths-ignore`:
```yaml
on:
  push:
    branches: [main]
    paths-ignore: ["**.md", "docs/**"]
  pull_request:
    branches: [main]
    paths-ignore: ["**.md", "docs/**"]
```

**3. Как и где в YAML указывается установка зависимостей?**
В `jobs.test.steps` отдельным шагом после `actions/setup-python`:
```yaml
- name: Install dependencies
  run: |
    python -m pip install --upgrade pip
    pip install -r requirements.txt
```

**4. Как запретить merge, если тесты упали?**
Через защиту ветки: включить *Require a pull request before merging* и *Require status checks to pass* с выбранной проверкой `test`. Тогда кнопка **Merge pull request** заблокирована, пока CI не станет зеленым.

**5. Всегда ли необходимо 100 %-е покрытие тестами?**
Нет. Покрытие показывает, какие строки были выполнены тестами, но не доказывает, что результат проверен правильно: можно получить 100 % без единого осмысленного `assert`. На практике ставят разумный порог и следят, чтобы критичная бизнес-логика была покрыта полностью и с осмысленными проверками.

**6. Какой стек технологий и фреймворк автоматизации используется?**
- Система контроля версий: Git, хостинг GitHub.
- CI/CD: GitHub Actions (конфигурация в YAML, `.github/workflows/ci.yml`), раннер `ubuntu-latest`.
- Python-проект: Python 3.12, pytest, coverage.py.
- Java-проект: JDK 17, Maven, JUnit 5, JaCoCo.