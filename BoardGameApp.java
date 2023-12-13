import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

interface Drawable {
    void draw(Graphics g, int x, int y, int cellSize);

    Color getColor();
}

abstract class BoardObject implements Drawable, Serializable {
    private int positionX;// x coordinate of the object
    private int positionY;// y coordinate of the object

    public BoardObject(int x, int y) {// constructor
        this.positionX = x;
        this.positionY = y;
    }

    public int getPositionX() {// getter for x coordinate
        return positionX;// return x coordinate
    }

    public int getPositionY() {
        return positionY;// return y coordinate
    }
}

abstract class Obstacle extends BoardObject {// obstacle class
    private int damage;// damage of the obstacle

    public Obstacle(int x, int y, int damage) {// constructor
        super(x, y);
        this.damage = damage;// set damage
    }

    public int getDamage() {// getter for damage
        return damage;// return damage
    }
}

abstract class Reward extends BoardObject {
    private int value;// value of the reward

    public Reward(int x, int y, int value) {
        super(x, y);// call super constructor
        this.value = value;
    }

    public int getValue() {// getter for value
        return value;
    }
}

class Wall extends Obstacle implements Serializable {
    public Wall(int x, int y, int damage) {// constructor
        super(x, y, damage);// call super constructor
    }

    @Override
    public void draw(Graphics g, int x, int y, int cellSize) {
        g.setColor(Color.BLACK);// set color to black
        g.fillRect(x, y, cellSize, cellSize);// draw a black rectangle
    }

    @Override// override getColor method
    public Color getColor() {// getter for color
        return Color.BLACK;// return black color
    }
}

class Coin extends Reward implements Serializable {// coin class
    public Coin(int x, int y, int value) {// constructor
        super(x, y, value);// call super constructor
    }

    @Override// override draw method
    public void draw(Graphics g, int x, int y, int cellSize) {// draw method
        g.setColor(Color.YELLOW);// set color to yellow
        g.fillOval(x, y, cellSize, cellSize);// draw a yellow circle
    }

    @Override
    public Color getColor() {// getter for color
        return Color.YELLOW;//  return yellow color
    }
}

class Broccoli extends Reward implements Serializable {// broccoli class
    public Broccoli(int x, int y, int value) {// constructor
        super(x, y, value);// call super constructor
    }

    @Override
    public void draw(Graphics g, int x, int y, int cellSize) {// draw method
        g.setColor(Color.GREEN);// set color to green
        g.fillArc(x, y, cellSize, cellSize, 0, 180);
    }

    @Override
    public Color getColor() {// getter for color
        return Color.GREEN;
    }
}

class IceCream extends Reward implements Serializable {
    public IceCream(int x, int y, int value) {// constructor
        super(x, y, value);// call super constructor
    }

    @Override
    public void draw(Graphics g, int x, int y, int cellSize) {// draw method
        g.setColor(Color.BLUE);// set color to blue
        int[] xPoints = {x, x + cellSize / 2, x + cellSize};// set coordinates
        int[] yPoints = {y + cellSize, y, y + cellSize};// set coordinates
        g.fillPolygon(xPoints, yPoints, 3);
    }

    @Override
    public Color getColor() {
        return Color.BLUE;// getter for color
    }
}

class SerializableBoard implements Serializable {// serializable board class
    private static final long serialVersionUID = 1L;// serial version id

    private BoardObject[][] board;// board object

    public SerializableBoard(BoardObject[][] board) {// constructor
        this.board = board;
    }

    public BoardObject[][] getBoard() {// getter for board
        return board;
    }
}

class Board extends JPanel {
    private static final int BOARD_SIZE = 32;// board size
    private static final int CELL_SIZE = 20;// cell size
    private static final int NUM_WALLS = 256;// number of walls
    private static final int NUM_REWARDS = 48; // 16 for each type of reward

    private BoardObject[][] board;// board object
    private int entranceX, entranceY, exitX, exitY;// entrance and exit coordinates

    public Board() {// constructor
        initializeBoard();// initialize board
    }

    private void initializeBoard() {// initialize board method
        board = new BoardObject[BOARD_SIZE][BOARD_SIZE];// initialize board
        setupEntranceAndExit();// setup entrance and exit
        placeWalls();
        placeRewards(NUM_REWARDS);// place rewards
        repaint(); // Repaint the board after initialization
    }

    private void setupEntranceAndExit() {// setup entrance and exit method
        ThreadLocalRandom random = ThreadLocalRandom.current();// random object
        entranceX = random.nextInt(BOARD_SIZE);// set entrance x coordinate
        entranceY = random.nextInt(BOARD_SIZE);// set entrance y coordinate

        do {
            exitX = random.nextInt(BOARD_SIZE);
            exitY = random.nextInt(BOARD_SIZE);
        } while (exitX == entranceX && exitY == entranceY);// set exit coordinates
    }

