package wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class Stubs {

    public final static Logger log = LogManager.getLogger();

    public StubMapping getStubForBasicAuthPreemptive() {
        BasicCredentials basicCredentials = new BasicCredentials("uname", "password");
        String json = basicCredentials.asAuthorizationHeaderValue();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("Creating Stub for: /basic/auth/preemptive");
        return
                stubFor(get(urlEqualTo("/basic/auth/preemptive"))
                        .withBasicAuth("uname", "password")
                        .willReturn(aResponse().withStatus(200).withJsonBody(jsonNode)))
                ;
    }

    public StubMapping getStubForBasicAuthHeader() {
        log.info("Creating Stub for: /basic/auth/case-insensitive");
        return stubFor(get(urlEqualTo("/basic/auth/case-insensitive"))
                .withBasicAuth("username", "password")
                .willReturn(aResponse()
                        .withStatus(200)
                ));
    }

    public StubMapping getStubForToolQuery() {
        String json = "{\"boolean\":true,\"color\":\"gold\",\"name\":\"Wiremock\",\"number\":123,\"Properties\":{\"description\":\"WireMock is a simulator for HTTP-based APIs. Some might consider it a service virtualization tool or a mock server\",\"client\":\"RestAssured\"},\"string\":\"Hello World\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return stubFor(
                get(urlPathEqualTo("/tool/mocking"))
                        .withHeader("Authorization", containing("BASIC dXNlcm5hbWU6cGFzc3dvcmQ="))
                        .withQueryParam("name", equalTo("Wiremock"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "text/plain")
                                        .withHeader("Accept", "text")
                                        .withStatusMessage("Everything goes well...")
                                        .withStatus(200)
                                        .withJsonBody(jsonNode)

                        )
        );
    }
}
