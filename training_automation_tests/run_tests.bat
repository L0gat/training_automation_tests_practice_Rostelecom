@echo off
chcp 65001 >nul
title Автотесты для API и UI

:menu
echo ============================================================
echo              ЗАПУСК АВТОТЕСТОВ
echo ============================================================
echo.
echo Выберите, что запустить:
echo   1 - Только API-тесты
echo   2 - Только UI-тесты
echo   3 - Все тесты
echo   4 - Выйти
echo.
set /p choice="Ваш выбор (1/2/3/4): "

if "%choice%"=="1" goto api
if "%choice%"=="2" goto ui
if "%choice%"=="3" goto all
if "%choice%"=="4" goto exit
goto menu

:api
echo.
echo [1/2] API-тесты...
echo ------------------------------------------------------------
cd api_tests
pip install -r requirements.txt
pytest -v --alluredir=allure-results
echo.
echo ✅ API-тесты завершены!
echo 📊 Отчёт: allure serve allure-results
echo ------------------------------------------------------------
cd ..
echo.
echo ============================================================
echo  Нажмите любую клавишу для выхода...
echo ============================================================
pause >nul
exit

:ui
echo.
echo [2/2] UI-тесты...
echo ------------------------------------------------------------
cd ui_tests
mvnw.cmd clean test
echo.
echo ✅ UI-тесты завершены!
echo 📊 Отчёт: mvn allure:serve
echo ------------------------------------------------------------
cd ..
echo.
echo ============================================================
echo  Нажмите любую клавишу для выхода...
echo ============================================================
pause >nul
exit

:all
echo.
echo ============================================================
echo  ЗАПУСК ВСЕХ ТЕСТОВ
echo ============================================================
echo.
echo [1/2] API-тесты...
echo ------------------------------------------------------------
cd api_tests
pip install -r requirements.txt
pytest -v --alluredir=allure-results
echo.
echo ✅ API-тесты завершены!
echo 📊 Отчёт: allure serve allure-results
echo ------------------------------------------------------------
cd ..
echo.
echo ============================================================
echo  Нажмите любую клавишу для продолжения к UI-тестам...
echo ============================================================
pause >nul
echo.
echo [2/2] UI-тесты...
echo ------------------------------------------------------------
cd ui_tests
mvnw.cmd clean test
echo.
echo ✅ UI-тесты завершены!
echo 📊 Отчёт: mvn allure:serve
echo ------------------------------------------------------------
cd ..
echo.

:result
echo.
echo ============================================================
echo              ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ!
echo ============================================================
echo.
echo 📊 Для просмотра Allure-отчётов выполните:
echo.
echo   1. API-тесты:
echo      cd api_tests
echo      allure serve allure-results
echo.
echo   2. UI-тесты:
echo      cd ui_tests
echo      mvn allure:serve
echo.
echo ============================================================
echo  Нажмите любую клавишу для выхода...
echo ============================================================
pause >nul
exit

:exit
exit