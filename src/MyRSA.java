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

    public MyRSA(int bitLength) {
        this.bitLength = bitLength;
        if (bitLength < 128) {
            alg = 0;
        } else {
            alg = 1;
        }
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
        this.d = e.modInverse(phi, alg);
    }

    public MyRSA(MyBigInteger e, MyBigInteger d, MyBigInteger n)
    {
        this.e = e;
        this.d = d;
        this.n = n;
    }

    public static void main(String[] args) {
        MyRSA myrsa = new MyRSA(256);
        String input = "haoxiangdeifenkuai";

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
}
