import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderTests {
    OrderClient orderClient;
    StellarBurgersUserClient client;
    UserWithoutNameField logedUser = new UserWithoutNameField("Odin@yandex.ru", "OdinOdin");
    String ingredients = "{\n" + "\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]\n" + "}\n";
    String emptyIngredients = "";

    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
        orderClient = new OrderClient();
    }

    @DisplayName("Создание заказа с авторизацией")
    @Description("Создание заказа.Позитивная проверка")
    @Step("Проверка возможности создание заказа с авторизацией")
    @Test
    public void createOrderWithAuthorizationTest() {
        String token = client.loginUser(logedUser).extract().path("accessToken");
        ValidatableResponse actual = orderClient.createOrderWithToken(ingredients, token.substring(7))
                .assertThat().statusCode(200).body("order.ingredients.id", notNullValue());
        assertThat("Массив Orders возвращается с NullValue id", actual, notNullValue());
    }

    @DisplayName("Создание заказа без авторизацией")
    @Description("Создание заказа.Негативная проверка")
    @Step("Проверка создания заказа без авторизации")
    @Test
    public void createOrderWithoutAuthorizationTest() {
        ValidatableResponse response = orderClient.createOrderWithoutToken(ingredients)
                .assertThat().statusCode(200).body("order.number", notNullValue());
        assertThat("Массив order возвращается с NullValue number", response, notNullValue());
    }

    @DisplayName("Создание заказа без авторизации и ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Step("Проверка создания заказа без авторизации и ингредиентов")
    @Test
    public void createOrderWithoutTokenAndIngredientsTest() {
        String expectedErrorMessage = "Ingredient ids must be provided";
        String actualErrorMessage = orderClient.createOrderWithoutToken(emptyIngredients)
                .assertThat().statusCode(400).extract().path("message");
        assertThat("При создании заказа без ингредиентов возвращается неверный errorMessage",
                actualErrorMessage, equalTo(expectedErrorMessage));
    }

    @DisplayName("Создание заказа с авторизации, без ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Step("Проверка создания заказа с авторизацией и без ингредиентов")
    @Test
    public void createOrderWithAuthorizationWithoutIngredient() {
        String expectedErrorMessage = "Ingredient ids must be provided";
        String token = client.loginUser(logedUser).extract().path("accessToken");
        String actualErrorMessage = orderClient.createOrderWithToken(emptyIngredients, token.substring(7))
                .assertThat().statusCode(400).extract().path("message");
        assertThat("При создании заказа без ингредиентов возвращается неверный errorMessage",
                actualErrorMessage, equalTo(expectedErrorMessage));
    }

    @DisplayName("Создание заказа с авторизацией и неверным хэшем ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Step("Проверка создания заказа с авторизацией и неверным хэшем ингридиентов")
    @Test
    public void createOrderWithAuthInvalidIngredientsHashTest() {
        String invalidIngredientsHash = "{\n" + "\"ingredients\": [1]\n" + "}";
        int actualStatusCode = orderClient.createOrderWithoutToken(invalidIngredientsHash).extract().statusCode();
        assertThat("При создании заказа без авторизации с невалидным хэшем возвращает !=500 статус код",
                actualStatusCode, equalTo(500));
    }

    @DisplayName("Создание заказа без авторизации и неверным хэшем ингридиентов")
    @Description("Создание заказа.Негативная проверка")
    @Step("Проверка создания заказа без авторизации и неверным хэшем ингридиентов")
    @Test
    public void createOrderWithoutAuthInvalidIngredientsHashTest() {
        String invalidIngredientsHash = "{\n" + "\"ingredients\": [1]\n" + "}";
        String token = client.loginUser(logedUser).extract().path("accessToken");
        int actualStatusCode = orderClient.createOrderWithToken(invalidIngredientsHash,
                token.substring(7)).extract().statusCode();
        assertThat("При создании заказа с авторизацией и невалидным хэшем возвращает !=500 статус код",
                actualStatusCode, equalTo(500));
    }

    @DisplayName("Получение заказов конкретного пользователя: с авторизацией")
    @Description("Получение заказов конкретного пользователя.Позитивная проверка")
    @Step("Проверка получения заказов конкретного пользователя с авторизацией")
    @Test
    public void getSpecificUserOrdersWithAuthorizationTest() {
        String token = client.loginUser(logedUser).extract().path("accessToken");
        ValidatableResponse actual = orderClient.getOrdersSpecificUserWithToken(token.substring(7))
                .statusCode(200).body("orders.id", notNullValue());
        assertThat("При получении данных о заказах конкретного user-а поле id = nullValue", actual, notNullValue());
    }

    @DisplayName("Получение заказов конкретного пользователя: без авторизации")
    @Description("Получение заказов конкретного пользователя.Негативная проверка")
    @Step("Проверка получения заказов конкретного пользователя без авторизации")
    @Test
    public void getSpecificUserOrdersWithoutAuthTest() {
        String expectedMessage = "You should be authorised";
        String actualMessage = orderClient.getOrdersSpecificUserWithoutToken()
                .assertThat().statusCode(401).extract().path("message");
        assertThat("При получении данных о заказах user-а без авторизации, возвращается неверный actualMessage"
                , actualMessage, equalTo(expectedMessage));
    }
}
