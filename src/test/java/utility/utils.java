package utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.StringReader;
import java.io.StringWriter;

import static io.restassured.RestAssured.given;

public class utils {

    public static Response GET_Response(String endPoint) {
        Response response = given()
                .when()
                .get(endPoint)
                .prettyPeek()
                .then()
                .log().status()
                .extract().response();
        return response;
    }

    public static Response POST_Request(String endPoint, Object object) {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(object)
                        .when()
                        .post(endPoint)
                        .prettyPeek()
                        .then().log().status()
                        .extract().response();
        return response;
    }

    public static Response PUT_Request(String endPoint, Object object) {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(object)
                        .when()
                        .put(endPoint)
                        .then().log().status()
                        .extract().response();
        return response;
    }

    public static Response PATCH_Request(String endPoint, Object object) {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .body(object)
                        .when()
                        .patch(endPoint)
                        .then().log().status()
                        .extract().response();
        return response;
    }

    public static Response DELETE_Request(String endPoint) {
        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .when()
                        .patch(endPoint)
                        .then().log().status()
                        .extract().response();
        return response;
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
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }


}
