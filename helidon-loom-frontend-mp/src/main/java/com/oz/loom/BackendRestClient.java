package com.oz.loom;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/latency")
@RegisterRestClient
public interface BackendRestClient {

    @GET
    String sendRequest(@QueryParam("requestNumber") String requestNumber);

}
