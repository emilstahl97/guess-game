/*
 * Class for handling guesses with a secret number.
 */
public class GuessHandler {

	int secretNumber;

	public GuessHandler(int secretNumber) {
		this.secretNumber = secretNumber;
	}

	public String checkGuess(int guess) {
		if (guess < secretNumber) {
			return "<p>Too low! Try again.</p>";
		} else if (guess > secretNumber) {
			return "<p>Too high! Try again.</p>";
		} else if (guess == secretNumber) {
			return "<p>Wow! Your guess was right! The secret number was " + guess + ".</p>";
		}
		return "";
	}

}
