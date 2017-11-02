import java.util.Random;

public class MyBigInteger {
    private byte[] bytesBI;

    public MyBigInteger(byte[] bytes){
        while (bytes[0] == 0) {
            byte[] newBytes = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, newBytes, 0, newBytes.length);
            bytes = newBytes;
        }
        this.bytesBI = bytes;
    }

    public static void main(String[] args){
        byte a = (byte)0xff;
        a++;
        System.out.print(a);
    }

    public static boolean isPrime(MyBigInteger myBI) {
        return true;
    }

    public void addInt(int add) {

    }

    public void incOne() {
        int i;
        for (i = this.bytesBI.length - 1; i >= 0; i--) {
            this.bytesBI[i]++;
            if (this.bytesBI[i] > 0) {
                break;
            }
        }
        if (i == -1) {
            byte[] newBytes = new byte[this.bytesBI.length + 1];
            newBytes[0] = 1;
            System.arraycopy(this.bytesBI, 0, newBytes, 1, this.bytesBI.length);
            this.bytesBI = newBytes;
        }
    }

    public static MyBigInteger max(int byteLength) {
        byte[] bytes = new byte[byteLength];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)0xff;
        }
        return new MyBigInteger(bytes);
    }

    public int getByteLength() {
        return this.bytesBI.length;
    }

    public byte[] getBytesBI() {
        return this.bytesBI;
    }

    public int compareTo(MyBigInteger myBI) {
        for (int i = 0; i < this.getByteLength(); i++) {
            if (this.bytesBI[i] > myBI.getBytesBI()[i]) {
                return 1;
            } else if (this.bytesBI[i] < myBI.getBytesBI()[i]) {
                return -1;
            }
        }
        return 0;
    }

    public static MyBigInteger randomPrime(int byteLength, Random r) {
        byte[] randomBytes = new byte[byteLength];
        r.nextBytes(randomBytes);
        if (randomBytes[randomBytes.length - 1] % 2 == 0) {
            randomBytes[randomBytes.length - 1] += 1;
        }
        MyBigInteger max = MyBigInteger.max(byteLength);
        while (true) {
            MyBigInteger myBI = new MyBigInteger(randomBytes);
            if (isPrime(myBI)) {
                return myBI;
            } else {
                myBI.incOne();
                myBI.incOne();
                if (myBI.compareTo(max) > 0) {
                    r.nextBytes(randomBytes);
                    if (randomBytes[randomBytes.length - 1] % 2 == 0) {
                        randomBytes[randomBytes.length - 1] += 1;
                    }
                }
            }
        }
    }
}
