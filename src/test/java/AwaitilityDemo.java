import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class AwaitilityDemo {

    @Test
    public void test_createBooking(){
        given().body("{\n" +
                        "    \"firstname\" : \"Jim\",\n" +
                        "    \"lastname\" : \"Brown\",\n" +
                        "    \"totalprice\" : 111,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2018-01-01\",\n" +
                        "        \"checkout\" : \"2019-01-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .post("https://restful-booker.herokuapp.com/booking");
    }

    @Test
    public void test01() {
        Awaitility.await().atMost(10, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
            final Response body = given().
                    contentType(ContentType.JSON).
                    header("X-Auth-Token", "token").
                    when().
                    get("https://sample URL");
            return body.path("Status").equals("Success");
        });
    }

    public boolean waitMethod() {
        System.out.println("hello...");
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
    private Callable<Boolean> waitmeme(int timeout) {
        return new Callable<Boolean>() {
            int counter = 0;
            int limit = timeout;
            public Boolean call() throws Exception {
                System.out.println("Hello");
                counter++;
                return (counter == limit);
            }
        };
    }

    @Test
    public void test02() {
        Awaitility.await()
                .with()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(11, TimeUnit.SECONDS)
                .until(waitmeme(10));
    }



}
