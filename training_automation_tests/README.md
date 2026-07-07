# Автотесты для API и UI

Автоматизированное тестирование:
- **API-тесты** для сервиса управления сотрудниками (Python + pytest)
- **UI-тесты** для игрового портала (Java + Selenium + TestNG)

---

## 📋 Требования

Перед запуском убедитесь, что установлены:

| Инструмент | Версия | Где скачать |
|------------|--------|-------------|
| **Java** | 17 или выше | [Adoptium](https://adoptium.net/) |
| **Python** | 3.10 или выше | [python.org](https://www.python.org/downloads/) |
| **Google Chrome** | последняя версия | [google.com/chrome](https://www.google.com/chrome/) |

**Maven не требуется!** В проекте используется **Maven Wrapper** — он скачает Maven автоматически.
* Для запуска api тестов необходимо скачать training_swagger-main image в docker desktop и запустить его.
---

## 🚀 Быстрый запуск (Windows)

Запустите файл `run_tests.bat` двойным кликом.

Выберите:
- `1` — только API-тесты
- `2` — только UI-тесты
- `3` — все тесты
- `4` — выход

После завершения тестов консоль останется открытой для просмотра результатов.

---

## 🚀 Запуск тестов Отчёты Allure

```bash
cd 'C:/Downloads/training_automation_tests
cd api_tests
pip install -r requirements.txt
pytest -v
cd ui_tests
mvnw.cmd clean test

## Отчёты Allure
cd api_tests
pytest --alluredir=allure-results
allure serve allure-results

cd ui_tests
mvn allure:serve