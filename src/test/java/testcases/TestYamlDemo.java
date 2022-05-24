package testcases;

import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import pojos.Student;

import java.io.FileNotFoundException;
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
    public void test02() throws FileNotFoundException {
        //InputStream inputStream = new FileInputStream("batman.yaml");
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("batman.yaml");
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);
        System.out.println(data);
    }

    @Test
    public void test03(){
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("customer_with_type.yaml");
        Customer customer = yaml.load(inputStream);
        System.out.println(customer);
    }

    @Test
    public void test03_1(){
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("batman.yaml");
        Yaml yaml = new Yaml(new Constructor(Student.class));
        Student data = yaml.load(inputStream);
        System.out.println(data);
        Customer customer = yaml.load(inputStream);
        System.out.println(customer);
    }

    //TODO
    @Test
    public void test04(){
        Yaml yaml = new Yaml(new Constructor(Customer.class));
        System.out.println(yaml.getName());
    }

    @Test
    public void test05(){
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

    // TODO
    @Test
    public void test06(){
        Yaml yaml = new Yaml(new Constructor(People.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("multiple_document.yaml");
        //Iterable<Object> load = yaml.loadAll(inputStream);
        //System.out.println(load);
        Iterable<Object> docs = yaml.loadAll(inputStream);
        for (Object doc: docs){
            //System.out.println(doc);
            People people = (People) doc;
            System.out.println(people.row);
            System.out.println(people.getFirstName());
            System.out.println(people.getLastName());
            System.out.println(people.getAge());

            /*for (k,v in doc.items()){
                System.out.println();//
            }*/
        }
        //load.
        /*int count = 0;
        Iterable<Object> objects = yaml.loadAll(inputStream);
        List<People> peopleList =  yaml.loadAll(inputStream);*/
        //yaml.loadAll(inputStream).forEach(System.out::println);
        /*for (Object object : yaml.loadAll(inputStream)) {
            count++;
            System.out.println(object);
        }*/
    }







}
