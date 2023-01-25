package com.oz.loom;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.helidon.common.http.Http;
import io.helidon.common.http.InternalServerException;
import io.helidon.config.Config;
import io.helidon.nima.webclient.http1.Http1Client;
import io.helidon.nima.webserver.WebServer;
import io.helidon.nima.webserver.http.HttpRouting;
import io.helidon.nima.webserver.http.ServerRequest;
import io.helidon.nima.webserver.http.ServerResponse;

public class NimaMain {
    private static final Http.HeaderValue SERVER = Http.Header.create(Http.Header.SERVER, "Nima");
    private static final AtomicInteger COUNTER = new AtomicInteger();
    // no need to use secure random to compute sleep times
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {

        Config config = Config.create();

        WebServer.builder()
                .routing(NimaMain::routing)
                .start();

        Http1Client client = Http1Client.builder()
                .baseUri(config.get("remote.url").asString().get())
                .build();

        LatencyService.setClient(client);
        BlockingService.setClient(client);
    }

    static void routing(HttpRouting.Builder rules) {
        rules.addFilter((chain, req, res) -> {
                    res.header(SERVER);
                    chain.proceed();
                })
                .get("/remote", NimaMain::remote)
                .register("/blocking", new BlockingService())
                .register("/latency", new LatencyService());
    }

    private static void remote(ServerRequest req, ServerResponse res) {
        // the remote service will randomly sleep up to half a second
        int sleepMillis = RANDOM.nextInt(500);
        int counter = COUNTER.incrementAndGet();

        try {
            TimeUnit.MILLISECONDS.sleep(sleepMillis);
        } catch (InterruptedException e) {
            throw new InternalServerException("Failed to sleep", e);
        }
        res.send("remote_" + counter);
    }
}
