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
     *
     * @param file_path
     * @param enc_method
     */
    public void EncryptFile(String file_path, String enc_method){
        process_as_BMP = false;
        file_ex = getFileExtension(file_path);
        file_name = getFileName(file_path);
        file_enc_method = enc_method;
        if(file_ex.equals("bmp"))
            process_as_BMP = true;
        byte[] file_contents = getFileContents(file_path);
        byte[] file_header = null;
        if(process_as_BMP) {
            file_header = Arrays.copyOfRange(file_contents, 0, 54);
            file_contents = Arrays.copyOfRange(file_contents, 54, file_contents.length);
        }

        initAESKey("770A8A65DA156D24EE2A093277530142");
        initCipher(enc_method);

        try {
            byte[] enc_contents = enc_cipher.doFinal(file_contents);
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
     *
     * @param enc_method
     */
    private void initCipher(String enc_method){
        try {
            enc_cipher = Cipher.getInstance("AES/" + enc_method + "/PKCS5Padding");
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
        byte[] keyData = DatatypeConverter.parseHexBinary(key);
        enc_key = new SecretKeySpec(keyData, "AES");
    }

    /***
     *
     * @param file_path
     * @return
     */
    private byte[] getFileContents(String file_path){
        try {
            return Files.readAllBytes(Paths.get(file_path));
        } catch (IOException iex){
            System.out.println("Error reading file: " + file_path);
            System.out.println("Full trace:");
            iex.printStackTrace();
        }

        return null;
    }

    /***
     *
     * @param file_contents
     */
    private void saveFileToDisk(byte[] file_contents){
        String file_out = file_name + "-" + file_enc_method + "." + file_ex;
        try {
            FileOutputStream enc_file_output = new FileOutputStream(file_out);
            enc_file_output.write(file_contents);
            enc_file_output.flush();
            enc_file_output.close();
        } catch (Exception ex){
            System.out.println("Error writing file to:" + file_out);
            ex.printStackTrace();
        }
    }

    /***
     *
     * @param header
     * @param enc_file
     */
    private void writeAsImage(byte[] header, byte[] enc_file){
        String out_file_name = file_name + "-" + file_enc_method + "." + file_ex;
        try {
            FileOutputStream fo = new FileOutputStream(out_file_name);
            fo.write(header);
            fo.write(enc_file);
            fo.flush();
            fo.close();
        } catch (Exception ex){
            System.out.println("Error writing file: " + out_file_name);
            ex.printStackTrace();
        }
    }

    /***
     *
     * @param file_path
     * @return
     */
    private String getFileExtension(String file_path){
        String[] split = file_path.split("\\.");
        return split[split.length - 1];
    }

    /***
     *
     * @param file_path
     * @return
     */
    private String getFileName(String file_path){
        String[] split = file_path.split("\\\\");
        String[] file = split[split.length-1].split("\\.");
        return file[0];
    }
}
