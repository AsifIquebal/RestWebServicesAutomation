package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.RestAssured;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Base {

    public final static Logger log = LogManager.getLogger();

    @BeforeClass
    public void setUp(){
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("src/main/resources/config.properties");
            properties.load(input);
            System.out.println(properties.getProperty("key1"));
            System.out.println(properties.getProperty("key2"));
            RestAssured.baseURI = properties.getProperty("testurl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public void beforeTestMethod(ITestResult result){
    //public void beforeTestMethod(ITestContext context){
        //log.info("Context Name: " + context.);
        log.info("Executing -> " + result.getMethod().getMethodName());
    }

    @AfterMethod
    public void afterTestMethod(ITestResult result){
        log.info("Finished Executing -> "+result.getMethod().getMethodName());
    }
    /*@AfterMethod
    public void logResultsAfterTestMethod(ITestResult result){
        if(result.isSuccess()){
            log.info("------------------Test Passed?" + result.isSuccess());
        }

    }*/


}
