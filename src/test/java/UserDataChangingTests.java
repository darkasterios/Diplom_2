import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDataChangingTests {
    StellarBurgersUserClient client;
    String token;

    @Step("Создание StellarBurgerClient перед тестом")
    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
    }

    @Step("Удаления User-а после теста в случае если token заполнен")
    @After
    public void deleteTestUser() {
        if (token != null)
            client.deleteUser(token.substring(7));
    }

    @DisplayName("Изменение email User-a с авторизацией")
    @Description("Изменение email User-a с авторизацией.Позитивная проверка")
    @Test
    public void userEmailChangingWithAuthorizationTest() {
        User user = User.getUser();
        ValidatableResponse response = client.createUser(user);
        String email = response.extract().path("user.email");
        token = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(user)).extract().path("accessToken");
        ValidatableResponse response1 = client.changeUserEmail(UserWithEmailFieldOnly.getUserWithEmailField(), token.substring(7));
        String emailAfterChanging = response1.extract().path("user.email");
        assertNotEquals("Поле email не было изменено в методе userNameChangingWithAuthorizationTest с авторизацией",
                email,
                emailAfterChanging);
    }

    @DisplayName("Изменение name User-a с авторизацией")
    @Description("Изменение name User-a с авторизацией.Позитивная проверка")
    @Test
    public void userNameChangingWithAuthorizationTest() {
        User user = User.getUser();
        ValidatableResponse response = client.createUser(user);
        String name = response.extract().path("user.name");
        token = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(user)).extract().path("accessToken");
        ValidatableResponse response1 = client.changeUserName(UserWithNameFieldOnly.getUserWithNameField(), token.substring(7));
        String nameAfterChanging = response1.extract().path("user.name");
        assertNotEquals("Поле name не было изменено в методе userNameChangingWithAuthorizationTest с авторизацией",
                name,
                nameAfterChanging);
    }

    @DisplayName("Изменение email User-a без авторизации")
    @Description("Изменение email User-a без авторизации.Негативная проверка")
    @Test
    public void userEmailChangingWithoutAutorizationTest() {
        boolean isEmailChanged = client.changeUserEmailWithoutToken(UserWithEmailFieldOnly.getUserWithEmailField())
                .statusCode(401).extract().path("success");
        assertFalse("Без авторизации возможно изменить поле email", isEmailChanged);
    }

    @DisplayName("Изменение name User-a без авторизации")
    @Description("Изменение name User-a без авторизации.Негативная проверка")
    @Test
    public void userNameChangingWithoutAutorizationTest() {
        boolean isNameChanged = client.changeUserNameWithoutToken(UserWithNameFieldOnly.getUserWithNameField())
                .statusCode(401).extract().path("success");
        assertFalse("Без авторизации возможно изменить поле name", isNameChanged);
    }

    @DisplayName("Проверка сообщения об ошибке при изменении userData без авторизации")
    @Description("Проверка сообщения об ошибке при изменении userData без авторизации.Негативная проверка")
    @Test
    public void checkErrorMessageInChangingWithoutAutorizationTest() {
        String errorMessage = client.changeUserNameWithoutToken(UserWithNameFieldOnly.getUserWithNameField())
                .statusCode(401).extract().path("message");
        assertEquals("Возвращается неверный errorMessage при изменении UserData без авторизации",
                errorMessage, "You should be authorised");
    }
}
