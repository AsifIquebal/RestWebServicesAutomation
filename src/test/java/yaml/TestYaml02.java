package yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestYaml02 {

    JSONArray jsonArray;
    List<JSONObject> jsonObjectList;

    @BeforeTest
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Object object;
        try {
            object = objectMapper.readValue(new File("src/test/resources/batman2.yaml"), Object.class);
            ObjectMapper jsonWriter = new ObjectMapper();
            String value = jsonWriter.writeValueAsString(object);
            try {
                jsonArray = (JSONArray) new JSONTokener(value).nextValue();
                jsonObjectList = IntStream
                        .range(0, jsonArray.length())
                        .mapToObj(index -> (JSONObject) jsonArray.get(index))
                        .collect(Collectors.toList());
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void test01() {
        System.out.println(jsonArray.getJSONObject(0));
    }

    @Test
    public void test02() {
        int row = 1;
        System.out.println(jsonArray.getJSONObject(row));
    }

    @Test
    public void test03(){
        System.out.println(jsonObjectList.stream().filter(item -> item.get("name").equals("Joker")).findAny());
    }

}
