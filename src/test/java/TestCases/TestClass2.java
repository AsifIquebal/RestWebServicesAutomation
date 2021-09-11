package TestCases;

import base.Base;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojos.MyPost;
import utility.utils;

import static io.restassured.RestAssured.given;

public class TestClass2 extends Base {

    Response response;

    @Test
    public void verifyPostsAddedSuccessfully() {
        String title = "Test Title";
        String body = "Test Body";
        String userId = "786";
        MyPost myPost = new MyPost();
        myPost
                .setTitle(title)
                .setBody(body)
                .setUserId(userId);
        response = utils.POST_Request("/posts", myPost);
        Assert.assertEquals(JsonPath.read(response.asString(), "$.title"), title);
        Assert.assertEquals(JsonPath.read(response.asString(), "$.body"), body);
        Assert.assertEquals(JsonPath.read(response.asString(), "$.userId"), userId);
    }

    @Test
    public void verifyPostUpdateSuccessfully() {
        String newTitle = "This is a new title";
        String id = "1";
        String oldTitle = given().when().get("/posts/" + id).then().extract().path("title");
        System.out.println(oldTitle);
        MyPost myPost = new MyPost();
        myPost
                .setTitle(newTitle)
                .setUserId(id);
        response = utils.PUT_Request("/posts/" + id, myPost);
        System.out.println(response.asString());
        Assert.assertEquals(JsonPath.read(response.asString(), "$.title"), newTitle);
        Assert.assertNotEquals(JsonPath.read(response.asString(), "$.title"), oldTitle);
    }

    @Test
    public void verifyPostsCanBeDeletedSuccessfully() {
        String id = "1";
        response = utils.DELETE_Request("posts/" + id);
        Assert.assertTrue(response.getStatusCode() == 200);
        // usually we call a get/put after deletion and expect 404
    }

}
