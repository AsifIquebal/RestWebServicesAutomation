package TestCases;

import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TestClass3 {

    @Test
    public void test1() {
        Response response = given()
                .when()
                .get("http://api.icndb.com/jokes/random?limitTo=[nerdy]")
                .then().extract()
                .response();
        Assert.assertTrue(response.getStatusCode() == 200, "Status Code didn't matched");
        List<String> cats = JsonPath.read(response.asString(), "$.value.categories");
        Assert.assertTrue(cats.get(0).equals("nerdy"), "Category didn't matched");
    }


    @Test
    public void test2() {
        Response response = given()
                .when()
                .get("http://api.icndb.com/jokes/random?limitTo=[puneri]")
                .then().log().status()
                .extract()
                .response();
        System.out.println(response.asString());
        Assert.assertTrue(response.getStatusCode() == 200, "Status Code didn't matched");
        String type = JsonPath.read(response.asString(), "$.type");
        Assert.assertTrue(type.equals("NoSuchCategoryException"), "Type didn't matched");
    }

    @Test
    public void test01() {
        Response response = given()
                .when()
                .get("https://ifsc.razorpay.com/CITI0000005")
                .then().log().status()
                .extract()
                .response();
        System.out.println(response.asString());
        Assert.assertTrue(response.getStatusCode() == 200, "Status Code didn't matched");
        String type = JsonPath.read(response.asString(), "$.BRANCH");
        Assert.assertTrue(type.equals("PUNE"), "Branch didn't matched");
    }

    @Test
    public void test02() {
        Response response = given()
                .when()
                .get("https://ifsc.razorpay.com/CITI0000444")
                .then().log().status()
                .extract()
                .response();
        Assert.assertTrue(response.asString().equals("\"Not Found\""), "Negative test case failed");
        Assert.assertTrue(response.getStatusCode() == 404, "Status Code didn't matched");
    }

}
