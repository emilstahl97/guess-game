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

		while (true) {
			Socket s = ss.accept();
			BufferedReader request = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String str = request.readLine();
			System.out.println(str);
			StringTokenizer tokens = new StringTokenizer(str, " ?");
			tokens.nextToken();
			String requestedDocument = tokens.nextToken();
			while ((str = request.readLine()) != null && str.length() > 0) {
				System.out.println(str);
			}
			s.shutdownInput();

			PrintStream response = new PrintStream(s.getOutputStream());
			response.println("HTTP/1.0 200 OK");
			response.println("Server : Slask 0.1 Beta");
			if (requestedDocument.indexOf(".html") != -1)
				response.println("Content-Type: text/html");
			if (requestedDocument.indexOf(".gif") != -1)
				response.println("Content-Type: image/gif");

			response.println("Set-Cookie: clientId=1; expires=Wednesday,31-Dec-2017 21:00:00 GMT");
			response.println();

			File f = new File("." + requestedDocument);
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
			System.out.println("FIRST:" + firstHalfHtml);
			System.out.println("CONTENT:" + content);
			System.out.println("SECOND:" + secondHalfHtml);

			buffReader.close();
			s.shutdownOutput();
			s.close();
		}
	}
}
