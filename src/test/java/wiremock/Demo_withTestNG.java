package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

public class Demo_withTestNG {
    WireMockServer wireMockServer;

    //@BeforeClass
    public void setUp() {
        wireMockServer = new WireMockServer();
        // In case no arguments are provided, the server host defaults to localhost and the server port to 8080.
        wireMockServer.start();

        //stubFor(get(urlEqualTo("/asif/1"))
        stubFor(get(urlEqualTo("http://localhost:8080/asif/1"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "text/plain")
                                .withHeader("Accept", "text")
                                .withStatusMessage("Everything goes well...")
                                .withStatus(200)
                                .withBody("End point called successfully...")

                )
        );
        // If you’d prefer to use slightly more BDDish language in your tests you can replace stubFor with givenThat
        stubFor(get(urlEqualTo("/getinfo/asif"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)
                                .withBodyFile("test.json")
                        // it searches for file in src\test\resources\__files\test.json
                )
        );

        /*stubFor(post(urlEqualTo("/asif/add/"))
                .withRequestBody()
                .withBasicAuth()
                .withHeader()
                .withQueryParam()
                //.withName()
                //.withId()

        );*/
        // Research
        stubFor(any(urlPathEqualTo("/everything"))
                .withHeader("Accept", containing("xml"))
                .withCookie("session", matching(".*12345.*"))
                .withQueryParam("search_term", equalTo("WireMock"))
                .withBasicAuth("jeff@example.com", "jeffteenjefftyjeff")
                .withRequestBody(equalToXml("<search-results />"))
                .withRequestBody(matchingXPath("//search-results"))
                .withMultipartRequestBody(
                        aMultipart()
                                .withName("info")
                                .withHeader("Content-Type", containing("charset"))
                                .withBody(equalToJson("{}"))
                )
                .willReturn(aResponse()));
        // More examples at
        // https://github.com/tomakehurst/wiremock/blob/master/src/test/java/ignored/Examples.java#374
    }

    @Test
    public void test1() {
        Response response =
                given()
                        //   .formParam()
                        .when()
                        .get("asif/1")
                        .then()
                        .extract()
                        .response();
        System.out.println(response.asString());
    }

    @Test
    public void responseSpec() {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        //responseSpecBuilder.expectBody().expectContentType().expectHeader().expectCookie().expectStatusCode().expectResponseTime()
    }

    @Test
    public void getRequest() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.requestSpecification = new RequestSpecBuilder().
                setBaseUri("http://localhost:8080/").
                setContentType(ContentType.JSON).
                build();
        //log().all();
        /*System.out.println("----------------------------------------------------------");
        RestAssured.responseSpecification = new ResponseSpecBuilder().
                build().
                log().all();*/
        System.out.println("----------------------------------------------------------");
        /*given()
                .when()
                .get("asif/1")
                .then().log().all();*/

        // Log all request specification details including parameters, headers and body
        given(requestSpecification).log().all();
        System.out.println("----------------------------------------------------------");
        // Log only the parameters of the request
        given().log().params();
        System.out.println("----------------------------------------------------------");
        // Log only the request body
        given().log().body();
        System.out.println("----------------------------------------------------------");
        // Log only the request headers
        given().log().headers();
        System.out.println("----------------------------------------------------------");
        // Log only the request cookies
        given().log().cookies();
        System.out.println("----------------------------------------------------------");
        // Log only the request method
        given().log().method();
        System.out.println("----------------------------------------------------------");
        // Log only the request path
        //given().log().path();
    }

    @Test
    public void test2() {
        /*Student student = new Student();
        student
                .setName("Asif")
                .setRoll(2)
                .setStd(10);*/
        Response response =
                given()
                        .when()
                        .get("getinfo/asif")
                        .then()
                        .extract()
                        .response();
        System.out.println(response.asString());
        /*ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
    }

    @Test
    public void getHeaders() {
        System.out.println("---------------------------------------------------------------");
        Headers headers = given()
                .when()
                .get("/asif/1")
                .getHeaders();
        for (Header header : headers) {
            System.out.println("Header Name: " + header.getName() + ", Header Value: " + header.getValue());
        }
        String acceptHeaderValue = headers.get("Accept").getValue();
        System.out.println("acceptHeaderValue: " + acceptHeaderValue);
        String acceptHeaderValue1 = headers.getValue("Accept");
        System.out.println("acceptHeaderValue1: " + acceptHeaderValue1);
        System.out.println("---------------------------------------------------------------");
    }

    @Test
    public void getHeadersUsingResponse() {
        System.out.println("---------------------------------------------------------------");
        Response response = given()
                .when()
                .get("/asif/1")
                .then()
                .extract().response();
        Headers headers = response.getHeaders();
        for (Header header : headers) {
            System.out.println("Header Name: " + header.getName() + ", Header Value: " + header.getValue());
        }
        System.out.println("---------------------------------------------------------------");
    }

    @Test
    public void basicAuth() {
        given().
                auth().preemptive().basic("uname", "password").
                when().
                get("").
                then().extract().response();
    }

    @AfterClass
    public void tearDown() {
        if (null != wireMockServer && wireMockServer.isRunning()) {
            // graceful shutdown
            wireMockServer.shutdownServer();
        }
        // force stop
        // wireMockServer.stop();
    }

    public void turnOffWiremockLogging() {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        //System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.Log");
        //org.eclipse.jetty.util.log.Log;
        System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
        System.setProperty("org.eclipse.jetty.util.log.announce", "false");
        /*org.eclipse.jetty.util.log.Log.getProperties().setProperty("org.eclipse.jetty.LEVEL", "OFF");
        org.eclipse.jetty.util.log.Log.getProperties().setProperty("org.eclipse.jetty.util.log.announce", "false");
        org.eclipse.jetty.util.log.Log.getRootLogger().setDebugEnabled(false);*/
    }

}
