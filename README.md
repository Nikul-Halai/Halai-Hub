# Board Game Simulator

**Author:**
- Nikul Halai

## Overview:

The "Board Game Simulator" is a Java application that simulates a dynamic board game environment with obstacles, rewards, and a customizable design feature. This project serves as an illustrative example of object-oriented programming, graphical user interface (GUI) implementation using Swing, and serialization for saving and loading game states.

## Project Details:

### Features:

1. **Board Objects:**
   - Implements various classes representing objects on the game board, such as walls, coins, broccoli, and ice cream.
   - Each object extends the `BoardObject` class and implements the `Drawable` interface for consistent drawing and color representation.

2. **Board Design:**
   - Generates a dynamic game board with a specified size, including walls, entrance, and exit points.
   - Randomly places obstacles (walls) and rewards (coins, broccoli, ice cream) on the board.

3. **Serialization:**
   - Implements the `Serializable` interface for saving and loading game states.
   - Allows users to design a custom board, save it, and load it for future gameplay.

4. **User Interface:**
   - Utilizes Swing to create a user-friendly interface with a menu bar.
   - Includes options for designing a new board, saving the current state, and loading a previously saved state.

### How to Use:

1. **Run the Application:**
   - Compile and run the `BoardFrame` class to start the application.
   ```bash
   javac BoardFrame.java
   java BoardFrame
   ```

2. **Design a Board:**
   - Select "Design" from the menu to create a new board with obstacles and rewards.
   - The new design will replace the current board.

3. **Save and Load:**
   - Use the "Save" option to save the current board state.
   - Use the "Load" option to load a previously saved board state.

### Project Structure:

- The project consists of multiple classes representing different elements of the board game.
- The `BoardFrame` class manages the GUI and user interactions.
- Object-oriented principles, such as inheritance and interfaces, are employed for code organization.

### Sample Output:

The application presents a graphical representation of the game board with walls, rewards, and an entrance/exit.

## Contributions:

Contributions to enhance the functionality, improve code structure, or fix bugs are welcome. Submit a pull request to contribute to the project's development.

## Author's Note:

This project is designed to demonstrate essential Java programming concepts in the context of a board game simulator. It was created as part of a programming course to showcase practical application skills.
