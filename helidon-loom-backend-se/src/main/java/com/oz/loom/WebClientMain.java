package com.oz.loom;

import io.helidon.common.reactive.Single;
import io.helidon.config.Config;
import io.helidon.config.ConfigValue;
import io.helidon.media.jackson.JacksonSupport;
import io.helidon.webclient.WebClient;

public class WebClientMain {


    private WebClientMain() {
    }

    /**
     * Executes WebClient examples.
     *
     * If no argument provided it will take server port from configuration server.port.
     *
     * User can override port from configuration by main method parameter with the specific port.
     *
     * @param args main method
     */
    public static void main(String[] args) {
        Config config = Config.create();
        String url;
        ConfigValue<Integer> port = config.get("server.port").asInt();
        if (!port.isPresent() || port.get() == -1) {
            throw new IllegalStateException("Unknown port! Please specify port as a main method parameter "
                    + "or directly to config server.port");
        }
        url = "http://localhost:" + port.get() + "/greet";

        WebClient webClient = WebClient.builder()
                .baseUri(url)
                .config(config.get("client"))
                    .addMediaSupport(JacksonSupport.create())
                .build();

        performGetMethod(webClient).await();
    }

    static Single<String> performGetMethod(WebClient webClient) {
        System.out.println("Get request execution.");
        return webClient.get()
                .request(String.class)
                .peek(string -> {
                    System.out.println("GET request successfully executed.");
                    System.out.println(string);
                });
    }
}
