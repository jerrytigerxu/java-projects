package flappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;
    public final int WIDTH = 1200, HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;

    public int ticks, yMotion, score;

    public ArrayList<Rectangle> columns;

    public boolean gameOver, started;
    public Random rand;


    public FlappyBird() { // the main class runs the JFrame in which the game actually runs
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();

        jframe.add(renderer);
        jframe.setTitle("Flappy Bird");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        bird = new Rectangle(WIDTH / 2, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean start) { // the method that adds columns to the array that will be generated on the screen using a for loop
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column) { // the method that actually creates the columns
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump() { // the method that runs whenever there is the mouseClick or keyPressed
        if (gameOver) { // resetting all the objects
            bird = new Rectangle(WIDTH / 2, HEIGHT / 2 - 10, 20, 20);

            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }
        if (!started) {
            started = true;
        }
        else if (!gameOver) {
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { // the method that creates the animations
        int speed = 10;

        ticks ++;

        if (started) { //
            for (Rectangle column : columns) { // making the columns move to the left - simulating that the bird is moving forward
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) { // moving the bird fall because of "gravity"
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);

                // when the column moves off the left side of the screen, remove that column from the columns array
                if (column.x + column.width < 0) { // when the entire column is out of the frame, remove it
                    columns.remove(column);

                    if (column.y == 0) {
                        addColumn(false);
                    }

                }
            }

            bird.y += yMotion;

            // checking to see if the bird gets past the columns or collides into them
            for (Rectangle column : columns) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                }

                if (column.intersects(bird)) { // checking to see if the bird collides with the column (preventing the bird from going through the columns)
                    gameOver = true;

                    if (bird.x < column.x) { // the bird will get pushed by the column as the column is moving
                        bird.x = column.x - bird.width;
                    } else {
                        if (column.y != 0) {
                            bird.y = column.y - bird.height;
                        } else if (bird.y < column.height) {
                           bird.y = column.height;
                        }
                    }
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) { // gameOver if the bird goes too high or too low
                gameOver = true;
            }

            if (bird.y + yMotion >= HEIGHT - 120) {
                bird.y = HEIGHT - 120 - bird.height;
            }
        }

        renderer.repaint(); // keep repainting the graphics

    }

    public void repaint(Graphics g) {
        // Drawing the sky
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Drawing the ground
        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);
        // Drawing the grass
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        // Drawing the "bird"
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        // Drawing the columns
        for (Rectangle column : columns) {
            paintColumn(g, column);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 100));

        if (!started) { // what is displayed when the game has not yet been started
            g.drawString("Click to start!", WIDTH / 2 - 350, HEIGHT / 2 - 50);
        }

        if (gameOver) { // what is displayed when it's gameOver
            g.drawString("Game Over!", WIDTH / 2 - 300, HEIGHT / 2 - 50);
        }

        if (!gameOver && started) { // displaying the score when it's not gameOver and the game is started
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }


    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) { // jump when the mouse is clicked
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) { // jump when the space bar is pressed
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }

    }
}
