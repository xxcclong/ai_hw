package game;


public class CommThread implements Runnable
{
    Map m;
    public CommThread(Map outm)
    {
        m = outm;
    }
    public void run()
    {
        try {
            while(true)
            {
                int mess = Integer.parseInt(Map.fromServer.readLine());
                int which = mess / 10;
                mess -= which * 10;
                switch (mess)
                {
                    case 1:
                        m.s[which].goed = false;
                        m.s[which].dir = Direction.LEFT;
                        break;
                    case 2:
                        m.s[which].goed = false;
                        m.s[which].dir = Direction.RIGHT;
                        break;
                    case 3:
                        m.s[which].goed = false;
                        m.s[which].dir = Direction.UP;
                        break;
                    case 4:
                        m.s[which].goed = false;
                        m.s[which].dir = Direction.DOWN;
                        break;
                    default:
                        System.out.println(mess);
                        break;
                }
            }

        } catch (Exception e) {
        }
    }
}
