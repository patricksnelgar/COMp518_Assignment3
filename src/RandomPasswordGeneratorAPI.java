import java.util.Random;

/**
 * Created by Patrick Snelgar on 26-Sep-15.
 * Name: Random Password Generator API
 * Description: A public call, generatePassword(), is exposed providing a method to generate a random password of a given length
 */
public class RandomPasswordGeneratorAPI {

   private int password_length;

   public static void main(String[] args){
      // For API testing purposes only
      RandomPasswordGeneratorAPI pw_gen = new RandomPasswordGeneratorAPI();
      System.out.println(pw_gen.generatePassword(16));
      System.out.println(pw_gen.generatePassword(16));
      System.out.println(pw_gen.generatePassword(16));
   }

   /***
    * Generates a password of random characters and returns as a string
    * @param given_length integer length for the password, between 8 and 80 characters
    * @return the String representations of the password.
    */
   public String generatePassword(int given_length){
      // Make sure the requested length is within the allowed range for the API
      if(given_length < 8 || given_length > 80){
         System.out.println("Invalid password length.\nPlease choose a length between 8 and 80 characters.");
         System.exit(1);
      }
      password_length = given_length;
      // Use a string builder to hold the generated password
      StringBuilder pw_builder = new StringBuilder();
      // Generate each character for the given length of the password
      for(int i = 0; i < password_length; i++){
         // Generates the next character and adds it to the string builder
         pw_builder.append(generatePasswordCharacter());
      }
      // Return the generated password
      return pw_builder.toString();
   }

   /***
    * Generates a random character using ASCII decimal values as the range
    * @return a valid random ASCII character
    */
   private char generatePasswordCharacter(){
      Random r = new Random();
      // valid range for characters is: 32 - 126
      // nextInt(X) generates a value between 0 an X with std dist.
      int next_char = r.nextInt(126 - 32 + 1) + 32; // lift the min of nextInt() to 32 instead of 0
      return (char)(next_char); // Cast the int as a character to return it
   }
}