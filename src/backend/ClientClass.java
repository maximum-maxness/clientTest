/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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
    private final String UPLOAD_TO_SERVER = "202";
    private final String DOWNLOAD_FROM_SERVER = "205";
    private final String FINISHED_UPLOAD = "206";
    private final String DISCONNECT_FROM_SERVER = "250";
    private final String SERVER_REPLY_READY = "302";

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
        }
    }

    public void connect() {
        try {
            this.socket.connect(this.socketAdd, 10000);
            if (getReplyFromServer() == this.SERVER_REPLY_READY) {
                System.out.println(this.SERVER_REPLY_READY + " Server is Ready!");
                return;
            } else {
                System.err.println("Server may not be ready.. continuing anyways");
                return;
            }
        } catch (IOException ex) {
            System.err.println("Cant Connect to Server @" + this.ip + ";" + this.port);
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
            if(getReplyFromServer() == this.SERVER_REPLY_READY){
                
            } else {
                
            }
        } else {
            
        }
    }

    private String getReplyFromServer() {
        BufferedReader bf;
        try {
            bf = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String text = bf.readLine();
            bf.close();
            return text;
        } catch (IOException ex) {
            System.err.println("Can't get input stream!");
            return null;
        }
    }

    private void sendReplyToServer(String replyToSend) {
        PrintStream ps;
        try {
            ps = new PrintStream(this.socket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Can't get output stream!");
            return;
        }
        ps.println(replyToSend);
        ps.close();
    }
}
