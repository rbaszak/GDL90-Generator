package rbaszak.encoder;

import java.io.IOException;
import java.net.*;

public class Generator{

    public static void main(String[] args) throws InterruptedException, IOException{

        String hostname = "192.168.3.59";
        int port = 4000;

        try {
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();

            Encoder enc = new Encoder();
            enc.crcInit();

            String hbMessage = enc.genHeartbeat();
            String ownMessage = enc.genOwnship();
            String tgtMessage = enc.genTraffic();

            while (true) {
                System.out.println(hbMessage);

                byte[] buffer = enc.hexStringToByteArray(hbMessage);

                DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(request);

                Thread.sleep(50);

                System.out.println(ownMessage);

                buffer = enc.hexStringToByteArray(ownMessage);

                request = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(request);

                Thread.sleep(50);

                System.out.println(tgtMessage);

                buffer = enc.hexStringToByteArray(tgtMessage);

                request = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(request);

                Thread.sleep(1000);
            }

        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }
}

