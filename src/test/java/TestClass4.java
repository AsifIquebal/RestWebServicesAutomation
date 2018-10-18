import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo2.Request;
import demo2.Subnets;
import demo2.Tags;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utility.utils;

import java.util.ArrayList;
import java.util.List;

public class TestClass4 {

    @Test
    public void test1(){
        Subnets subnets = new Subnets();
        Request request = new Request();

        Tags tag0 = new Tags();
        tag0
                .setKey("Name")
                .setValue("testing");
        Tags[] tags = new Tags[1];
        tags[0] = tag0;
        /*ArrayList<Tags> tagsList = new ArrayList<>();
        Tags tags1 = new Tags();
        tags1
                .setKey("Name")
                .setValue("testing");
        System.out.println(tags1);
        tagsList.add(tags1);*/

        request
                .setAvailability_zone("us-west-2a")
                .setCidr("10.0.0.0/24")
                .setTags(tags)
                .setVpc_id("vpc-fcca9785");

        subnets.setRequest(request);
        // Have a look at the pay load being passed
        printJson(subnets);

        Response response = utils.POST_Request("/subnets?cloud_provider=aws&account_name=account_name",subnets);
    }

    public static void printJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
