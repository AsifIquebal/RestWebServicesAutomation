package testcases;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RAndD {

    @Test
    public void settingContentTypeAsContentType() {
        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body("{\r\n" +
                        "    \"firstname\" : \"Jim\",\r\n" +
                        "    \"lastname\" : \"Brown\",\r\n" +
                        "    \"totalprice\" : 111,\r\n" +
                        "    \"depositpaid\" : true,\r\n" +
                        "    \"bookingdates\" : {\r\n" +
                        "        \"checkin\" : \"2018-01-01\",\r\n" +
                        "        \"checkout\" : \"2019-01-01\"\r\n" +
                        "    },\r\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\r\n" +
                        "}")
                .post("https://restful-booker.herokuapp.com/booking")
                .then()
                .log()
                .all();
    }

    @Test
    public void cacheControlExample() {
        given().param("bookId", "123")
                .get("/api/books/")
                .then()
                .statusCode(200)
                .header("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
    }

    @Test
    public void addSingleUser() {
        given()
                .param("ID", "13", "FIRSTNAME", "POL", "LASTNAME", "CRI", "STREET", "1 Main St.", "CITY", "CAD")
                .when()
                .put("thomas-bayer.com/sqlrest/CUSTOMER/13")
                .then()
                .body("CUSTOMER.ID", equalTo("13"));
    }

    @Test
    public void test_paramsAsMap() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", "John");
        parameters.put("token", "1234");
        given().params(parameters).when().get("/cookie").then().assertThat().body(equalTo("username, token"));
    }

    @Test
    public void test_resourceFile() throws IOException {
        URL file = Resources.getResource("PublishFlag_False_Req.json");
        String myJson = Resources.toString(file, Charsets.UTF_8);

        Response responsedata = given().header("Authorization", "AuthorizationValue")
                .header("X-App-Client-Id", "XappClintIDvalue")
                .contentType("application/vnd.api+json")
                .body(myJson)
                .with()
                .when()
                .post("dataPostUrl");
    }
}
