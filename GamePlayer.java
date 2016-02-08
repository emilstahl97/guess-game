import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GamePlayer {
	Socket clientSocket;
	BufferedReader clientInput;
	DataOutputStream output;
	private final int tooHIGH = 1;
	private final int tooLOW = -1;
	private final int CORRECT = 0;
	private final int sessionID = 1337;

	public void playGame(int low, int high) throws IOException {
		int guess = (int) (low + high) / 2;
		System.out.println("GUESS: " + guess + " HIGH " + high + " LOW: " + low);
		int status = makeGuess(guess);
		switch (status) {
		case tooHIGH:
			high = guess;
			playGame(low, high);
			break;
		case tooLOW:
			low = guess + 1;
			playGame(low, high);
			break;
		case CORRECT:
			System.out.println("YAY");
		}
	}

	public int makeGuess(int guess) throws IOException {
		clientSocket = new Socket("127.0.0.1", 4711);
		clientInput = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		output = new DataOutputStream(clientSocket.getOutputStream());
		String message = guessMessage(guess);
		output.writeUTF(message);
		clientSocket.shutdownOutput();

		String mStr = "";
		while (mStr != null) {
			if (mStr.contains("Too low")) {
				return tooLOW;
			} else if (mStr.contains("Too high")) {
				return tooHIGH;
			} else if (mStr.contains("Game over")) {
				return CORRECT;
			} else if (mStr.contains("Wow!")) {
				System.out.println(mStr);
				return CORRECT;
			}
			mStr = serverInput.readLine();
		}
		clientSocket.close();
		return 10;
	}

	public String guessMessage(int guess) {
		String message = "GET /index.html?guess=" + guess + " HTTP/1.1 \r\n";
		message += "Cookie: clientId=" + sessionID + "\r\n";
		return message;
	}

	public String newGameMessage() {
		String message = "GET /index.html?newgame= HTTP/1.1 \r\n";
		message += "Cookie: clientId=" + sessionID + "\r\n";
		return message;
	}

	public void newGame() throws Exception {
		clientSocket = new Socket("127.0.0.1", 4711);
		clientInput = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		output = new DataOutputStream(clientSocket.getOutputStream());
		String message = newGameMessage();
		output.writeUTF(message);
		clientSocket.shutdownOutput();
		String mStr = "";
		while (mStr != null) {
			mStr = serverInput.readLine();
		}
		clientSocket.close();
	}

	public static void main(String[] args) {
		try {
			GamePlayer player = new GamePlayer();
			for (int c = 1; c < 100; c++) {
				player.newGame();
				player.playGame(1, 100);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}