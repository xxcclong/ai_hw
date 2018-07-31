package game;

import com.sun.javafx.tools.packager.Param;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;
import java.util.Random;


/*
* plain area : 0
* snake1 : 1
* snake2 : 2
* egg : 3
* wall : 4
* hole : 5
*
*
* */


public class Map
{
    int[][]map;
    public int mapSize = Params.mapSize, eggNum = Params.eggNum, wallNum = Params.wallNum, holeNum = Params.holeNum;
    public int iter = 0, formerIter = 0;
    Snake[] s = new Snake[Params.snakeNum];
    Coord[] hole = new Coord[Params.holeNum];
    Coord[] egg = new Coord[Params.eggNum];
    Coord[] wall = new Coord[Params.wallNum];
    int whoAmI = 1;
    boolean gameContinue = true;
    boolean stopped; //= true;
    Socket sock;
    static BufferedReader fromServer;
    static DataOutputStream toServer;



    private void initWall()
    {
        for(int i = 0;i<wallNum;++i)
        {
            int outx = Params.r.nextInt(mapSize);
            int outy = Params.r.nextInt(mapSize);
            while(map[outx][outy] != Symbols.PLAIN)
            {
                outx = Params.r.nextInt(mapSize);
                outy = Params.r.nextInt(mapSize);
            }
            map[outx][outy] = Symbols.WALL;
            wall[i] = new Coord(outx, outy);
            for(int j = 0;j<Params.continueWall;++j)
            {
                int k = 8;
                if(i >= Params.wallNum - 1) break;
                Coord c = wall[i].randomWalk();
                while(--k > 0)
                {
                    if(map[c.x][c.y] == Symbols.PLAIN)
                    {
                        ++i;

                        map[c.x][c.y] = Symbols.WALL;
                        wall[i] = c;
                        break;
                    }
                    else c = wall[i].randomWalk();
                }
                if(k <= 0)
                    break;
                if(i >= Params.wallNum - 1) break;
            }
        }
    }

    public void initEgg()
    {
        eggNum = Params.eggNum;
        for(int i = 0;i<eggNum;++i)
        {
            int outx = Params.r.nextInt(mapSize);
            int outy = Params.r.nextInt(mapSize);
            while(map[outx][outy] != Symbols.PLAIN)
            {
                outx = Params.r.nextInt(mapSize);
                outy = Params.r.nextInt(mapSize);
            }
            map[outx][outy] = Symbols.EGG;
            egg[i] = new Coord(outx, outy);
        }
    }

    public void initSnake() // wait to be randomed
    {
        LinkedList<Coord> b = new LinkedList<Coord>();
        b.add(new Coord(Params.r.nextInt(mapSize),Params.r.nextInt(mapSize)));
        b.add(b.getFirst().randomWalk());
        LinkedList<Direction> bD = new LinkedList<Direction>();
        bD.add(Direction.RIGHT);
        bD.add(Direction.RIGHT);
        s[0] = new Snake(b, Direction.RIGHT, Symbols.SNAKE1, bD);
        LinkedList<Coord> c = new LinkedList<Coord>();
        c.add(new Coord(Params.r.nextInt(mapSize),Params.r.nextInt(mapSize)));
        c.add(c.getFirst().randomWalk());
        LinkedList<Direction> cD = new LinkedList<Direction>();
        cD.add(Direction.RIGHT);
        cD.add(Direction.RIGHT);
        s[1] = new Snake(c, Direction.RIGHT, Symbols.SNAKE2, cD);
    }

    public void initHole()
    {
        for(int i = 0;i<holeNum;++i)
        {
            int outx = Params.r.nextInt(mapSize);
            int outy = Params.r.nextInt(mapSize);
            while(map[outx][outy] != Symbols.PLAIN)
            {
                outx = Params.r.nextInt(mapSize);
                outy = Params.r.nextInt(mapSize);
            }
            map[outx][outy] = Symbols.HOLE;
            hole[i] = new Coord(outx, outy);
        }
    }

    private void setCoord(Coord c, int flag)
    {
        map[c.x][c.y] = flag;
    }

