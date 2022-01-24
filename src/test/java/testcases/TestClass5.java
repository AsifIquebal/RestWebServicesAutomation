package testcases;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.XML;
import org.testng.annotations.Test;
import utility.Utils;

import static io.restassured.RestAssured.given;

public class TestClass5 {

    @Test
    public void test1() {
        given().
                auth().basic("uname", "password").
                when().
                get("").
                then().extract().response();

        Response response = given()
                .when()
                .get("http://api.icndb.com/jokes/random?limitTo=[nerdy]")
                .then().extract()
                .response();
        System.out.println("Response as JOSN:\n" + response.asString());

        JSONObject json = new JSONObject(response.asString());
        String xml = XML.toString(json, "root");
        System.out.println("Converted to XML: \n" + Utils.prettyFormat(xml));

        /*System.out.println(response.asString());
        Assert.assertTrue(response.getStatusCode()==200,"Status Code didn't matched");
        List<String> cats = JsonPath.read(response.asString(),"$.value.categories");
        String category = cats.get(0);
        Assert.assertTrue(category.equals("nerdy"),"Category didn't matched");*/
    }

    @Test
    public void test02_jsonToXml() {
        String json_data = "{\"student\":{\"name\":\"Abhinav Jain\", \"age\":\"22\"}}";
        JSONObject obj = new JSONObject(json_data);
        //converting json to xml
        String xml_data = XML.toString(obj);
        System.out.println(xml_data);
    }

    @Test
    public void test03_XmlToJson() {
        String xml_data = "<student><name>Abhinav Jain</name><age>22</age></student>";
        //converting xml to json
        JSONObject obj = XML.toJSONObject(xml_data);
        System.out.println(obj.toString());
    }

}
