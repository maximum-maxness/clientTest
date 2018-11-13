package frontend;

import backend.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
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

    private class OpenSourceFileAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String pathToFile = theMainViewDisplay.getFilePath(true);
            System.out.println(pathToFile);

            //if (pathToFile != null) {
            try {
                theBackendModel.theTextFile = new TextFile(pathToFile);
                theMainViewDisplay.updateTextContentField();
                theMainViewDisplay.textContentLabel.setText("File Chosen " + pathToFile);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ModelsAndViewsController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //}
        }

    }

    private class SaveResultToFileAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String pathToFile = theMainViewDisplay.getFilePath(false);
            System.out.println(pathToFile);

            if (pathToFile != null) {
                try {
                    theBackendModel.theTextFile.fileContent = theMainViewDisplay.textContentField.getText();
                    theBackendModel.theTextFile.saveToDisk(pathToFile);
                } catch (IOException ex) {
                    Logger.getLogger(ModelsAndViewsController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

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

    public ModelsAndViewsController(BackendModelSetup aBackend, MainViewDisplay aMainViewDisplay) throws IOException {
        this.theBackendModel = aBackend;
        this.theMainViewDisplay = aMainViewDisplay;

        this.initController();
    }

    private void initController() {
        this.theMainViewDisplay.openSourceFileButton.addActionListener(new GetTextFromServerAction());
        this.theMainViewDisplay.saveResultToFileButton.addActionListener(new ReturnTextToServerAction());
        //this.theMainViewDisplay.openSourceFileButton.addActionListener(new OpenSourceFileAction());
        //this.theMainViewDisplay.saveResultToFileButton.addActionListener(new SaveResultToFileAction());
    }
}
