import java.util.Random;

/**
 * Created by Patrick on 26-Sep-15.
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

   public String generatePassword(int given_length){
      if(given_length < 8 || given_length > 80){
         System.out.println("Invalid password length.\nPlease choose a length between 8 and 80 characters.");
         System.exit(1);
      }
      password_length = given_length;
      StringBuilder pw_builder = new StringBuilder();
      for(int i = 0; i < password_length; i++){
         pw_builder.append(generatePasswordCharacter());
      }
      return pw_builder.toString();
   }

   private char generatePasswordCharacter(){
      // valid range for characters is: 32 - 126
      Random r = new Random();
      int next_char = r.nextInt(126 - 32 + 1) + 32;
      //System.out.println(next_char + ";" + (char)next_char);
      return (char)(next_char);
   }
}