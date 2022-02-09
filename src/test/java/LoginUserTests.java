import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginUserTests {
    StellarBurgersUserClient client;

    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
    }

    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя.Позитивная проверка")
    @Step("Проверка авторизации User-a с валидной парой email/password")
    @Test
    public void loginUserTest() {
        User user = new User("Alex@yandex.ru", "AlexAlex", "Alex");
        boolean isLoggedIn = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(user))
                .assertThat().statusCode(200).extract().path("success");
        assertThat("При авторизации существующего User, возвращается false", isLoggedIn, equalTo(true));
    }

    @DisplayName("Авторизация пользователя с несуществующей парой email/password")
    @Description("Авторизация пользователя.Негативная проверка")
    @Step("Проверка невозможности авторизации User-a с невалидной парой email/password")
    @Test
    public void loginUserWithoutObligatoryField() {
        boolean isLoggedIn = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(User.getUser()))
                .assertThat().statusCode(401).extract().path("success");
        assertThat("User может залогиниться с несуществующей парой email/password", isLoggedIn, equalTo(false));
    }
}
