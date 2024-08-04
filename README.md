Griffith University 2805ICT/3815ICT/7805ICT (Group 4)
* Student name: Fiston Kayeye, Student ID: 5303300, Enrolled Course Code: 2805ICT,
* Student name: Jack Carrall, Student ID: 5241918, Enrolled Course Code: 3815ICT
* Student name: Lucas Setiady, Student ID: 5178088, Enrolled Course Code: 2805ICT
* Student name: Stefan Barosan, Student ID: 5277574, Enrolled Course Code: 2805ICT
* Student name: Joseph Fernando, Student ID: 5326097, Enrolled Course Code: 2805ICT

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
![image](https://github.com/user-attachments/assets/13f41e00-0bb3-47f9-be14-0a586174d31e)

- `field/`
  - `FieldPane.java`: Panel that displays the game field (interactive area of game).
- `panel/`
  - `ConfigurePanel.java`: Allows user configuration or settings.
  - `GamePanel.java`: Displays the main game interface.
  - `HighScorePanel.java`: Shows high scores.
  - `MainPanel.java`: The main panel that might include menus or game start options.
  - `PlayPanel.java`: Specific to gameplay, includes controls and active game display.
- `MainFrame.java`: The main application window/frame.
- `SplashScreen.java`: The initial screen shown when the game starts.
- `UIGenerator.java`: Helper class for generating UI components.

### Util
- `CommFun.java`: Contains common utility functions used throughout the application.

## Getting Started

1. Clone the repository
2. Open the project in your preferred Java IDE
3. Run `Main.java` to start the game

## Features

- Classic Tetris gameplay
- High score tracking
- Configurable settings
- Splash screen on startup
