package TestCases;

import base.Base;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utility.Utils;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestClass1 extends Base {

    Response response;
    ResponseSpecification responseSpecification = null;

    @BeforeClass
    public void setupResponseSpecification() {
        // Create a ResponseSpecification
        responseSpecification = RestAssured.expect();
        responseSpecification.contentType(ContentType.JSON);
        responseSpecification.statusCode(200);
        responseSpecification.time(Matchers.lessThan(5000L));
        responseSpecification.statusLine("HTTP/1.1 200 OK");

    }

    @Description("Test Description Modification: Login test with wrong username and wrong password.")
    @Test(description = "Sample GET call testing")
    public void validateGetAlbumStatusCode() {
        log.info("Calling GET api: " + RestAssured.baseURI + "/albums");
        response = given()
                .when()
                .get("/albums")
                .then().log().status().spec(responseSpecification)
                .extract()
                .response();
        Assert.assertEquals(response.getStatusCode(), 200, "Status Code Mismatch...");
    }

    @Test(description = "GET call from utility")
    public void validateAlbumSize() {
        response = Utils.GET_Response("/albums");
        List<String> list = JsonPath.read(response.asString(), "$.[*].id");
        Assert.assertEquals(list.size(), 100, "Album size didn't matched");
        //response.jsonPath().getList("$.[*].id").size()
    }

    @Test
    public void validateUsersCompany() {
        response = Utils.GET_Response("/users");
        // can be dynamic, based on another end point response
        List<String> list = JsonPath.read(response.asString(), "$.[*].[?(@.name=='Leanne Graham')].company.name");
        Assert.assertEquals(list.get(0), "Romaguera-Crona");
    }

    // hamcrest matcher example
    @Test
    public void testPhotosContainSpecificContent() {
        given()
                .contentType(ContentType.JSON)
                .expect()
                .body("url[0]", equalTo("https://via.placeholder.com/600/92c952"))
                .body("url[4999]", equalTo("https://via.placeholder.com/600/6dd9cb"))
                .when()
                .get("/photos");
    }


}