    private void placeRewards(int numRewards) {
        ThreadLocalRandom random = ThreadLocalRandom.current();// random object
        for (int i = 0; i < numRewards; i++) {
            int x, y;
            do {
                x = random.nextInt(BOARD_SIZE);// set x coordinate
                y = random.nextInt(BOARD_SIZE);// set y coordinate
            } while (board[x][y] != null);

            Class<? extends Reward>[] rewardClasses = new Class[]{Coin.class, Broccoli.class, IceCream.class};// reward classes
            Class<? extends Reward> rewardClass = rewardClasses[i % rewardClasses.length];// reward class

            try {
                Reward reward = rewardClass.getDeclaredConstructor(int.class, int.class, int.class)// reward object
                        .newInstance(x, y, 0);
                board[x][y] = reward;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |// catch exceptions
                    InvocationTargetException e) {// catch exceptions
                e.printStackTrace();
            }
        }
    }

    private void placeWalls() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i][0] = new Wall(i, 0, 0);
            board[i][BOARD_SIZE - 1] = new Wall(i, BOARD_SIZE - 1, 0);
            board[0][i] = new Wall(0, i, 0);
            board[BOARD_SIZE - 1][i] = new Wall(BOARD_SIZE - 1, i, 0);
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();// random object
        for (int i = 0; i < NUM_WALLS; i++) {// place walls
            int x, y;
            do {
                x = random.nextInt(BOARD_SIZE);// set x coordinate
                y = random.nextInt(BOARD_SIZE);// set y coordinate
            } while (board[x][y] != null);

            board[x][y] = new Wall(x, y, 0);
        }
    }

    public SerializableBoard getSerializableBoard() {//     getter for serializable board
        return new SerializableBoard(board);// return serializable board
    }

    public void loadSerializableBoard(SerializableBoard serializableBoard) {// load serializable board method
        this.board = serializableBoard.getBoard();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {// paint component method
        super.paintComponent(g);// call super method

        for (int i = 0; i < BOARD_SIZE; i++) {// paint board
            for (int j = 0; j < BOARD_SIZE; j++) {// paint board
                int x = i * CELL_SIZE;
                int y = j * CELL_SIZE;

                if (board[i][j] != null) {// if board is not null
                    board[i][j].draw(g, x, y, CELL_SIZE);// draw board
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);// draw white rectangle
                }
            }
        }
    }
}

class BoardFrame extends JFrame {// board frame class
    private Board board;// board object

    public BoardFrame() {
        board = new Board();// initialize board
        setupUI();// setup ui
    }

    private void setupUI() {// setup ui method
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// set default close operation
        setSize(800, 800);// set size
        setLayout(new BorderLayout());// set layout

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem designMenuItem = new JMenuItem("Design");
        designMenuItem.addActionListener(new ActionListener() {// action listener
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new Board instance
                Board newBoard = new Board();

                // Update the displayed Board in the BoardFrame
                board.loadSerializableBoard(newBoard.getSerializableBoard());

                // Repaint the JFrame
                revalidate();
                repaint();
            }
        });

        JMenuItem saveMenuItem = new JMenuItem("Save");// save menu item
        saveMenuItem.addActionListener(new ActionListener() {// action listener
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();// file chooser
                int result = fileChooser.showSaveDialog(BoardFrame.this);// show save dialog
                if (result == JFileChooser.APPROVE_OPTION) {
                    SerializableBoard serializableBoard = board.getSerializableBoard();// serializable board
                    saveSerializableBoard(fileChooser.getSelectedFile().getAbsolutePath(), serializableBoard);// save serializable board
                }
            }
        });

        JMenuItem loadMenuItem = new JMenuItem("Load");// load menu item
        loadMenuItem.addActionListener(new ActionListener() {// action listener
            @Override
            public void actionPerformed(ActionEvent e) {// action listener
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(BoardFrame.this);// show open dialog
                if (result == JFileChooser.APPROVE_OPTION) {
                    SerializableBoard serializableBoard = loadSerializableBoard(fileChooser.getSelectedFile().getAbsolutePath());// load serializable board
                    board.loadSerializableBoard(serializableBoard);// load serializable board

                    // Repaint the JFrame
                    revalidate();
                    repaint();
                }
            }
        });

        menu.add(designMenuItem);// add menu item
        menu.add(saveMenuItem);
        menu.add(loadMenuItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);
        add(board, BorderLayout.CENTER);// add board
    }

    private void saveSerializableBoard(String fileName, SerializableBoard serializableBoard) {// save serializable board method
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {// object output stream
            oos.writeObject(serializableBoard);
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle the exception appropriately, for example, show a dialog to the user
        }
    }

    private SerializableBoard loadSerializableBoard(String fileName) {// load serializable board method
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (SerializableBoard) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            // Handle the exception appropriately, for example, show a dialog to the user
            return null;
        }
    }

    public static void main(String[] args) {// main method
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {// run method
                new BoardFrame().setVisible(true);// set visible
            }
        });
    }
}
