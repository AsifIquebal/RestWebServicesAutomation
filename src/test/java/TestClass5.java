import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.XML;
import org.testng.annotations.Test;
import utility.utils;

import static io.restassured.RestAssured.given;

public class TestClass5 {

    @Test
    public void test1(){
        Response response = given()
                .when()
                .get("http://api.icndb.com/jokes/random?limitTo=[nerdy]")
                .then().extract()
                .response();

        JSONObject json = new JSONObject(response.asString());
        String xml = XML.toString(json,"root");
        System.out.println(utils.prettyFormat(xml));

        /*System.out.println(response.asString());
        Assert.assertTrue(response.getStatusCode()==200,"Status Code didn't matched");
        List<String> cats = JsonPath.read(response.asString(),"$.value.categories");
        String category = cats.get(0);
        Assert.assertTrue(category.equals("nerdy"),"Category didn't matched");*/
    }

}
