import static java.nio.charset.StandardCharsets.UTF_8;

public class FrameHeader {
    public String sourceAddress;
    public String destinationAddress;

    public FrameHeader(String sourceAddress, String destinationAddress) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
    }

    public byte[] getSourceAddress(){
        return sourceAddress.getBytes(UTF_8);
    }

    public byte[] getDestinationAddress(){
        return destinationAddress.getBytes(UTF_8);
    }

    @Override
    public String toString() {
        return "SourceAddress: '" + sourceAddress +
                " \n destinationAddress: '" + destinationAddress;
    }
}