    private int checkPlace(int outx, int outy)
    {
        int t = map[outx][outy];
        switch (t)
        {
            case Symbols.PLAIN:
                return Symbols.PLAIN; // plain area
            case Symbols.EGG:
                return Symbols.EGG; // egg
            case Symbols.HOLE:
                return Symbols.HOLE; // hole
            case Symbols.FAKEHOLE:
                System.out.println("error fake hole");
                return -1;
            default: // fail
            {
                return -1;
            }
        }
    }

    public void gameOver(int id)
    {
        if(!s[id].alive) return;
        s[id].alive = false;
        System.out.println("player" + id + " lost");
        gameContinue = false;
    }

    private void lose(int index)
    {

    }


    private void pingju()
    {

    }

    private int isHole(Coord temp)
    {
        for(int i = 0;i<holeNum;++i)
        {
            if(temp.equals(hole[i]))
                return i;
        }
        return -1;
    }

    public Coord getNext(int i)
    {
        if(s[i].next != null)
            return s[i].next;
        Coord temp = new Coord();
        Coord h = s[i].getHead();
        switch(s[i].dir)
        {
            case LEFT:
                temp.setCoord(h.x - 1, h.y);
                break;
            case RIGHT:
                temp.setCoord(h.x + 1, h.y);
                break;
            case UP:
                temp.setCoord(h.x, h.y - 1);
                break;
            case DOWN:
                temp.setCoord(h.x, h.y + 1);
                break;
            default:
                break;
        }

        if(isHole(temp) >= 0)
        {
            temp = inHole(i, isHole(temp));
        }
        if(map[temp.x][temp.y] == Symbols.FAKEHOLE)
        {
            temp = outHole(i, s[i].whichHole);
            s[i].whichHole = -1;
        }
        return temp;
    }


    public Direction getTailDire(int index)
    {
        Coord tail = s[index].getTail();
        Coord ne = s[index].body.get(1);
        if(ne.isOf(tail) != Direction.NOTDEFINE)
            return ne.isOf(tail);
        for(int i = 0;i<holeNum;++i)
        {
            if(hole[i].isOf(tail) != Direction.NOTDEFINE)
                return hole[i].isOf(tail);
        }
        return Direction.NOTDEFINE;
    }

    public void moveSnakes() // 不涉及改变方向的问题 需要减去
    {
        Coord temp = new Coord();
        for(int i = 0;i<Params.snakeNum;++i)
        {

            temp = getNext(i);
            //s[i].next = null;
            s[i].nextHole = -1;
            int result = checkPlace(temp.x, temp.y);
            if(result == -1) // fail
            {
                if(i == 0 && s[1].getTail().x == temp.x && s[1].getTail().y == temp.y) // the snake 1 will go
                {
                    setCoord(s[i].addBody(temp.x, temp.y), s[i].flag);
                    removeMy(s[i].removeTail(), s[i].flag);
                    continue;
                }
                if(i == 1 && s[0].getHead().x == temp.x && s[0].getHead().y == temp.y )
                {
                    gameOver(0);
                }
                gameOver(i);
            }

            else if(result == Symbols.EGG) // there is egg
            {
                setCoord(s[i].addBody(temp.x, temp.y), s[i].flag);
                s[i].addBodyDir(s[i].dir);
                ++s[i].eggScore;
                --eggNum;
            }
            else if(result == Symbols.PLAIN) // there is nothing
            {
                setCoord(s[i].addBody(temp.x, temp.y), s[i].flag);
                s[i].addBodyDir(s[i].dir);
                removeMy(s[i].removeTail(), s[i].flag);
                s[i].bodyDir.removeFirst();
            }
            else if(result == Symbols.HOLE)
            {
                System.out.println("hole wrong");
            }
            s[i].goed = true;
        }
        if(!s[0].alive && !s[1].alive) pingju();
        else if(!s[0].alive) lose(0);
        else if(!s[1].alive) lose(1);
    }


    private void removeMy(Coord where, int what)
    {
        if(map[where.x][where.y] == what)
            map[where.x][where.y] = Symbols.PLAIN;
    }

