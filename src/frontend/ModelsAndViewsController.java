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

    private class GetTextFromServerAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = theBackendModel.theClient.getDataFromServer();
            theMainViewDisplay.textContentField.setText(text);
        }
    }

    private class ReturnTextToServerAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = theMainViewDisplay.textContentField.getText();
            theBackendModel.theClient.sendDataToServer(text);
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
