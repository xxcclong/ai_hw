package server;

import java.io.IOException;

class MessGetter {
    public void get(int id) {
        try {
            if (id == 0)
                Server.mess = Integer.parseInt(ServerThread.fromClient1.readLine());
            else
                Server.mess = Integer.parseInt(ServerThread.fromClient2.readLine());
        } catch (Exception e) {
            System.out.println("fail to recv from id " + id);
        }
        System.out.println("recv from id " + id);
        notify();
    }

    public void send() {
        try {
            wait();
            System.out.println("finish waiting");
        } catch (Exception e) {
        }
        try {
            ServerThread.toClient1.writeBytes("" + Server.mess);
            ServerThread.toClient2.writeBytes("" + Server.mess);
        } catch (IOException e) {
        }
    }
}
