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
    private PrintStream ps;
    private BufferedReader bf;

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
            this.bf = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.ps = new PrintStream(this.socket.getOutputStream());
            System.out.println("Connected to Server!");
        } catch (IOException ex) {
            System.err.println("Cant Connect to Server @" + this.ip + ";" + this.port);
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }

    public void disconnect() {
        try {
            sendReplyToServer(this.DISCONNECT_FROM_SERVER);
            this.bf.close();
            this.ps.close();
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
            String text = this.bf.readLine();
            System.out.println("Recieved: " + text);
            return text;
        } catch (IOException ex) {
            Logger.getLogger(ClientClass.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void sendReplyToServer(String replyToSend) {
        this.ps.println(replyToSend);
        System.out.println("Sent: " + replyToSend);
    }

}
