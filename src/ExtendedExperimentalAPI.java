import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Patrick Snelgar on 26-Sep-15.
 * Name: Extended Experimental API
 * Description: Exposes a method that takes a file and encryption method, the file is then encrypted using a fixed 128bit
 *              hexidecimal key following the AES standard and the given encryption method (ECB, CBC or CFB)
 */
public class ExtendedExperimentalAPI {
    private Cipher enc_cipher;
    private SecretKeySpec enc_key;
    private boolean process_as_BMP = false;
    private String file_ex, file_name, file_enc_method;

    public static void main(String[] args) {
        // Only used for testing the API
        ExtendedExperimentalAPI tester = new ExtendedExperimentalAPI();
        tester.EncryptFile("Image4Assignment.bmp", "CBC");
        tester.EncryptFile("Image4Assignment.bmp", "CFB");
        tester.EncryptFile("Image4Assignment.bmp", "ECB");
    }

    /***
     * Main function of the API, takes a file path and encryption method to encrypt the file with.
     * @param file_path file name to be encrypted
     * @param enc_method method to use when encrypting the file
     */
    public void EncryptFile(String file_path, String enc_method){
        process_as_BMP = false;
        file_ex = getFileExtension(file_path);  // Gets the type of file
        file_name = getFileName(file_path);     // Gets the name of the file to be used when saving after encryption
        file_enc_method = enc_method;
        // Only process BMP files in a way which they can be viewed as an image afterwards
        // This is to fulfill he assignemtn requirements and is not proper encryption
        if(file_ex.equals("bmp"))
            process_as_BMP = true;
        // Need to have file contents as a byte array in order to encrypt
        byte[] file_contents = getFileContents(file_path);
        byte[] file_header = null;
        // Need to preserve the BMP header before encryption in order to make the file viewable as an image
        if(process_as_BMP) {
            // Seperate the 54 byte BMP header from the file contents before encryption
            file_header = Arrays.copyOfRange(file_contents, 0, 54);
            file_contents = Arrays.copyOfRange(file_contents, 54, file_contents.length);
        }
        // Create a usable KeySpec from the given hexideciaml value.
        initAESKey("770A8A65DA156D24EE2A093277530142");
        // Initialize the cipher to use for encrypting the file
        initCipher(enc_method);

        // Use try block in order to catch any exceptions the cipher might throw when encrypting
        try {
            byte[] enc_contents = enc_cipher.doFinal(file_contents);
            // Need to write the header then file contents to save as a BMP
            if(process_as_BMP) {
                writeAsImage(file_header, enc_contents);
            } else {
                saveFileToDisk(enc_contents);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /***
     * Creates a instance of the Cipher class using AES with auto padding and the requested algorithm
     * @param enc_method algorithm to use when encrypting the file contents
     */
    private void initCipher(String enc_method){
        try {
            enc_cipher = Cipher.getInstance("AES/" + enc_method + "/PKCS5Padding");
            // Set the Cipher to encrypt mode with the generated KeySpec
            enc_cipher.init(Cipher.ENCRYPT_MODE, enc_key);
        } catch (Exception e){
            System.out.println("Error initializing cipher with: " + enc_method + " method");
            System.out.println("Full trace:");
            e.printStackTrace();
        }
    }

    /***
     *
     * @param key
     */
    private void initAESKey(String key){
        // KeySpec requires the input value to be a byte array
        byte[] keyData = DatatypeConverter.parseHexBinary(key);
        enc_key = new SecretKeySpec(keyData, "AES"); // Initialize the KeySpec with the byte array and AES type
    }

    /***
     * Takes the path of a file and returns the contents as a byte array
     * @param file_path path of the file to extract contents of
     * @return a byte array containing the contents of the given file
     */
    private byte[] getFileContents(String file_path){
        try {
            return Files.readAllBytes(Paths.get(file_path)); // Want the entire contents of the file
        } catch (IOException iex){
            System.out.println("Error reading file: " + file_path);
            System.out.println("Full trace:");
            iex.printStackTrace();
        }

        return null;
    }

    /***
     * Standard method to write a file contents to disk given the contents as a byte array
     * @param file_contents the byte array representation of the file to save
     */
    private void saveFileToDisk(byte[] file_contents){
        // Save the file with the same name plus appended encrytion method
        String file_out = file_name + "-" + file_enc_method + "." + file_ex;
        try {
            // Write the byte array to disk.
            FileOutputStream enc_file_output = new FileOutputStream(file_out);
            enc_file_output.write(file_contents);
            enc_file_output.flush();    // Ensure the contents have been written from the stream
            enc_file_output.close();    // Close the file to make it accessible by other programs
        } catch (Exception ex){
            System.out.println("Error writing file to:" + file_out);
            ex.printStackTrace();
        }
    }

    /***
     * Special method for the purpose of the Assignment and saves the encrypted file as a viewable BMP image
     * @param header The byte array pertaining to the bmp header information
     * @param enc_file The encrypted contents of the bmp file
     */
    private void writeAsImage(byte[] header, byte[] enc_file){
        // Save with the original file name plus appended encryption method
        String out_file_name = file_name + "-" + file_enc_method + "." + file_ex;
        try {
            // Write out the byte arrays using an output stream
            FileOutputStream fo = new FileOutputStream(out_file_name);
            fo.write(header);   // Write the non encrypted header first
            fo.write(enc_file); // Encrypted file contents
            fo.flush();         // Make sure the information is written out
            fo.close();         // Close the stream in order to make the file accessible by other programs
        } catch (Exception ex){
            System.out.println("Error writing file: " + out_file_name);
            ex.printStackTrace();
        }
    }

    /***
     * Method that returns the extension of the file given the file path
     * @param file_path directory and or the file name to process
     * @return the extension i.e. file type of the given file
     */
    private String getFileExtension(String file_path){
        String[] split = file_path.split("\\.");    // Regex for special characters requires escaping
        return split[split.length - 1];             // Extension is always at the end of the file
    }

    /***
     * Gets the name of the given file with out extension type
     * @param file_path path of the file to process
     * @return String holding the name of the file
     */
    private String getFileName(String file_path){
        String[] split = file_path.split("\\\\");           // Directory seperator is a special character in regex and requires escaping
        String[] file = split[split.length-1].split("\\."); // File plus extension will be after all the directories so last index
        return file[0];                                     // File name comes before extension
    }
}
