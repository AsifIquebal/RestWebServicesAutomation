package testcases;

import base.Base;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojos.UsersAPIResponsePojo;
import utility.Utils;

import java.util.Arrays;
import java.util.List;

public class TestClass6 extends Base {

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

    @Description("Use response as pojo")
    @Test
    public void getAllUsers() {
        response = Utils.GET_Response("/users");
        List<UsersAPIResponsePojo> users = Arrays.asList(response.getBody().as(UsersAPIResponsePojo[].class));
        System.out.println("Total user count: " + users.size());
        for (UsersAPIResponsePojo user : users) {
            System.out.println("ID: " + user.getId());
            System.out.println("Company: " + user.getCompany());
        }
    }


}
