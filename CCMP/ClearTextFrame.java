import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class ClearTextFrame {
    FrameHeader frameHeader;
//   public String sourceAddress;
//    public String destinationAddress;
    byte [] data;
    byte [] IV = new byte[16];
    byte [] MIC;

    public ClearTextFrame(String sourceAddress, String destinationAddress, String data){
        generateIV();
//        this.sourceAddress = sourceAddress;
//        this.destinationAddress = destinationAddress;
        frameHeader = new FrameHeader(sourceAddress, destinationAddress);
        this.data = data.getBytes(StandardCharsets.UTF_8);
    }

    public FrameHeader getFrameHeader() {
        return frameHeader;
    }

    public void setFrameHeader(FrameHeader frameHeader) {
        this.frameHeader = frameHeader;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getIV() {
        return IV;
    }

    public byte[] getMIC() {
        return MIC;
    }

    public void setMIC(byte[] MIC) {
        this.MIC = MIC;
    }

    public void generateIV(){
        //The nextBytes() method of java.security.SecureRandom class is used to generate a user-specified number of random bytes.
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(IV);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("FrameHeader: " + frameHeader );
        sb.append("\n Data: "  + new String(data,StandardCharsets.UTF_8) +"\n MIC: " + Base64.getEncoder().encodeToString(MIC) );
        return sb.toString();
    }



}
