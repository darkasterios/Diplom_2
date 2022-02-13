import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderTests {
    OrderClient orderClient;
    StellarBurgersUserClient client;
    UserWithoutNameField logedUser = new UserWithoutNameField("Odin@yandex.ru", "OdinOdin");
    String ingredients = "{\n" + "\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]\n" + "}\n";
    String emptyIngredients = "";

    @Step("Создание StellatBurger&Order clients перед тестом")
    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
        orderClient = new OrderClient();
    }

    @DisplayName("Создание заказа с авторизацией")
    @Description("Создание заказа.Позитивная проверка")
    @Test
    public void createOrderWithAuthorizationTest() {
        String token = client.loginUser(logedUser).extract().path("accessToken");
        ValidatableResponse actual = orderClient.createOrderWithToken(ingredients, token.substring(7))
                .statusCode(200).body("order.ingredients.id", notNullValue());
        assertNotNull("Массив Orders возвращается с NullValue id", actual);
    }

    @DisplayName("Создание заказа без авторизацией")
    @Description("Создание заказа.Негативная проверка")
    @Test
    public void createOrderWithoutAuthorizationTest() {
        ValidatableResponse response = orderClient.createOrderWithoutToken(ingredients)
                .statusCode(200).body("order.number", notNullValue());
        assertNotNull("Массив order возвращается с NullValue number", response);
    }

    @DisplayName("Создание заказа без авторизации и ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Test
    public void createOrderWithoutTokenAndIngredientsTest() {
        String expectedErrorMessage = "Ingredient ids must be provided";
        String actualErrorMessage = orderClient.createOrderWithoutToken(emptyIngredients)
                .statusCode(400).extract().path("message");
        assertEquals("При создании заказа без ингредиентов возвращается неверный errorMessage",
                actualErrorMessage, expectedErrorMessage);
    }

    @DisplayName("Создание заказа с авторизации, без ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Test
    public void createOrderWithAuthorizationWithoutIngredient() {
        String expectedErrorMessage = "Ingredient ids must be provided";
        String token = client.loginUser(logedUser).extract().path("accessToken");
        String actualErrorMessage = orderClient.createOrderWithToken(emptyIngredients, token.substring(7))
                .statusCode(400).extract().path("message");
        assertEquals("При создании заказа без ингредиентов возвращается неверный errorMessage",
                actualErrorMessage, expectedErrorMessage);
    }

    @DisplayName("Создание заказа с авторизацией и неверным хэшем ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Test
    public void createOrderWithAuthInvalidIngredientsHashTest() {
        String invalidIngredientsHash = "{\n" + "\"ingredients\": [1]\n" + "}";
        int actualStatusCode = orderClient.createOrderWithoutToken(invalidIngredientsHash).extract().statusCode();
        assertEquals("При создании заказа без авторизации с невалидным хэшем возвращает !=500 статус код",
                actualStatusCode,500);
    }

    @DisplayName("Создание заказа без авторизации и неверным хэшем ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Test
    public void createOrderWithoutAuthInvalidIngredientsHashTest() {
        String invalidIngredientsHash = "{\n" + "\"ingredients\": [1]\n" + "}";
        String token = client.loginUser(logedUser).extract().path("accessToken");
        int actualStatusCode = orderClient.createOrderWithToken(invalidIngredientsHash,
                token.substring(7)).extract().statusCode();
        assertEquals("При создании заказа с авторизацией и невалидным хэшем возвращает !=500 статус код",
                actualStatusCode,500);
    }

    @DisplayName("Получение заказов конкретного пользователя: с авторизацией")
    @Description("Получение заказов конкретного пользователя.Позитивная проверка")
    @Test
    public void getSpecificUserOrdersWithAuthorizationTest() {
        String token = client.loginUser(logedUser).extract().path("accessToken");
        ValidatableResponse actual = orderClient.getOrdersSpecificUserWithToken(token.substring(7))
                .statusCode(200).body("orders.id", notNullValue());
        assertNotNull("При получении данных о заказах конкретного user-а поле id = nullValue", actual);
    }

    @DisplayName("Получение заказов конкретного пользователя: без авторизации")
    @Description("Получение заказов конкретного пользователя.Негативная проверка")
    @Test
    public void getSpecificUserOrdersWithoutAuthTest() {
        String expectedMessage = "You should be authorised";
        String actualMessage = orderClient.getOrdersSpecificUserWithoutToken().statusCode(401).extract().path("message");
        assertEquals("При получении данных о заказах user-а без авторизации, возвращается неверный actualMessage"
                , actualMessage, expectedMessage);
    }
}
