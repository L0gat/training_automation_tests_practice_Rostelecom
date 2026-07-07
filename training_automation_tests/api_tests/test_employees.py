import allure
import pytest


@allure.epic("API Тесты")
@allure.feature("Сервис сотрудников")
class TestEmployees:

    # =========================================================
    # ПОЗИТИВНЫЕ ТЕСТЫ
    # =========================================================

    @allure.story("Создание сотрудника")
    @allure.title("Создание сотрудника с валидными данными")
    @allure.severity(allure.severity_level.CRITICAL)
    def test_create_employee_positive(self, api_client, random_employee_data):
        """Позитивный тест: создание сотрудника"""
        data = random_employee_data()
        response = api_client.post("/api/employees", json=data)
        
        assert response.status_code == 201
        assert response.json()["name"] == data["name"]
        assert response.json()["email"] == data["email"]
        assert response.json()["position"] == data["position"]
        assert response.json()["department"] == data["department"]
        assert response.json()["company"] == data["company"]
        assert response.json()["salary"] == data["salary"]
        assert "id" in response.json()
        assert "createdAt" in response.json()
        assert "updatedAt" in response.json()

    @allure.story("Получение сотрудника")
    @allure.title("Получение существующего сотрудника по ID")
    @allure.severity(allure.severity_level.CRITICAL)
    def test_get_employee_positive(self, api_client, created_employee_id):
        """Позитивный тест: получение сотрудника по ID"""
        response = api_client.get(f"/api/employees/{created_employee_id}")
        
        assert response.status_code == 200
        assert response.json()["id"] == created_employee_id
        assert "name" in response.json()
        assert "email" in response.json()
        assert "position" in response.json()
        assert "department" in response.json()
        assert "company" in response.json()
        assert "salary" in response.json()
        assert "createdAt" in response.json()
        assert "updatedAt" in response.json()

    @allure.story("Обновление сотрудника")
    @allure.title("Полное обновление сотрудника (PUT)")
    @allure.severity(allure.severity_level.CRITICAL)
    def test_update_employee_put_positive(self, api_client, created_employee_id):
        """Позитивный тест: полное обновление сотрудника через PUT"""
        updated_data = {
            "name": "Обновленное Имя",
            "email": "updated@example.com",
            "position": "Senior Java Developer",
            "department": "Архитектура",
            "company": "TechStar",
            "salary": 250000
        }
        
        response = api_client.put(f"/api/employees/{created_employee_id}", json=updated_data)
        
        assert response.status_code == 200
        assert response.json()["id"] == created_employee_id
        assert response.json()["name"] == "Обновленное Имя"
        assert response.json()["email"] == "updated@example.com"
        assert response.json()["position"] == "Senior Java Developer"
        assert response.json()["department"] == "Архитектура"
        assert response.json()["company"] == "TechStar"
        assert response.json()["salary"] == 250000

    @allure.story("Частичное обновление сотрудника")
    @allure.title("Частичное обновление только salary (PATCH)")
    @allure.severity(allure.severity_level.NORMAL)
    def test_patch_employee_only_salary(self, api_client, created_employee_id):
        """
        Позитивный тест: PATCH с salary.
        Поле name обязательно даже в PATCH-запросе.
        """
        # 1. Получаем текущие данные сотрудника
        get_response = api_client.get(f"/api/employees/{created_employee_id}")
        assert get_response.status_code == 200
        original = get_response.json()
        
        # 2. Отправляем PATCH с salary и name (обязательное поле)
        new_salary = 999999
        patch_data = {
            "name": original["name"],  # обязательно!
            "salary": new_salary
        }
        patch_response = api_client.patch(f"/api/employees/{created_employee_id}", json=patch_data)
        
        # 3. Проверяем статус
        assert patch_response.status_code == 200
        
        # 4. Проверяем, что зарплата обновилась
        updated = patch_response.json()
        assert updated["salary"] == new_salary
        
        # 5. Проверяем, что остальные поля не изменились
        assert updated["name"] == original["name"]
        assert updated["email"] == original["email"]
        assert updated["position"] == original["position"]
        assert updated["department"] == original["department"]
        assert updated["company"] == original["company"]

    @allure.story("Удаление сотрудника")
    @allure.title("Удаление существующего сотрудника")
    @allure.severity(allure.severity_level.CRITICAL)
    def test_delete_employee_positive(self, api_client, random_employee_data):
        """Позитивный тест: удаление сотрудника"""
        data = random_employee_data()
        create_response = api_client.post("/api/employees", json=data)
        employee_id = create_response.json()["id"]
        
        delete_response = api_client.delete(f"/api/employees/{employee_id}")
        assert delete_response.status_code == 204
        
        get_response = api_client.get(f"/api/employees/{employee_id}")
        assert get_response.status_code == 404

    @allure.story("Список сотрудников")
    @allure.title("Получение списка всех сотрудников")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_all_employees(self, api_client):
        """Позитивный тест: получение списка сотрудников"""
        response = api_client.get("/api/employees")
        
        assert response.status_code == 200
        assert isinstance(response.json(), list)
        if response.json():
            first = response.json()[0]
            assert "id" in first
            assert "name" in first
            assert "email" in first
            assert "position" in first
            assert "department" in first
            assert "company" in first
            assert "salary" in first

    # =========================================================
    # НЕГАТИВНЫЕ ТЕСТЫ
    # =========================================================

    @allure.story("Создание сотрудника")
    @allure.title("Создание сотрудника без обязательного поля (name)")
    @allure.severity(allure.severity_level.NORMAL)
    def test_create_employee_missing_name(self, api_client):
        """Негативный тест: отсутствует поле name"""
        data = {
            "email": "test@example.com",
            "position": "Developer",
            "department": "Разработка",
            "company": "Acme Corp",
            "salary": 100000
        }
        response = api_client.post("/api/employees", json=data)
        assert response.status_code in [400, 422]

    @allure.story("Создание сотрудника")
    @allure.title("Создание сотрудника с salary = 0")
    @allure.severity(allure.severity_level.NORMAL)
    def test_create_employee_salary_zero(self, api_client):
        """Негативный тест: salary = 0 (должно быть > 0)"""
        data = {
            "name": "Тест Тестов",
            "email": "test@example.com",
            "position": "Developer",
            "department": "Разработка",
            "company": "Acme Corp",
            "salary": 0
        }
        response = api_client.post("/api/employees", json=data)
        assert response.status_code in [400, 422]

    @allure.story("Получение сотрудника")
    @allure.title("Получение несуществующего сотрудника")
    @allure.severity(allure.severity_level.NORMAL)
    def test_get_employee_not_found(self, api_client):
        """Негативный тест: несуществующий ID"""
        response = api_client.get("/api/employees/999999")
        assert response.status_code == 404

    @allure.story("Удаление сотрудника")
    @allure.title("Удаление несуществующего сотрудника")
    @allure.severity(allure.severity_level.NORMAL)
    def test_delete_employee_not_found(self, api_client):
        """Негативный тест: удаление несуществующего ID"""
        response = api_client.delete("/api/employees/999999")
        assert response.status_code == 404