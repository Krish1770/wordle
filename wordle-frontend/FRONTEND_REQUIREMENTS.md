# Frontend Implementation Plan based on Wordle Backend

This document outlines everything that needs to be implemented in the React frontend (`wordle-frontend`) to connect and work with the existing Spring Boot backend (`wordle-backend`).

---

## 1. Backend Overview & API Contracts

The backend is built with Spring Boot, running on **`http://localhost:8080`**, with endpoints under the base path **`/v1/wordle`**.

### 1.1 Start Game Endpoint
- **URL**: `POST http://localhost:8080/v1/wordle/secretWord`
- **Headers**: `Content-Type: application/json`
- **Request Body**: *None*
- **Response Format**:
  ```json
  {
    "httpStatus": "OK",
    "message": "secret word id retrieved",
    "data": {
      "gameId": "c869ba1e-9fbc-49e0-8fa1-7f9f7435f19c"
    }
  }
  ```
- **Frontend Action**: Call this endpoint when the page loads or when the user clicks **"New Game"** / **"Play Again"**. Store `gameId` in state or `localStorage`.

---

### 1.2 Validate Guess Endpoint
- **URL**: `POST http://localhost:8080/v1/wordle/validate`
- **Headers**: `Content-Type: application/json`
- **Request Body** (`CheckWordDto`):
  ```json
  {
    "guessWord": "REACT",
    "gameId": "c869ba1e-9fbc-49e0-8fa1-7f9f7435f19c"
  }
  ```
- **Response Format** (`GuessResultDto`):
  ```json
  {
    "httpStatus": "OK",
    "message": "secret word id retrieved",
    "data": {
      "gameId": "c869ba1e-9fbc-49e0-8fa1-7f9f7435f19c",
      "guessResult": [
        "Correct",
        "Absent",
        "Present",
        "Absent",
        "Correct"
      ]
    }
  }
  ```
- **Feedback Values Returned**:
  | Value | Meaning | UI Representation |
  |---|---|---|
  | `"Correct"` | Letter matches and is in correct index | **Green** tile & keyboard key |
  | `"Present"` | Letter exists in word but wrong index | **Yellow** tile & keyboard key |
  | `"Absent"` | Letter is not in the word | **Gray** tile & keyboard key |

- **Error Response** (e.g., invalid game ID):
  ```json
  {
    "httpStatus": "NOT_FOUND",
    "message": "Game Id not found for <id>",
    "data": null
  }
  ```

---

## 2. Key Responsibilities for Frontend (Bridging Backend Logic)

Based on inspection of the current backend code (`WordleService.java`, `Game.java`):

1. **Case Normalization**:
   - The backend dictionary is uppercase.
   - Frontend must send guesses in **uppercase** (e.g. `word.toUpperCase()`).
2. **5-Letter Validation**:
   - The backend directly accesses index 0 through 4 (`guessWord.charAt(i)`).
   - Frontend **must prevent submitting** unless the word has exactly 5 letters.
3. **Attempt Tracking (Max 6 Guesses)**:
   - The backend currently does not track the number of attempts in the database.
   - Frontend must track the current row (0 to 5) and declare **Game Over (Lost)** if row reaches 6 without a win.
4. **Win Condition**:
   - Frontend should check if every item in `guessResult` equals `"Correct"`:
     ```js
     const isWon = guessResult.every((status) => status === "Correct");
     ```
5. **CORS Handling**:
   - The backend controller currently does not have `@CrossOrigin(origins = "http://localhost:5173")`.
   - To avoid browser CORS errors in development, configure a proxy in `vite.config.js` (see Section 5 below).

---

## 3. UI Component Structure to Implement

```text
src/
├── api/
│   └── wordleApi.js         # API calls (startNewGame, validateWord)
├── components/
│   ├── Header/
│   │   └── Header.jsx       # Title, New Game button, Dark mode toggle
│   ├── Grid/
│   │   ├── Board.jsx        # 6-row container
│   │   ├── Row.jsx          # 5-tile row (current row, completed row, empty row)
│   │   └── Tile.jsx         # Single letter tile with color status & flip animation
│   ├── Keyboard/
│   │   ├── Keyboard.jsx     # On-screen QWERTY keyboard layout
│   │   └── Key.jsx          # Individual key button showing Correct/Present/Absent colors
│   └── Modal/
│       └── GameOverModal.jsx# Win/loss banner with "Play Again" button
├── hooks/
│   └── useWordle.js         # Custom hook managing game state & keypress logic
├── App.jsx                  # Main layout assembling components
├── App.css / index.css      # Grid, Tile, and Keyboard styling
└── main.jsx
```

---

## 4. State Management Requirements

A standard Wordle game requires the following state:

| State Variable | Type | Description |
|---|---|---|
| `gameId` | `string \| null` | UUID returned from `POST /v1/wordle/secretWord` |
| `guesses` | `string[]` | Array of completed guess strings (up to 6) |
| `results` | `string[][]` | Array of feedback arrays from backend (e.g., `[["Correct", "Absent", ...]]`) |
| `currentGuess`| `string` | Currently typed word (0 to 5 characters) |
| `currentRow` | `number` | Active row index (`0` to `5`) |
| `gameStatus` | `'IN_PROGRESS' \| 'WON' \| 'LOST'` | Current state of the game |
| `keyboardStatus` | `Record<string, 'Correct' \| 'Present' \| 'Absent'>` | Highlighting for keyboard letters |
| `isLoading` | `boolean` | Disables input while waiting for backend validation |
| `errorMessage`| `string \| null` | Displays alerts like "Not enough letters" or "Game ID not found" |

---

## 5. Recommended Vite Proxy Configuration (`vite.config.js`)

To seamlessly connect to `http://localhost:8080` without hitting browser CORS restrictions:

```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/v1': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
```

With this proxy configured, frontend API requests can simply call:
- `fetch('/v1/wordle/secretWord', { method: 'POST' })`
- `fetch('/v1/wordle/validate', { method: 'POST', body: ... })`

---

## 6. Implementation Checklist

- [ ] Configure `vite.config.js` proxy for `/v1` to `http://localhost:8080`.
- [ ] Create `src/api/wordleApi.js` for `getSecretWord()` and `validateWord()`.
- [ ] Create `Board`, `Row`, and `Tile` components with 5x6 layout.
- [ ] Implement physical keyboard listener (`keydown`) for letters, `Backspace`, and `Enter`.
- [ ] Implement on-screen virtual keyboard with keys styled according to highest status (Green > Yellow > Gray).
- [ ] Connect `Enter` key to backend validation API.
- [ ] Implement win/loss detection and "Play Again" modal.
- [ ] Add flip and shake CSS animations for tiles.
