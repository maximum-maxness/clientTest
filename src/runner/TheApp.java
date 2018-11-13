/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runner;

import frontend.MainViewDisplay;
import frontend.ModelsAndViewsController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gamer
 */
public class TheApp implements Runnable {

    @Override
    public void run() {
        MainViewDisplay theMainViewDisplay = new MainViewDisplay();
        try {
            ModelsAndViewsController theMainViewsController = new ModelsAndViewsController(theMainViewDisplay);
        } catch (IOException ex) {
            Logger.getLogger(TheApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        theMainViewDisplay.setVisible(true);
    }
}
