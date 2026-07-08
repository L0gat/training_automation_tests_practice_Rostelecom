import pytest
import random
import string


@pytest.fixture
def api_client():
    """
    Фикстура, которая возвращает клиент для API.
    Используется в каждом тесте как параметр.
    """
    from utils.api_client import ApiClient
    return ApiClient


@pytest.fixture
def random_employee_data():
    """
    Фикстура, которая генерирует случайные данные для сотрудника.
    Возвращает функцию, чтобы каждый раз генерировать новые данные.
    """
    def _generate():
        # Генерируем случайное имя (Тест + 6 случайных букв)
        name = f"Тест {''.join(random.choices(string.ascii_letters, k=6))}"
        
        # Генерируем email из имени
        email = f"{name.lower().replace(' ', '.')}@example.com"
        
        # Возвращаем словарь с данными для API
        return {
            "name": name,
            "email": email,
            "position": random.choice(["QA Engineer", "Java Developer", "Manager", "Analyst"]),
            "department": random.choice(["Разработка", "Тестирование", "Аналитика", "DevOps"]),
            "company": random.choice(["Acme Corp", "TechStar", "DataFlow", "CloudInc"]),
            "salary": random.randint(60000, 200000)
        }
    return _generate


@pytest.fixture
def created_employee_id(api_client, random_employee_data):
    """
    Фикстура, которая:
    1. Создает сотрудника
    2. Возвращает его ID
    3. После теста автоматически удаляет сотрудника
    """
    # 1. Генерируем данные и создаем сотрудника
    data = random_employee_data()
    response = api_client.post("/api/employees", json=data)
    
    # Проверяем, что создалось успешно
    assert response.status_code == 201, f"Не удалось создать сотрудника: {response.text}"
    
    # 2. Получаем ID из ответа
    employee_id = response.json()["id"]
    
    # 3. Отдаем ID в тест
    yield employee_id
    
    # 4. После теста удаляем сотрудника (очистка)
    api_client.delete(f"/api/employees/{employee_id}")