package net;

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketClient {
    private static final int SPORT = 18000;//server端口号
    private static final int CPORT = 18001;//client端口号
    private static InetAddress serverAddr;//serverIP
    private static final String password = "Hulu";//广播内容
    DatagramSocket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    Socket echoSocket;
    public void start() throws IOException {
        findServer();
        waitServer();
        startConnection();
    }

    private static ArrayList<InetAddress> getAllLocalIpAddr() { //获取所有的IP地址
        ArrayList<InetAddress> ipList = new ArrayList<InetAddress>();
        try {
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
                Enumeration ipAddrEnum = ni.getInetAddresses();
                while (ipAddrEnum.hasMoreElements()) {
                    InetAddress addr = (InetAddress) ipAddrEnum.nextElement();
                    if (addr.isLoopbackAddress())//回环地址跳过
                        continue;
                    String ip = addr.getHostAddress();
                    if (ip.contains(":"))//ipv6地址跳过
                        continue;
                    ipList.add(addr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipList;
    }

    private void findServer() throws SocketException {
        HashSet<InetAddress> BROADCAST_IP = new HashSet<InetAddress>();//所有广播地址
        ArrayList<InetAddress> list = getAllLocalIpAddr();
        NetworkInterface networkInterface;
        for (InetAddress addr : list) {
            networkInterface = NetworkInterface.getByInetAddress(addr);
            for (InterfaceAddress address : networkInterface.getInterfaceAddresses())
                if (address.getBroadcast() != null)
                    BROADCAST_IP.add(address.getBroadcast());
        }
        if (BROADCAST_IP.isEmpty()) {
            System.err.println("未正确连接网络，找不到本地IP");
            System.exit(1);
        }
        System.out.println("正在寻找服务器");
        try {
            socket = new DatagramSocket(CPORT);
            for (InetAddress addr : BROADCAST_IP) {//广播通知服务器
                DatagramPacket packet = new DatagramPacket(password.getBytes(), password.length(), addr, SPORT);
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void waitServer() throws IOException {//等待服务器回信
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        serverAddr = packet.getAddress();
        System.out.println("已寻找到服务器:" + serverAddr);
        socket.close();
    }

    private void startConnection() {
        System.out.println("正在与" + serverAddr + "建立连接");
        try {
            echoSocket = new Socket(serverAddr, SPORT);
            System.out.println("与" + serverAddr + "连接成功");
            writer = new PrintWriter(echoSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverAddr);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    serverAddr);
            System.exit(1);
        }
    }

    public String read() throws IOException {
        String inputLine;
        while ((inputLine = reader.readLine()) == null) {
            ;
        }
        return inputLine;
    }

    public void write(String message) {
        writer.println(message);
    }
}



