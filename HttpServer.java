import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpServer {

	public static void main(String[] args) throws IOException {

		ServerSocket ss = new ServerSocket(4711);
		int clientCount = 0;

		while (true) {
			System.out.println("Waiting for requests...");
			Socket s = ss.accept();
			BufferedReader request = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String str = request.readLine(); // GET, POST etc.
			System.out.println(str);
			StringTokenizer tokens = new StringTokenizer(str, " ?");
			tokens.nextToken();
			String requestedDocument = tokens.nextToken();
			String token = tokens.nextToken();

			// A user has pressed the submit button
			if (token.contains("guess=")) {
				Integer guess = Integer.parseInt(token.split("guess=")[1]);
				System.out.println("GUESS: " + guess);
			}

			boolean existingUser = false;

			while ((str = request.readLine()) != null && str.length() > 0) {
				if (str.contains("Cookie:")) {
					Integer sessionId = Integer.parseInt(str.split("Cookie: clientId=")[1]);
					System.out.println("SESSION ID: " + sessionId);
					existingUser = true;
				}
				System.out.println(str);
			}
			s.shutdownInput();

			if (existingUser) {
				// Handle guess
			} else {
				// Handle new client
				PrintStream response = new PrintStream(s.getOutputStream());
				response.println("HTTP/1.0 200 OK");
				response.println("Server : Slask 0.1 Beta");
				if (requestedDocument.indexOf(".html") != -1)
					response.println("Content-Type: text/html");
				if (requestedDocument.indexOf(".gif") != -1)
					response.println("Content-Type: image/gif");

				clientCount++;
				response.println("Set-Cookie: clientId=" + clientCount + ";expires=Wednesday,31-Dec-2017 21:00:00 GMT");
				response.println();

				File f = new File("." + requestedDocument);
				// Kanske ha en splitHtml()-metod eller nåt...
				BufferedReader buffReader = new BufferedReader(new FileReader(f.getAbsolutePath()));
				String firstHalfHtml = "";
				String content = "";
				String secondHalfHtml = "";
				String line = "";

				while (!((line = buffReader.readLine()).contains("/**"))) {
					firstHalfHtml += line + "\n";
				}

				while (!((line = buffReader.readLine()).contains("**/"))) {
					content += line + "\n";
				}

				while (((line = buffReader.readLine()) != null)) {
					secondHalfHtml += line + "\n";
				}

				response.println(firstHalfHtml + content + secondHalfHtml);

				buffReader.close();
				s.shutdownOutput();
				s.close();
			}

		}
	}
}
