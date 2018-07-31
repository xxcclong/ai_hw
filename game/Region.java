package game;

public class Region
{
    int x,y,w,h;
    public Region()
    {
        x=0;
        y=0;
        w=0;
        h=0;
    }

    public Region(int outx, int outy, int outw, int outh)
    {
        x=outx;
        y=outy;
        w=outw;
        h=outh;
    }

    public void setRegion(int outx, int outy, int outw, int outh)
    {
        x=outx;
        y=outy;
        w=outw;
        h=outh;
    }
}
