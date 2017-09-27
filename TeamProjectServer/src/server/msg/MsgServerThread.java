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
                    String userId = message.substring("[LOGIN]".length()); // 아이디 설정
                    myId = userId;
                    ConnectionDao.getInstance().loginConnection(myId, socket.getInetAddress().getHostAddress());
                } else if(message.startsWith("[EXIT]")) {
                    isStop = true;
                } else if(message.startsWith("[CONNECT_REQUEST]")) {
                    String[] idArr = message.substring("[CONNECT_REQUEST]".length()).split("#"); // 아이디들
                                                                                                 // 0: 제어하는 아이디
                                                                                                 // 1: 제어 당할 아이디
                                                                                                 // 2: 제어하는 애의 아이피주소
                    otherId = idArr[1];
                    sendToOther(idArr[1], message + "#" + socket.getInetAddress().getHostAddress());
                } else if(message.startsWith("[CONNECT_RESPONSE]")) {
                    String[] msgArr = message.substring("[CONNECT_RESPONSE]".length()).split("#"); // 메세지 분류
                                                                                                   // 0: 결과값
                                                                                                   // 1: 응답 받을 아이디
                    otherId = msgArr[1];
                    sendToOther(msgArr[1], message);
                } else if(message.startsWith("[DISCONNECT]")) {
                    String disconnectId = message.substring("[DISCONNECT]".length());
                    sendToOther(disconnectId, "[DISCONNECT]");
                } else {
                    // 마우스 제어, 키보드 제어 등
                    sendToOther(otherId, message);
                }
            }
            System.out.println(socket.getInetAddress() + " " + myId + " 정상적으로 종료하셨습니다");
            System.out.println("list size : " + ms.getList().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(socket.getInetAddress() + " " + myId + "비정상적으로 종료하셨습니다");
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
