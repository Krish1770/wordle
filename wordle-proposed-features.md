# Wordle Application --- Proposed Features

This document captures the features discussed so far for the Wordle
application, separated into frontend and backend responsibilities. It
reflects the current proposed scope, not a claim that every feature has
already been implemented.

## 1. Frontend

### 1.1 Game board

-   Display a **5-column × 6-row** Wordle grid.
-   Each row represents one guess; each cell represents one letter.
-   Show the current guess as the player enters it.
-   Show feedback for submitted guesses using tile states:
    -   **Correct position** --- the letter is in the secret word and in
        the correct position.
    -   **Present, wrong position** --- the letter is in the secret word
        but in a different position.
    -   **Not present** --- the letter does not occur in the secret
        word.

### 1.2 User input

-   Allow the player to enter a five-letter guess using the keyboard.
-   Support submitting a guess and moving to the next row after a valid
    submission.
-   Prevent the player from entering more guesses after the game has
    ended.
-   The frontend may perform basic input checks, such as requiring
    exactly five letters, for a better user experience. The backend
    remains responsible for authoritative validation.

### 1.3 Game feedback and state

-   Display the number of attempts used / remaining.
-   Show a success message when the player guesses the word.
-   Show a game-over message when the player uses all six attempts
    without guessing correctly.
-   Display loading and error states while requests are being processed.
-   Add tile feedback animations and user-facing messages.

### 1.4 Backend communication

-   Call the backend to create a new game.
-   Submit each guess to the backend.
-   Retrieve the current game state when needed.
-   Render the response returned by the backend; do not determine the
    secret word or independently decide whether a guess is correct.

## 2. Backend

### 2.1 Game creation

-   Provide an API to create a new game:
    -   `POST /api/games`
-   Select a secret word when a game is created.
-   Store the secret word against that specific game.
-   Persist the game in PostgreSQL using Spring Data JPA.
-   Return the newly created game's identifier and the information the
    frontend needs to start playing.
-   Never expose the secret word in the API response.

### 2.2 Guess submission and validation

-   Provide an API to submit a guess:
    -   `POST /api/games/{id}/guesses`
-   Validate that the submitted guess:
    -   Is present.
    -   Contains exactly five letters.
    -   Is a valid word in the application's dictionary.
-   Load the game using its game ID and reject guesses for games that do
    not exist or have already ended.
-   Compare the guess with the stored secret word on the backend.
-   Return per-letter feedback for the frontend to display.

### 2.3 Word dictionary

-   Keep the initial word list in `src/main/resources/words.csv`.
-   Load the CSV once when the application starts.
-   Normalize words to uppercase and store them in a `Set<String>` for
    efficient lookup.
-   Accept only five-letter words (the proposed format check is
    `[A-Z]{5}`).
-   Use the dictionary to reject guesses that are not recognized words.
-   A single CSV file is sufficient for the initial version. A separate
    list for possible secret answers can be considered later if needed.

### 2.4 Word comparison

-   Perform the comparison on the backend, not in the frontend.
-   Return feedback for each character:
    -   Correct letter and position.
    -   Letter exists but is in another position.
    -   Letter does not exist in the secret word.
-   Handle repeated letters carefully so a letter is not incorrectly
    marked as present more times than it occurs in the secret word.

### 2.5 Attempts and game status

-   Allow a maximum of six guesses.
-   Track the number of guesses used.
-   Mark the game as won when the guess matches the secret word.
-   Mark the game as lost when all six attempts are used without a
    correct guess.
-   Persist the updated attempts and game status.

### 2.6 Game retrieval

-   Provide an API to retrieve a game's current state:
    -   `GET /api/games/{id}`
-   Return the information needed to restore or display the game, such
    as its status and guesses/attempts.
-   Do not return the secret word while the game is in progress.
-   A separate result endpoint was discussed as an optional addition; it
    is not required for the initial API set.

### 2.7 Response structure

-   Use a common generic response wrapper, `ResponseDTO<T>`, with:
    -   `HttpStatus httpStatus`
    -   `String message`
    -   `T data`
-   Use `ResponseEntity` in the controller to apply the actual HTTP
    status code. Storing an `HttpStatus` value inside the DTO alone does
    not set the HTTP response status.

### 2.8 Persistence

-   Use PostgreSQL with Spring Data JPA.
-   Store each game's secret word and game state in its own persisted
    game record.
-   Keep the secret word associated with the individual game rather than
    in a single static variable. This supports multiple games and allows
    game state to survive application restarts.

## 3. Proposed API summary

  ---------------------------------------------------------------------------
  Method                  Endpoint                    Purpose
  ----------------------- --------------------------- -----------------------
  `POST`                  `/api/games`                Create a new game and
                                                      return its game ID and
                                                      initial state

  `POST`                  `/api/games/{id}/guesses`   Validate and submit a
                                                      guess; return
                                                      per-letter feedback and
                                                      updated game state

  `GET`                   `/api/games/{id}`           Retrieve the current
                                                      state of a game
  ---------------------------------------------------------------------------

**Optional, not part of the initial required set:** a separate endpoint
to retrieve the final game result.

## 4. Responsibility boundary

  -----------------------------------------------------------------------
  Concern                 Frontend                Backend
  ----------------------- ----------------------- -----------------------
  Display the 5×6 grid    Yes                     No

  Capture keyboard input  Yes                     No

  Basic five-letter input Optional                Must validate
  check for UX                                    

  Check whether a word is No authoritative check  Yes
  in the dictionary                               

  Store and protect the   No                      Yes
  secret word                                     

  Compare guess with      No                      Yes
  secret word                                     

  Calculate               Display returned        Yes
  repeated-letter-aware   feedback                
  tile feedback                                   

  Track attempts and      Display returned state  Yes, and persist it
  win/loss status                                 

  Display loading,        Yes                     Return appropriate
  errors, animations and                          responses
  messages                                        

  Persist game data       No                      Yes, PostgreSQL/JPA
  -----------------------------------------------------------------------

## 5. Current scope notes

-   The game format discussed so far is **five letters and six
    attempts**.
-   The backend is responsible for all game rules and the secret word.
-   The frontend is responsible for interaction and presentation.
-   The initial dictionary source is one `words.csv` file loaded once at
    startup.
-   The proposed stack is a Spring Boot backend with PostgreSQL/JPA and
    a separate frontend.
-   These are the features proposed in the discussion; implementation
    status is not implied.
