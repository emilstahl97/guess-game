import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 * Class for handling guesses with a secret number.
 */
public class GuessHandler {

	private int secretNumber;
	private Random r = new Random();
	private boolean gameOver;
	private int numberOfGuesses;
	private boolean win;
	private ArrayList<HashMap<String, Integer>> oldGames;

	// Initialize the secret number to be between 0 and 100.
	public GuessHandler() {
		oldGames = new ArrayList();
		this.secretNumber = r.nextInt(100);
		this.gameOver = false;
		this.numberOfGuesses = 0;
		this.win = false;
	}

	public String checkGuess(Integer guess) {
		if (gameOver) {
			if (win) {
				return "<p>You have already won, start a new game and go for it again! Last time you got it on "
						+ numberOfGuesses + " guesses.</p>";
			} else {
				return "<p>Game over. You have already used 10 guesses.</p>";
			}
		} else if (guess == null) {
			return "<p>You have " + (10 - numberOfGuesses)
					+ " guesses to guess the right number between 0 and 100.</p>";
		} else if (guess < 0 || guess > 100) {
			return "<p>You have to give a number between 0 and 100.</p>";
		}

		checkGame();
		if (guess < secretNumber) {
			numberOfGuesses++;
			if (gameOver) {
				return "<p>Too low! The game is now over. The secret number was " + secretNumber + ".</p>";
			} else {
				return "<p>Too low! Try again. You now have " + (10 - numberOfGuesses) + " guesses left.</p>";
			}

		} else if (guess > secretNumber) {
			numberOfGuesses++;
			if (gameOver) {
				return "<p>Too high! The game is now over. The secret number was " + secretNumber + ".</p>";
			} else {
				return "<p>Too high! Try again. You now have " + (10 - numberOfGuesses) + " guesses left.</p>";
			}

		} else if (guess == secretNumber) {
			numberOfGuesses++;
			gameOver = true;
			win = true;
			return "<p>Wow! Your guess was right! The secret number was " + guess + ". You got in on " + numberOfGuesses
					+ " guesses.</p>";
		}
		return "";
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void checkGame() {
		if (numberOfGuesses >= 9) {
			gameOver = true;
		}
	}

	public String getOldGames() {
		String s = "<br><b>GAMES PLAYED:</b><br>";
		for (HashMap<String, Integer> oldGame : oldGames) {
			if (oldGame.get("win") == 1) {
				s += "<br>Won game in ";
			} else {
				s += "<br>Lost game in ";
			}
			s += oldGame.get("numberOfGuesses") + " guesses.<br>";
		}
		return s;
	}

	public void startNewGame() {
		HashMap<String, Integer> h = new HashMap<>();
		h.put("numberOfGuesses", numberOfGuesses);
		if (win) {
			h.put("win", 1);
		} else {
			h.put("win", 0);
		}
		oldGames.add(h);

		this.secretNumber = r.nextInt(100);
		this.gameOver = false;
		this.numberOfGuesses = 0;
		this.win = false;
	}

}
