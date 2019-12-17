import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Application {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler(objectMapper));
        server.setExecutor(null); // creates a default executor
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
            final String response = this.objectMapper.writeValueAsString(new Person("soroosh", "sorooshi"));
            t.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.close();
        }
    }
}
