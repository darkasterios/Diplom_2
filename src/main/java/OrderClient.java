import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends StellarBurgerClient{
    private static final String ORDER_PATH = "/api/orders";

    public ValidatableResponse createOrderWithToken(String ingredients, String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }
    public ValidatableResponse createOrderWithoutToken(String ingredients){
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }
    public ValidatableResponse getOrdersSpecificUserWithToken(String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .get(ORDER_PATH)
                .then();
    }
    public ValidatableResponse getOrdersSpecificUserWithoutToken(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

}
