
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class sha1 {
    public static String hash(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            
            byte[] bytes = input.getBytes();
            sha1.update(bytes);

            byte[] hashBytes = sha1.digest();
            
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hexStringBuilder.append(String.format("%02x", hashByte));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException error) {
            error.printStackTrace();
            return null;
        }
    }
}
