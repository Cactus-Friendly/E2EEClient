import java.math.BigInteger;
import java.util.ArrayList;

public class Encryptor {

    private static final double LOG2 = Math.log(2.0);
    private long symbolCount;
    private long blockSize;

    Encryptor(long symbolCount, int keySize) {

        this.symbolCount = symbolCount;

       BigInteger bits = BigInteger.TWO;

        bits = bits.pow(keySize);

        blockSize = (long) Math.floor(logBigInteger(bits) / Math.log(symbolCount));
    }

    public String encryptMessage(String message, BigInteger[] key) {

        ArrayList<BigInteger> encryptedBlocks = new ArrayList<>();
        String estring = "";

        BigInteger n = key[1];
        BigInteger e = key[2];

        ArrayList<BigInteger> blocks = getBlocksFromText(message);

        for (BigInteger block : blocks) {
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

    private ArrayList<BigInteger> getBlocksFromText(String message) {

        ArrayList<BigInteger> blockInts = new ArrayList<>();
        BigInteger letter, symbol;

        for (int blockStart = 0; blockStart < message.length(); blockStart += blockSize) {

            BigInteger blockInt = BigInteger.ZERO;

            for (int i = blockStart; i < Math.min(blockStart + blockSize, message.length()); i++) {
                int ascii = message.charAt(i);
                letter = new BigInteger(ascii + "");
                symbol = new BigInteger(symbolCount + "");
                int modi = (int) (i % blockSize);
                symbol = symbol.pow(modi);
                letter = letter.multiply(symbol);
                blockInt = blockInt.add(letter);
            }

            blockInts.add(blockInt);

        }

        return blockInts;

    }

    private double logBigInteger(BigInteger val) {
        int blex = val.bitLength() - 1022;
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG2 : res;
    }

}
