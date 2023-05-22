package com.company;

import java.awt.*;
import javax.swing.*;
import java.util.Arrays;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameField extends JPanel implements ActionListener {
    private int w = Toolkit.getDefaultToolkit().getScreenSize().width; //1920
    private int h = Toolkit.getDefaultToolkit().getScreenSize().height; //1080
    private Font font = new Font("Arial", Font.BOLD, 40);

    private boolean inGame = true;
    private Timer timer;

    private int countWall = 30;
    private Image wall;
    private int[] wallX = new int[countWall];
    private int[] wallY = new int[countWall];
    // for collision with wall
    private boolean checkUp = false;
    private boolean checkDown = false;
    private boolean checkLeft = false;
    private boolean checkRight = false;

    private Image tank_up;
    private Image tank_down;
    private Image tank_left;
    private Image tank_right;
    private Image tankDirection;

    private Image shoot;
    private boolean shootBoolean = false;
    private Image laserHorizon;
    private Image laserVertical;

    private int countHelth = 3;
    private Image health;

    private int countBush = 15;
    private Image bush;
    private int[] bushX = new int[countBush];
    private int[] bushY = new int[countBush];

    private int x = 480;
    private int y = 32;

    public GameField() {
        initGame();

        loadImage();

        generateCoordinateWall_1();
        generateCoordinateBush();

        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame() {
        timer = new Timer(60, this);
        timer.start();
    }

    public void loadImage() {
        ImageIcon iitu = new ImageIcon("src/Tank_up.png");
        tank_up = iitu.getImage();
        ImageIcon iitd = new ImageIcon("src/Tank_down.png");
        tank_down = iitd.getImage();
        ImageIcon iitl = new ImageIcon("src/Tank_left.png");
        tank_left = iitl.getImage();
        ImageIcon iitr = new ImageIcon("src/Tank_right.png");
        tank_right = iitr.getImage();

        ImageIcon iis = new ImageIcon("src/Shoot.png");
        shoot = iis.getImage();
        ImageIcon iilh = new ImageIcon("src/Laser_horizon.png");
        laserHorizon = iilh.getImage();
        ImageIcon iilv = new ImageIcon("src/Laser_vertical.png");
        laserVertical = iilv.getImage();

        ImageIcon iiw = new ImageIcon("src/Wall.png");
        wall = iiw.getImage();

        ImageIcon iib = new ImageIcon("src/Bush.png");
        bush = iib.getImage();

        ImageIcon iih = new ImageIcon("src/Health.png");
        health = iih.getImage();

        tankDirection = tank_down;
    }

    public void generateCoordinateWall_1() {
        for (int i = 0; i < countWall; i++)
            generateCoordinateWall_2(i);

        for (int i = 0; i < countWall; i++)
            for (int j = 0; j < countWall; j++)
                if (wallX[i] == wallX[j] && wallY[i] == wallY[j] && i != j)
                    generateCoordinateWall_2(i);

        boolean sorted = true;

        while (sorted) {
            sorted = false;

            for (int i = 0; i < countWall; i++)
                for (int j = 0; j < countWall; j++)
                    if (wallX[i] == wallX[j] && wallY[i] == wallY[j] && i != j) {
                        generateCoordinateWall_2(i);
                        sorted = true;
                    }
        }

        Arrays.sort(wallX);
        /*Arrays.sort(wallY);*/
    }

    public void generateCoordinateWall_2(int i) {
        // ot 15 do 45
        int randomX = (int) (Math.random() * (45 - 15) + 15) * 32;
        // ot 1 do 31
        int randomY = (int) (Math.random() * (31 - 1) + 1) * 32;

        wallX[i] = randomX;
        wallY[i] = randomY;
    }

    public void generateCoordinateBush() {
        for (int i = 0; i < countBush; i++) {
            // ot 15 do 45
            int randomX = (int) (Math.random() * (45 - 15) + 15) * 32;
            // ot 1 do 31
            int randomY = (int) (Math.random() * (31 - 1) + 1) * 32;

            bushX[i] = randomX;
            bushY[i] = randomY;
        }

        Arrays.sort(wallX);
        /*Arrays.sort(wallY);*/
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, w, h);
        super.paintComponents(g);

        if (inGame) {
            g.setColor(Color.gray);
            g.fillRect(0, 0, w, h);
            g.setColor(Color.ORANGE);
            g.fillRect(480, 32, 960, 960);

            // health *************************************************
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawString("Health: ", 32, 64);
            for (int i = 0; i < countHelth; i++)
                g.drawImage(health, 180 + i * 40, 32, null);

            // wall **************************************************
            for (int i = 0; i < countWall; i++)
                g.drawImage(wall, wallX[i], wallY[i], this);

            // tank **************************************************
            g.drawImage(tankDirection, x, y, this);

            // shoot *************************************************
            shoot(g);

            // bush **************************************************
            for (int i = 0; i < countBush; i++)
                g.drawImage(bush, bushX[i], bushY[i], this);
        }
    }

    public void shoot(Graphics g) {
        int object_left = 448;
        int object_right = 1440;
        int object_down = 992;
        int object_up = 32 - 32;

        if (shootBoolean) {
            // left and right *****************************************
            for (int i = 0; i < countWall; i++)
                if (wallY[i] == y) {
                    if (wallX[i] < x && wallX[i] > object_left)
                        object_left = wallX[i];

                    if (wallX[i] > x && wallX[i] < object_right)
                        object_right = wallX[i];
                }

            if (tankDirection == tank_left) {
                for (int i = x - 32; i > object_left; i -= 32)
                    g.drawImage(laserHorizon, i, y, this);
                shootBoolean = false;
            }

            if (tankDirection == tank_right) {
                for (int i = x + 32; i < object_right; i += 32)
                    g.drawImage(laserHorizon, i, y, this);
                shootBoolean = false;
            }

            // up and down ********************************************
            for (int i = 0; i < countWall; i++)
                if (wallX[i] == x) {
                    if (wallY[i] < y && wallY[i] > object_up)
                        object_up = wallY[i];

                    if (wallY[i] > y && wallY[i] < object_down)
                        object_down = wallY[i];
                }

            if (tankDirection == tank_up) {
                for (int i = y - 32; i > object_up; i -= 32)
                    g.drawImage(laserVertical, x, i, this);
                shootBoolean = false;
            }

            if (tankDirection == tank_down) {
                for (int i = y + 32; i < object_down; i += 32)
                    g.drawImage(laserVertical, x, i, this);
                shootBoolean = false;
            }
        }
    }

    public void checkCollisions() {
        for (int i = 0; i < countWall; i++) {
            // left ************************************
            if (wallY[i] == y)
                if (x - wallX[i] == 32)
                    checkLeft = true;

            // right ***********************************
            if (wallY[i] == y)
                if (wallX[i] - x == 32)
                    checkRight = true;

            // down ************************************
            if (wallX[i] == x)
                if (wallY[i] - y == 32)
                    checkDown = true;

            // up **************************************
            if (wallX[i] == x)
                if (y - wallY[i] == 32)
                    checkUp = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkCollisions();
        }

        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            if (e.getKeyCode() == KeyEvent.VK_LEFT && x != 480 && !checkLeft) {
                x -= 32;
                tankDirection = tank_left;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && x != 1408 && !checkRight) {
                x += 32;
                tankDirection = tank_right;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP && y != 32 && !checkUp) {
                y -= 32;
                tankDirection = tank_up;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN && y != 960 && !checkDown) {
                y += 32;
                tankDirection = tank_down;
            }
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                shootBoolean = true;
            }

            checkLeft = false;
            checkRight = false;
            checkUp = false;
            checkDown = false;
        }
    }
}

