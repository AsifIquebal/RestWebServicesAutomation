package TestCases;

import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.Map;

public class TestYamlDemo {

    @Test
    public void test01() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("customer.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
    }

    @Test
    public void test04(){
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("multiple_document.yaml");
        int count = 0;
        yaml.loadAll(inputStream).forEach(System.out::println);
        /*for (Object object : yaml.loadAll(inputStream)) {
            count++;
            System.out.println(object);
        }*/
    }

    @Test
    public void test02(){
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("customer_with_type.yaml");
        Customer customer = yaml.load(inputStream);
        System.out.println(customer);
    }

    //TODO
    @Test
    public void test03(){
        Yaml yaml = new Yaml(new Constructor(Customer.class));
        System.out.println(yaml.getName());
    }




}
