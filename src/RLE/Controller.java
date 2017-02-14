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

/**
 * ===========================================================
 * mod1 Author: Toby Zhang
 * This program is designed to to do run length encoding for
 * text files as a proof of concept example
 * ============================================================
 */

public class Controller implements Initializable {


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

    File homedir = new File("H:/var/gist/8100/mod1/");

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

    public void setLoad() throws FileNotFoundException {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open text file");
            fileChooser.setInitialDirectory(homedir);
            File input = fileChooser.showOpenDialog(pane.getScene().getWindow());
            inputTextArea.setText("");
            outputTextArea.setText("");
            if (input != null) {
                path.setText(String.valueOf(input));
            } else {
                path.setText("Invalid File Selected");
            }
            Scanner inputScan = new Scanner(input);
            while (inputScan.hasNext()) {
                inputTextArea.appendText(inputScan.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setEncode() throws IOException {


        try {
            File inputFile = new File(path.getText());
            FileInputStream f_is = new FileInputStream(inputFile);
            DataInputStream d_is = new DataInputStream(f_is);
            byte[] inputByte = new byte[d_is.available()];
            d_is.readFully(inputByte);
            d_is.close();
            int j = 1;
            counter.setText(String.valueOf(inputByte[0]));

            byte currentByte = inputByte[0];

            for (int i = 1; i < inputByte.length; i++) {
                if (currentByte == inputByte[i]) {
                    j++;
                } else {
                    outputTextArea.appendText(String.valueOf(j));
                    outputTextArea.appendText(new String(new byte[]{currentByte}));
                    j = 1;
                    currentByte = inputByte[i];
                }
                if (inputByte.length - 2 == i) {
                    if (currentByte == inputByte[i + 1]) {
                        j++;
                        outputTextArea.appendText(String.valueOf(j));
                        outputTextArea.appendText(new String(new byte[]{currentByte}));
                        counter.setText("no2");
                        break;
                    } else {
                        outputTextArea.appendText(String.valueOf(j));
                        outputTextArea.appendText(new String(new byte[]{currentByte}));
                        currentByte = inputByte[i + 1];
                        outputTextArea.appendText(String.valueOf(1));
                        outputTextArea.appendText(new String(new byte[]{currentByte}));
                        counter.setText("no1");
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
            String[] strArray = new String[255];
            int[] intArray = new int[255];
            int j = 0;
            int sum = 0;

            System.out.print(String.valueOf(inputByte));
            counter.setText("d1");
            for (int i = 0; i < inputByte[i]; i++) {
                counter.setText("d2");
                j = 0;
                while ((int) inputByte[i] < 63) {
                    //add to int
                    if (j > 0) {
                        sum = ((int) inputByte[i]) + j;
                        intArray[intArray.length - 1] = ((byte) sum);

                        counter.setText("d3");


                    } else {

                        intArray = new int[]{inputByte[i]};
                        i++;
                        j = inputByte[i];

                        counter.setText("d4");
                    }
                }
                System.out.print("array is:" + intArray);
                if (((int) inputByte[i]) > 63) {
                    //add to string array
                    strArray = new String[]{String.valueOf(inputByte[i])};

                    counter.setText("d5");
                }


                System.out.print("Array: " + strArray);
            }
            for (int i = 0; i < intArray.length; i++) {

                counter.setText(String.valueOf(strArray[i]));
                int iter = 1;
                while (iter <= intArray[i]) {
                    outputTextArea.appendText(strArray[i]);
                    iter++;

                    counter.setText("d7");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setSave() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as text file");
        fileChooser.setInitialDirectory(homedir);
        File input = fileChooser.showSaveDialog(pane.getScene().getWindow());

        if (input != null) {

            String output = outputTextArea.getText();
            FileOutputStream f_os = new FileOutputStream(input);
            DataOutputStream d_os = new DataOutputStream(f_os);
            d_os.writeChars(output);
            d_os.close();
        }
    }
}

