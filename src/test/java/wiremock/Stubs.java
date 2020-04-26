package wiremock;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class Stubs {

    public final static Logger log = LogManager.getLogger();

    public StubMapping getStubForBasicAuthPreemptive() {
        log.info("Creating Stub for: /basic/auth/preemptive");
        return
                stubFor(get(urlEqualTo("/basic/auth/preemptive"))
                        .withBasicAuth("uname", "password")
                        .willReturn(aResponse().withStatus(200)));
    }

    public StubMapping getStubForBasicAuthHeader(){
        log.info("Creating Stub for: /basic/auth/case-insensitive");
        return stubFor(get(urlEqualTo("/basic/auth/case-insensitive"))
                .withBasicAuth("username", "password")
                .willReturn(aResponse()
                        .withStatus(200)
                ));
    }

}
