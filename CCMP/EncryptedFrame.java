import java.util.Arrays;
import java.util.Base64;

public class EncryptedFrame {
    FrameHeader frameHeader;
    byte[] encryptedData;
    byte [] encriptMIC;
    byte [] MIC = new byte[8];

    byte [] IV = new byte[16];
    int blocksOfData;


    public void setMIC(byte[] MIC) {
        this.MIC = MIC;
    }

    public void setEncriptMIC(byte[] encriptMIC) {
        this.encriptMIC = encriptMIC;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public void setFrameHeader(FrameHeader frameHeader) {
        this.frameHeader = frameHeader;
    }

    public byte[] getIV() {
        return IV;
    }

    public void setIV(byte[] IV) {
        this.IV = IV;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public int getBlocksOfData() {
        return blocksOfData;
    }

    public FrameHeader getFrameHeader() {
        return frameHeader;
    }

    public void setBlocksOfData(int blocksOfData) {
        this.blocksOfData = blocksOfData;
    }

    public byte[] getMIC() {
        return MIC;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(frameHeader.toString() + "\n Data: " + Base64.getEncoder().encodeToString(encryptedData) +
                "\n MIC: " + Base64.getEncoder().encodeToString(encriptMIC));
        return sb.toString();
    }
}
