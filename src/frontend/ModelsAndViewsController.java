package frontend;

import frontend.MainViewDisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelsAndViewsController {
    
    BackendModelSetup theBackendModel;
    MainViewDisplay theMainViewDisplay;
    public String openFilePath;

    String text = "";
    String temp = "";
    Scanner kbScanner = new Scanner(System.in);
    Socket socket = new Socket("127.0.0.1", 12445);
    Scanner socketScanner = new Scanner(socket.getInputStream());

    private class GetTextFromServerAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                temp = socketScanner.nextLine();
                theMainViewDisplay.textContentField.setText(temp);
                System.out.println("worked");
            } catch (NoSuchElementException ex) {

            }
        }
    }

    private class ReturnTextToServerAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            text = theMainViewDisplay.textContentField.getText();
            PrintStream printSocket;
            try {
                printSocket = new PrintStream(socket.getOutputStream());
                printSocket.println(text);
                System.out.println("Sent " + text);
            } catch (IOException ex) {
                Logger.getLogger(ModelsAndViewsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public ModelsAndViewsController(BackendModelSetup aBackendModelSetup, MainViewDisplay aMainViewDisplay) throws IOException {
        this.theMainViewDisplay = aMainViewDisplay;
        this.theBackendModel = aBackendModelSetup;
        this.initController();
    }

    private void initController() {
        this.theMainViewDisplay.openSourceFileButton.addActionListener(new GetTextFromServerAction());
        this.theMainViewDisplay.saveResultToFileButton.addActionListener(new ReturnTextToServerAction());
    }
}
