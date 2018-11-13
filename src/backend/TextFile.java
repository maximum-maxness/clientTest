package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import frontend.*;

public class TextFile {
    public String path, fileContent;
    
    public TextFile(String path) throws FileNotFoundException { // throws IOException ?
        this.path = path;
        File plainFile = new File(this.path);
        String fileString = "";

        Scanner fileScanner = new Scanner(plainFile);
        while (fileScanner.hasNextLine()) {
            fileString += fileScanner.nextLine() + "\n";
        }
        
        this.fileContent = fileString;
    }
    
    public void saveToDisk(String path) throws IOException {
        FileWriter fw = new FileWriter(path); 
        PrintWriter output = new PrintWriter(fw);
        
        output.println(this.fileContent);
        output.close();
        fw.close();
    }

    public static String readFile(String path) throws FileNotFoundException { // throws IOException
        System.out.println(path);
        File plainFile = new File(path);
        String fileString = "";

        Scanner fileScanner = new Scanner(plainFile);
        while (fileScanner.hasNextLine()) {
            fileString += fileScanner.nextLine() + "\n";
        }
        
        return fileString;
    }
    
}
