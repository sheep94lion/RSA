import java.util.Arrays;
import java.util.Random;

public class MyBigInteger {
    //The first byte is the most significant byte
    private byte[] bytesBI;

    public MyBigInteger(byte[] bytes){
        while (bytes[0] == 0 && bytes.length != 1) {
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
        byte[] a = {3, 5, 7, 78, 23, 123, 101, 89};
        byte[] b = {5, 123, 8, 8, 101, 32};
        MyBigInteger mba = new MyBigInteger(a);
        MyBigInteger mbb = new MyBigInteger(b);
        MyBigInteger quotient = new MyBigInteger(0);
        MyBigInteger remain = new MyBigInteger(0);
        mba.divide(mbb, quotient, remain);
        quotient.print();
        remain.print();
    }

    public static boolean isPrime(MyBigInteger myBI) {
        return true;
    }

    public void print() {
        for (int i = 0; i < this.bytesBI.length; i++) {
            System.out.print((int)this.bytesBI[i] & 0xff);
            System.out.print(" ");
        }
        System.out.println();
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
            sum = fromA + fromThis + carry;
            remain = (byte)(sum % 256);
            carry = (byte)(sum / 256);
            this.bytesBI[this.bytesBI.length - i] = remain;
        }
    }

    public void trim() {
        int i = 0;
        while (true) {
            if (this.bytesBI[i] == 0 && i < this.bytesBI.length - 1) {
                i++;
            } else {
                break;
            }
        }
        byte[] bytes = new byte[this.bytesBI.length - i];
        System.arraycopy(this.bytesBI, i, bytes, 0, bytes.length);
        setBytesBI(bytes);
    }

    public void subtract(MyBigInteger a) {
        assert this.compareTo(a) > 0;
        int borrow = 0, nextBorrow;
        for (int i = 1; i <= this.bytesBI.length; i++) {
            int fromThis = (int)(this.bytesBI[this.bytesBI.length - i]) & 0xff;
            int fromA;
            if (i <= a.bytesBI.length) {
                fromA = (int)(a.bytesBI[a.bytesBI.length - i]) & 0xff;
            } else {
                fromA = 0;
            }
            fromThis = fromThis - borrow;
            if (fromThis < fromA) {
                borrow = 1;
            } else {
                borrow = 0;
            }
            if (fromThis == -1) {
                fromThis = 0xff;
            }
            int result = fromThis - fromA;
            this.bytesBI[this.bytesBI.length - i] = (byte)result;
        }
        this.trim();
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
                this.copyTo(remain);
                remain.subtract(previous);
                break;
            }
            product.add(divisor);
            previous.add(divisor);
        }
        return quotient;
    }

    public void appendByte(byte tail) {
        byte[] bytes = new byte[this.bytesBI.length + 1];
        System.arraycopy(this.bytesBI, 0, bytes, 0, this.bytesBI.length);
        bytes[bytes.length - 1] = tail;
        setBytesBI(bytes);
    }

    public void divide(MyBigInteger divisor, MyBigInteger quotient, MyBigInteger remain) {
        //Find the first position of quotient
        if (this.compareTo(divisor) < 0) {
            byte[] bytes = new byte[1];
            bytes[0] = (byte)0;
            quotient.setBytesBI(bytes);
            this.copyTo(remain);
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
        //quetient[0] ---- this[i]
        //for every byte of quotient
        MyBigInteger currentRemain = new MyBigInteger(0);
        for (int j = 0; j < quotient.bytesBI.length; j++) {
            MyBigInteger currentToDivide = new MyBigInteger(0);
            if (j == 0) {
                currentToDivide = this.firstNBytes(j + i + 1);
            } else {
                currentRemain.copyTo(currentToDivide);
                byte comeDown = this.bytesBI[j + i];
                currentToDivide.appendByte(comeDown);
            }
            byte currentQuotient;
            currentQuotient = currentToDivide.divideSimple(divisor, currentRemain);
            quotient.bytesBI[j] = currentQuotient;
        }
        currentRemain.copyTo(remain);
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
        if (this.getByteLength() > myBI.getByteLength()) {
            return 1;
        } else if (this.getByteLength() < myBI.getByteLength()) {
            return -1;
        }
        for (int i = 0; i < this.getByteLength(); i++) {
            int a  = (int)this.bytesBI[i] & 0xff;
            int b  = (int)myBI.bytesBI[i] & 0xff;
            if (a > b) {
                return 1;
            } else if (a < b) {
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
