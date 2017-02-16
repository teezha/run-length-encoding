package RLE;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.*;

/**
 * ===========================================================
 * mod1 Author: Toby Zhang
 * This program is designed to to do run length encoding for
 * text files as a proof of concept example
 * ============================================================
 */

public class Controller implements Initializable {

    /**
     * FXML hooks
     */

    @FXML
    Pane pane;
    @FXML
    TextField path;
    @FXML
    Text greetings;
    @FXML
    Text counter;
    @FXML
    TextArea inputTextArea;
    @FXML
    TextArea outputTextArea;

    //At school
    //File homedir = new File("H:/var/gist/8100/mod1/");
    //At local
    File homedir = new File(System.getProperty("user.home"));


    //initialize + title load
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            System.out.println("This is mod1");
            greetings.setFont(new Font(24));
            greetings.setTextAlignment(TextAlignment.CENTER);
            greetings.setText("This is mod1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load file using gui
    public void setLoad() throws FileNotFoundException {

        try {
            //opens up the file chooser window
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open text file");
            fileChooser.setInitialDirectory(homedir);
            File input = fileChooser.showOpenDialog(pane.getScene().getWindow());
            //wipes input and output text space
            inputTextArea.setText("");
            outputTextArea.setText("");
            //if invlaid file selected
            if (input != null) {
                path.setText(String.valueOf(input));
            } else {
                path.setText("Invalid File Selected");
            }
            //outputs file contents onto the input text space
            Scanner inputScan = new Scanner(input);
            while (inputScan.hasNext()) {
                inputTextArea.appendText(inputScan.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //encode method
    public void setEncode() throws IOException {


        try {
            //takes input file path and puts the data into input stream
            File inputFile = new File(path.getText());
            FileInputStream f_is = new FileInputStream(inputFile);
            DataInputStream d_is = new DataInputStream(f_is);
            //sets a byte array the size of the stream bytes
            byte[] inputByte = new byte[d_is.available()];

            //pulls in the stream as a byte array
            d_is.readFully(inputByte);
            //closes stream as the bytes are in an array now
            d_is.close();

            //sets the letter count to be 1
            int j = 1;
            counter.setText(String.valueOf(inputByte[0]));
            //sets the first variable equal to the first letter
            //this sets the currentByte variable as the reference letter
            byte currentByte = inputByte[0];

            //for loop that starts the check on each character to the reference character current byte
            for (int i = 1; i < inputByte.length; i++) {
                //if character is equal to reference character, letter counter +1
                if (currentByte == inputByte[i]) {
                    j++;
                } else {
                    //if character is not the same as the reference character, prints out the letter counter first,
                    // followed by the reference character
                    outputTextArea.appendText(String.valueOf(j));
                    outputTextArea.appendText(new String(new byte[]{currentByte}));
                    //reset the letter counter to 1 and the reference character to the new character
                    j = 1;
                    currentByte = inputByte[i];
                }

                //if loop position is 2nd value from the end, follow conditions
                if (inputByte.length - 2 == i) {
                    //if current byte is equal to the next byte in loop
                    if (currentByte == inputByte[i + 1]) {
                        //letter counter add 1, prints out results
                        j++;
                        outputTextArea.appendText(String.valueOf(j));
                        outputTextArea.appendText(new String(new byte[]{currentByte}));
                        counter.setText("no2");
                        //break out of for loop
                        break;

                    } else {
                        //if next letter is different from the current letter, prints out last letter
                        outputTextArea.appendText(String.valueOf(j));
                        outputTextArea.appendText(new String(new byte[]{currentByte}));
                        //sets reference to next letter
                        currentByte = inputByte[i + 1];
                        //prints out 1 and the next letter
                        outputTextArea.appendText(String.valueOf(1));
                        outputTextArea.appendText(new String(new byte[]{currentByte}));
                        counter.setText("no1");
                        //breaks out the loop
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setDecode() throws IOException {

        try {
            File inputFile = new File(path.getText());
            FileInputStream f_is = new FileInputStream(inputFile);
            DataInputStream d_is = new DataInputStream(f_is);
            byte[] inputByte = new byte[d_is.available()];
            d_is.readFully(inputByte);
            d_is.close();

            inputTextArea.setText(new String(inputByte));
            String ita = inputTextArea.getText();
            byte[] bt = ita.getBytes(StandardCharsets.US_ASCII);

            int j = 0;
            int k = 0;
            int count = 0;
            String cat = "";
            String let = "";
            String decode = "";

            for (int i = 0; i < bt.length; i++) {
                if (bt[i] > 47 && bt[i] < 58) {
                    j = bt[i]-47;


                    //counter.setText(String.valueOf(j));
                    while (bt[i + 1] > 47 && bt[i + 1] < 58) {
                        i++;

                        k = bt[i] - 47;
                        cat = String.valueOf(j) + String.valueOf(k);
                        j = Integer.parseInt(cat);

                        counter.setText(cat);
                    }

                    count = j;
                    i++;
                } else {//if ((inputByte[i] > 64 && inputByte[i] < 91)
                        //|| (inputByte[i] > 96 && inputByte[i] < 123)
                        //&& count > 0) {

                    counter.setText("t4");
                    let = new String(new byte[]{bt[i]});
                    for (int c = 0; c < count; c++) {

                        counter.setText("t5");
                        outputTextArea.appendText(let);
                        //decode = decode+let;
                    }

                    counter.setText("t6");
                }

            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setSave() throws IOException {

        //opens save dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as text file");
        fileChooser.setInitialDirectory(homedir);
        File input = fileChooser.showSaveDialog(pane.getScene().getWindow());

        //if file path is found
        if (input != null) {

            //get text from output area, opens output stream and writes the text as bytes
            String output = outputTextArea.getText();
            FileOutputStream f_os = new FileOutputStream(input);
            DataOutputStream d_os = new DataOutputStream(f_os);
            d_os.writeBytes(output);
            d_os.close();
        }
    }
}

