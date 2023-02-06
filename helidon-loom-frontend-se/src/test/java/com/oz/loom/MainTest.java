
package com.oz.loom;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import io.helidon.media.jackson.JacksonSupport;
import io.helidon.common.http.Http;
import io.helidon.webclient.WebClient;
import io.helidon.webclient.WebClientResponse;
import io.helidon.webserver.WebServer;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MainTest {


    private static WebServer webServer;
    private static WebClient webClient;

    @BeforeAll
    static void startTheServer() {
        webServer = Main.startServer().await();

        webClient = WebClient.builder()
                .baseUri("http://localhost:" + webServer.port())
                .addMediaSupport(JacksonSupport.create())
                .build();
    }

    @AfterAll
    static void stopServer() throws ExecutionException, InterruptedException, TimeoutException {
        if (webServer != null) {
            webServer.shutdown()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);
        }
    }

    @Test
    void testSimpleGreet() {
        Message json = webClient.get()
                .path("/simple-greet")
                .request(Message.class)
                .await();
        assertThat(json.getMessage(), is("Hello World!"));
    }
}
