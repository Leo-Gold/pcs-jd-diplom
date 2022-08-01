import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 8989);
                BufferedReader response = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println(input.readLine());
            String result = response.readLine();
            while (result != null) {
                System.out.println(result);
                result = response.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
