import java.util.Random;

public class MyBigInteger {
    private byte[] bytesBI;

    public MyBigInteger(byte[] bytes){
        this.bytesBI = bytes;
    }

    public static void main(String[] args){

    }

    public static boolean isPrime(MyBigInteger myBI) {
        return true;
    }

    public MyBigInteger addInt(int add) {
        return this;
    }

    public static MyBigInteger randomPrime(int bitlength, Random r) {
        byte[] randomBytes = new byte[bitlength];
        r.nextBytes(randomBytes);
        if (randomBytes[randomBytes.length - 1] % 2 == 0) {
            randomBytes[randomBytes.length - 1] += 1;
        }
        while (true) {
            MyBigInteger myBI = new MyBigInteger(randomBytes);
            if (isPrime(myBI)) {
                return myBI;
            } else {
                myBI.addInt(1);
                //TODO if it reach max
            }
        }
    }
}
