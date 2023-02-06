package com.oz.loom;

import io.helidon.common.http.Http;
import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Backend implements Service {

    ScheduledExecutorService scheduler;

    private final int delay;

    Backend(Config config) {
        this.delay = config.get("scheduler.delay").asInt().get();
        this.scheduler = new ScheduledThreadPoolExecutor(config.get("scheduler.size").asInt().get());
    }

    @Override
    public void update(Routing.Rules rules) {
        rules.get("/", this::response);
    }

    private void response(ServerRequest request,
                          ServerResponse response) {
        scheduler.schedule(() -> {
            response.status(Http.Status.OK_200);
            response.send("Delayed response " + request.queryParams().first("requestNumber").get() + "!");
        }, delay, TimeUnit.MILLISECONDS);
    }

}
