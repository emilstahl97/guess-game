import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GamePlay {

	private final String USER_AGENT = "Mozila/5.0";

	// HTTP GET request
	private void sendGet() throws Exception {

		String url = "http://localhost:4711/index.html";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		String myCookie = "userId=12312";
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.addRequestProperty("Cookie", myCookie);
		// con.setRequestProperty("Cookie", "cliendId=" + 5);
		// int cookie = getCookie(con.getHeaderField("Set-Cookie"));
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine.toString());
		}
		in.close();
		// System.out.println(cookie);

	}

	public Integer getCookie(String cookie) {
		String temp = "";
		Integer intCookie = null;
		int index = cookie.indexOf("=") + 1;
		while (!cookie.substring(index, index + 1).equals(";")) {
			temp += cookie.substring(index, index + 1);
			index++;
		}
		try {
			intCookie = Integer.parseInt(temp);
		} catch (NumberFormatException e) {
			intCookie = null;
		}
		return intCookie;

	}

	public static void main(String[] args) throws Exception {

		GamePlay http = new GamePlay();

		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet();

	}
}