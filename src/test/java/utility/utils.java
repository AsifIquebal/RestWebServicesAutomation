package utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class utils {

    public static Response GET_Response(String endPoint) {
        Response response = given()
                .when()
                .get(endPoint)
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



}
