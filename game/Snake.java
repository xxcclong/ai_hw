package game;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;

public class Snake
{
    LinkedList<Coord> body = new LinkedList<Coord>();
    LinkedList<Direction> bodyDir;
    Direction dir = Direction.NOTDEFINE;
    int flag = Symbols.PLAIN;
    boolean goed = false;
    boolean alive = true;
    Coord next = null;
    int nextHole = -1;
    int whichHole = -1;
    int formerHole = -1;
    int ininHole = -1;
    int eggScore = 0;


    public Coord getHead()
    {
        return body.getLast();
    }
    public Coord getTail() { return body.getFirst(); }

    public Snake(LinkedList<Coord> b, Direction d, int f, LinkedList<Direction> bD)
    {
        body = b;
        dir = d;
        flag = f;
        eggScore = 0;
        bodyDir = bD;
    }

    public Coord addBody(int x, int y)
    {
        body.add(new Coord(x,y));
        return new Coord(x, y);
    }

    public void addBodyDir(Direction d)
    {
        bodyDir.add(d);
    }

    public Coord removeTail()
    {
        return body.removeFirst();
    }

    public void keyPressed(KeyEvent ev)
    {
        int k = ev.getKeyCode();
        if(!goed) return;
        if(whichHole >= 0) return;
        if(flag == Symbols.SNAKE1)
        {
            switch (k) {
                case KeyEvent.VK_LEFT:
                    if (dir != Direction.RIGHT && goed)
                    {
                        if(dir != Direction.LEFT)
                        {
                            //goed = false;
                            try {

                                Map.toServer.writeBytes("1" + "\n");

                            } catch (IOException e){}

                        }
                        //dir = Direction.LEFT;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (dir != Direction.LEFT && goed) {
                        if(dir != Direction.RIGHT)
                        {
                            try {
                                Map.toServer.writeBytes("2\n");
                            } catch (IOException e){}
                        }
                            //goed = false;
                        //dir = Direction.RIGHT;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (dir != Direction.DOWN && goed) {
                        if(dir != Direction.UP)
                        {
                            try {
                                Map.toServer.writeBytes("3" + "\n");
                                System.out.println("send 3 " + Symbols.SNAKE1);
                            } catch (IOException e){System.out.println("fail to send 3 1");}
                        }
                        //dir = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (dir != Direction.UP && goed) {
                        if(dir != Direction.DOWN)
                        {
                            try {
                                Map.toServer.writeBytes("4\n");
                            } catch (IOException e){}
                        }
                        //dir = Direction.DOWN;
                    }
                default:
                    break;
            }
        }
        else if(flag == Symbols.SNAKE2)
        {
            switch (k)
            {
                case KeyEvent.VK_A:
                    if (dir != Direction.RIGHT && goed)
                    {
                        if(dir != Direction.LEFT)
                        {
                            try {
                                Map.toServer.writeBytes("11\n");
                            } catch (IOException e){}
                        }
                        //dir = Direction.LEFT;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (dir != Direction.LEFT && goed) {
                        if(dir != Direction.RIGHT)
                        {
                            try {
                                Map.toServer.writeBytes("12\n");
                            } catch (IOException e){}
                        }
                        //dir = Direction.RIGHT;
                    }
                    break;
                case KeyEvent.VK_W:
                    if (dir != Direction.DOWN && goed) {
                        if(dir != Direction.UP)
                        {
                            try {

                                Map.toServer.writeBytes("13\n");
                                System.out.println("send 13 " + Symbols.SNAKE2);
                            } catch (IOException e){System.out.println("fail to send 13 2");}
                        }
                        //dir = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (dir != Direction.UP && goed) {
                        if(dir != Direction.DOWN)
                        {
                            try {
                                Map.toServer.writeBytes("14\n");
                            } catch (IOException e){}
                        }
                        //dir = Direction.DOWN;
                    }
                default:
                    break;
            }
        }
    }
}

