/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gamer
 */
public class ClientClass {

    public Socket socket;
    public SocketAddress socketAdd;
    public InetSocketAddress basicAdd;
    public int port;
    public String ip;
    private InputStream inputStream;
    private OutputStream outputStream;

    private final String UPLOAD_TO_SERVER = "202";
    private final String DOWNLOAD_FROM_SERVER = "205";
    private final String FINISHED_UPLOAD = "206";
    private final String DISCONNECT_FROM_SERVER = "250";
    private final String SERVER_REPLY_READY = "302";
    private final String SERVER_REPLY_BEGIN_TRANSFER = "303";
    private final String SERVER_REPLY_FINSIHED = "301";

    public ClientClass(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.socket = new Socket();
        updateAddress();
    }

    public void setPort(int port) {
        if (!this.socket.isConnected()) {
            this.port = port;
            updateAddress();
        }
    }

    public void setIP(String ip) {
        if (!this.socket.isConnected()) {
            this.ip = ip;
            updateAddress();
        }
    }

    private void updateAddress() {
        if (!this.socket.isConnected()) {
            this.basicAdd = new InetSocketAddress(this.ip, this.port);
            this.socketAdd = this.basicAdd;
            System.out.println("Updated address!");
        }
    }

    public void connect() {
        try {
            this.socket.connect(this.socketAdd);
            this.inputStream = this.socket.getInputStream();
            this.outputStream = this.socket.getOutputStream();
            System.out.println("Connected to Server!");
//            if (getReplyFromServer() == this.SERVER_REPLY_READY) {
//                System.out.println(this.SERVER_REPLY_READY + " Server is Ready!");
//                return;
//            } else {
//                System.err.println("Server may not be ready.. continuing anyways");
//                return;
//            }
        } catch (IOException ex) {
            System.err.println("Cant Connect to Server @" + this.ip + ";" + this.port);
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }

    public void disconnect() {
        try {
            sendReplyToServer(this.DISCONNECT_FROM_SERVER);
            this.socket.close();
        } catch (IOException ex) {
            System.err.println("Error Disconnecting!");
            return;
        }
    }

    public void sendDataToServer(String data) {
        sendReplyToServer(this.UPLOAD_TO_SERVER);

        if (getReplyFromServer() == this.SERVER_REPLY_READY) {
            sendReplyToServer(data);
            sendReplyToServer(this.FINISHED_UPLOAD);
            if (getReplyFromServer() == this.SERVER_REPLY_FINSIHED) {
                System.out.println("Finished Upload with no Errors!");
            } else {
                System.err.println("Error: Server didn't reply with ready.. Continuing Anyways..");
            }
        } else {
            System.err.println("Error: Server didn't reply with ready.. Continuing Anyways..");
        }
    }

    public String getDataFromServer() {
        sendReplyToServer(this.DOWNLOAD_FROM_SERVER);
        if (getReplyFromServer() == this.SERVER_REPLY_BEGIN_TRANSFER) {
            String text = getReplyFromServer();
            if (getReplyFromServer() == this.SERVER_REPLY_FINSIHED) {
                System.out.println("Finished Download with no Errors!");
            } else {
                System.err.println("Error: Server didn't reply with finished transfer.. Continuing Anyways..");
            }
            return text;
        } else {
            System.err.println("Error: Server didn't reply with begin transfer.. aborting..");
        }
        return null;
    }

    private String getReplyFromServer() {
        try {
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(this.inputStream));
            String text = bf.readLine();
            bf.close();
            System.out.println("Recieved: " + text);
            return text;
        } catch (IOException ex) {
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void sendReplyToServer(String replyToSend) {
        PrintStream ps;
//        try {
        ps = new PrintStream(this.outputStream);
//        } catch (IOException ex) {
//            System.err.println("Can't get output stream!");
//            return;
//        }
        ps.println(replyToSend);
        ps.close();
        System.out.println("Sent: " + replyToSend);
    }

}
