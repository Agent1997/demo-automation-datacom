import dto.UserDataResponseDTO;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GetUsersTests {
    @Test
    public void shouldReturn200WhenGettingUsers() {
        given().baseUri(Urls.BASE_URL)
                .queryParam("page", 1)
                .when()
                .get(Urls.USERS_ENDPOINT)
                .then()
                .statusCode(is(200));
    }

    @Test(dataProvider = "User Data")
    public void userDataShouldBeCorrect(String email, String firstName, String lastName, String avatar) {
        given().baseUri(Urls.BASE_URL)
                .queryParam("page", 2)
                .when()
                .get(Urls.USERS_ENDPOINT)
                .then().statusCode(equalTo(200))
                .body(String.format("data.find {it.email == '%s' }.email", email), equalTo(email))
                .body(String.format("data.find {it.email == '%s' }.first_name", email), equalTo(firstName))
                .body(String.format("data.find {it.email == '%s' }.last_name", email), equalTo(lastName))
                .body(String.format("data.find {it.email == '%s' }.avatar", email), equalTo(avatar));
    }

    @Test(dataProvider = "User Data")
    public void userDataShouldBeCorrectUsingDTO(String email, String firstName, String lastName, String avatar) {
        Response response = given().baseUri(Urls.BASE_URL)
                .queryParam("page", 2)
                .when()
                .get(Urls.USERS_ENDPOINT);

        UserDataResponseDTO actUserData = response.jsonPath().getObject(String.format("data.find {it.email == '%s' }", email), UserDataResponseDTO.class);
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(actUserData.getEmail(), email);
        sa.assertEquals(actUserData.getFirst_name(), firstName);
        sa.assertEquals(actUserData.getLast_name(), lastName);
        sa.assertEquals(actUserData.getAvatar(), avatar);
        sa.assertAll();
    }

    @Test
    public void successResponseSchemaShouldBeCorrect() {
        given().baseUri(Urls.BASE_URL)
                .queryParam("page", 2)
                .when()
                .get(Urls.USERS_ENDPOINT)
                .then()
                .statusCode(equalTo(200))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schema/users-schema.json"));
    }

    @DataProvider(name = "User Data")
    public Object[][] userData() {
        return new Object[][]{
                {"michael.lawson@reqres.in", "Michael", "Lawson", "https://reqres.in/img/faces/7-image.jpg"},
                {"lindsay.ferguson@reqres.in", "Lindsay", "Ferguson", "https://reqres.in/img/faces/8-image.jpg"},
                {"tobias.funke@reqres.in", "Tobias", "Funke", "https://reqres.in/img/faces/9-image.jpg"},
                {"byron.fields@reqres.in", "Byron", "Fields", "https://reqres.in/img/faces/10-image.jpg"}
        };
    }
}
