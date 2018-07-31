package server;

public class Test
{
    static public void main(String[] Args)
    {
        ServerThread st = new ServerThread();
        MessGetter outm = new MessGetter();
        Thread mr1 = new Thread(new MessRecvThread(0, outm));
        mr1.start();
        Thread mr2 = new Thread(new MessRecvThread(1, outm));
        mr2.start();
        Thread ms = new Thread(new MessSendThread(outm));
        ms.start();
    }
}
