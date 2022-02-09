import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateUserTests {
    StellarBurgersUserClient client;
    String token;

    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
    }

    @DisplayName("Создание User-a.")
    @Description("Создание User-a.Позитивная проверка")
    @Step("Проверка создания User-a с передачей всех обязательных полей")
    @Test
    public void createUniqueUserTest() {
        User user = User.getUser();
        ValidatableResponse response = client.createUser(user);
        boolean isCreated = response.assertThat().statusCode(200).extract().path("success");
        token = response.extract().path("accessToken");
        assertThat("Позитивный кейс создания User возвращает неверный message", isCreated, equalTo(true));
        client.deleteUser(token.substring(7));
    }

    @DisplayName("Создание существующего User-a.")
    @Description("Создание существующего User-a.Негативная проверка")
    @Step("Проверка невозможности создания User-a уже существующего в базе")
    @Test
    public void createIdenticalUserTest() {
        User user = User.getUser();
        client.createUser(user);
        String actualMessage = client.createUser(user).assertThat().statusCode(403).extract().path("message");
        assertThat("При создании существующего User возвращается неверный message",
                actualMessage, equalTo("User already exists"));
    }

    @DisplayName("Создание User-а без name field")
    @Description("Создание User-a без поля name.Негативная проверка")
    @Step("Проверка невозможности создания User-a без обязательного поля name")
    @Test
    public void createUserWithoutNameFieldTest() {
        UserWithoutNameField user = UserWithoutNameField.getUserWithoutName();
        String actualMessage = client.createUserWithoutName(user).assertThat().statusCode(403).extract().path("message");
        assertThat("При создании User без поля name возвращается неверный message",
                actualMessage, equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Создание User-а без password field")
    @Description("Создание User-a без поля password.Негативная проверка")
    @Step("Проверка создания User-a без обязательного поля password")
    @Test
    public void createUserWithoutPasswordFieldTest() {
        UserWithoutPasswordField user = UserWithoutPasswordField.getUserWithoutPassword();
        String actualMessage = client.createUserWithoutPassword(user).assertThat().statusCode(403).extract().path("message");
        assertThat("При создании User без поля password возвращается неверный errorMessage",
                actualMessage, equalTo("Email, password and name are required fields"));

    }

    @DisplayName("Создание User-а без email field")
    @Description("Создание User-a без поля email.Негативная проверка")
    @Step("Проверка создания User-a без обязательного поля email")
    @Test
    public void createUserWithoutEmailFieldTest() {
        UserWithoutEmailField user = UserWithoutEmailField.getUserWithoutEmailField();
        String actualMessage = client.createUserWithoutEmail(user).assertThat().statusCode(403).extract().path("message");
        assertThat("При создании User без поля email возвращается неверный errorMessage",
                actualMessage, equalTo("Email, password and name are required fields"));

    }

}
