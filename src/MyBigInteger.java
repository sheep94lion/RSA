
import java.util.Arrays;
import java.util.Random;
import java.math.BigInteger;

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
        if (n < 256) {
            this.bytesBI = new byte[1];
            this.bytesBI[0] = (byte)n;
        }
    }

    public static void main(String[] args){
        int n = 8 / 9;
        byte ii = -128;
        byte jj = (byte)(ii >>> 3);
        Random r;
        r = new Random();
        //boolean bl = isPrime(new MyBigInteger(113));
        MyBigInteger rp = randomPrime(128, r, 1);
        rp.print();
        BigInteger rpJ = rp.giveupAnd2BigInteger();
        System.out.print(rpJ.isProbablePrime(100));
        /*
        byte[] a = {-1, 67};
        byte[] b = {101, 89};
        byte[] c = {1, -1};

        MyBigInteger mba = new MyBigInteger(a);
        MyBigInteger mbaa = new MyBigInteger(0);
        mba.copyTo(mbaa);
        mba.print();
        //mba.rightShift(4);
        //mba.rightShift(4);
        //int r = mba.getRAndBeD();
        //mba.print();
        MyBigInteger mbb = new MyBigInteger(b);
        MyBigInteger mbc = new MyBigInteger(c);
        MyBigInteger rr = mba.powMod(mbb, mbc);
        rr.print();

        MyBigInteger quotient = new MyBigInteger(0);
        MyBigInteger remain = new MyBigInteger(0);
        //MyBigInteger product = new MyBigInteger(0);
        //product = mba.multiply(mbb);
        //product.print();
        mbaa.divide(mba, quotient, remain);
        quotient.print();
        remain.print();
        */
    }

    public boolean diviseable(MyBigInteger divisor) {
        MyBigInteger quotient = new MyBigInteger(0);
        MyBigInteger remain = new MyBigInteger(0);
        this.divide(divisor, quotient, remain);
        MyBigInteger n0 = new MyBigInteger(0);
        if (remain.compareTo(n0) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void removeLastNBytes(int n) {
        byte[] bytes = new byte[this.getByteLength() - n];
        int newL = this.getByteLength() - n;
        System.arraycopy(this.bytesBI, 0, bytes, 0, newL);
        this.bytesBI = bytes;
    }

    public void rightShift(int n) {
        if (n == 0) {
            return;
        }
        int[] masks = {0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff};
        int mask = masks[8-n-1];
        //n should be smaller than 8
        for (int i = this.getByteLength() - 1; i >= 0; i--) {
            if (i < this.getByteLength() - 1) {
                byte temp = (byte)(this.bytesBI[i] << (8-n));
                this.bytesBI[i + 1] = (byte)(this.bytesBI[i + 1] | temp);
            }
            this.bytesBI[i] = (byte)((this.bytesBI[i] >>> n) & mask);
        }
        this.trim();
    }

    public int getRAndBeD() {
        int r = 0;
        while (this.bytesBI[this.getByteLength() - 1 - r / 8] == (byte)0) {
            r = r + 8;
        }
        this.removeLastNBytes(r / 8);
        int nToShift = 0;
        while ((this.bytesBI[this.getByteLength() - 1] & (1 << nToShift)) == 0) {
            nToShift++;
            r++;
        }
        this.rightShift(nToShift);
        return r;
    }

    public static boolean witnessLoop(MyBigInteger n, int k, Random rand, int alg) {
        int r = 0;
        MyBigInteger nD = new MyBigInteger(0);
        MyBigInteger n1 = new MyBigInteger(1);
        MyBigInteger n2 = new MyBigInteger(2);
        MyBigInteger nM1 = new MyBigInteger(0);
        MyBigInteger nM2 = new MyBigInteger(0);
        n.copyTo(nD);
        n.copyTo(nM2);
        nM2.subtract(new MyBigInteger(2));
        nD.subtract(new MyBigInteger(1));
        nD.copyTo(nM1);
        r = nD.getRAndBeD();

        for (int i = 0; i < k; i++) {
            int l = nM2.getByteLength();
            byte[] bytes = new byte[l];
            rand.nextBytes(bytes);
            MyBigInteger randTemp = new MyBigInteger(bytes);
            MyBigInteger randR = new MyBigInteger(0);
            MyBigInteger quotient = new MyBigInteger(0);
            randTemp.divide(nM1, quotient, randR);
            if (randR.compareTo(n2) < 0) {
                n2.copyTo(randR);
            }
            MyBigInteger x = new MyBigInteger(0);
            x = randR.powMod(nD, n, alg);
            if (x.compareTo(n1) == 0 || x.compareTo(nM1) == 0) {
                continue;
            }

            for (int j = 0; j < r - 1; j++) {
                x = x.powMod(n2, n, alg);
                if (x.compareTo(n1) == 0) {
                    return false;
                }
                if (x.compareTo(nM1) == 0) {
                    break;
                }
            }
            if (x.compareTo(nM1) == 0) {
                continue;
            }
            return false;
        }
        return true;
    }

    public int getbitLength() {
        int nByte = this.getByteLength() - 1;
        int firstByte = this.bytesBI[0] & 0xff;
        int i = 8;
        while (true) {
            int mask = 1 << (i - 1);
            if ((firstByte & mask) != 0) {
                break;
            }
            i--;
        }
        return nByte * 8 + i;
    }

    public int getBit(int n) {
        //count from right, from 0
        int iByte = n / 8;
        int cByte = this.bytesBI[this.getByteLength() - 1 - iByte] & 0xff;
        int iBit = n - iByte * 8;
        int mask = 1 << (iBit);
        if ((cByte & mask) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public void mod(MyBigInteger n) {
        if (this.compareTo(n) >= 0) {
            MyBigInteger temp = new MyBigInteger(0);
            MyBigInteger q = new MyBigInteger(0);
            this.copyTo(temp);
            temp.divide(n, q, this);
        }
    }

    public MyBigInteger powMod(MyBigInteger pow, MyBigInteger n) {
        MyBigInteger n0 = new MyBigInteger(0);
        MyBigInteger remain = new MyBigInteger(1);
        if (pow.compareTo(n0) == 0) {
            return remain;
        }
        int nBitLength = pow.getbitLength();
        MyBigInteger refs[] = new MyBigInteger[nBitLength];
        MyBigInteger ref = new MyBigInteger(0);
        this.copyTo(ref);
        for (int i = 0; i < nBitLength; i++) {
            ref.mod(n);
            refs[i] = ref;
            ref = new MyBigInteger(0);
            ref = refs[i].multiply(refs[i]);
        }
        for (int i = 0; i < nBitLength; i++) {
            if (pow.getBit(i) == 1) {
                remain = remain.multiply(refs[i]);
                remain.mod(n);
            }
        }
        /*
        for (; i.compareTo(pow) < 0; i.incOne()) {
            remain = this.multiply(remain);
            if (remain.compareTo(n) >= 0) {
                MyBigInteger temp = new MyBigInteger(0);
                MyBigInteger q = new MyBigInteger(0);
                remain.copyTo(temp);
                temp.divide(n, q, remain);
            }
        }
        */
        return remain;
    }

    public BigInteger giveupAnd2BigInteger() {
        byte[] bytes;
        if (this.bytesBI[0] < 0) {
            bytes = new byte[this.getByteLength() + 1];
            bytes[0] = 0;
            System.arraycopy(this.bytesBI, 0, bytes, 1, this.getByteLength());
        } else {
            bytes = new byte[this.getByteLength()];
            System.arraycopy(this.bytesBI, 0, bytes, 0, this.getByteLength());
        }
        return new BigInteger(bytes);
    }

    public MyBigInteger powMod(MyBigInteger pow, MyBigInteger n, int alg) {
        if (alg == 0) {
            return this.powMod(pow, n);
        }
        BigInteger powJ = pow.giveupAnd2BigInteger();
        BigInteger th = this.giveupAnd2BigInteger();
        BigInteger nJ = n.giveupAnd2BigInteger();
        BigInteger rJ = th.modPow(powJ, nJ);
        byte[] bytes = rJ.toByteArray();
        MyBigInteger r = new MyBigInteger(bytes);
        return r;
    }

    public static boolean isPrime(MyBigInteger myBI, int k, Random rand, int alg) {
        return witnessLoop(myBI, k, rand, alg);
    }

    public static boolean isPrime(MyBigInteger myBI) {
        if (myBI.getByteLength() == 1) {
            int n = (int)myBI.getBytesBI()[0];
            if (n == 2 || n == 3) {
                return true;
            }
        }
        MyBigInteger n2 = new MyBigInteger(2);
        MyBigInteger n3 = new MyBigInteger(3);
        if (myBI.diviseable(n2) || myBI.diviseable(n3)) {
            return false;
        }
        MyBigInteger i = new MyBigInteger(5);
        MyBigInteger w = new MyBigInteger(2);
        MyBigInteger n6 = new MyBigInteger(6);

        while(i.multiply(i).compareTo(myBI) <= 0) {
            //i.print();
            if (myBI.diviseable(i)) {
                return false;
            }
            i.add(w);
            n6.subtract(w);
            n6.copyTo(w);
            n6 = new MyBigInteger(6);
        }

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

    public void appendBytes(byte[] tail) {
        byte[] bytes = new byte[this.getByteLength() + tail.length];
        System.arraycopy(this.bytesBI, 0, bytes, 0, this.getByteLength());
        System.arraycopy(tail, 0, bytes, this.getByteLength(), tail.length);
        setBytesBI(bytes);
    }

    public void appendZeroByte(int n) {
        byte[] bytesZero = new byte[n];
        Arrays.fill(bytesZero, (byte)0);
        appendBytes(bytesZero);
    }

    public MyBigInteger multiplySimple(int a) {
        byte[] bytes = new byte[this.getByteLength() + 1];
        MyBigInteger product = new MyBigInteger(0);
        product.setBytesBI(bytes);
        int carry = 0;
        for (int i = 0; i < this.getByteLength(); i++) {
            int fromThis = ((int)this.bytesBI[this.getByteLength() - 1 - i]) & 0xff;
            int currentProduct = fromThis * a + carry;
            int remain = currentProduct % 256;
            carry = currentProduct / 256;
            product.bytesBI[product.getByteLength() - 1 - i] = (byte)remain;
        }
        product.bytesBI[0] = (byte)carry;
        product.trim();
        return product;
    }

    public MyBigInteger multiply(MyBigInteger a) {
        //itself doesn't change
        MyBigInteger product = new MyBigInteger(0);
        for (int i = 0; i < a.getByteLength(); i++) {
            int currentByteNumber = ((int)a.bytesBI[i]) & 0xff;
            MyBigInteger currentProduct = this.multiplySimple(currentByteNumber);
            currentProduct.appendZeroByte(a.getByteLength() - 1 - i);
            product.add(currentProduct);
        }
        return product;
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
        this.add(new MyBigInteger(1));
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

    public static MyBigInteger randomPrime(int byteLength, Random r, int alg) {
        //System.out.println("call randomPrime");
        byte[] randomBytes = new byte[byteLength];
        r.nextBytes(randomBytes);
        if (randomBytes[randomBytes.length - 1] % 2 == 0) {
            randomBytes[randomBytes.length - 1] += 1;
        }
        MyBigInteger max = MyBigInteger.max(byteLength);
        MyBigInteger myBI = new MyBigInteger(randomBytes);
        while (true) {
            if (isPrime(myBI, 50, r, alg)) {
                return myBI;
            } else {

                myBI.add(new MyBigInteger(2));
                //System.out.println("next candidate");
                if (myBI.compareTo(max) > 0) {
                    return randomPrime(byteLength, r, alg);
                }

                //return randomPrime(byteLength, r);
            }
        }
    }
}
