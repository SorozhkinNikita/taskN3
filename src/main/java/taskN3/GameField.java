package taskN3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int WIDTH = 800;
    private final int PC_Y = 465;
    private final int ALL_SOFTWARES = 7;
    private int delay;
    private int score;
    private LinkedList<Integer> queue = new LinkedList<>();
    private boolean inGame = true;
    private int pcX;
    private int[] softwareX = new int[ALL_SOFTWARES];
    private int[] softwareY = new int[ALL_SOFTWARES];
    private Image background;
    private Image pc;
    private Image endGame;
    private Image[] images = new Image[ALL_SOFTWARES];
    private boolean[] onDesk = new boolean[ALL_SOFTWARES];
    private int timeToRespawn = 110;

    GameField(int difficulty) {
        timeToRespawn -= difficulty*10;
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    private void initGame() {
        delay = 0;
        score = 0;
        pcX = WIDTH / 2;
        Timer timer = new Timer(25, this);
        timer.start();
        createSoftware();
    }

    private void createSoftware() {
        int num;
        do {
            num = new Random().nextInt(ALL_SOFTWARES);
        } while (onDesk[num]);
        int HEIGHT = 600;
        softwareX[num] = new Random().nextInt(HEIGHT);
        softwareY[num] = 0;
        queue.add(num);
        onDesk[num] = true;
    }

    private void loadImages() {
        ImageIcon iib = new ImageIcon("images/fon.png");
        background = iib.getImage();
        ImageIcon iipc = new ImageIcon("images/pc.png");
        pc = iipc.getImage();
        ImageIcon iie = new ImageIcon("images/end_game.png");
        endGame = iie.getImage();
        for (int i = 0; i < 7; i++) {
            ImageIcon iip = new ImageIcon("images/p" + i + ".png");
            images[i] = iip.getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(background, 0, 0, this);
            g.drawImage(pc, pcX, PC_Y, this);
            for (int i = 0; i < ALL_SOFTWARES; i++) {
                if (onDesk[i])
                    g.drawImage(images[i], softwareX[i], softwareY[i], this);
            }
        } else {
            g.drawImage(endGame, 0, 0, this);
        }
    }

    //при вызове происходит проверка на пустоту очереди, так что ошибки быть не может быть
    private void caught() {
        int PC_WIDTH = 100;
        if (softwareY[queue.peekFirst()] >= PC_Y- PC_WIDTH /2) {
            int PC_HEIGHT = 108;
            if (softwareX[queue.peekFirst()] > pcX - PC_HEIGHT / 2 &&
                    softwareX[queue.peekFirst()] < pcX + PC_HEIGHT * 1.5) {
                onDesk[queue.pollFirst()] = false;
                score++;
            } else {
                JOptionPane.showMessageDialog(null, "Ваш счет: " + score);
                inGame = false;
            }
        }
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

            int pcWidthPart = 48;
            int step = 20;
            if (key == KeyEvent.VK_ESCAPE) System.exit(0);
            else if (key == KeyEvent.VK_LEFT) {
                if (pcX - step > -pcWidthPart) pcX -= step;
                else pcX = WIDTH - pcWidthPart;
            } else if (key == KeyEvent.VK_RIGHT) {
                if (pcX + step < WIDTH - pcWidthPart) pcX += step;
                else pcX = -pcWidthPart;
            } else if (key == KeyEvent.VK_DOWN && queue.peek()!=null) softwareY[queue.peekFirst()] += step;
        }
    }

    private void move() {
        for (int i = 0; i < ALL_SOFTWARES; i++) {
            if (onDesk[i])
                softwareY[i] += 3;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            if (queue.peek()!=null) caught();
            move();
            delay++;
            if (delay == timeToRespawn-score) {
                createSoftware();
                delay = 0;
            }
        }
        repaint();
    }
}
