import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GamePlay {

	private String cookie = "";
	private String url = "http://localhost:4711/index.html";
	private final String USER_AGENT = "Mozila/5.0";

	private void getCookie() throws Exception {

		HttpURLConnection con = connect("");

		// Grab Set-Cookie headers:
		String cookie = con.getHeaderFields().get("Set-Cookie").get(0);
		this.cookie = cookie.split(";")[0];
		System.out.println("COOKIE: " + cookie);
		con.disconnect();

	}

	private String guess(int guess) throws IOException {

		String result = "";

		HttpURLConnection con = connect("?guess=" + guess);
		con.setRequestProperty("Cookie", cookie);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains("low")) {
				System.out.println("LOW");
				result = "LOW";
			} else if (inputLine.contains("high")) {
				System.out.println("HIGH");
				result = "HIGH";
			} else if (inputLine.contains("Wow")) {
				System.out.println("WOW");
				result = "WOW";
			}
		}
		in.close();
		con.disconnect();
		return result;
	}

	public void playGame(int low, int high) throws IOException {
		int guess = (int) (low + high) / 2;
		System.out.println("GUESS: " + guess + " HIGH " + high + " LOW: " + low);
		String status = guess(guess);
		switch (status) {
		case "HIGH":
			high = guess;
			playGame(low, high);
			break;
		case "LOW":
			low = guess + 1;
			playGame(low, high);
			break;
		case "WOW":
			System.out.println("YAY");
		}
	}

	private HttpURLConnection connect(String request) throws IOException {

		URL obj = new URL(url + request);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		return con;
	}

	public static void main(String[] args) throws Exception {

		GamePlay player = new GamePlay();
		player.getCookie();

		for (int c = 1; c < 100; c++) {
			player.newGame();
			player.playGame(1, 100);
		}
		player.playGame(0, 100);

	}

	private void newGame() throws IOException {
		HttpURLConnection con = connect("?newgame=NEW+GAME");
		con.setRequestProperty("Cookie", cookie);
		con.disconnect();
	}
}