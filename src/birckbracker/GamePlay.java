package birckbracker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    public int score = 0;

    private int totalBricks = 40;

    private final Timer timer;

    private int playerX = 300;

    private int ballPosX = 290;
    private int ballPosY = 350;
    private int ballDirX = getRandomNumberFor(-3);
    private int ballDirY = getRandomNumberFor(-5);

    private MapGenerator mapPlay;

    private final String mensagemStart  = "Press Enter/Left/Right Arrow to start the game!";

    public GamePlay() {
        mapPlay = new MapGenerator(4, 10);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 9;
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics graphics) {
        //background
        graphics.setColor(Color.black);
        graphics.fillRect(1, 1, 692, 592);

        //drawing map of bricks
        mapPlay.draw((Graphics2D) graphics, Color.WHITE);

        //border
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, 3, 592);
        graphics.fillRect(0, 0, 692, 3);
        graphics.fillRect(691, 1, 3, 592);

        //score
        graphics.setColor(Color.white);
        graphics.setFont(new Font("serif", Font.BOLD, 22));
        graphics.drawString("Score: " + score + "/200", 490, 30);

        //paddle
        graphics.setColor(Color.green);
        graphics.fillRect(playerX, 550, 100, 8);

        startGameplay(graphics);

        ballColor(graphics);

        wirOrLose(totalBricks <= 0, graphics, "You Win! Score: ");

        wirOrLose(ballPosY > 570, graphics, "Game over! Score: ");

        graphics.dispose();
    }

    private void wirOrLose(boolean totalBricks, Graphics graphics, String x) {
        if (totalBricks) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;

            //hiding the ball after game over
            graphics.setColor(Color.black);
            graphics.fillOval(ballPosX, ballPosY, 23, 23);

            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString(x + score, 200, 300);

            graphics.setColor(Color.YELLOW);
            graphics.setFont(new Font("serif", Font.BOLD, 20));
            graphics.drawString("Press Enter to Restart..", 230, 330);

            //above score hiding
            graphics.setColor(Color.black);
            graphics.setFont(new Font("serif", Font.BOLD, 22));
            graphics.drawString("Score: " + score + "/200", 490, 30);

            //hide remains bricks
            mapPlay.draw((Graphics2D) graphics, Color.BLACK);

            //paddle
            graphics.setColor(Color.black);
            graphics.fillRect(playerX, 550, 100, 8);

            //game start message
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
            graphics.drawString(mensagemStart, 90, 350);
        }
    }

    private void ballColor(Graphics graphics) {
        if (score >= 50 && score < 100) {
            //ball color & size change
            graphics.setColor(Color.yellow);
            graphics.fillOval(ballPosX, ballPosY, 21, 21);
        } else if (score >= 100 && score < 150) {
            //ball
            graphics.setColor(Color.orange);
            graphics.fillOval(ballPosX, ballPosY, 22, 22);
        } else if (score >= 150) {
            //ball
            graphics.setColor(Color.red);
            graphics.fillOval(ballPosX, ballPosY, 23, 23);
        }
    }

    private void startGameplay(Graphics graphics) {
        if (!play) {
            //game start message
            graphics.setColor(Color.YELLOW);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
            graphics.drawString(mensagemStart, 90, 350);

            //ball hiding
            graphics.setColor(Color.black);
            graphics.fillOval(ballPosX, ballPosY, 20, 20);
        } else {
            //ball showing
            graphics.setColor(Color.green);
            graphics.fillOval(ballPosX, ballPosY, 20, 20);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                move(+20);
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                move(-20);
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                playerX = 310;
                ballPosX = 290;
                ballPosY = 350;
                ballDirX = getRandomNumberFor(-5);
                ballDirY = getRandomNumberFor(-3);
                totalBricks = 40;

                mapPlay = new MapGenerator(4, 10);
                score = 0;

                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void move(int move) {
        play = true;
        playerX = playerX + move;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        timer.start();

        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            A:
            for (int i = 0; i < mapPlay.map.length; i++) {
                for (int j = 0; j < mapPlay.map[0].length; j++) {
                    if (mapPlay.map[i][j] > 0) {
                        var brickX = j * mapPlay.brickWidth + 80;
                        var brickY = i * mapPlay.brickHeight + 50;
                        var brickWidth = mapPlay.brickWidth;
                        var brickHeight = mapPlay.brickHeight;

                        var brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        var ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            mapPlay.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPosX += ballDirX;
            ballPosY += ballDirY;

            if (ballPosX < 0) {  //for left
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0) { //for top
                ballDirY = -ballDirY;
            }
            if (ballPosX > 670) { //for right
                ballDirX = -ballDirX;
            }
        }
        repaint();
    }

    public int getRandomNumberFor(int min) 
    {
        var random = new Random();
        int max = -1;
        return min + random.nextInt(max - min + 1);
    }
}
