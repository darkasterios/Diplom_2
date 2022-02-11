import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class StellarBurgersUserClient extends  StellarBurgerClient{
    private static final String USER_PATH = "/api/auth/";


    public ValidatableResponse createUser(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    public ValidatableResponse createUserWithoutName(UserWithoutNameField user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }
    public ValidatableResponse createUserWithoutPassword(UserWithoutPasswordField user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }
    public ValidatableResponse createUserWithoutEmail(UserWithoutEmailField user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }
    public ValidatableResponse loginUser(UserWithoutNameField userWithoutObligatoryField){
        return given()
                .spec(getBaseSpec())
                .body(userWithoutObligatoryField)
                .when()
                .post(USER_PATH + "login")
                .then();
    }

    public ValidatableResponse changeUserName(UserWithNameFieldOnly user, String token){
    return given()
            .spec(getBaseSpec())
            .auth().oauth2(token)
            .body(user)
            .when()
            .patch(USER_PATH + "user")
            .then();
    }
    public ValidatableResponse changeUserEmail(UserWithEmailFieldOnly user, String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(user)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }
    public ValidatableResponse changeUserEmailWithoutToken(UserWithEmailFieldOnly user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }
    public ValidatableResponse changeUserNameWithoutToken(UserWithNameFieldOnly user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }
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
