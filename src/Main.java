import java.math.BigInteger;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<BigInteger[]> keys = new ArrayList<>();

        Encryptor encryptor = new Encryptor();
        Decryptor decryptor = new Decryptor();

        KeyMaker.makeKeys(1024);

        keys.add(KeyMaker.getPubKey());
        keys.add(KeyMaker.getPrivKey());

    }

}
