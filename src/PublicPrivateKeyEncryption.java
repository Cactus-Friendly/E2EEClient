import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class PublicPrivateKeyEncryption {

    private static final double LOG2 = Math.log10(2.0);
    public static final long SYMBOL_COUNT = 200;

    public static void main(String[] args) {

        Random rand = new Random();

        String message = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 !?.(){}[]-_=+";

        BigInteger p = BigInteger.probablePrime(1024, rand);
        BigInteger q = BigInteger.probablePrime(1024, rand);

        BigInteger[] privKey = new BigInteger[2];
        BigInteger[] pubKey = new BigInteger[2];

        while (p.equals(q)) {
            p = BigInteger.probablePrime(1024, rand);
            q = BigInteger.probablePrime(1024, rand);
        }

        BigInteger n = p.multiply(q);
        BigInteger e = BigInteger.ZERO;

        while (true) {
            e = BigInteger.probablePrime(1024, rand);
            BigInteger p1 = p.subtract(BigInteger.ONE);
            BigInteger q1 = q.subtract(BigInteger.ONE);
            if (gcd(e, p1.multiply(q1)).equals(BigInteger.ONE)) {
                break;
            }
        }

        BigInteger p1 = p.subtract(BigInteger.ONE);
        BigInteger q1 = q.subtract(BigInteger.ONE);
        BigInteger d = e.modInverse(p1.multiply(q1));

        System.out.println("Public Key: " + n + " , " + e);
        System.out.println("Private Key: " + n + " , " + d);

        pubKey[0] = n;
        pubKey[1] = e;
        privKey[0] = n;
        privKey[1] = d;

        String estring = encryptMessage(message, pubKey);
        String dstring = decryptMessage(estring, privKey);

        System.out.println(message);
        System.out.println(estring);
        System.out.println(dstring);


    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {

        while (!a.equals(BigInteger.ZERO)) {
            BigInteger temp = a;
            a = b.mod(a);
            b = temp;
        }

        return b;

    }

    public static ArrayList<Long> getBlocksFromText(String message, int blockSize) {

        ArrayList<Long> blockInts = new ArrayList<>();

        for (int blockStart = 0; blockStart < message.length(); blockStart += blockSize){

            long blockInt = 0;

            for (int i = blockStart; i < Math.min(blockStart + blockSize, message.length()); i++) {
                blockInt += message.charAt(i) * (Math.pow(SYMBOL_COUNT, (i % blockSize)));
            }

            blockInts.add(blockInt);
        }

        return blockInts;

    }

    public static String getTextFromBlock(long[] blockInts, int messageLength, int blockSize) {

        ArrayList<String> message = new ArrayList<>();

        for (long blockInt : blockInts) {
            ArrayList<String> blockMessage = new ArrayList<>();
            for (int i = blockSize - 1; i > -1; i--) {
                if (message.size() + i < messageLength) {
                    int ascii = (int) (blockInt / Math.pow(SYMBOL_COUNT, i));
                    blockInt = (long) (blockInt % Math.pow(SYMBOL_COUNT, i));
                    blockMessage.add(0,((char)ascii) + "");
                }
            }
            message.addAll(blockMessage);
        }

        String endMessage = "";

        for (int i = 0; i < message.size(); i++) {
            endMessage += message.get(i);
        }

        return endMessage;

    }

    public static String encryptMessage(String message, BigInteger[] key) {

        ArrayList<BigInteger> encryptedBlocks = new ArrayList<>();

        String estring = "";

        BigInteger n = key[0];
        BigInteger e = key[1];

        BigInteger twos = new BigInteger("2");
        BigInteger charLen = new BigInteger(SYMBOL_COUNT + "");

        double log = logBigInteger(charLen) / logBigInteger(twos);

        int blockSize = (int) Math.floor(log);

        for (long block : getBlocksFromText(message, blockSize)) {
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

    public static String decryptMessage(String message, BigInteger[] key) {

        BigInteger n = key[0];
        BigInteger d = key[1];

        String[] content = message.split("_");

        int messageLength = Integer.parseInt(content[0]);
        int blockSize = Integer.parseInt((content[1]));
        String[] blocks = content[2].split(",");

        BigInteger[] intBlocks = new BigInteger[blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            intBlocks[i] = new BigInteger(blocks[i]);
        }

        long[] decryptedBlocks = new long[intBlocks.length];

        for (int i = 0; i < intBlocks.length; i++) {
            decryptedBlocks[i] = intBlocks[i].modPow(d, n).longValue();
        }

        return getTextFromBlock(decryptedBlocks, messageLength, blockSize);

    }

    public static double logBigInteger(BigInteger val) {
        int blex = val.bitLength() - 1022; // any value in 60..1023 is ok
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log10(val.doubleValue());
        return blex > 0 ? res + blex * LOG2 : res;
    }

}
