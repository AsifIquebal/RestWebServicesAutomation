package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
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
        token = setCredentials("asif", "superSecret");
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
    public void test01_sampleGet_queryParams() {
        stubs.getStubForToolQuery(token);
        Response response = given().
                header("Authorization", token).
                when().
                queryParam("name", "Wiremock").
                get("/tool/mocking").
                then().extract().response();
        int num = JsonPath.read(response.asString(), "$.number");
        printJson(response.asString());
        Assert.assertEquals(num, 123, "Failed: Number field mismatch");
    }

    @Test
    public void test02_samplePostJsonPayload() {
        stubFor(post(urlPathEqualTo("/form/params"))
                .withRequestBody(matchingJsonPath("$.gurus[?(@.tool == 'Rest Assured')]"))
                .willReturn(aResponse().withBodyFile("test02.json")));
        File file = new File("src/test/resources/__files/test02.json");
        Response response = given().
                when().
                body(file).
                post("/form/params").
                then().extract().response();
        System.out.println(response.asString());
    }

    @Test
    public void test03_responseDefinitionBuilder() {
        ResponseDefinitionBuilder responseDefinitionBuilder = new ResponseDefinitionBuilder();
        responseDefinitionBuilder
                .withHeader(ContentTypeHeader.KEY, "application/json")
                .withStatus(200)
                .withStatusMessage("Status: OK")
                .withBody("This is a test");
        WireMock.stubFor(WireMock.get("/tool/selenium")
                .willReturn(responseDefinitionBuilder));
        Response response = given().
                when().
                get("/tool/selenium").
                then().extract().response();
        System.out.println(response.getStatusLine());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody().asString());
    }

    @Test
    public void test04_responseFile() {
        ResponseDefinitionBuilder responseDefinitionBuilder = new ResponseDefinitionBuilder();
        responseDefinitionBuilder
                .withHeader(ContentTypeHeader.KEY, "application/json")
                .withStatus(200)
                .withStatusMessage("Status: OK")
                .withBodyFile("test.json");
        stubFor(WireMock.get(urlPathEqualTo("/getinfo/guru"))
                .withQueryParam("name", equalTo("johan-haleby"))
                .willReturn(responseDefinitionBuilder)
        );
        Response response = given().
                when().
                queryParam("name", "johan-haleby").
                get("/getinfo/guru").
                then().extract().response();
        System.out.println(response.asString());
    }

    @Test
    public void test04_stafeFul() {
        // todo
        ResponseDefinitionBuilder responseDefinitionBuilder01 = new ResponseDefinitionBuilder();
        responseDefinitionBuilder01
                .withHeader(ContentTypeHeader.KEY, "application/json")
                .withStatus(200)
                .withStatusMessage("Status: OK")
                .withBody("KEY:01");
        ResponseDefinitionBuilder responseDefinitionBuilder02 = new ResponseDefinitionBuilder();
        responseDefinitionBuilder02
                .withHeader(ContentTypeHeader.KEY, "application/json")
                .withStatus(200)
                .withStatusMessage("Status: OK")
                .withBody("KEY:02");

        stubFor(get(urlPathEqualTo("/todo/items"))//.inScenario("TestScenario")
                        .withQueryParam("num", equalTo("a"))
                        .withQueryParam("num", equalTo("b"))
                        //.whenScenarioStateIs(STARTED)
                        .willReturn(responseDefinitionBuilder01)
                //.willSetStateTo("2nd Value")
        );
        stubFor(get(urlPathEqualTo("/todo/items"))//.inScenario("TestScenario")
                        .withQueryParam("num", equalTo("b"))
                        //.whenScenarioStateIs(STARTED)
                        .willReturn(responseDefinitionBuilder02)
                //.willSetStateTo("Cancel")
        );

        Response response = given().
                when().
                queryParam("num", "b").
                get("/todo/items").
                then().extract().response();
        System.out.println(response.asString());

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

    public String setCredentials(String userName, String passWord) {
        stubs.getStubForBasicAuthPreemptiveAuthToken();
        Response response = given().
                auth().preemptive().basic(userName, passWord).
                when().
                get("/basic/auth/preemptive").
                then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Auth Token didn't generated...");
        return JsonPath.read(response.asString(), "$.auth_token");
    }

    @Test
    public void testPriority() {
        //TODO
        //Catch-all case
        stubFor(get(urlMatching("/api/.*")).atPriority(5)
                .willReturn(aResponse().withStatus(401)));

        //Specific case
        stubFor(get(urlEqualTo("/api/specific-resource")).atPriority(1) //1 is highest
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Resource state")));
    }
}
