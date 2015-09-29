/**
 * Created by Patrick on 22/09/2015.
 * Name: CaesarAPI
 * Description: An API that implements methods to encrypt, decrypt and analyze encrypted input without a key.
 */
public class CaesarAPI {

    public static void main(String[] args) {
        // For API testing purposes only
        CaesarAPI t = new CaesarAPI();
        System.out.println(t.Encrypt("I am currently studying Cyber Security module at the Department of Computer Science, University of Waikato.", 16));
        System.out.println(t.Decrypt("Zbydomdsxq kx SD sxpbkcdbemdebo sxmvenoc gsno bkxqo yp kmdsfsdsoc drkd rkfo dy lo zobpybwon sx cixm gsdr okmr ydrob. Sx drsc vomdebo, go ohzvybo dro lkcsmc yp lesvnsxq kx SD cicdow drkd sc cdboxqdroxon dy lo k comebo. Dro cdboxqdroxsxq zbymocc boaesboc rkbnoxsxq nsppoboxd mywzyxoxdc yp dro SD sxpbkcdbemdebo sxmvensxq Yzobkdsxq Cicdowc, Xodgybuc kxn Kzzvsmkdsyxc.", 10));
        t.Cryptanlysis("SHEM veskiui ed iyn cqzeh jxucui: Fheludqdsu, Kiuh-Sudjhysyjo, Lyikqbyiqjyed, Usedecysi, Xqhtmqhu, qdt Jeebi & Tqjqiuji. Jewujxuh, jxuiu iyn jxucu udqrbu jxu hujkhd ev sedjheb ev tqjq je kiuhi, jxuhuro hutksydw hubyqdsu ed jxyht-fqhjo ludtehi eh jhkij hubqjyedixyfi seccedbo vekdt yd ceij soruh iuskhyjo sedjhqsji jetqo.");
        //System.out.println(t.Encrypt("A", 10));
    }

    /***
     * Public method to encrypt any given text using the Caesar algorithm and a provided key for encryption
     * @param text the text to encrypt
     * @param key the key to use for encrypting the given text
     * @return returns the encrypted text once it has been passed through the Caesar Cipher
     */
    public String Encrypt(String text, int key){
        // Catches an invalid key size
        // can only offset by a character position in the alphabet
        // 0 offset is the same as 26 therefor 0-25 is the acceptable range
        if(key < 0 || key > 25){
            System.out.println("Error with given key: Invalid size" + key);
            System.exit(1);
        }
        // If the text is null in length or somehow negative
        // then the program should not try and process the input
        if(text == null || text.length() < 0){
            System.out.println("Error with input");
            System.exit(1);
        }
        // Clear the output String
        String output = "";
        // Process every character individually
        // allows output text to have punctuation.
        // Maintains character case.
        for(char c : text.toCharArray()){
            // 97 = 'a' / 123 = 'z'
            if( 97 <= c && c < 123) {
                //System.out.println(c + key + ":" + (c+key)%26 + ":" + ((c+key)%26+97));
                // Requires an offset of 7 to balance the modulo operation
                // difference between given key and the result of the modulo operation after adding the key and character.
                output += (char) ((c + key + 7) % 26 + 97);
            // 65 = 'A' / 91 = 'Z'
            } else if ( 65 <= c && c < 91){
                //System.out.println(c + key + ":" + (c+key)%26 + ":" + ((c+key)%26+65));
                // Offset of 13 is calculated by calculating the difference between
                // the given key and the key + character value after modulo operation.
                output += (char) ((c + key + 13) % 26 + 65);
            } else output += c; // Handles all other characters to maintain original text structure
        }
        return output;
    }

    /***
     * Handles the decryption of a given text input and key pair to provide a un encrypted output.
     * Effectively shifts to the left instead of the right (encryption)
     * @param text the encrypted text input to decrypt
     * @param key the key to use when decrypting the text
     * @return a 'decrypted' string
     */
    public String Decrypt(String text, int key){
        // Catches an invalid key size
        // can only offset by a character position in the alphabet
        // 0 offset is the same as 26 therefor 0-25 is the acceptable range
        if(key < 0 || key > 25){
            System.out.println("Error with given key: Invalid size" + key);
            System.exit(1);
        }
        // If the text is null in length or somehow negative
        // then the program should not try and process the input
        if(text == null || text.length() < 0){
            System.out.println("Error with input");
            System.exit(1);
        }
        // Decryption is the same as encrypting with the inverse key
        return Encrypt(text, (26 - key) % 26);
    }

    /***
     * Cycles through all possible keys and outputs the result for each iteration
     * @param text the 'encrypted' text to analyse
     */
    public void Cryptanlysis(String text){
        // If the text is null in length or somehow negative
        // then the program should not try and process the input
        if(text == null || text.length() < 0){
            System.out.println("Error with input");
            System.exit(1);
        } else {
            // Cycle through all possible keys and attempt to decrypt the given text
            for (int key = 0; key < 26; key++) {
                System.out.println(Decrypt(text, key)); // Call the decrypt mehod with the current key and the input text
            }
        }
    }
}
