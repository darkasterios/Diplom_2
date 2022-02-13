import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CreateUserTests {
    StellarBurgersUserClient client;
    String token;

    @Step("Создание StellarBurgerClient перед тестом")
    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
    }

    @Step("Удаления User-а после теста в случае если token заполнен")
    @After
    public void deleteTestUser(){
            if (token != null)
            client.deleteUser(token.substring(7));
    }



    @DisplayName("Создание User-a.")
    @Description("Создание User-a.Позитивная проверка")
    @Test
    public void createUniqueUserTest() {
        User user = User.getUser();
        ValidatableResponse response = client.createUser(user);
        boolean isCreated = response.statusCode(200).extract().path("success");
        token = response.extract().path("accessToken");
        assertTrue("Позитивный кейс создания User возвращает неверный message", isCreated);
    }

    @DisplayName("Создание существующего User-a.")
    @Description("Создание существующего User-a.Негативная проверка")
    @Test
    public void createIdenticalUserTest() {
        User user = User.getUser();
        client.createUser(user);
        String actualMessage = client.createUser(user).statusCode(403).extract().path("message");
        assertEquals("При создании существующего User возвращается неверный message",
                actualMessage, "User already exists");
    }

    @DisplayName("Создание User-а без name field")
    @Description("Создание User-a без поля name.Негативная проверка")
    @Test
    public void createUserWithoutNameFieldTest() {
        UserWithoutNameField user = UserWithoutNameField.getUserWithoutName();
        String actualMessage = client.createUserWithoutName(user).statusCode(403).extract().path("message");
        assertEquals("При создании User без поля name возвращается неверный message",
                actualMessage, "Email, password and name are required fields");
    }

    @DisplayName("Создание User-а без password field")
    @Description("Создание User-a без поля password.Негативная проверка")
    @Test
    public void createUserWithoutPasswordFieldTest() {
        UserWithoutPasswordField user = UserWithoutPasswordField.getUserWithoutPassword();
        String actualMessage = client.createUserWithoutPassword(user).statusCode(403).extract().path("message");
        assertEquals("При создании User без поля password возвращается неверный errorMessage",
                actualMessage, "Email, password and name are required fields");

    }

    @DisplayName("Создание User-а без email field")
    @Description("Создание User-a без поля email.Негативная проверка")
    @Test
    public void createUserWithoutEmailFieldTest() {
        UserWithoutEmailField user = UserWithoutEmailField.getUserWithoutEmailField();
        String actualMessage = client.createUserWithoutEmail(user).statusCode(403).extract().path("message");
        assertEquals("При создании User без поля email возвращается неверный errorMessage",
                actualMessage, "Email, password and name are required fields");

    }

}
