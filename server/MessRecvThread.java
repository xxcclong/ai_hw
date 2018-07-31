package server;

public class MessRecvThread implements Runnable
{
    MessGetter ms;
    int id;
    public MessRecvThread(int out, MessGetter outm)
    {
        id = out;
        ms = outm;
    }
    public void run()
    {
        while(true)
        {
            ms.get(id);
        }
    }
}
