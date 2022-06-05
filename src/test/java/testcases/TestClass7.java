package testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class TestClass7 {

    @Test
    public void stringToMap() {
        ObjectMapper mapper = new ObjectMapper();
        String str = "{\"Asif\":12,\"Meena\":10,\"Geeta\":22,\"Harry\":20,\"Lawrence\":6}";
        try {
            Map<String, String> map = mapper.readValue(str, Map.class);
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mapToJSON() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("name", "mkyong");
        map.put("age", "37");
        try {
            // convert map to JSON string
            String json = mapper.writeValueAsString(map);
            System.out.println(json);   // compact-print
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            System.out.println(json);   // pretty-print
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test01() {
        ObjectMapper mapper = new ObjectMapper();
        String str = "{\"Asif\":12,\"Meena\":10,\"Geeta\":22,\"Harry\":20,\"Lawrence\":6}";
        try {
            Map<String, Integer> map = mapper.readValue(str, Map.class);
            //System.out.println(map);
            map.entrySet().stream()
                    .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue())) // watch out for the minus sign
                    .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sortByValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("X", 26);
        map.put("Asif", 12);
        map.put("Meena", 10);
        map.put("Geeta", 22);
        map.put("Harry", 20);
        map.put("Lawrence", 6);
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        System.out.println(result);
        Stream<Map.Entry<String, Integer>> sorted = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue());
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        sorted.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        System.out.println(sortedMap);
    }

    @Test
    public void sortByKey() {
        Map<String, Integer> map = new HashMap<>();
        map.put("X", 26);
        map.put("Asif", 12);
        map.put("Meena", 10);
        map.put("Geeta", 22);
        map.put("Harry", 20);
        map.put("Lawrence", 6);
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

}
