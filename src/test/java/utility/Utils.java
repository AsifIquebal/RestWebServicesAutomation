package utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonValue;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

import static io.restassured.RestAssured.given;

@Log4j2
public class Utils {


    public void compareResponse(Response response1, Response response2) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        boolean b = objectMapper.readTree(response1.getBody().asString())
                .equals(objectMapper.readTree(response2.getBody().asString()));
        if (!b) {
            difference(response1, response2);
        }
    }

    public void difference(Response response1, Response response2) {
        JsonValue source = Json.createReader(new StringReader(response1.asString().replaceAll("\r?\n",""))).readValue();
        JsonValue target = Json.createReader(new StringReader(response2.asString().replaceAll("\r?\n",""))).readValue();
        JsonPatch diff = Json.createDiff(source.asJsonObject(), target.asJsonObject());
        log.info(diff.toJsonArray());
    }

    @Step("GET Call, End Point: {0}")
    public static Response GET_Response(String endPoint) {
        log.debug("This is a test for Allure");
        return given()
                .when()
                .get(endPoint)
                //.prettyPeek()
                .then()
                //.log().status()
                .extract().response();
    }

    @Step("POST Call, End Point: {0}")
    public static Response POST_Request(String endPoint, Object object) {
        return given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(object)
                .when()
                .post(endPoint)
                //.prettyPeek()
                .then()//.log().status()
                .extract().response();
    }

    public static Response PUT_Request(String endPoint, Object object) {
        return given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(object)
                .when()
                .put(endPoint)
                .then().log().status()
                .extract().response();
    }

    public static Response PATCH_Request(String endPoint, Object object) {
        return given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(object)
                .when()
                .patch(endPoint)
                .then().log().status()
                .extract().response();
    }

    public static Response DELETE_Request(String endPoint) {
        return given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when()
                .delete(endPoint)
                .then().log().status()
                .extract().response();
    }

    public static void printJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }

}
