package game;

import java.awt.*;
import java.util.Random;

public class Params
{
    static public final int mapSize = 24;
    static public final int holeNum = 4;
    static public final int eggNum = 2;
    static public final int wallNum = 0;
    static public final int waitTime = 10;
    static public final int panelSize = 720;
    static public final int gridSize = panelSize / mapSize;
    static public final int snakeNum = 2;
    static public final int fluency = 10;
    static public final int smallGridSize = gridSize / fluency;
    static public final Random r = new Random();
    static public final int continueWall = 9;
    static public final int holeSize = 5;
    static public final int thinGridSize = (int)(0.8  * gridSize);
    static public boolean ifComm = false;
}

