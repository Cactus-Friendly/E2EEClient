import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        BigInteger[][] keys = new BigInteger[2][2];

        long symbolCount = 0x10ffff;

        String message = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890 !?.(){}[]-_=+";

        KeyMaker.makeKeys(1024);

        keys[0] = KeyMaker.getPubKey();
        keys[1] = KeyMaker.getPrivKey();

        Encryptor encryptor = new Encryptor(symbolCount, keys[0][0].intValue());
        Decryptor decryptor = new Decryptor(symbolCount);

        String estring = encryptor.encryptMessage(message, keys[0]);
        String dstring = decryptor.decryptMessage(estring, keys[1]);

        int coma = 0;

        for (char c : estring.toCharArray()) {
            if (c == ',') {
               coma++;
            }
        }

        System.out.println(message);
        System.out.println(estring);
        System.out.println(dstring);
        System.out.println(coma);
        System.out.println(message.equals(dstring));

    }

}
