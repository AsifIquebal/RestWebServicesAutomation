package wiremock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static io.restassured.RestAssured.given;

public class MockBase {
    public final static Logger log = LogManager.getLogger();
    public WireMockServer wireMockServer;


    @BeforeClass
    public void setUpBase() {
    }

    @AfterClass
    public void tearDown() {
        if (null != wireMockServer && wireMockServer.isRunning()) {
            // graceful shutdown
            log.info("Shutting Down WireMock...");
            wireMockServer.shutdownServer();
        }
        // force stop
        // wireMockServer.stop();
    }

    @BeforeMethod
    public void beforeTestMethod(ITestResult result) {
        //public void beforeTestMethod(ITestContext context){
        //log.info("Context Name: " + context.);
        log.info("Executing -> " + result.getMethod().getMethodName());
    }

    @AfterMethod
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

    public static void printJson(String obj) {
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
