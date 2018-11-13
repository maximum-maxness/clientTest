package frontend;

import backend.*;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class MainViewDisplay extends JFrame {

    BackendModelSetup theBackendModel;

    JLabel textContentLabel;
    JTextArea textContentField;
    JButton openSourceFileButton;
    JButton saveResultToFileButton;
    JScrollPane textContentPane;
    
    String filePath;

    public MainViewDisplay(BackendModelSetup aBackend) {
        this.theBackendModel = aBackend;
        this.initComponents();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 500));

        this.textContentLabel = new JLabel();
        this.textContentLabel.setText("Choose A File To Begin!");

        this.textContentField = new JTextArea();
        this.textContentField.setText("");
        this.textContentField.setLineWrap(true);
        this.textContentField.setEditable(true);
        this.textContentField.setWrapStyleWord(true);
        
        this.textContentPane = new JScrollPane(this.textContentField);

        this.openSourceFileButton = new JButton();
        this.openSourceFileButton.setText("Select File");

        this.saveResultToFileButton = new JButton();
        this.saveResultToFileButton.setText("Update File");

        Container mainDisplayPane = this.getContentPane();
        mainDisplayPane.setLayout(new GridBagLayout());

        //Label
        GridBagConstraints c;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        mainDisplayPane.add(this.textContentLabel, c);

        //Text Field
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 6;
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 500;
        c.ipady = 300;
        mainDisplayPane.add(this.textContentPane, c);

        //Open File Button
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.ipadx = 60;
        c.ipady = 30;
        mainDisplayPane.add(this.openSourceFileButton, c);

        //Save File Button
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.ipadx = 50;
        c.ipady = 30;
        mainDisplayPane.add(this.saveResultToFileButton, c);

        this.pack();
    }

    void updateTextContentField() {
        String text;
        if (this.theBackendModel.theTextFile == null) {
            text = "";
        } else {
            text = this.theBackendModel.theTextFile.fileContent;
        }
        this.textContentField.setText(text);

    }
    


    String showChooseFileDialog() {
        JFileChooser jfc = new JFileChooser();
        int status = jfc.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File theFile = jfc.getSelectedFile();
            String thePath = theFile.getAbsolutePath();
            return thePath;
        }

        return null;
    }
    
    String getFilePath(Boolean b) { //true if openDialog. false if saveDIalog;
        if (b) { //if called by openDialog
            this.filePath = this.showChooseFileDialog();
        }
        return this.filePath;
    }
}

