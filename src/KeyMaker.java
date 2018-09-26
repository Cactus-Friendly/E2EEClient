import java.math.BigInteger;
import java.util.Random;

public class KeyMaker {

    private static BigInteger[] privKey = new BigInteger[3];
    private static BigInteger[] pubKey = new BigInteger[3];
    private static final Random RAND = new Random();

    public static BigInteger[] getPubKey() {
        return pubKey;
    }

    public static BigInteger[] getPrivKey() {
        return privKey;
    }

    public static void makeKeys(int keySize) {

        BigInteger size = new BigInteger(keySize + "");

        BigInteger p = BigInteger.probablePrime(keySize, RAND);
        BigInteger q = BigInteger.probablePrime(keySize, RAND);

        while (p.equals(q)) {
            p = BigInteger.probablePrime(keySize, RAND);
            q = BigInteger.probablePrime(keySize, RAND);
        }

        BigInteger n = p.multiply(q);
        BigInteger e, p1, q1, d;

        while (true) {
            e = BigInteger.probablePrime(keySize, RAND);
            p1 = p.subtract(BigInteger.ONE);
            q1 = q.subtract(BigInteger.ONE);
            if (gcd(e, p1.multiply(q1)).equals(BigInteger.ONE)) {
                break;
            }
        }

        d = e.modInverse(p1.multiply(q1));

        pubKey[0] = size;
        pubKey[1] = n;
        pubKey[2] = e;
        privKey[0] = size;
        privKey[1] = n;
        privKey[2] = d;

    }

    private static BigInteger gcd(BigInteger a, BigInteger b) {

        while (!a.equals(BigInteger.ZERO)) {
            BigInteger temp = a;
            a = b.mod(a);
            b = temp;
        }

        return b;

    }


}
