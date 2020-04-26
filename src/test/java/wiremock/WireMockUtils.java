package wiremock;

import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class WireMockUtils {

    private static Stubs stubs;

    public static String getAuthToken() {
        stubs.getStubForBasicAuthPreemptiveAuthToken();
        Response response = given().
                auth().preemptive().basic("uname", "password").
                when().
                get("/basic/auth/preemptive").
                then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Failed: Status Code didn't matched");
        String token = JsonPath.read(response.asString(), "$.auth_token");
        return token;
    }

}
