import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class main {

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException {
        String sourceMAC = "00:00:5e:00:53:af";
        String destinationMAC = "ce:92:1d:de:d9:51";
        String message = "THIS SHOULD ALSO BE THE OUTPUT ;)";
        String key = "securekey123";


        ClearTextFrame clearTextFrame = new ClearTextFrame(sourceMAC, destinationMAC, message);

        System.out.println("Encrypted frame:");/*printanje na enkripcijata*/
        EncryptedFrame encryptedFrame = Protocol.encryptFrame(clearTextFrame,key);/*objekt od EncryptedFrame*/
        System.out.println(encryptedFrame);

        System.out.println("\n");

        System.out.println("Derypted frame:");
        ClearTextFrame decryptedFrame = Protocol.decryptFrame(encryptedFrame,key);
        System.out.println(decryptedFrame);/*printanje na dekripcijata*/
    }
}
