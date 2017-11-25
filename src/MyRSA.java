import java.util.Random;

public class MyRSA {
    private int bitLength;
    private int alg;
    private Random rand;
    private MyBigInteger p;
    private MyBigInteger q;
    private MyBigInteger n;
    private MyBigInteger phi;
    private MyBigInteger d;
    private MyBigInteger e;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public MyRSA(int bitLength) {
        this.bitLength = bitLength;

        if (bitLength < 256) {
            alg = 0;
        } else {
            alg = 1;
        }

        //alg = 1;
        rand = new Random();
        this.p = MyBigInteger.randomPrime(bitLength/8, rand, alg);
        this.q = MyBigInteger.randomPrime(bitLength/8, rand, alg);
        MyBigInteger one = new MyBigInteger(1);
        MyBigInteger pM1 = new MyBigInteger(0);
        MyBigInteger qM1 = new MyBigInteger(0);
        p.copyTo(pM1);
        q.copyTo(qM1);
        pM1.subtract(one);
        qM1.subtract(one);
        this.phi = pM1.multiply(qM1);
        this.n = p.multiply(q);
        this.e = MyBigInteger.randomPrime(bitLength/16, rand, alg);
        while (phi.gcd(e).compareTo(one) != 0) {
            e.add(one);
            if (e.compareTo(phi) >= 0) {
                e = MyBigInteger.randomPrime(bitLength/16, rand, alg);
            }
        }
        //System.out.println("modInverse");
        this.d = e.modInverse(phi, alg);
    }

    public MyRSA(MyBigInteger e, MyBigInteger d, MyBigInteger n, MyBigInteger p, MyBigInteger q)
    {
        this.e = e;
        this.d = d;
        this.n = n;
        this.p = p;
        this.q = q;
    }

    public static void main(String[] args) {



        MyRSA myrsa = new MyRSA(64);
        String input = "ha";

        byte[] encryptedBytes = myrsa.encrypt(input.getBytes());

        byte[] decryptedBytes = myrsa.decrypt(encryptedBytes);

        System.out.println("result: " + new String(decryptedBytes));

    }

    public byte[] encrypt(byte[] message)
    {
        /*
        if (this.bitLength < 256) {
            alg = 0;
        } else {
            alg = 1;
        }
        */
        MyBigInteger en = new MyBigInteger(message);
        MyBigInteger ened = en.powMod(this.e, this.n, alg);
        return ened.getBytesBI();
    }

    public byte[] decrypt(byte[] message)
    {
        /*
        if (this.bitLength < 256) {
            alg = 0;
        } else {
            alg = 1;
        }
        */
        MyBigInteger de = new MyBigInteger(message);
        MyBigInteger deed = de.powMod(this.d, this.n, alg);
        return deed.getBytesBI();
    }

    public String getPString() {
        return bytesToHex(p.getBytesBI());
    }

    public String getQString() {
        return bytesToHex(q.getBytesBI());
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public String getNString() {
        return bytesToHex(n.getBytesBI());
    }

    public String getDString() {
        return bytesToHex(d.getBytesBI());
    }

    public String getEString() {
        return bytesToHex(e.getBytesBI());
    }
}
