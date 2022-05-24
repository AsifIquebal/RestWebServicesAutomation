package wiremock;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import wiremock.myPojos.Guru;

import java.io.File;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

@Log4j2
public class TestClass1 {

    Stubs stubs;
    String token;
    MockBase mockBase;

    //@BeforeClass
    public void setUp() {
        mockBase = new MockBase();
        mockBase.turnOffWiremockLogging();
        mockBase.startWireMockServer();
        stubs = new Stubs();
        token = mockBase.getAuthToken("asif", "superSecret");
    }

    //@AfterClass
    public void tearDown() {
        mockBase.stopWireMockServer();
        mockBase.closePrintStream();
    }

    //@Test
    public void test01_BasicAuth() {
        stubs.getStubForBasicAuthHeader();
        Response response = given()
                .spec(mockBase.setRALogFilter())
                .header("Authorization", token)
                .when()
                .get("/basic/auth/case-insensitive")
                .then()
                .extract().response();
        log.info(response.asString());
        Assert.assertEquals(response.getStatusCode(), 200, "Failed: Status Code didn't matched");
    }

    //@Test
    public void test02_sampleGet_queryParams() {
        stubs.getStubForToolQuery(token);
        Response response = given()
                .auth().oauth2(token)
                // oauth2 does the same thing, it puts the token into the header
                //.header("Authorization", token)
                .when()
                .queryParam("name", "Wiremock")
                .get("/tool/mocking")
                .then().log().headers().extract().response();
        int num = JsonPath.read(response.asString(), "$.number");
        mockBase.printJson(response.asString());
        Assert.assertEquals(num, 123, "Failed: Number field mismatch");
    }

    //@Test
    public void test03_samplePostJsonPayload() {
        stubFor(post(urlPathEqualTo("/form/params"))
                .withRequestBody(matchingJsonPath("$.gurus[?(@.tool == 'Rest Assured')]"))
                .willReturn(aResponse().withBodyFile("test02.json")));
        File file = new File("src/test/resources/__files/test02.json");
        Response response = given()
                .when()
                .body(file)
                .post("/form/params")
                .then().extract().response();
        List<Guru> gurus = JsonPath.read(response.asString(), "$.gurus[?(@.tool =~ /^[r|R]est.*/)]");
        System.out.println(gurus.get(0));
    }

    //@Test
    public void test04_jsonPathParamExample() {
        stubFor(get(urlPathEqualTo("/all/gurus"))
                .willReturn(aResponse()
                        .withHeader(ContentTypeHeader.KEY, "application/json")
                        .withBodyFile("test02.json")));

        List<Guru> gurus01 = given()
                .when().get("/all/gurus").then()
                .extract().jsonPath().getList("gurus", Guru.class);
        System.out.println("Size: " + gurus01.size() + "\n" + gurus01.get(0).toString());

        List<Guru> gurus02 = given()
                .when().get("/all/gurus").then()
                .extract().jsonPath().param("id", 2).getList("gurus.findAll {it.id == id}", Guru.class);
        System.out.println("Size: " + gurus02.size() + "\n" + gurus02.get(0).toString());

        List<Guru> gurus03 = given()
                .when().get("/all/gurus").then()
                .extract().jsonPath().param("id", 2).getList("gurus.findAll { it -> it.id == id }", Guru.class);
        System.out.println("Size: " + gurus03.size() + "\n" + gurus03.get(0).toString());

        /*Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(file)
                .post("/form/params")
                .then().extract().response();*/
        //List<Guru> gurus = JsonPath.read(response.asString(), "$.gurus[?(@.tool =~ /^[r|R]est.*/)]");
        //Guru guru1 = response.as(Guru.class);
        //System.out.println(guru1);

        /*Guru guru;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<MyClass>>(){});
        List<MyClass> result = objectReader.readValue(inputStream);
        ObjectMapper mapper = new ObjectMapper();

        mapper.readValue(gurus.get(0).toString(), new TypeReference<List<Guru>>(){});

        System.out.println("===============\n"+gurus1.get(0).toString()+"===============\n");*/
    }

    //@Test
    public void test05_responseDefinitionBuilder() {
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

    //@Test
    public void test06_responseFile() {
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
        Response response = given()
                .when()
                .queryParam("name", "johan-haleby")
                .get("/getinfo/guru")
                .then().extract().response();
        System.out.println(response.asString());
    }

    //@Test
    public void test07_stafeFul() {
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

    //@Test
    public void testForDynamicPort() {
        //TODO
    }

    //@Test
    public void testForSpecificPort() {
        //TODO
        //wireMockServer = new WireMockServer(wireMockConfig().port(2345));
        //WireMock.configureFor("localhost",2345);
        //WireMock.configureFor();
        /*WireMockResponse response = testClient.getWithPreemptiveCredentials(
                "/basic/auth/preemptive", wireMockServer.port(), "the-username", "thepassword");*/
    }

    //@Test
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
