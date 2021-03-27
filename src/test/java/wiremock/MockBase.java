package wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.JsonPath;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

@Log4j2
public class MockBase {

    private WireMockServer wireMockServer;

    private RequestSpecification requestSpecification;
    PrintStream printStream;

    public RequestSpecification setRALogFilter() {
        try {
            printStream = new PrintStream(new FileOutputStream("log/app.log", true));
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        requestSpecification = new RequestSpecBuilder()
                .addFilter(RequestLoggingFilter.logRequestTo(printStream))
                .addFilter(ResponseLoggingFilter.logResponseTo(printStream, LogDetail.ALL))
                .build();
        return requestSpecification;
    }

    public void closePrintStream() {
        log.info("Closing PS...");
        if (null != printStream) {
            printStream.flush();
            printStream.close();
        }
    }

    public void startWireMockServer() {
        log.info("Starting WireMockServer");
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    public void stopWireMockServer() {
        if (null != wireMockServer && wireMockServer.isRunning()) {
            log.info("Shutting Down WireMock...");
            wireMockServer.shutdownServer();
        }
    }

    public String getAuthToken(String userName, String passWord) {
        log.info("Setting User Credentials...");
        new Stubs().getStubForBasicAuthPreemptiveAuthToken();
        Response response = given().
                auth().preemptive().basic(userName, passWord).
                when().
                get("/basic/auth/preemptive").
                then().extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Auth Token didn't generated...");
        return JsonPath.read(response.asString(), "$.auth_token");
    }

    public void tearDown() {
        if (null != wireMockServer && wireMockServer.isRunning()) {
            // graceful shutdown
            log.info("Shutting Down WireMock...");
            wireMockServer.shutdownServer();
        }
        // force stop
        // wireMockServer.stop();
    }

    //@BeforeMethod
    public void beforeTestMethod(ITestResult result) {
        log.info("Executing -> " + result.getMethod().getMethodName());
    }

    //@AfterMethod
    public void afterTestMethod(ITestResult result) {
        log.info("Finished Executing -> " + result.getMethod().getMethodName());
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

    public void printJson(String obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            Object json = objectMapper.readValue(obj, Object.class);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
