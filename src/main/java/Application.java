import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer server = HttpServer.create(address, 0);
        server.createContext("/test", new MyHandler(objectMapper));
        server.setExecutor(executorService); // creates a default executor
        System.out.println(String.format("Starting web server on %s ...", address));
        server.start();
    }

    static class MyHandler implements HttpHandler {
        private final ObjectMapper objectMapper;

        public MyHandler(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            OutputStream os = t.getResponseBody();
            t.sendResponseHeaders(200, 0);
            this.objectMapper.writeValue(os, new Person("soroosh", "sorooshi"));
            os.close();
        }
    }
}
