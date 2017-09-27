package server.msg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dao.ConnectionDao;

public class MsgServerThread implements Runnable {
    private String myId;
    private String otherId;

    private Socket socket;
    private MsgServer ms;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public MsgServerThread(MsgServer ms) {
        this.ms = ms;
    }

    public synchronized void run() {
        boolean isStop = false;
        try {
            socket = ms.getSocket();
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            Object rcvObject = null;
            String message = null;
            while(!isStop) {
                rcvObject = ois.readObject();
                message = (String)rcvObject;

                if(message.startsWith("[LOGIN]")) {
                    String userId = message.substring("[LOGIN]".length()); // ���̵� ����
                    myId = userId;
                    ConnectionDao.getInstance().loginConnection(myId, socket.getInetAddress().getHostAddress());
                } else if(message.startsWith("[EXIT]")) {
                    isStop = true;
                } else if(message.startsWith("[CONNECT_REQUEST]")) {
                    String[] idArr = message.substring("[CONNECT_REQUEST]".length()).split("#"); // ���̵��
                                                                                                 // 0: �����ϴ� ���̵�
                                                                                                 // 1: ���� ���� ���̵�
                                                                                                 // 2: �����ϴ� ���� �������ּ�
                    otherId = idArr[1];
                    sendToOther(idArr[1], message + "#" + socket.getInetAddress().getHostAddress());
                } else if(message.startsWith("[CONNECT_RESPONSE]")) {
                    String[] msgArr = message.substring("[CONNECT_RESPONSE]".length()).split("#"); // �޼��� �з�
                                                                                                   // 0: �����
                                                                                                   // 1: ���� ���� ���̵�
                    otherId = msgArr[1];
                    sendToOther(msgArr[1], message);
                } else if(message.startsWith("[DISCONNECT]")) {
                    String disconnectId = message.substring("[DISCONNECT]".length());
                    sendToOther(disconnectId, "[DISCONNECT]");
                } else {
                    // ���콺 ����, Ű���� ���� ��
                    sendToOther(otherId, message);
                }
            }
            System.out.println(socket.getInetAddress() + " " + myId + " ���������� �����ϼ̽��ϴ�");
            System.out.println("list size : " + ms.getList().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(socket.getInetAddress() + " " + myId + "������������ �����ϼ̽��ϴ�");
            System.out.println("list size : " + ms.getList().size());
        } finally {
            ConnectionDao.getInstance().exitConnection(myId);
            ms.getList().remove(this);

        }
    }

    public void sendToOther(String toId, Object obj) throws IOException {
        for(MsgServerThread ct : ms.getList()) {
            if(ct.myId.equals(toId)) {
                ct.send(obj);
                break;
            }
        }
    }

    public void broadCasting(String message) throws IOException {
        for(MsgServerThread ct : ms.getList()) {
            ct.send(message);
        }
    }

    public void send(String message) throws IOException {
        oos.writeObject(message);
    }

    public void send(Object message) throws IOException {
        oos.writeObject(message);
    }

}
