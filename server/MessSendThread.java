package server;

public class MessSendThread implements Runnable
{
    MessGetter ms;
    public MessSendThread(MessGetter outm)
    {
        ms = outm;
    }
    public void run()
    {
        while(true) {
            ms.send();
        }
    }
}
