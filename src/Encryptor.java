import java.math.BigInteger;
import java.util.ArrayList;

public class Encryptor {

    private final long SYMBOL_COUNT = 200;
    private int blockSize;

    Encryptor() {
        blockSize = (int) Math.floor(Math.log10(SYMBOL_COUNT) / Math.log10(2));
    }

    public String encryptMessage(String message, BigInteger[] key) {

        ArrayList<BigInteger> encryptedBlocks = new ArrayList<>();
        String estring = "";

        BigInteger n = key[0];
        BigInteger e = key[1];

        ArrayList<Long> blocks = getBlocksFromText(message);

        for (long block : blocks) {
            BigInteger b = new BigInteger("" + block);
            encryptedBlocks.add(b.modPow(e, n));
        }

        for (int i = 0; i < encryptedBlocks.size(); i++) {
            if (i != encryptedBlocks.size() - 1) {
                estring += encryptedBlocks.get(i).toString() + ",";
            } else {
                estring += encryptedBlocks.get(i).toString();
            }
        }

        return String.format("%d_%d_%s", message.length(), blockSize, estring);

    }

    private ArrayList<Long> getBlocksFromText(String message) {

        ArrayList<Long> blockInts = new ArrayList<>();

        for (int blockStart = 0; blockStart < message.length(); blockStart += blockSize) {

            long blockInt = 0;

            for (int i = blockStart; i < Math.min(blockStart + blockSize, message.length()); i++) {
                blockInt += message.charAt(i) * (Math.pow(SYMBOL_COUNT, (i % blockSize)));
            }

            blockInts.add(blockInt);

        }

        return blockInts;

    }

}
