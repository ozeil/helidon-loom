package com.oz.loom;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;

@Path("/latency")
@ApplicationScoped
public class LatencyResource {

    @Inject
    @RestClient
    BackendRestClient client;

    @GET
    public String latency(@DefaultValue ("3") @QueryParam("requestCount") int numberOfRequests) {
        List<String> responses = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            responses.add(client.sendRequest(String.valueOf(i)));
        }
        return String.join(" ", responses);
    }
}
