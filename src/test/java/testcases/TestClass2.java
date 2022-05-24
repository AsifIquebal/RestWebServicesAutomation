package testcases;

import base.Base;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ComparisonChain;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojos.MyPost;
import pojos.PojoA;
import utility.Utils;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.*;

import static io.restassured.RestAssured.given;

public class TestClass2 extends Base {

    Response response;

    @Test
    public void verifyPostsAddedSuccessfully_01() {
        String title = "Test Title";
        String body = "Test Body";
        String userId = "786";
        MyPost myPost = new MyPost();
        myPost.setTitle(title)
                .setBody(body)
                .setUserId(userId);
        response = Utils.POST_Request("/posts", myPost);
        Assert.assertEquals(JsonPath.read(response.asString(), "$.title"), title);
        Assert.assertEquals(JsonPath.read(response.asString(), "$.body"), body);
        Assert.assertEquals(JsonPath.read(response.asString(), "$.userId"), userId);
    }

    @Test
    public void verifyPostsAddedSuccessfully_02() {
        String title = "Test Title";
        String body = "Test Body";
        String userId = "786";
        MyPost myPost = new MyPost();
        myPost.setTitle(title)
                .setBody(body)
                .setUserId(userId);
        response = Utils.POST_Request("/posts", myPost);
        PojoA expected = new PojoA();
        expected.setTitle(title).setBody("fail it").setUserId(userId);
        PojoA actual = new PojoA();
        actual.setTitle(JsonPath.read(response.asString(), "$.title"))
                .setBody(JsonPath.read(response.asString(), "$.body"))
                .setUserId(JsonPath.read(response.asString(), "$.userId"));
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void verifyPostsAddedSuccessfully_03() {
        String title = "Test Title";
        String body = "Test Body";
        String userId = "786";
        MyPost myPost = new MyPost();
        myPost.setTitle(title)
                .setBody(body)
                .setUserId(userId);
        response = Utils.POST_Request("/posts", myPost);
        PojoA expected = new PojoA();
        expected.setTitle(title).setBody("fail it").setUserId(userId);
        PojoA actual = new PojoA();
        actual.setTitle(JsonPath.read(response.asString(), "$.title"))
                .setBody(JsonPath.read(response.asString(), "$.body"))
                .setUserId(JsonPath.read(response.asString(), "$.userId"));
        Assert.assertTrue(compareResponse(expected, actual));
    }

    @Test
    public void verifyPostsAddedSuccessfully_04() throws IllegalAccessException {
        //private static List<String> difference(Student s1, Student s2) throws IllegalAccessException {
        String title = "Test Title";
        String body = "Test Body";
        String userId = "786";
        MyPost myPost = new MyPost();
        myPost.setTitle(title)
                .setBody(body)
                .setUserId(userId);
        response = Utils.POST_Request("/posts", myPost);
        System.out.println(response.asString());
        PojoA expected = new PojoA();
        expected.setTitle(title).setBody("fail it").setUserId(userId);
        PojoA actual = new PojoA();
        actual.setTitle(JsonPath.read(response.asString(), "$.title"))
                .setBody(JsonPath.read(response.asString(), "$.body"))
                .setUserId(JsonPath.read(response.asString(), "$.userId"));

        List<String> changedProperties = new ArrayList<>();
        for (Field field : expected.getClass().getDeclaredFields()) {
            // You might want to set modifier to public first (if it is not public yet)
            field.setAccessible(true);
            Object value1 = field.get(actual);
            Object value2 = field.get(expected);
            if (value1 != null && value2 != null) {
                //System.out.println(field.getName() + "=" + value1);
                //System.out.println(field.getName() + "=" + value2);
                if (!Objects.equals(value1, value2)) {
                    System.out.println("differs in: " + field.getName() + ", actual: [" + value1 + "], expected: [" + value2 + "]");
                    //changedProperties.add(field.getName());
                }
            }
        }
        //System.out.println(changedProperties);
        //return changedProperties;
    }

    @Test
    public void verifyPostsAddedSuccessfully_05() {
        String title = "Test Title";
        String body = "Test Body";
        String userId = "786";
        MyPost myPost = new MyPost();
        myPost.setTitle(title)
                .setBody(body)
                .setUserId(userId);
        response = Utils.POST_Request("/posts", myPost);
        PojoA expected = new PojoA();
        expected.setTitle(title).setBody(body).setUserId(userId);
        PojoA actual = new PojoA();
        actual.setTitle(JsonPath.read(response.asString(), "$.title"))
                .setBody(JsonPath.read(response.asString(), "$.body"))
                .setUserId(JsonPath.read(response.asString(), "$.userId"));
        int comparisonResult = ComparisonChain.start()
                .compare(expected.getTitle(), actual.getTitle())
                .compare(expected.getBody(), actual.getBody())
                .compare(expected.getUserId(), actual.getUserId())
                .result();
        System.out.println("ComparisonResult: " + comparisonResult);
        Assert.assertEquals(comparisonResult, 0);
    }

    @Test
    public void verifyPostUpdateSuccessfully() {
        String newTitle = "This is a new title";
        String id = "1";
        String oldTitle = given().when().get("/posts/" + id).then().extract().path("title");
        System.out.println(oldTitle);
        MyPost myPost = new MyPost();
        myPost.setTitle(newTitle)
                .setUserId(id);
        response = Utils.PUT_Request("/posts/" + id, myPost);
        System.out.println(response.asString());
        Assert.assertEquals(JsonPath.read(response.asString(), "$.title"), newTitle);
        Assert.assertNotEquals(JsonPath.read(response.asString(), "$.title"), oldTitle);
    }

    @Test
    public void verifyPostsCanBeDeletedSuccessfully() {
        String id = "1";
        response = Utils.DELETE_Request("posts/" + id);
        Assert.assertTrue(response.getStatusCode() == 200);
        // usually we call a get/put after deletion and expect 404
    }

    public boolean compareResponse(Object obj1, Object obj2) {
        ObjectMapper mapper = new ObjectMapper();
        if (null == obj1) {
            System.out.println("Object1 is null");
            return false;
        }
        if (null == obj2) {
            System.out.println("Object2 is null");
            return false;
        }
        boolean b;
        try {
            b = mapper.readTree(mapper.writeValueAsString(obj1)).equals(mapper.readTree(mapper.writeValueAsString(obj2)));
            if (!b) {
                difference(obj1, obj2);
            }
            return b;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void difference(Object obj1, Object obj2) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValue source = Json.createReader(new StringReader(mapper.writeValueAsString(obj1))).readValue();
        JsonValue target = Json.createReader(new StringReader(mapper.writeValueAsString(obj2))).readValue();
        JsonPatch diff = Json.createDiff(source.asJsonObject(), target.asJsonObject());
        System.out.println(format(diff.toJsonArray()));
    }

    public static String format(JsonValue json) {
        StringWriter stringWriter = new StringWriter();
        prettyPrint(json, stringWriter);
        return stringWriter.toString();
    }

    public static void prettyPrint(JsonValue json, Writer writer) {
        Map<String, Object> config =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);
        try (JsonWriter jsonWriter = writerFactory.createWriter(writer)) {
            jsonWriter.write(json);
        }
    }

}
