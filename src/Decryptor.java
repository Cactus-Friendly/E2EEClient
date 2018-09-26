import java.math.BigInteger;

public class Decryptor {

    private long symbolCount;

    Decryptor(long symbolCount) {
        this.symbolCount = symbolCount;
    }

    public String decryptMessage(String message, BigInteger[] key) {

        BigInteger n = key[1];
        BigInteger d = key[2];

        String[] content = message.split("_");

        int messageLength = Integer.parseInt(content[0]);
        int blockSize = Integer.parseInt(content[1]);
        String[] blocks = content[2].split(",");

        int blockNums = blocks.length;

        BigInteger[] intBlocks = new BigInteger[blockNums];

        for (int i = 0; i < blockNums; i++) {
            intBlocks[i] = new BigInteger(blocks[i]);
        }

        BigInteger[] decryptedBlocks = new BigInteger[blockNums];

        for (int i = 0; i < blockNums; i++) {
            decryptedBlocks[i] = intBlocks[i].modPow(d, n);
        }

        return getTextFromBlock(decryptedBlocks, messageLength, blockSize);

    }

    private String getTextFromBlock(BigInteger[] blockInts, int messageLength, int blockSize) {

        String message = "";

        for (BigInteger blockInt : blockInts) {

            String blockMessage = "";

            for (int i = blockSize - 1; i > -1; i--) {
                if (message.length() + i < messageLength) {
                    BigInteger t = new BigInteger(symbolCount + "");
                    t = t.pow(i);
                    int ascii = blockInt.divide(t).intValue();
                    blockInt = blockInt.mod(t);
                    blockMessage += ((char)ascii) + "";
                }
            }

            blockMessage = new StringBuilder(blockMessage).reverse().toString();
            message += blockMessage;

        }

        return message;

    }

}
