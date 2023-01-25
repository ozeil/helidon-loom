package com.oz.loom;

import io.helidon.nima.webclient.http1.Http1Client;
import io.helidon.nima.webserver.http.HttpRules;
import io.helidon.nima.webserver.http.HttpService;
import io.helidon.nima.webserver.http.ServerRequest;
import io.helidon.nima.webserver.http.ServerResponse;

import java.util.ArrayList;
import java.util.List;

public class LatencyService implements HttpService {

    private static Http1Client client;

    static void setClient(Http1Client client) {
        LatencyService.client = client;
    }

    private static String sendRequest(String queryParam) {
        return client.get().path("/latency").queryParam("requestNumber", queryParam).request().as(String.class);
    }

    @Override
    public void routing(HttpRules httpRules) {
        httpRules.get("/", this::latency);
    }

    public void latency(ServerRequest req, ServerResponse res) {
        int numberOfRequests = req.query().first("requestCount").map(Integer::parseInt).orElse(3);
        List<String> responses = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            responses.add(sendRequest(String.valueOf(i)));
        }
        res.send(String.join(" ", responses));
    }
}
