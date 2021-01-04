package net;

import java.io.*;
import java.net.*;
import java.net.Inet4Address;
import java.net.InetAddress;

public class SocketServer {
    private final int SPORT = 18000;//server端口号
    private final int CPORT = 18001;//client端口号
    private static InetAddress clintAddr;//clintIP
    private static String password = "Hulu";
    private PrintWriter writer;
    private BufferedReader reader;
    DatagramSocket socket;
    public void start() throws IOException {
        waitClient();
        tellClient();
        waitConnection();
    }

    private void waitClient(){//等待客户端连接
        byte[] buf = new byte[256];//存储发来的消息
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket = new DatagramSocket(SPORT);
            System.out.println("等待客户连接");
            while (true) {
                socket.receive(packet);
                String received = new String(packet.getData());
                System.out.println("接收到客户端地址：" + received);
                System.out.println("接收到客户端端口：" + packet.getPort());
                if (received.substring(0, 4).equals(password) && CPORT == packet.getPort()) {//password与端口正确则停止监测并保存客户端IP
                    clintAddr = packet.getAddress();
                    System.out.println("已搜索到客户端，地址：" + clintAddr);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tellClient() throws IOException {
        try {
            DatagramSocket socket = new DatagramSocket();
            System.out.println("正在向客户端" + clintAddr + "发送连接请求");
            DatagramPacket packet = new DatagramPacket(password.getBytes(), password.length(), clintAddr, CPORT);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitConnection() {
        System.out.println("正在等待客户端" + clintAddr + "连接");
        try {
            ServerSocket serverSocket = new ServerSocket(SPORT);
            Socket clientSocket = serverSocket.accept();
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("与" + clintAddr + "连接成功");

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + SPORT + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    public String read() throws IOException {
        String inputLine;
        while ((inputLine = reader.readLine()) == null) {
            ;
        }
        //System.out.println("write: "+inputLine);
        return inputLine;
    }

    public void write(String message) {
        writer.println(message);
        //System.out.println("write: "+message);
    }
}

