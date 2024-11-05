package com.kodenix;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static io.restassured.RestAssured.given;


public class WebHttpServerTest {
    private static Thread serverThread;
    private static final int DEFAULT_PORT = 8080;

    @BeforeAll
    public static void setUp() {
        serverThread = new Thread(() -> {
            try {
                WebHttpServer server = new WebHttpServer(DEFAULT_PORT);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Wait a moment for the server to start up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
    }
    
    @Test
    void basePathResponds200WithGetMethod() {
        Response response = whenPerformGETRequestFor(DEFAULT_PORT);
        thenHttpResponseShouldBeOk(response);
    }

    private static Response whenPerformGETRequestFor(int defaultPort) {
        return given().port(defaultPort).when().
                get("http://localhost:"+defaultPort);
    }

    private void thenHttpResponseShouldBeOk(Response response) {
        response.then().
                statusCode(200);
    }

}
