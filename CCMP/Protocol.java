import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class Protocol {

    public static byte[] getSlice(byte[] array, int startIndex, int endIndex)
    {
        // Get the slice of the Array
        byte[] slicedArray = new byte[endIndex - startIndex];
        //copying array elements from the original array to the newly created sliced array
        for (int i = 0; i < slicedArray.length; i++)
        {
            slicedArray[i] = array[startIndex + i];
        }
        //returns the slice of an array
        return slicedArray;
    }
    public static EncryptedFrame encryptFrame(ClearTextFrame frame, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException {

        EncryptedFrame encryptedFrame = new EncryptedFrame();
        byte [] SA = getSlice(frame.getFrameHeader().getSourceAddress(), 0, 16);
        byte [] DA = getSlice(frame.getFrameHeader().getDestinationAddress(), 0, 16);

        byte [] iv = AES.encrypt(frame.getIV(), secretKey);
//        iv = cipher.update(iv);

        //TODO: MIC
        //source addr
        int count = 0;
        for(int i = 0; i< SA.length; i++){
            iv[count] = (byte) (i ^ iv[count]);
            count++;
        }
        iv = AES.encrypt(iv, secretKey);

        //dest addr
        count = 0;
        for(int i = 0; i< DA.length; i++){
            iv[count] = (byte) (i ^ iv[count]);
            count++;
        }
        iv = AES.encrypt(iv, secretKey);

        //data for mic
        int numOfBlocks;
        byte [] data = frame.getData();

        if (data.length % 16 == 0){
            numOfBlocks = data.length / 16;
        }else {
            numOfBlocks = (data.length / 16) + 1;
        }

        count = 0;
        byte[][] blocks = new byte[numOfBlocks][16];
        for (int i = 0; i < numOfBlocks; i++) {
            for (int j = 0; j < 16; j++) {
                if(count != data.length){
                    blocks[i][j] = data[count++];
                }
            }
        }

        for (int i = 0; i < numOfBlocks; i++) {
            count = 0;
            for (int j = 0; j < 15; j++) {
                iv[count] = (byte) (blocks[i][j] ^ iv[count]);
                count++;
            }
            iv = AES.encrypt(iv, secretKey);
        }


        byte [] MIC = getSlice(iv, 0, 8);
        encryptedFrame.setMIC(MIC);
        frame.setMIC(MIC);

        //TODO: Encryption
        //Ctr preload - nonce + counter, counter starts from 0
        byte[] nonce = new byte[13];
        byte[] counter = new byte[3];
        byte[] ctrPreload = new byte[16];

        counter = "000".getBytes();
        nonce = getSlice(frame.getIV(), 0, 13);

        for (int i = 0; i < 13; i++) {
            ctrPreload[i] = nonce[i];
        }
        int iterator = 13;
        for (int i = 0; i < 3; i++) {
            ctrPreload[iterator++] = counter[i];
        }

        //MIC
        byte [] encryptedMIC = new byte[8];
        byte [] ctrPreloadForMIC = getSlice(AES.encrypt(ctrPreload, secretKey), 0,8);

        int i = 0;
        for (byte b : MIC) {
            encryptedMIC[i] = (byte) (b ^ ctrPreloadForMIC[i]);
            i++;
        }

        encryptedFrame.setEncriptMIC(encryptedMIC);

        //cipherText
        byte[] encryptedText = new byte[frame.getData().length];
        int size = 0;
        for (i = 0; i <numOfBlocks; i++) {
            //counter: 111
            for(int j = 0; j<counter.length; j++){
                counter[j]++;
            }
            iterator = 13;
            for (int j = 0; j < 3; j++) {
                ctrPreload[iterator++] = counter[j];
            }
            
            ctrPreload = AES.encrypt(ctrPreload, secretKey);
                for (int j = 0; j < 16; j++) {
                    if(size != frame.getData().length) {
                        encryptedText[size++] = (byte) (blocks[i][j] ^ ctrPreload[j]);
                    }else{
                        break;
                }
            }

        }

        encryptedFrame.setFrameHeader(frame.getFrameHeader());
        encryptedFrame.setEncryptedData(encryptedText);
        encryptedFrame.setIV(frame.getIV());
        encryptedFrame.setBlocksOfData(numOfBlocks);


        return encryptedFrame;
    }

    public static ClearTextFrame decryptFrame(EncryptedFrame encryptedFrame, String keySecret){

        byte [] clearText = new byte[encryptedFrame.getEncryptedData().length];

        byte[] nonce = new byte[13];
        byte[] counter = new byte[3];
        byte[] ctrPreload = new byte[16];

        counter = "000".getBytes();
        nonce = getSlice(encryptedFrame.getIV(), 0, 13);

        int numOfBlocks;
        byte [] data = encryptedFrame.getEncryptedData();

        int count = 0;
        byte[][] blocks = new byte[encryptedFrame.getBlocksOfData()][16];
        for (int i = 0; i < encryptedFrame.getBlocksOfData(); i++) {
            for (int j = 0; j < 16; j++) {
                if(count != data.length){
                    blocks[i][j] = data[count++];
                }
            }
        }

        for (int i = 0; i < 13; i++) {
            ctrPreload[i] = nonce[i];
        }
        int iterator = 13;
        for (int i = 0; i < 3; i++) {
            ctrPreload[iterator++] = counter[i];
        }

        int size = 0;
        for (int i = 0; i <encryptedFrame.getBlocksOfData(); i++) {
            //counter: 111
            for(int j = 0; j<counter.length; j++){
                counter[j]++;
            }
            iterator = 13;
            for (int j = 0; j < 3; j++) {
                ctrPreload[iterator++] = counter[j];
            }

            ctrPreload = AES.encrypt(ctrPreload, keySecret);
            for (int j = 0; j < 16; j++) {
                if(size != encryptedFrame.getEncryptedData().length) {
                    clearText[size++] = (byte) (blocks[i][j] ^ ctrPreload[j]);
                }else{
                    break;
                }
            }

        }

        ClearTextFrame clearTextFrame = new ClearTextFrame(new String(encryptedFrame.getFrameHeader().getSourceAddress()),
               new String( encryptedFrame.getFrameHeader().getDestinationAddress()), new String(clearText));

        clearTextFrame.setData(clearText);
        clearTextFrame.setMIC(encryptedFrame.getMIC());

        return clearTextFrame;
    }
}
