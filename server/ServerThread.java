package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerThread
{
    Server server;
    static DataOutputStream toClient1, toClient2;
    static BufferedReader fromClient1, fromClient2;
    public ServerThread()
    {
        try {
            server = new Server();
            server.s[0] = server.ss.accept();
            toClient1 = new DataOutputStream(server.s[0].getOutputStream());
            server.s[1] = server.ss.accept();
            toClient2 = new DataOutputStream(server.s[1].getOutputStream());

            fromClient1 = new BufferedReader(new InputStreamReader(server.s[0].getInputStream()));
            fromClient2 = new BufferedReader(new InputStreamReader(server.s[1].getInputStream()));
            toClient1.writeBytes("" + 0 + "\n");
            toClient2.writeBytes("" + 1+ "\n");
            System.out.println("finish init server");
        }catch (IOException e){}
    }

}
