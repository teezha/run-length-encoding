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
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    File homedir = new File("H:/var/gist/8100/mod1/");
    //At local
    //File homedir = new File(System.getProperty("user.home"));


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
            //if invalid file selected throws invlaid file error at path bar, if not then sets path to selected file
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
            //wipes the output text field to look neater
            outputTextArea.setText("");
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
            }
            outputTextArea.appendText(String.valueOf(j));
            outputTextArea.appendText(new String(new byte[]{currentByte}));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setDecode() throws IOException {

        try {
            //wipes output text to look cleaner
            outputTextArea.setText("");
            //opens a string buffer to store the data
            StringBuffer out = new StringBuffer();
            //regex expression that looks for [ONE OR MORE NUMBERS] OR [LETTERS]
            String expression = "[0-9]+|[a-zA-Z]";
            //sets output to empty string (initialize)
            String outputText = "";
            // Java Pattern object regular expression compile
            Pattern pattern = Pattern.compile(expression);
            //sets the input text as string
            String inputText = inputTextArea.getText();
            // calls for the pattern object then matches the complied regex expression
            Matcher matcher = pattern.matcher(inputText);

            //while the matcher can find
            while (matcher.find()) {

                //sets the count to the matched number from matcher
                int count = Integer.parseInt(matcher.group());
                //matcher finds next (which in this case is a letter as the previous expression gorupped all the numbers)
                matcher.find();
                //count decreases every cycle until count is = to 0
                while (count-- != 0) {
                    //appends the matched letter to the string buffer
                    out.append(matcher.group());
                }

            }
            //converts the string buffer to a string
            outputText = out.toString();
            //writes to output area
            outputTextArea.setText(outputText);
        } catch (NumberFormatException e) {
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

