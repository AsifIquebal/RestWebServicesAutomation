package testcases;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

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
    public void cacheControlExample(){
        given().param("bookId", "123")
                .get("/api/books/")
                .then()
                .statusCode(200)
                .header("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
    }

}