    private Coord outHole(int index, int whichHole)
    {
        int newh;
        if(s[index].nextHole >= 0)
            newh = s[index].nextHole;
        else
        {
            newh = Params.r.nextInt(holeNum);
            while (newh == whichHole) newh = Params.r.nextInt(holeNum);
            s[index].nextHole = newh;
            s[index].formerHole = newh;
        }
        // random go into another hole
        // ? if have to wait 1s
        Coord h = hole[newh];
        int i = index;
        Coord temp = h.move(s[i].dir);
        return temp;
    }

    private Coord inHole(int index, int whichHole)
    {
        s[index].whichHole = whichHole;
        s[index].ininHole = whichHole;
        if(index == 0)
            return new Coord(1 + Params.holeSize,1+Params.mapSize + 1 + Params.holeSize).move(s[index].dir);
        return new Coord(1+Params.mapSize + 1 + Params.holeSize,1 + Params.holeSize).move(s[index].dir);
    }

    public Map(int size)
    {
        mapSize = size;
        map = new int[size + 3 + 2*Params.holeSize+1][size + 3 + 2*Params.holeSize+1];
        for(int i = 0;i<size + 3 + 2*Params.holeSize + 1;++i)
            for(int j = 0;j<size + 3 + 2*Params.holeSize + 1; ++j)
            {
                map[i][j] = Symbols.PLAIN;
            }
        map[0][1 + Params.mapSize + 1 + Params.holeSize] = Symbols.FAKEHOLE;
        map[1 + Params.holeSize][1+Params.mapSize] = Symbols.FAKEHOLE;
        map[1 + Params.holeSize][1+Params.mapSize + 2 * Params.holeSize + 2] = Symbols.FAKEHOLE;
        map[2 * Params.holeSize + 2][1+Params.mapSize + 1 + Params.holeSize] = Symbols.FAKEHOLE;
        map[1 + Params.holeSize][1+Params.mapSize + 1 + Params.holeSize] = Symbols.FAKEHOLE;

        map[1+Params.mapSize + 1 + Params.holeSize][0] = Symbols.FAKEHOLE;
        map[1+Params.mapSize][1 + Params.holeSize] = Symbols.FAKEHOLE;
        map[1+Params.mapSize + 2 * Params.holeSize + 2][1 + Params.holeSize] = Symbols.FAKEHOLE;
        map[1+Params.mapSize + 1 + Params.holeSize][2 * Params.holeSize + 2] = Symbols.FAKEHOLE;
        map[1+Params.mapSize + 1 + Params.holeSize][1 + Params.holeSize] = Symbols.FAKEHOLE;

        initSnake();
        initWall();
        initEgg();
        initHole();
        if(Params.ifComm) {
            try {
                sock = new Socket("127.0.0.1", 8888);
                fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                toServer = new DataOutputStream(sock.getOutputStream());
                whoAmI = Integer.parseInt(fromServer.readLine());
                System.out.println("finish init client, id is " + whoAmI);
                stopped = false;
            } catch (IOException e) {
                System.out.println("fail to init sock");

            }
        }
    }

    public void initMap(int size)
    {
        mapSize = size;
        map = new int[size][size];
        for(int i = 0;i<size;++i)
            for(int j = 0;j<size; ++j)
            {
                map[i][j] = 0;
            }
        initSnake();
        initWall();
        initEgg();
        initHole();
    }


    public void printMap()
    {
        for(int i = 0;i<mapSize + Params.holeSize * 2 +1+ 3;++i)
        {
            for (int j = 0; j < mapSize+ Params.holeSize * 2 +1+ 3; ++j)
            {
                if (map[j][i] == 0)
                    System.out.print("* ");
                else if(map[j][i] == 1)
                {
                    if (s[0].getHead().equals(new Coord(j, i)))
                        System.out.print("8 ");
                    else System.out.print("0 ");
                }
                else if(map[j][i] == 2) System.out.print("X ");
                else if(map[j][i] == 5) System.out.print("# ");
                else if(map[j][i] == 4) System.out.print("@ ");
                else if(map[j][i] == 3) System.out.print("E ");
                else if(map[j][i] == 6) System.out.print("~ ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            s[whoAmI].keyPressed(e);
        }
    }



    static public void main(String[] Args)
    {
        GameArea g = new GameArea();
    }

}
