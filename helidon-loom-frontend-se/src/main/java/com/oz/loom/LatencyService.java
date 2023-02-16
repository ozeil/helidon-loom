package com.oz.loom;

import io.helidon.common.reactive.Multi;
import io.helidon.common.reactive.Single;
import io.helidon.config.Config;
import io.helidon.webclient.WebClient;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class LatencyService implements Service {

    private static WebClient client;

    public LatencyService(Config config) {
    }

    static void setClient(WebClient client) {
        LatencyService.client = client;
    }

    private static Single<String> sendRequest(String queryParam) {
        return client.get().path("/latency").queryParam("requestNumber", queryParam).request(String.class);
    }

    @Override
    public void update(Routing.Rules rules) {
        rules.get("/", this::latency);
    }

    private void latency(ServerRequest request,
                         ServerResponse response) {
        int numberOfRequests = request.queryParams().first("requestCount").map(Integer::parseInt).orElse(3);

        Multi.range(0, numberOfRequests)
                .flatMap(i -> sendRequest(String.valueOf(i)))
                .collectList()
                .map(list -> String.join(" ", list))
                .onError(response::send)
                .forSingle(response::send);
    }
}
