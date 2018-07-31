package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    ServerSocket ss;
    Socket[] s;
    static int mess = -1;
    public Server()
    {
        try {
            ss = new ServerSocket(8888);
            s = new Socket[2];
        } catch (IOException e){}
    }
}
