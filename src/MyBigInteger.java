import java.util.Arrays;
import java.util.Random;

public class MyBigInteger {
    //The first byte is the most significant byte
    private byte[] bytesBI;

    public MyBigInteger(byte[] bytes){
        while (bytes[0] == 0) {
            byte[] newBytes = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, newBytes, 0, newBytes.length);
            bytes = newBytes;
        }
        this.bytesBI = bytes;
    }

    public MyBigInteger(int n) {
        if (n == 0) {
            this.bytesBI = new byte[1];
            this.bytesBI[0] = (byte)0;
        }
    }

    public static void main(String[] args){
        byte a = (byte)0xff;
        byte b = (byte)0xff;

        int c = ((int)a & 0xff) + ((int)b & 0xff);
        a = (byte)(c % 256);
        b = (byte)(c / 256);

        System.out.print((int)a & 0xff);
    }

    public static boolean isPrime(MyBigInteger myBI) {
        return true;
    }

    public void addInt(int add) {

    }

    public void add(MyBigInteger a) {
        if (this.bytesBI.length < a.bytesBI.length) {
            byte[] bytes = new byte[a.bytesBI.length];
            int i;
            for (i = 0; i < a.bytesBI.length - this.bytesBI.length; i++) {
                bytes[i] = 0;
            }
            System.arraycopy(this.bytesBI, 0, bytes, i, this.bytesBI.length);
            this.setBytesBI(bytes);
        }
        byte remain, carry = 0;
        for (int i = 1; ; i++) {
            int sum;
            int fromA, fromThis;
            if (a.bytesBI.length - i < 0 && this.bytesBI.length - i < 0) {
                if (carry != 0) {
                    byte[] bytes = new byte[this.bytesBI.length + 1];
                    bytes[0] = carry;
                    System.arraycopy(this.bytesBI, 0, bytes, 1, this.bytesBI.length);
                    this.setBytesBI(bytes);
                }
                break;
            } else if (a.bytesBI.length - i < 0) {
                fromA = 0;
            } else {
                fromA = (int)(a.bytesBI[a.bytesBI.length - i]) & 0xff;
            }
            fromThis = (int)(this.bytesBI[this.bytesBI.length - i]) & 0xff;
            sum = ((int)a.bytesBI[a.bytesBI.length - i] & 0xff) + ((int)this.bytesBI[this.bytesBI.length - i] & 0xff) + ((int)carry & 0xff);
            remain = (byte)(sum % 256);
            carry = (byte)(sum / 256);
            this.bytesBI[this.bytesBI.length - i] = remain;
        }
    }

    public void subtract(MyBigInteger a) {
        assert this.compareTo(a) > 0;
        
    }

    public void setBytesBI(byte[] bytes) {
        this.bytesBI = new byte[bytes.length];
        System.arraycopy(bytes, 0, this.bytesBI, 0, bytes.length);
    }

    public void copyTo(MyBigInteger dst) {
        dst.setBytesBI(this.bytesBI);
    }

    private MyBigInteger firstNBytes(int n) {
        assert n <= this.bytesBI.length;
        byte[] bytes = Arrays.copyOfRange(this.bytesBI, 0, n);
        return new MyBigInteger(bytes);
    }

    private byte divideSimple(MyBigInteger divisor, MyBigInteger remain) {
        byte quotient = 0;
        MyBigInteger previous = new MyBigInteger(0);
        MyBigInteger product = new MyBigInteger(0);
        divisor.copyTo(product);
        for (int i = 0; i < 256; i++) {
            if (this.compareTo(product) < 0) {
                quotient = (byte)i;
                break;
            }
            product.add(divisor);
            previous.add(divisor);
            //TODO: remain
        }
        return quotient;
    }

    public void divide(MyBigInteger divisor, MyBigInteger quotient, MyBigInteger remain) {
        //Find the first position of quotient
        if (this.compareTo(divisor) < 0) {
            byte[] bytes = new byte[1];
            bytes[0] = (byte)0;
            quotient.setBytesBI(bytes);
            divisor.copyTo(remain);
            return;
        }
        int i;
        for (i = 0; i < this.bytesBI.length; i++) {
            MyBigInteger toDivide = this.firstNBytes(i+1);
            if (toDivide.compareTo(divisor) > 0) {
                break;
            }
        }
        int lengthOfQuotient = this.bytesBI.length - i;
        byte[] bytes = new byte[lengthOfQuotient];
        quotient.setBytesBI(bytes);
        for (int j = 0; j < quotient.bytesBI.length; j++) {
            MyBigInteger currentToDivide = this.firstNBytes(j + i + 1);
            MyBigInteger currentRemain = new MyBigInteger(0);
            byte currentQuotient;
            //currentQuotient =
        }
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
        MyBigInteger myBI = new MyBigInteger(randomBytes);
        while (true) {
            if (isPrime(myBI)) {
                return myBI;
            } else {
                myBI.incOne();
                myBI.incOne();
                if (myBI.compareTo(max) > 0) {
                    return randomPrime(byteLength, r);
                }
            }
        }
    }
}
