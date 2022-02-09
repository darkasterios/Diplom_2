import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserDataChangingTests {
    StellarBurgersUserClient client;

    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
    }

    @DisplayName("Изменение email User-a с авторизацией")
    @Description("Изменение email User-a с авторизацией.Позитивная проверка")
    @Step("Проверка возможности изменения поля email User-a с авторизацией")
    @Test
    public void userEmailChangingWithAuthorizationTest() {
        User user = User.getUser();
        ValidatableResponse response = client.createUser(user);
        String email = response.extract().path("user.email");
        String token = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(user)).extract().path("accessToken");
        ValidatableResponse response1 = client.changeUserEmail(UserWithEmailFieldOnly.getUserWithEmailField(), token.substring(7));
        String emailAfterChanging = response1.extract().path("user.email");
        assertThat("Поле email не было изменено в методе userNameChangingWithAuthorizationTest с авторизацией",
                email,
                is(not(equalTo(emailAfterChanging))));
                client.deleteUser(token.substring(7));

    }

    @DisplayName("Изменение name User-a с авторизацией")
    @Description("Изменение name User-a с авторизацией.Позитивная проверка")
    @Step("Проверка возможности изменения поля name User-a с авторизацией")
    @Test
    public void userNameChangingWithAuthorizationTest() {
        User user = User.getUser();
        ValidatableResponse response = client.createUser(user);
        String name = response.extract().path("user.name");
        String token = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(user)).extract().path("accessToken");
        ValidatableResponse response1 = client.changeUserName(UserWithNameFieldOnly.getUserWithNameField(), token.substring(7));
        String nameAfterChanging = response1.extract().path("user.name");
        assertThat("Поле name не было изменено в методе userNameChangingWithAuthorizationTest с авторизацией",
                name,
                is(not(equalTo(nameAfterChanging))));
        client.deleteUser(token.substring(7));
    }

    @DisplayName("Изменение email User-a без авторизации")
    @Description("Изменение email User-a без авторизации.Негативная проверка")
    @Step("Проверка возможности изменения поля email User-a без авторизации")
    @Test
    public void userEmailChangingWithoutAutorizationTest() {
        boolean isEmailChanged = client.changeUserEmailWithoutToken(UserWithEmailFieldOnly.getUserWithEmailField())
                .assertThat().statusCode(401).extract().path("success");
        assertThat("Без авторизации возможно изменить поле email", isEmailChanged, equalTo(false));
    }

    @DisplayName("Изменение name User-a без авторизации")
    @Description("Изменение name User-a без авторизации.Негативная проверка")
    @Step("Проверка возможности изменения поля name User-a без авторизации")
    @Test
    public void userNameChangingWithoutAutorizationTest() {
        boolean isNameChanged = client.changeUserNameWithoutToken(UserWithNameFieldOnly.getUserWithNameField())
                .assertThat().statusCode(401).extract().path("success");
        assertThat("Без авторизации возможно изменить поле name", isNameChanged, equalTo(false));
    }

    @DisplayName("Проверка сообщения об ошибке при изменении userData без авторизации")
    @Description("Проверка сообщения об ошибке при изменении userData без авторизации.Негативная проверка")
    @Step("Проверка соответствия сообщения об ошибке требованиям при изменении userData без авторизации.")
    @Test
    public void checkErrorMessageInChangingWithoutAutorizationTest() {
        String errorMessage = client.changeUserNameWithoutToken(UserWithNameFieldOnly.getUserWithNameField())
                .assertThat().statusCode(401).extract().path("message");
        assertThat("Возвращается неверный errorMessage при изменении UserData без авторизации",
                errorMessage, equalTo("You should be authorised"));
    }
}
