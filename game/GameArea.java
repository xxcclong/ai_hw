package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GameArea extends JFrame
{
    GamePanel p = new GamePanel();
    Thread panelThread = new Thread(p);

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE) // stop the game
            {
                p.m.stopped = !p.m.stopped;
                System.out.println("space");
                return;
            }
            if(e.getKeyCode() == KeyEvent.VK_F1) // stop the game
            {
                restart();
                System.out.println("f1");
                return;
            }
            if(p.picIter % Params.fluency > 1)
            {
                p.e = e;
                return;
            }
            p.m.s[p.m.whoAmI].keyPressed(e);
        }
    }


    public void restart()
    {
        panelThread.interrupt();
        p.m.initMap(Params.mapSize);
        panelThread.start();
    }
    GameArea()
    {
        super("tanchishe");
        this.add(p);
        this.setSize(Params.panelSize, Params.panelSize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addKeyListener(new KeyMonitor());
        panelThread.start();
    }
}
