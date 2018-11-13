package runner;

import frontend.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TheApp implements Runnable {

    @Override
    public void run() {
        BackendModelSetup theBackendModel = new BackendModelSetup();
        MainViewDisplay theMainViewDisplay = new MainViewDisplay(theBackendModel);
        try {
            ModelsAndViewsController theMainViewsController = new ModelsAndViewsController(theBackendModel, theMainViewDisplay);
        } catch (IOException ex) {
            Logger.getLogger(TheApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        theMainViewDisplay.setVisible(true);
    }
}