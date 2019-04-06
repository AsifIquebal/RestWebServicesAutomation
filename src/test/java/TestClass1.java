import base.Base;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.utils;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestClass1 extends Base {

    Response response;

    @Test
    public void validateGetAlbumStatusCode() {
        log.info("Calling GET api: " + RestAssured.baseURI + "/albums");
        response = given()
                .when()
                .get("/albums")
                .then().log().status()
                .extract()
                .response();
        Assert.assertTrue(response.getStatusCode() == 200, "Status Code Mismatch...");
    }

    @Test
    public void validateAlbumSize() {
        response = utils.GET_Response("/albums");
        List<String> list = JsonPath.read(response.asString(), "$.[*].id");
        Assert.assertTrue(list.size() == 100, "Album size didn't matched");
        //response.jsonPath().getList("$.[*].id").size()
    }

    @Test
    public void validateUsersCompany() {
        response = utils.GET_Response("/users");
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
                .body("url[0]", equalTo("http://placehold.it/600/92c952"))
                .body("url[4999]", equalTo("http://placehold.it/600/6dd9cb"))
                .when()
                .get("/photos");
    }


}
