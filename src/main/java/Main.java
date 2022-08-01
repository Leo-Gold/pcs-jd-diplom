import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        //System.out.println(engine.search("бизнес"));

        try (ServerSocket server = new ServerSocket(8989);) {
            try (
                    Socket socket = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
            ) {
                String json = new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create()
                        .toJson(engine.search(in.readLine()));
                out.print(json);
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}