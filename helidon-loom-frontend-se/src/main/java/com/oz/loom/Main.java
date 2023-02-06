package com.oz.loom;


import io.helidon.media.jackson.JacksonSupport;
import io.helidon.common.LogConfig;
import io.helidon.common.reactive.Single;
import io.helidon.config.Config;
import io.helidon.webclient.WebClient;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;



/**
 * The application main class.
 */
public final class Main {

    /**
     * Cannot be instantiated.
     */
    private Main() {
    }

    /**
     * Application main entry point.
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        startServer();
    }

    /**
     * Start the server.
     * @return the created {@link WebServer} instance
     */
    static Single<WebServer> startServer() {

        // load logging configuration
        LogConfig.configureRuntime();

        // By default this will pick up application.yaml from the classpath
        Config config = Config.create();

        WebClient client = WebClient.builder()
                .baseUri(config.get("remote.url").asString().get())
                .build();

        LatencyService.setClient(client);

        WebServer server = WebServer.builder(createRouting(config))
                .config(config.get("server"))
                .addMediaSupport(JacksonSupport.create())
                .build();

        Single<WebServer> webserver = server.start();

        // Try to start the server. If successful, print some info and arrange to
        // print a message at shutdown. If unsuccessful, print the exception.
        webserver.thenAccept(ws -> {
            System.out.println("WEB server is up! http://localhost:" + ws.port() + "/simple-greet");
            ws.whenShutdown().thenRun(() -> System.out.println("WEB server is DOWN. Good bye!"));
        })
        .exceptionallyAccept(t -> {
            System.err.println("Startup failed: " + t.getMessage());
            t.printStackTrace(System.err);
        });

        return webserver;
    }

    /**
     * Creates new {@link Routing}.
     *
     * @return routing configured with JSON support, a health check, and a service
     * @param config configuration of this server
     */
    private static Routing createRouting(Config config) {
        SimpleGreetService simpleGreetService = new SimpleGreetService(config);
        LatencyService latencyService = new LatencyService(config);

        Routing.Builder builder = Routing.builder()
                .register("/simple-greet", simpleGreetService)
                .register("/latency", latencyService);

        return builder.build();
    }
}
