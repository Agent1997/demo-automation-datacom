import dto.UserRequestDTO;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTests {

    @Test
    public void verifySuccessfulCreationOfAUser() {
        String name = "Rommel";
        String job = "AE";

        given().baseUri(Urls.BASE_URL)
                .contentType("application/json")
                .body(new UserRequestDTO(name, job))
                .when()
                .post(Urls.USERS_ENDPOINT)
                .then()
                .statusCode(equalTo(201))
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .extract()
                .response()
                .prettyPrint();
    }
}
