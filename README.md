Griffith University 2805ICT/3815ICT/7805ICT (Group 4)
* Student name: Fiston Kayeye, Student ID: 5303300, Enrolled Course Code: 2805ICT, GH: **Sarvinfils**
* Student name: Jack Carrall, Student ID: 5241918, Enrolled Course Code: 3815ICT, GH: **TheCarrot3702**
* Student name: Lucas Setiady, Student ID: 5178088, Enrolled Course Code: 2805ICT, GH: **Iseti1**
* Student name: Stefan Barosan, Student ID: 5277574, Enrolled Course Code: 2805ICT, GH: **MatchaBreak**
* Student name: Joseph Fernando, Student ID: 5326097, Enrolled Course Code: 2805ICT, GH: **jfernando02**

# Tetris Game
This is a custom Java implementation of the classic Tetris game utilising IntelliJ

## Project Structure
![image](https://github.com/user-attachments/assets/92cd6c44-b9d2-4cfe-8bb5-db639a43c7a4)

```
TetrisGame/
├── src/
│   ├── main/
│   ├── model/
│   ├── resources/
│   ├── ui/
│   └── util/
```

### Main
- `Main.java`: Entry point to the application, handles the game loop and other game-related logic.

### Model
- `Game.java`: Manages game state, updates, and overall control.
- `Score.java`: Handles scoring logic.
- `TetrisShape.java`: Defines the shapes and their rotations.
- `TetrisShapeInstance.java`: Represents a specific instance of a Tetris shape in the game.

### Resources
- `images/`: Stores image resources like Tetris block images.

### UI
![image](https://github.com/user-attachments/assets/989910a6-6b1d-467e-ba2e-7e3327004abf)

- `field/`
  - `FieldPane.java`: Panel that displays the game field (interactive area of game).
- `panel/`
  - `ConfigurePanel.java`: Allows user configuration or settings.
  - `GamePanel.java`: Displays the main game interface which nests the FieldPane.
  - `HighScorePanel.java`: Shows high scores.
  - `MainPanel.java`: The main panel that might include menus or game start options.
  - `SplashPanel.java`: The initial screen shown when the game starts.
- `MainFrame.java`: The main application window/frame which triggers different pannels and holds the main game state to share data.
- `UIGenerator.java`: Helper class for generating UI components (needs refactoring)

### Util
- `CommFun.java`: Contains common utility functions used throughout the application. (needs refactoring)

## Getting Started

1. Clone the repository
2. Open the project in your preferred Java IDE
3. Run `Main.java` to start the game

## Features

- Smooth Tetris Gameplay, with a potential future optional feature of discrete Tetromino movement.
- High score tracking (In the works)
- Configurable settings (In the works)
- AI Gameplay (In the works)
- Multiplayer (In the works)
- Splash screen on startup
