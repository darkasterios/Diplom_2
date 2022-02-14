import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends StellarBurgerClient{
    private static final String ORDER_PATH = "/api/orders";

    @Step("Создание заказа с отправкой токена")
    public ValidatableResponse createOrderWithToken(String ingredients, String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Создание заказа без токена")
    public ValidatableResponse createOrderWithoutToken(String ingredients){
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получить заказы конкретного User-a с отправкой токена")
    public ValidatableResponse getOrdersSpecificUserWithToken(String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получить заказы конкретного User-a без отправки токена")
    public ValidatableResponse getOrdersSpecificUserWithoutToken(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

}
