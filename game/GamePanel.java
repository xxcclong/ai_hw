package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.renderable.RenderableImage;
import java.io.DataInput;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable
{
    Image bg, head1, bodyPic, eggPic, holePic, bodyPic2, head2, wallPic;
    Map m;
    int picIter = 0;
    KeyEvent e = null;
    public GamePanel()
    {

        try
        {
            bg = ImageIO.read(new File("bg.png"));
            head1 = ImageIO.read(new File("head1.png"));
            bodyPic = ImageIO.read(new File("bodyPic.png"));
            eggPic = ImageIO.read(new File("eggPic.png"));
            holePic = ImageIO.read(new File("holePic.png"));
            bodyPic2 = ImageIO.read(new File("bodyPic2.png"));
            head2 = ImageIO.read(new File("head2.png"));
            wallPic = ImageIO.read(new File("wallPic.png"));
        }
        catch (IOException e)
        {
            System.out.println("error load image");
        }
        m = new Map(Params.mapSize);
    }

    private void drawHead(Image i, Region r, Graphics g)
    {
        //System.out.println(r.x + " " + r.y + " "+ r.w + " " + r.h);
        if(r.x + r.w > Params.panelSize)
        {
            g.drawImage(i, r.x, r.y, Params.gridSize, r.h, this);
            g.drawImage(i, 0, r.y,r.w - Params.gridSize, r.h, this);
            return;
        }
        if(r.x < 0)
        {
            g.drawImage(i, Params.panelSize + r.x , r.y, -r.x, r.h, this);
            g.drawImage(i, 0, r.y, Params.gridSize, r.h,this);
            return;
        }
        if(r.y+r.h > Params.panelSize)
        {
            g.drawImage(i, r.x, r.y, r.w, Params.gridSize,this);
            g.drawImage(i, r.x, 0, r.w, r.h - Params.gridSize,this);
            return;
        }
        if(r.y < 0)
        {
            g.drawImage(i, r.x, Params.panelSize + r.y, r.w, -r.y,this);
            g.drawImage(i, r.x, 0, r.w, Params.gridSize,this);
            return;
        }
        g.drawImage(i, r.x,r.y,r.w, r.h, this);
    }

    public Region setRegion(int index, Coord temp) // for head
    {
        int newx = temp.x;
        int newy = temp.y;
        Region r = new Region();
        switch (m.s[index].dir)
        {
            case UP:
                r.setRegion(newx * Params.gridSize, (newy + 1) * Params.gridSize - (picIter % Params.fluency)*Params.smallGridSize, Params.gridSize, (picIter % Params.fluency)*Params.smallGridSize);
                break;
            case DOWN:
                r.setRegion(newx * Params.gridSize, newy * Params.gridSize, Params.gridSize, (picIter % Params.fluency)*Params.smallGridSize);
                break;
            case RIGHT:
                r.setRegion(newx * Params.gridSize, newy * Params.gridSize , (picIter % Params.fluency)*Params.smallGridSize, Params.gridSize);
                break;
            case LEFT:
                r.setRegion((newx + 1) * Params.gridSize - (picIter % Params.fluency)*Params.smallGridSize, newy * Params.gridSize , (picIter % Params.fluency)*Params.smallGridSize, Params.gridSize);
                break;
            default:
                break;
        }
        return r;
    }


    private boolean ifRotate(Direction d1, Direction d2)
    {
        if(d1 == Direction.UP && d2 == Direction.DOWN)
            return false;
        if(d1 == Direction.DOWN && d2 == Direction.UP)
            return false;
        if(d1 == Direction.RIGHT&& d2 == Direction.LEFT)
            return false;
        if(d1 == Direction.LEFT&& d2 == Direction.RIGHT)
            return false;
        return true;

    }

    public Region setTailRegion(int index, Coord temp)
    {
        Region r = new Region();
        int newx = temp.x;
        int newy = temp.y;
        switch (m.getTailDire(index))
        {
            case RIGHT:
                r.setRegion(newx * Params.gridSize + (picIter % Params.fluency) * Params.smallGridSize,newy * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, Params.gridSize - (picIter % Params.fluency) * Params.smallGridSize, Params.thinGridSize );
                break;
            case LEFT:
                r.setRegion(newx * Params.gridSize, newy * Params.gridSize+ (Params.gridSize - Params.thinGridSize) / 2, Params.gridSize - (picIter % Params.fluency) * Params.smallGridSize, Params.thinGridSize);
                break;
            case UP:
                r.setRegion(newx * Params.gridSize+ (Params.gridSize - Params.thinGridSize) / 2, newy * Params.gridSize, Params.thinGridSize , Params.gridSize - (picIter % Params.fluency) * Params.smallGridSize);
                break;
            case DOWN:
                r.setRegion(newx * Params.gridSize+ (Params.gridSize - Params.thinGridSize) / 2, newy * Params.gridSize + (picIter % Params.fluency) * Params.smallGridSize, Params.thinGridSize, Params.gridSize - (picIter % Params.fluency) * Params.smallGridSize);
                break;
            default:
                break;
        }
        return r;
    }

    public void drawSnake(int index, Graphics g)
    {
        for(int i = 0;i<m.s[index].body.size();++i)
        {
            if(i == 0) // tail
            {
                Region r = setTailRegion(0, m.s[index].getTail());
                drawHead(bodyPic, r, g);
            }
            else if(i == m.s[index].body.size() - 1)
            {
                Region r;
                g.drawImage(head1, m.s[index].body.get(i).x  * Params.gridSize , m.s[index].body.get(i).y  * Params.gridSize, Params.gridSize,Params.gridSize, this);
                Coord temp = m.getNext(0);
                r = setRegion(0, temp);
                drawHead(head1, r, g);
            }
            else
            {
                if(m.s[index].body.get(i).x >= Params.mapSize || m.s[index].body.get(i).y >= Params.mapSize)
                    continue;
                Direction d1, d2;
                if(m.s[index].body.get(i - 1).inTheHole())
                {

                    d1 = m.hole[m.s[index].formerHole].isOf(m.s[index].body.get(i));
                }
                else
                    d1 = m.s[index].body.get(i - 1).isOf(m.s[index].body.get(i));
                if(m.s[index].body.get(i + 1).inTheHole())
                {
                    d2 = m.hole[m.s[index].ininHole].isOf((m.s[index].body.get(i)));
                }
                else
                    d2 = m.s[index].body.get(i + 1).isOf(m.s[index].body.get(i));
                if((d1 == Direction.UP && d2 == Direction.LEFT) || (d1 == Direction.LEFT && d2 == Direction.UP))
                {
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, Params.thinGridSize, Params.thinGridSize, this);
                    //up
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize , Params.thinGridSize, (Params.gridSize - Params.thinGridSize) / 2, this);
                    //left
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize , m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, (Params.gridSize - Params.thinGridSize) / 2, Params.thinGridSize, this);
                }
                else if((d1 == Direction.UP && d2 == Direction.RIGHT) || (d1 == Direction.RIGHT && d2 == Direction.UP))
                {
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, Params.thinGridSize, Params.thinGridSize, this);
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize , Params.thinGridSize, (Params.gridSize - Params.thinGridSize) / 2, this);
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize + Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2,(Params.gridSize - Params.thinGridSize) / 2,Params.thinGridSize,this);
                }
                else if((d1 == Direction.DOWN && d2 == Direction.RIGHT) || (d1 == Direction.RIGHT && d2 == Direction.DOWN))
                {
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, Params.thinGridSize, Params.thinGridSize, this);
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize + Params.thinGridSize) / 2, Params.thinGridSize, (Params.gridSize - Params.thinGridSize) / 2, this);
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize + Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2,(Params.gridSize - Params.thinGridSize) / 2, Params.thinGridSize,this);
                }
                else if((d1 == Direction.DOWN && d2 == Direction.LEFT) || (d1 == Direction.LEFT && d2 == Direction.DOWN))
                {
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, Params.thinGridSize, Params.thinGridSize, this);
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize +  (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize + Params.thinGridSize) / 2, Params.thinGridSize, (Params.gridSize - Params.thinGridSize) / 2, this);
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize , m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, (Params.gridSize - Params.thinGridSize) / 2, Params.thinGridSize, this);
                }
                else if(d1 == Direction.DOWN || d1 == Direction.UP)
                {
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, m.s[index].body.get(i).y * Params.gridSize, Params.thinGridSize, Params.gridSize, this);

                }
                else
                {
                    g.drawImage(bodyPic, m.s[index].body.get(i).x * Params.gridSize, m.s[index].body.get(i).y * Params.gridSize + (Params.gridSize - Params.thinGridSize) / 2, Params.gridSize, Params.thinGridSize, this);
                }
            }
        }
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(bg,0,0,Params.panelSize, Params.panelSize,this);
        drawSnake(0, g);
        for(int i = 0;i<m.mapSize;++i)
            for(int j = 0;j<m.mapSize;++j)
            {
                if(m.map[i][j] == Symbols.EGG)
                    g.drawImage(eggPic, i * Params.gridSize, j * Params.gridSize, Params.gridSize,Params.gridSize, this);
                else if(m.map[i][j] == Symbols.HOLE)
                    g.drawImage(holePic, i * Params.gridSize , j * Params.gridSize, Params.gridSize,Params.gridSize, this);
                else if(m.map[i][j] == Symbols.WALL)
                    g.drawImage(wallPic, i * Params.gridSize , j * Params.gridSize, Params.gridSize,Params.gridSize, this);

                else if(m.map[i][j] == Symbols.SNAKE2)
                {
                    if(m.s[1].getHead().x == i && m.s[1].getHead().y == j)
                    {
                        Region r;
                        g.drawImage(head2, i * Params.gridSize , j * Params.gridSize, Params.gridSize,Params.gridSize, this);
                        Coord temp = m.getNext(1);
                        r = setRegion(1, temp);
                        drawHead(head2, r, g);
                    }
                    else if(m.s[1].getTail().x == i && m.s[1].getTail().y == j)
                    {

                        Region r = setTailRegion(1, m.s[1].getTail());
                        drawHead(bodyPic2, r, g);
                    }
                    else g.drawImage(bodyPic2, i * Params.gridSize , j * Params.gridSize, Params.gridSize,Params.gridSize, this);
                }
            }
    }


    public void run()
    {
        Thread commT = new Thread(new CommThread(m));
        commT.start();
        while(m.gameContinue)
        {
            if(!m.stopped) {
                if ((picIter % Params.fluency) == 0) {
                //    Map.toServer.writeBytes("3\n");
                    m.moveSnakes();
                    //m.printMap();
                    if (e != null) {
                        m.s[m.whoAmI].keyPressed(e);
                        e = null;
                    }
                    ++m.iter;
                    if (m.eggNum == 0 && m.iter - m.formerIter > 2)
                        m.formerIter = m.iter;
                    if ((m.iter - m.formerIter) == 2 && m.formerIter != 0) {
                        m.initEgg();
                        m.formerIter = 0;
                    }
                }
                this.repaint();
            }
            try
            {
                Thread.sleep(Params.waitTime);
                if(!m.stopped)
                    ++picIter;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
