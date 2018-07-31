package game;

import javafx.scene.layout.CornerRadii;

public class Coord
{
    int x = 0, y = 0;
    int limit = Params.mapSize;

    public boolean inTheHole()
    {
        if(x >= Params.mapSize || y >= Params.mapSize)
        {
            return true;
        }
        return false;
    }

    public Coord()
    {
        x = -1;
        y = -1;
    }

    public Coord(int outx, int outy)
    {
        x = outx;
        y = outy;
    }

    public Coord(int outx, int outy, int outlimit)
    {
        x = outx;
        y = outy;
        limit = outlimit;
    }

    public void setCoord(int outx, int outy)
    {
        if(outx == -1) outx = limit - 1;
        if(outy == -1) outy = limit - 1;
        if(outx <= limit) outx = outx % limit;
        if(outy <= limit) outy = outy % limit;
        x = outx;
        y = outy;
    }

    public boolean equals(Coord out)
    {
        if(out.x == x && out.y == y)
            return true;
        else return false;
    }

    public Coord move(Direction dir)
    {
        Coord temp = new Coord();
        Coord h = this;
        switch (dir)
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
        return temp;
    }

    public Direction isOf(Coord out)
    {
        if(x == out.x && y % Params.mapSize == (out.y - 1 + Params.mapSize)%Params.mapSize)
            return Direction.UP;
        if(x == out.x && (y)%Params.mapSize == (out.y + 1)%Params.mapSize)
            return Direction.DOWN;
        if(x%Params.mapSize == (out.x - 1 + Params.mapSize)%Params.mapSize && y == out.y)
            return Direction.LEFT;
        if(x%Params.mapSize == (out.x + 1)%Params.mapSize && y == out.y)
            return Direction.RIGHT;
        return Direction.NOTDEFINE;
    }

    public Coord randomWalk()
    {
        Coord h = this;
        Coord temp = new Coord();
        switch (Params.r.nextInt(4))
        {
            case 0:
                temp.setCoord(h.x - 1, h.y);
                break;
            case 1:
                temp.setCoord(h.x + 1, h.y);
                break;
            case 2:
                temp.setCoord(h.x, h.y - 1);
                break;
            case 3:
                temp.setCoord(h.x, h.y + 1);
                break;
            default:
                break;
        }
        return temp;
    }

}
