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

	// Initialize the secret number to be between 0 and 100.
	public GuessHandler() {
		this.secretNumber = r.nextInt(100);
		this.gameOver = false;
		this.numberOfGuesses = 0;
		this.win = false;

	}

	public String checkGuess(Integer guess) {
		if (gameOver) {
			if (win) {
				return "<p>You have already won, start a new game and go for it again! Last time you got it on "
						+ numberOfGuesses + " gueses.</p>";
			} else {
				return "<p>Game over. You have already used 10 guesses.</p>";
			}
		} else if (guess == null) {
			return "<p>You have " + (10 - numberOfGuesses)
					+ " guesses to guess the right number between 0 and 100.</p>";
		} else if (guess < 0 || guess > 100) {
			return "<p>You have to give a number between 0 and 100</p>";
		}

		checkGame();
		if (guess < secretNumber) {
			numberOfGuesses++;
			if (gameOver) {
				return "<p>Too low! The game is now over. The secret number was " + secretNumber + "</p>";
			} else {
				return "<p>Too low! Try again. You now have " + (10 - numberOfGuesses) + "left</p>";
			}

		} else if (guess > secretNumber) {
			numberOfGuesses++;
			if (gameOver) {
				return "<p>Too low! The game is now over. The secret number was " + secretNumber + "</p>";
			} else {
				return "<p>Too high! Try again. You now have " + (10 - numberOfGuesses) + "left</p>";
			}

		} else if (guess == secretNumber) {
			numberOfGuesses++;
			gameOver = true;
			win = true;
			return "<p>Wow! Your guess was right! The secret number was " + guess + ". You got in on " + numberOfGuesses
					+ " of guesses</p>";
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

}
