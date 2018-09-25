import java.math.BigInteger;
import java.util.ArrayList;

public class Decryptor {

    private final long SYMBOL_COUNT = 200;

    public String decryptMessage(String message, BigInteger[] key) {

        BigInteger n = key[0];
        BigInteger d = key[1];

        String[] content = message.split("_");

        int messageLength = Integer.parseInt(content[0]);
        int blockSize = Integer.parseInt(content[1]);
        String[] blocks = content[2].split(",");

        int blockNums = blocks.length;

        BigInteger[] intBlocks = new BigInteger[blockNums];

        for (int i = 0; i < blockNums; i++) {
            intBlocks[i] = new BigInteger(blocks[i]);
        }

        long[] decryptedBlocks = new long[blockNums];

        for (int i = 0; i < blockNums; i++) {
            decryptedBlocks[i] = intBlocks[i].modPow(d, n).longValue();
        }

        return getTetxFromBlock(decryptedBlocks, messageLength, blockSize);

    }

    private String getTetxFromBlock(long[] blockInts, int messageLength, int blockSize) {

        String message = "";

        for (long blockInt : blockInts) {

            String blockMessage = "";

            for (int i = blockSize - 1; i > -1; i--) {
                if (message.length() + i < messageLength) {
                    int ascii = (int) (blockInt / Math.pow(SYMBOL_COUNT, i));
                    blockInt = (long) (blockInt % Math.pow(SYMBOL_COUNT, i));
                    blockMessage += ((char)ascii) + "";
                }
            }

            blockMessage = new StringBuilder(blockMessage).reverse().toString();
            message += blockMessage;

        }

        return message;

    }

}
