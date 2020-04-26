package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static io.restassured.RestAssured.given;

public class TestClass1 extends MockBase {

    Stubs stubs;
    String token;

    @BeforeClass
    public void setUp() {
        turnOffWiremockLogging();
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        stubs = new Stubs();
        token = setCredentials("asif","superSecret");
    }

    @Test
    public void testBasicAuth() {
        stubs.getStubForBasicAuthHeader();
        Response response = given().
                header("Authorization", token).
                when().
                get("/basic/auth/case-insensitive").
                then().extract().response();
        System.out.println(response.asString());
        Assert.assertEquals(response.getStatusCode(), 200, "Failed: Status Code didn't matched");
    }

    @Test
    public void test01(){
        stubs.getStubForToolQuery(token);
        Response response = given().
                header("Authorization", token).
                when().
                queryParam("name","Wiremock").
                get("/tool/mocking").
                then().extract().response();
        int num = JsonPath.read(response.asString(),"$.number");
        printJson(response.asString());
        Assert.assertEquals(num, 123, "Failed: Number field mismatch");
    }

    @Test
    public void testForDynamicPort() {
        //TODO
    }

    @Test
    public void testForSpecificPort() {
        //TODO
        //wireMockServer = new WireMockServer(wireMockConfig().port(2345));
        //WireMock.configureFor("localhost",2345);
        //WireMock.configureFor();
        /*WireMockResponse response = testClient.getWithPreemptiveCredentials(
                "/basic/auth/preemptive", wireMockServer.port(), "the-username", "thepassword");*/
    }

    public String setCredentials(String userName, String passWord){
        stubs.getStubForBasicAuthPreemptiveAuthToken();
        Response response = given().
                auth().preemptive().basic(userName, passWord).
                when().
                get("/basic/auth/preemptive").
                then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Auth Token didn't generated...");
        return JsonPath.read(response.asString(),"$.auth_token");
    }

}
