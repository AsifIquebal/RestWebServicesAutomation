package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class TestClass1 {

    MockBase mockBase;
    WireMockServer wireMockServer;

    @BeforeClass
    public void setUp() {
        mockBase = new MockBase();
        wireMockServer = mockBase.wireMockServer;
    }

    @Test
    public void matchesPreemptiveBasicAuthWhenCredentialAreCorrect() {
        mockBase.turnOffWiremockLogging();
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        stubFor(get(urlEqualTo("/basic/auth/preemptive"))
                .withBasicAuth("uname", "password")
                .willReturn(aResponse().withStatus(200)));
        Response response = given().
                auth().preemptive().basic("uname", "password").
                when().
                get("/basic/auth/preemptive").
                then().extract().response();
        System.out.println(response.getStatusLine());
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

}
