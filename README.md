# guess-game
This is a guessing game written in java.

# Questions
* What is the difference between GET and POST?
  * GET requests data from a specified source. Variables are sent through the URL and therefore sensitive data should not be sent here. It remains in browser history.
  ```
   GET /index.html?guess=67 HTTP/1.1
   ```
  * POST submits data to a specified source. The data is sent in the HTTP body and does not remain in browser history.
  ```
   POST /index.html HTTP/1.1
   Host: w3schools.com
   guess=67
   ```

