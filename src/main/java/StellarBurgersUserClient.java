import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class StellarBurgersUserClient extends  StellarBurgerClient{
    private static final String USER_PATH = "/api/auth/";

    @Step("Создание User-a с валидными полями name,email,password")
    public ValidatableResponse createUser(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step("Создание User-a без поля name")
    public ValidatableResponse createUserWithoutName(UserWithoutNameField user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step("Создание User-a без поля пароль")
    public ValidatableResponse createUserWithoutPassword(UserWithoutPasswordField user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step("Создание User-a без поля email")
    public ValidatableResponse createUserWithoutEmail(UserWithoutEmailField user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step("Авторизация User-a с существующей в базе парой email/password")
    public ValidatableResponse loginUser(UserWithoutNameField userWithoutObligatoryField){
        return given()
                .spec(getBaseSpec())
                .body(userWithoutObligatoryField)
                .when()
                .post(USER_PATH + "login")
                .then();
    }

    @Step("Измение поля name User-a с авторизацией")
    public ValidatableResponse changeUserName(UserWithNameFieldOnly user, String token){
    return given()
            .spec(getBaseSpec())
            .auth().oauth2(token)
            .body(user)
            .when()
            .patch(USER_PATH + "user")
            .then();
    }

    @Step("Измение поля email User-a с авторизацией")
    public ValidatableResponse changeUserEmail(UserWithEmailFieldOnly user, String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(user)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

    @Step("Измение поля email User-a без авторизацией")
    public ValidatableResponse changeUserEmailWithoutToken(UserWithEmailFieldOnly user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

    @Step("Измение поля name User-a без авторизацией")
    public ValidatableResponse changeUserNameWithoutToken(UserWithNameFieldOnly user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

    @Step("Удаление User-a")
    public void deleteUser(String token){
        given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .delete(USER_PATH + "user")
                .then()
                .statusCode(202);
    }
}
