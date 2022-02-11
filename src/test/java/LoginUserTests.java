import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginUserTests {
    StellarBurgersUserClient client;

    @Step("Создание StellarBurgerClient перед тестом")
    @Before
    public void setUp() {
        client = new StellarBurgersUserClient();
    }

    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя.Позитивная проверка")
    @Test
    public void loginUserTest() {
        User user = new User("Alex@yandex.ru", "AlexAlex", "Alex");
        boolean isLoggedIn = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(user))
                .assertThat().statusCode(200).extract().path("success");
        assertTrue("При авторизации существующего User, возвращается false", isLoggedIn);
    }

    @DisplayName("Авторизация пользователя с несуществующей парой email/password")
    @Description("Авторизация пользователя.Негативная проверка")
    @Test
    public void loginUserWithoutObligatoryField() {
        boolean isLoggedIn = client.loginUser(UserWithoutNameField.getUsersEmailAndPassword(User.getUser()))
                .assertThat().statusCode(401).extract().path("success");
        assertFalse("User может залогиниться с несуществующей парой email/password", isLoggedIn);
    }
}
