import requests
import allure


class ApiClient:

    BASE_URL = "http://localhost:8080"

    @staticmethod
    def _request(method, endpoint, **kwargs):
        """
        Универсальный метод для отправки любых запросов.
        method: GET, POST, PUT, PATCH, DELETE
        endpoint: /api/employees
        kwargs: json, params, data и т.д.
        """
        # Склеиваем базовый URL и эндпоинт
        url = f"{ApiClient.BASE_URL}{endpoint}"
        
        # Шаг в Allure отчете (чтобы было видно, какой запрос отправляли)
        with allure.step(f"{method.upper()} {endpoint}"):
            # Отправляем запрос
            response = requests.request(method, url, **kwargs)
            
            # Сохраняем статус-код в отчет
            allure.attach(
                str(response.status_code), 
                name="Status Code", 
                attachment_type=allure.attachment_type.TEXT
            )
            
            # Если есть тело ответа — сохраняем его в отчет
            if response.text:
                allure.attach(
                    response.text, 
                    name="Response Body", 
                    attachment_type=allure.attachment_type.JSON
                )
            
            # Возвращаем ответ для проверок в тесте
            return response

    @staticmethod
    def get(endpoint, params=None):
        """GET-запрос (получение данных)"""
        return ApiClient._request("GET", endpoint, params=params)

    @staticmethod
    def post(endpoint, json=None, data=None):
        """POST-запрос (создание данных)"""
        return ApiClient._request("POST", endpoint, json=json, data=data)

    @staticmethod
    def put(endpoint, json=None, data=None):
        """PUT-запрос (полное обновление данных)"""
        return ApiClient._request("PUT", endpoint, json=json, data=data)

    @staticmethod
    def patch(endpoint, json=None, data=None):
        """PATCH-запрос (частичное обновление данных)"""
        return ApiClient._request("PATCH", endpoint, json=json, data=data)

    @staticmethod
    def delete(endpoint):
        """DELETE-запрос (удаление данных)"""
        return ApiClient._request("DELETE", endpoint)