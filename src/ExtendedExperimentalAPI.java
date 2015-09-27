import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Patrick on 26-Sep-15.
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

    public void EncryptFile(String file_path, String enc_method){
        process_as_BMP = false;
        file_ex = getFileExtension(file_path);
        file_name = getFileName(file_path);
        file_enc_method = enc_method;
        if(file_ex.equals("bmp"))
            process_as_BMP = true;
        byte[] file_contents = getFileContents(file_path);
        byte[] file_header;
        if(process_as_BMP)
            file_header = getFileHeader(file_contents);

        initAESKey("770A8A65DA156D24EE2A093277530142");
        initCipher(enc_method);

        try {
            saveFileToDisk(enc_cipher.doFinal(file_contents));
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

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

    private void initAESKey(String key){
        byte[] keyData = DatatypeConverter.parseHexBinary(key);
        enc_key = new SecretKeySpec(keyData, "AES");
    }

    private byte[] getFileHeader(byte[] file_contents){
        return null;
    }

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

    private void saveFileToDisk(byte[] file_contents){
        String file_out = file_name + "-" + file_enc_method + "." + file_ex;
        try {
            if(process_as_BMP) {
               // TODO: generate BMP file header
               // then write to disk
            } else {
                FileOutputStream enc_file_output = new FileOutputStream(file_out);
                enc_file_output.write(file_contents);
                enc_file_output.flush();
                enc_file_output.close();
            }
        } catch (Exception ex){
            System.out.println("Error writing file to:" + file_out);
            ex.printStackTrace();

        }
    }

    private String getFileExtension(String file_path){
        String[] split = file_path.split("\\.");
        return split[split.length - 1];
    }

    private String getFileName(String file_path){
        String[] split = file_path.split("\\\\");
        String[] file = split[split.length-1].split("\\.");
        return file[0];
    }
}
