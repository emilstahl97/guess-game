import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpServer {

	public static void main(String[] args) throws IOException {

		ServerSocket ss = new ServerSocket(4711);
		int clientCount = 0;
		HashMap<Integer, GuessHandler> clientMap = new HashMap<Integer, GuessHandler>();
		GuessHandler guessHandler;

		while (true) {
			Integer guess = null;
			Integer sessionId = null;
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
				if (token.split("guess=").length > 1) {
					try {
						guess = Integer.parseInt(token.split("guess=")[1]);
					} catch (NumberFormatException e) {
						guess = null;
					}
					System.out.println("GUESS: " + guess);
				}
			}

			// Parses for sessionId. If it does not exist, it will still be
			// null.
			while ((str = request.readLine()) != null && str.length() > 0) {
				if (str.contains("Cookie:")) {
					System.out.println(str);
					sessionId = Integer.parseInt(str.split("Cookie: clientId=")[1]);
				}
			}

			// Checks if sessionId is in out hashmap. If it's not, the
			// clientCounter is raised with one and
			// a new sessionId is created with a new guesshandler.
			if (clientMap.containsKey(sessionId)) {
				guessHandler = clientMap.get(sessionId);
				if (token.contains("newgame=")) {
					guessHandler.startNewGame();
				}

			} else {
				clientCount++;
				sessionId = clientCount;
				guessHandler = new GuessHandler();
				clientMap.put(sessionId, guessHandler);
			}

			s.shutdownInput();
			// Handle new client
			PrintStream response = new PrintStream(s.getOutputStream());
			response.println("HTTP/1.0 200 OK");
			response.println("Server : Slask 0.1 Beta");
			if (requestedDocument.indexOf(".html") != -1)
				response.println("Content-Type: text/html");
			if (requestedDocument.indexOf(".gif") != -1)
				response.println("Content-Type: image/gif");

			response.println("Set-Cookie: clientId=" + sessionId + ";expires=Wednesday,31-Dec-2017 21:00:00 GMT");
			response.println();

			File f = new File("." + requestedDocument);
			// Kanske ha en splitHtml()-metod eller nåt...
			BufferedReader buffReader = new BufferedReader(new FileReader(f.getAbsolutePath()));
			String firstHalfHtml = "";

			// The guesshandler checks the guess. A guess can either be a
			// number, or null if it is not given. It returns our message to the
			// user.
			String content = guessHandler.checkGuess(guess);
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

			content += guessHandler.getOldGames();
			response.println(firstHalfHtml + content + secondHalfHtml);

			buffReader.close();
			s.shutdownOutput();
			s.close();

		}
	}

	private String createHtmlOutputString() {
		return "";
	}

}
