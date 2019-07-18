// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.nio.ByteOrder;

class CityAndFarmHash_1_1
{
    private static final CityAndFarmHash_1_1 INSTANCE;
    static final CityAndFarmHash_1_1 NATIVE_CITY;
    static final long K0 = -4348849565147123417L;
    static final long K1 = -5435081209227447693L;
    static final long K2 = -7286425919675154353L;
    private static final long K_MUL = -7070675565921424023L;
    
    static long shiftMix(final long val) {
        return val ^ val >>> 47;
    }
    
    static long hashLen16(final long u, final long v) {
        return hashLen16(u, v, -7070675565921424023L);
    }
    
    static long hashLen16(final long u, final long v, final long mul) {
        final long a = shiftMix((u ^ v) * mul);
        return shiftMix((v ^ a) * mul) * mul;
    }
    
    static long mul(final long len) {
        return -7286425919675154353L + (len << 1);
    }
    
    static long hash1To3Bytes(final int len, final int firstByte, final int midOrLastByte, final int lastByte) {
        final int y = firstByte + (midOrLastByte << 8);
        final int z = len + (lastByte << 2);
        return shiftMix(y * -7286425919675154353L ^ z * -4348849565147123417L) * -7286425919675154353L;
    }
    
    static long hash4To7Bytes(final long len, final long first4Bytes, final long last4Bytes) {
        final long mul = mul(len);
        return hashLen16(len + (first4Bytes << 3), last4Bytes, mul);
    }
    
    static long hash8To16Bytes(final long len, final long first8Bytes, final long last8Bytes) {
        final long mul = mul(len);
        final long a = first8Bytes - 7286425919675154353L;
        final long c = Long.rotateRight(last8Bytes, 37) * mul + a;
        final long d = (Long.rotateRight(a, 25) + last8Bytes) * mul;
        return hashLen16(c, d, mul);
    }
    
     <T> long fetch64(final Access<T> access, final T in, final long off) {
        return access.getLong(in, off);
    }
    
     <T> int fetch32(final Access<T> access, final T in, final long off) {
        return access.getInt(in, off);
    }
    
    long toLittleEndian(final long v) {
        return v;
    }
    
    int toLittleEndian(final int v) {
        return v;
    }
    
     <T> long hashLen0To16(final Access<T> access, final T in, final long off, final long len) {
        if (len >= 8L) {
            final long a = this.fetch64(access, in, off);
            final long b = this.fetch64(access, in, off + len - 8L);
            return hash8To16Bytes(len, a, b);
        }
        if (len >= 4L) {
            final long a = Primitives.unsignedInt(this.fetch32(access, in, off));
            final long b = Primitives.unsignedInt(this.fetch32(access, in, off + len - 4L));
            return hash4To7Bytes(len, a, b);
        }
        if (len > 0L) {
            final int a2 = access.getUnsignedByte(in, off);
            final int b2 = access.getUnsignedByte(in, off + (len >> 1));
            final int c = access.getUnsignedByte(in, off + len - 1L);
            return hash1To3Bytes((int)len, a2, b2, c);
        }
        return -7286425919675154353L;
    }
    
     <T> long hashLen17To32(final Access<T> access, final T in, final long off, final long len) {
        final long mul = mul(len);
        final long a = this.fetch64(access, in, off) * -5435081209227447693L;
        final long b = this.fetch64(access, in, off + 8L);
        final long c = this.fetch64(access, in, off + len - 8L) * mul;
        final long d = this.fetch64(access, in, off + len - 16L) * -7286425919675154353L;
        return hashLen16(Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d, a + Long.rotateRight(b - 7286425919675154353L, 18) + c, mul);
    }
    
    private <T> long cityHashLen33To64(final Access<T> access, final T in, final long off, final long len) {
        final long mul = mul(len);
        long a = this.fetch64(access, in, off) * -7286425919675154353L;
        long b = this.fetch64(access, in, off + 8L);
        final long c = this.fetch64(access, in, off + len - 24L);
        final long d = this.fetch64(access, in, off + len - 32L);
        final long e = this.fetch64(access, in, off + 16L) * -7286425919675154353L;
        final long f = this.fetch64(access, in, off + 24L) * 9L;
        final long g = this.fetch64(access, in, off + len - 8L);
        final long h = this.fetch64(access, in, off + len - 16L) * mul;
        final long u = Long.rotateRight(a + g, 43) + (Long.rotateRight(b, 30) + c) * 9L;
        final long v = (a + g ^ d) + f + 1L;
        final long w = Long.reverseBytes((u + v) * mul) + h;
        final long x = Long.rotateRight(e + f, 42) + c;
        final long y = (Long.reverseBytes((v + w) * mul) + g) * mul;
        final long z = e + f + c;
        a = Long.reverseBytes((x + z) * mul + y) + b;
        b = shiftMix((z + a) * mul + d + h) * mul;
        return b + x;
    }
    
     <T> long cityHash64(final Access<T> access, final T in, long off, long len) {
        if (len <= 32L) {
            if (len <= 16L) {
                return this.hashLen0To16((Access<Object>)access, in, off, len);
            }
            return this.hashLen17To32((Access<Object>)access, in, off, len);
        }
        else {
            if (len <= 64L) {
                return this.cityHashLen33To64((Access<Object>)access, in, off, len);
            }
            long x = this.fetch64(access, in, off + len - 40L);
            long y = this.fetch64(access, in, off + len - 16L) + this.fetch64(access, in, off + len - 56L);
            long z = hashLen16(this.fetch64(access, in, off + len - 48L) + len, this.fetch64(access, in, off + len - 24L));
            long a3 = len;
            long b3 = z;
            final long w4 = this.fetch64(access, in, off + len - 64L);
            final long x2 = this.fetch64(access, in, off + len - 64L + 8L);
            final long y2 = this.fetch64(access, in, off + len - 64L + 16L);
            final long z2 = this.fetch64(access, in, off + len - 64L + 24L);
            a3 += w4;
            b3 = Long.rotateRight(b3 + a3 + z2, 21);
            final long c3 = a3;
            a3 += x2 + y2;
            b3 += Long.rotateRight(a3, 44);
            long vFirst = a3 + z2;
            long vSecond = b3 + c3;
            long a4 = y - 5435081209227447693L;
            long b4 = x;
            final long w5 = this.fetch64(access, in, off + len - 32L);
            final long x3 = this.fetch64(access, in, off + len - 32L + 8L);
            final long y3 = this.fetch64(access, in, off + len - 32L + 16L);
            final long z3 = this.fetch64(access, in, off + len - 32L + 24L);
            a4 += w5;
            b4 = Long.rotateRight(b4 + a4 + z3, 21);
            final long c4 = a4;
            a4 += x3 + y3;
            b4 += Long.rotateRight(a4, 44);
            long wFirst = a4 + z3;
            long wSecond = b4 + c4;
            x = x * -5435081209227447693L + this.fetch64(access, in, off);
            len = (len - 1L & 0xFFFFFFFFFFFFFFC0L);
            do {
                x = Long.rotateRight(x + y + vFirst + this.fetch64(access, in, off + 8L), 37) * -5435081209227447693L;
                y = Long.rotateRight(y + vSecond + this.fetch64(access, in, off + 48L), 42) * -5435081209227447693L;
                x ^= wSecond;
                y += vFirst + this.fetch64(access, in, off + 40L);
                z = Long.rotateRight(z + wFirst, 33) * -5435081209227447693L;
                long a5 = vSecond * -5435081209227447693L;
                long b5 = x + wFirst;
                final long w6 = this.fetch64(access, in, off);
                final long x4 = this.fetch64(access, in, off + 8L);
                final long y4 = this.fetch64(access, in, off + 16L);
                final long z4 = this.fetch64(access, in, off + 24L);
                a5 += w6;
                b5 = Long.rotateRight(b5 + a5 + z4, 21);
                final long c5 = a5;
                a5 += x4 + y4;
                b5 += Long.rotateRight(a5, 44);
                vFirst = a5 + z4;
                vSecond = b5 + c5;
                long a6 = z + wSecond;
                long b6 = y + this.fetch64(access, in, off + 16L);
                final long w7 = this.fetch64(access, in, off + 32L);
                final long x5 = this.fetch64(access, in, off + 32L + 8L);
                final long y5 = this.fetch64(access, in, off + 32L + 16L);
                final long z5 = this.fetch64(access, in, off + 32L + 24L);
                a6 += w7;
                b6 = Long.rotateRight(b6 + a6 + z5, 21);
                final long c6 = a6;
                a6 += x5 + y5;
                b6 += Long.rotateRight(a6, 44);
                wFirst = a6 + z5;
                wSecond = b6 + c6;
                final long tmp = x;
                x = z;
                z = tmp;
                len -= 64L;
                off += 64L;
            } while (len != 0L);
            return hashLen16(hashLen16(vFirst, wFirst) + shiftMix(y) * -5435081209227447693L + z, hashLen16(vSecond, wSecond) + x);
        }
    }
    
    public static LongHashFunction asLongHashFunctionWithoutSeed() {
        return AsLongHashFunction.INSTANCE;
    }
    
    public static LongHashFunction asLongHashFunctionWithSeed(final long seed) {
        return new AsLongHashFunctionSeeded(-7286425919675154353L, seed);
    }
    
    public static LongHashFunction asLongHashFunctionWithTwoSeeds(final long seed0, final long seed1) {
        return new AsLongHashFunctionSeeded(seed0, seed1);
    }
    
    private <T> long naHashLen33To64(final Access<T> access, final T in, final long off, final long len) {
        final long mul = mul(len);
        final long a = this.fetch64(access, in, off) * -7286425919675154353L;
        final long b = this.fetch64(access, in, off + 8L);
        final long c = this.fetch64(access, in, off + len - 8L) * mul;
        final long d = this.fetch64(access, in, off + len - 16L) * -7286425919675154353L;
        final long y = Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d;
        final long z = hashLen16(y, a + Long.rotateRight(b - 7286425919675154353L, 18) + c, mul);
        final long e = this.fetch64(access, in, off + 16L) * mul;
        final long f = this.fetch64(access, in, off + 24L);
        final long g = (y + this.fetch64(access, in, off + len - 32L)) * mul;
        final long h = (z + this.fetch64(access, in, off + len - 24L)) * mul;
        return hashLen16(Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30) + h, e + Long.rotateRight(f + a, 18) + g, mul);
    }
    
     <T> long naHash64(final Access<T> access, final T in, long off, final long len) {
        final long seed = 81L;
        if (len <= 32L) {
            if (len <= 16L) {
                return this.hashLen0To16((Access<Object>)access, in, off, len);
            }
            return this.hashLen17To32((Access<Object>)access, in, off, len);
        }
        else {
            if (len <= 64L) {
                return this.naHashLen33To64((Access<Object>)access, in, off, len);
            }
            long x = 81L;
            long y = 2480279821605975764L;
            long z = shiftMix(y * -7286425919675154353L + 113L) * -7286425919675154353L;
            long v1 = 0L;
            long v2 = 0L;
            long w1 = 0L;
            long w2 = 0L;
            x = x * -7286425919675154353L + this.fetch64(access, in, off);
            final long end = off + (len - 1L >> 6) * 64L;
            final long last64 = end + (len - 1L & 0x3FL) - 63L;
            do {
                x = Long.rotateRight(x + y + v1 + this.fetch64(access, in, off + 8L), 37) * -5435081209227447693L;
                y = Long.rotateRight(y + v2 + this.fetch64(access, in, off + 48L), 42) * -5435081209227447693L;
                x ^= w2;
                y += v1 + this.fetch64(access, in, off + 40L);
                z = Long.rotateRight(z + w1, 33) * -5435081209227447693L;
                long a = v2 * -5435081209227447693L;
                long b = x + w1;
                final long z2 = this.fetch64(access, in, off + 24L);
                a += this.fetch64(access, in, off);
                b = Long.rotateRight(b + a + z2, 21);
                final long c = a;
                a += this.fetch64(access, in, off + 8L);
                a += this.fetch64(access, in, off + 16L);
                b += Long.rotateRight(a, 44);
                v1 = a + z2;
                v2 = b + c;
                long a2 = z + w2;
                long b2 = y + this.fetch64(access, in, off + 16L);
                final long z3 = this.fetch64(access, in, off + 32L + 24L);
                a2 += this.fetch64(access, in, off + 32L);
                b2 = Long.rotateRight(b2 + a2 + z3, 21);
                final long c2 = a2;
                a2 += this.fetch64(access, in, off + 32L + 8L);
                a2 += this.fetch64(access, in, off + 32L + 16L);
                b2 += Long.rotateRight(a2, 44);
                w1 = a2 + z3;
                w2 = b2 + c2;
                final long t = z;
                z = x;
                x = t;
            } while ((off += 64L) != end);
            off = last64;
            final long mul = -5435081209227447693L + ((z & 0xFFL) << 1);
            w1 += (len - 1L & 0x3FL);
            v1 += w1;
            w1 += v1;
            x = Long.rotateRight(x + y + v1 + this.fetch64(access, in, off + 8L), 37) * mul;
            y = Long.rotateRight(y + v2 + this.fetch64(access, in, off + 48L), 42) * mul;
            x ^= w2 * 9L;
            y += v1 * 9L + this.fetch64(access, in, off + 40L);
            z = Long.rotateRight(z + w1, 33) * mul;
            long a3 = v2 * mul;
            long b3 = x + w1;
            final long z4 = this.fetch64(access, in, off + 24L);
            a3 += this.fetch64(access, in, off);
            b3 = Long.rotateRight(b3 + a3 + z4, 21);
            final long c3 = a3;
            a3 += this.fetch64(access, in, off + 8L);
            a3 += this.fetch64(access, in, off + 16L);
            b3 += Long.rotateRight(a3, 44);
            v1 = a3 + z4;
            v2 = b3 + c3;
            long a4 = z + w2;
            long b4 = y + this.fetch64(access, in, off + 16L);
            final long z5 = this.fetch64(access, in, off + 32L + 24L);
            a4 += this.fetch64(access, in, off + 32L);
            b4 = Long.rotateRight(b4 + a4 + z5, 21);
            final long c4 = a4;
            a4 += this.fetch64(access, in, off + 32L + 8L);
            a4 += this.fetch64(access, in, off + 32L + 16L);
            b4 += Long.rotateRight(a4, 44);
            w1 = a4 + z5;
            w2 = b4 + c4;
            final long t2 = z;
            z = x;
            x = t2;
            return hashLen16(hashLen16(v1, w1, mul) + shiftMix(y) * -4348849565147123417L + z, hashLen16(v2, w2, mul) + x, mul);
        }
    }
    
     <T> long naHash64WithSeeds(final Access<T> access, final T in, final long off, final long len, final long seed0, final long seed1) {
        return hashLen16(this.naHash64(access, in, off, len) - seed0, seed1);
    }
    
    long uoH(final long x, final long y, final long mul, final int r) {
        long a = (x ^ y) * mul;
        a = shiftMix(a);
        final long b = (y ^ a) * mul;
        return Long.rotateRight(b, r) * mul;
    }
    
     <T> long uoHash64WithSeeds(final Access<T> access, final T in, long off, final long len, final long seed0, final long seed1) {
        if (len <= 64L) {
            return this.naHash64WithSeeds((Access<Object>)access, in, off, len, seed0, seed1);
        }
        long x = seed0;
        long y = seed1 * -7286425919675154353L + 113L;
        long z = shiftMix(y * -7286425919675154353L) * -7286425919675154353L;
        long v0 = seed0;
        long v2 = seed1;
        long w0 = 0L;
        long w2 = 0L;
        long u = x - z;
        x *= -7286425919675154353L;
        final long mul = -7286425919675154353L + (u & 0x82L);
        final long end = off + (len - 1L >> 6) * 64L;
        final long last64 = end + (len - 1L & 0x3FL) - 63L;
        do {
            final long a0 = this.fetch64(access, in, off);
            final long a2 = this.fetch64(access, in, off + 8L);
            final long a3 = this.fetch64(access, in, off + 16L);
            final long a4 = this.fetch64(access, in, off + 24L);
            final long a5 = this.fetch64(access, in, off + 32L);
            final long a6 = this.fetch64(access, in, off + 40L);
            final long a7 = this.fetch64(access, in, off + 48L);
            final long a8 = this.fetch64(access, in, off + 56L);
            x += a0 + a2;
            y += a3;
            z += a4;
            v0 += a5;
            v2 += a6 + a2;
            w0 += a7;
            w2 += a8;
            x = Long.rotateRight(x, 26);
            x *= 9L;
            y = Long.rotateRight(y, 29);
            z *= mul;
            v0 = Long.rotateRight(v0, 33);
            v2 = Long.rotateRight(v2, 30);
            w0 ^= x;
            w0 *= 9L;
            z = Long.rotateRight(z, 32);
            z += w2;
            w2 += z;
            z *= 9L;
            long t = u;
            u = y;
            y = t;
            z += a0 + a7;
            v0 += a3;
            v2 += a4;
            w0 += a5;
            w2 += a6 + a7;
            x += a2;
            y += a8;
            y += v0;
            v0 += x - y;
            v2 += w0;
            w0 += v2;
            w2 += x - y;
            x += w2;
            w2 = Long.rotateRight(w2, 34);
            t = u;
            u = z;
            z = t;
        } while ((off += 64L) != end);
        off = last64;
        u *= 9L;
        v2 = Long.rotateRight(v2, 28);
        v0 = Long.rotateRight(v0, 20);
        w0 += (len - 1L & 0x3FL);
        u += y;
        y += u;
        x = Long.rotateRight(y - x + v0 + this.fetch64(access, in, off + 8L), 37) * mul;
        y = Long.rotateRight(y ^ v2 ^ this.fetch64(access, in, off + 48L), 42) * mul;
        x ^= w2 * 9L;
        y += v0 + this.fetch64(access, in, off + 40L);
        z = Long.rotateRight(z + w0, 33) * mul;
        long a9 = v2 * mul;
        long b = x + w0;
        final long z2 = this.fetch64(access, in, off + 24L);
        a9 += this.fetch64(access, in, off);
        b = Long.rotateRight(b + a9 + z2, 21);
        final long c = a9;
        a9 += this.fetch64(access, in, off + 8L);
        a9 += this.fetch64(access, in, off + 16L);
        b += Long.rotateRight(a9, 44);
        v0 = a9 + z2;
        v2 = b + c;
        long a10 = z + w2;
        long b2 = y + this.fetch64(access, in, off + 16L);
        final long z3 = this.fetch64(access, in, off + 32L + 24L);
        a10 += this.fetch64(access, in, off + 32L);
        b2 = Long.rotateRight(b2 + a10 + z3, 21);
        final long c2 = a10;
        a10 += this.fetch64(access, in, off + 32L + 8L);
        a10 += this.fetch64(access, in, off + 32L + 16L);
        b2 += Long.rotateRight(a10, 44);
        w0 = a10 + z3;
        w2 = b2 + c2;
        return this.uoH(hashLen16(v0 + x, w0 ^ y, mul) + z - u, this.uoH(v2 + y, w2 + z, -7286425919675154353L, 30) ^ x, -7286425919675154353L, 31);
    }
    
    public static LongHashFunction naWithoutSeeds() {
        return Na.INSTANCE;
    }
    
    public static LongHashFunction naWithSeed(final long seed) {
        return new NaSeeded(-7286425919675154353L, seed);
    }
    
    public static LongHashFunction naWithSeeds(final long seed0, final long seed1) {
        return new NaSeeded(seed0, seed1);
    }
    
    public static LongHashFunction uoWithoutSeeds() {
        return Uo.INSTANCE;
    }
    
    public static LongHashFunction uoWithSeed(final long seed) {
        return new UoWithOneSeed(seed);
    }
    
    public static LongHashFunction uoWithSeeds(final long seed0, final long seed1) {
        return new UoSeeded(seed0, seed1);
    }
    
    static {
        INSTANCE = new CityAndFarmHash_1_1();
        NATIVE_CITY = (LongHashFunction.NATIVE_LITTLE_ENDIAN ? CityAndFarmHash_1_1.INSTANCE : BigEndian.INSTANCE);
    }
    
    private static class BigEndian extends CityAndFarmHash_1_1
    {
        private static final BigEndian INSTANCE;
        
        @Override
         <T> long fetch64(final Access<T> access, final T in, final long off) {
            return Long.reverseBytes(super.fetch64(access, in, off));
        }
        
        @Override
         <T> int fetch32(final Access<T> access, final T in, final long off) {
            return Integer.reverseBytes(super.fetch32(access, in, off));
        }
        
        @Override
        long toLittleEndian(final long v) {
            return Long.reverseBytes(v);
        }
        
        @Override
        int toLittleEndian(final int v) {
            return Integer.reverseBytes(v);
        }
        
        static {
            INSTANCE = new BigEndian();
        }
    }
    
    static class AsLongHashFunction extends LongHashFunction
    {
        public static final AsLongHashFunction INSTANCE;
        private static final long serialVersionUID = 0L;
        private static final int FIRST_SHORT_BYTE_SHIFT;
        private static final int FIRST_SHORT_BYTE_MASK;
        private static final int SECOND_SHORT_BYTE_SHIFT;
        private static final int SECOND_SHORT_BYTE_MASK;
        
        private Object readResolve() {
            return AsLongHashFunction.INSTANCE;
        }
        
        @Override
        public long hashLong(long input) {
            input = CityAndFarmHash_1_1.NATIVE_CITY.toLittleEndian(input);
            final long hash = CityAndFarmHash_1_1.hash8To16Bytes(8L, input, input);
            return this.finalize(hash);
        }
        
        @Override
        public long hashInt(int input) {
            input = CityAndFarmHash_1_1.NATIVE_CITY.toLittleEndian(input);
            final long unsignedInt = Primitives.unsignedInt(input);
            final long hash = CityAndFarmHash_1_1.hash4To7Bytes(4L, unsignedInt, unsignedInt);
            return this.finalize(hash);
        }
        
        @Override
        public long hashShort(final short input) {
            return this.hashChar((char)input);
        }
        
        @Override
        public long hashChar(final char input) {
            final int unsignedInput = input;
            final int firstByte = unsignedInput >> AsLongHashFunction.FIRST_SHORT_BYTE_SHIFT & AsLongHashFunction.FIRST_SHORT_BYTE_MASK;
            final int secondByte = unsignedInput >> AsLongHashFunction.SECOND_SHORT_BYTE_SHIFT & AsLongHashFunction.SECOND_SHORT_BYTE_MASK;
            final long hash = CityAndFarmHash_1_1.hash1To3Bytes(2, firstByte, secondByte, secondByte);
            return this.finalize(hash);
        }
        
        @Override
        public long hashByte(final byte input) {
            final int unsignedByte = Primitives.unsignedByte(input);
            final long hash = CityAndFarmHash_1_1.hash1To3Bytes(1, unsignedByte, unsignedByte, unsignedByte);
            return this.finalize(hash);
        }
        
        @Override
        public long hashVoid() {
            return -7286425919675154353L;
        }
        
        @Override
        public <T> long hash(final T input, final Access<T> access, final long off, final long len) {
            long hash;
            if (access.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
                hash = CityAndFarmHash_1_1.INSTANCE.cityHash64(access, input, off, len);
            }
            else {
                hash = BigEndian.INSTANCE.cityHash64(access, input, off, len);
            }
            return this.finalize(hash);
        }
        
        long finalize(final long hash) {
            return hash;
        }
        
        static {
            INSTANCE = new AsLongHashFunction();
            FIRST_SHORT_BYTE_SHIFT = (AsLongHashFunction.NATIVE_LITTLE_ENDIAN ? 0 : 8);
            FIRST_SHORT_BYTE_MASK = (AsLongHashFunction.NATIVE_LITTLE_ENDIAN ? 255 : -1);
            SECOND_SHORT_BYTE_SHIFT = 8 - AsLongHashFunction.FIRST_SHORT_BYTE_SHIFT;
            SECOND_SHORT_BYTE_MASK = (AsLongHashFunction.NATIVE_LITTLE_ENDIAN ? -1 : 255);
        }
    }
    
    private static class AsLongHashFunctionSeeded extends AsLongHashFunction
    {
        private static final long serialVersionUID = 0L;
        final long seed0;
        final long seed1;
        private transient long voidHash;
        
        private AsLongHashFunctionSeeded(final long seed0, final long seed1) {
            this.seed0 = seed0;
            this.seed1 = seed1;
            this.voidHash = this.finalize(-7286425919675154353L);
        }
        
        @Override
        public long hashVoid() {
            return this.voidHash;
        }
        
        protected long finalize(final long hash) {
            return CityAndFarmHash_1_1.hashLen16(hash - this.seed0, this.seed1);
        }
    }
    
    private static class Na extends AsLongHashFunction
    {
        public static final Na INSTANCE;
        private static final long serialVersionUID = 0L;
        
        private Object readResolve() {
            return Na.INSTANCE;
        }
        
        @Override
        public <T> long hash(final T input, final Access<T> access, final long off, final long len) {
            long hash;
            if (access.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
                hash = CityAndFarmHash_1_1.INSTANCE.naHash64(access, input, off, len);
            }
            else {
                hash = BigEndian.INSTANCE.naHash64(access, input, off, len);
            }
            return this.finalize(hash);
        }
        
        static {
            INSTANCE = new Na();
        }
    }
    
    private static class NaSeeded extends Na
    {
        private static final long serialVersionUID = 0L;
        final long seed0;
        final long seed1;
        private transient long voidHash;
        
        private NaSeeded(final long seed0, final long seed1) {
            this.seed0 = seed0;
            this.seed1 = seed1;
            this.voidHash = this.finalize(-7286425919675154353L);
        }
        
        @Override
        public long hashVoid() {
            return this.voidHash;
        }
        
        protected long finalize(final long hash) {
            return CityAndFarmHash_1_1.hashLen16(hash - this.seed0, this.seed1);
        }
    }
    
    private static final class Uo extends AsLongHashFunction
    {
        public static final Uo INSTANCE;
        private static final long serialVersionUID = 0L;
        
        private Object readResolve() {
            return Uo.INSTANCE;
        }
        
        @Override
        public <T> long hash(final T input, final Access<T> access, final long off, final long len) {
            final CityAndFarmHash_1_1 instance = (access.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) ? CityAndFarmHash_1_1.INSTANCE : BigEndian.INSTANCE;
            if (len <= 64L) {
                return instance.naHash64(access, input, off, len);
            }
            return instance.uoHash64WithSeeds(access, input, off, len, 81L, 0L);
        }
        
        static {
            INSTANCE = new Uo();
        }
    }
    
    private static final class UoWithOneSeed extends AsLongHashFunctionSeeded
    {
        private static final long serialVersionUID = 0L;
        
        private UoWithOneSeed(final long seed) {
            super(-7286425919675154353L, seed);
        }
        
        @Override
        public <T> long hash(final T input, final Access<T> access, final long off, final long len) {
            final CityAndFarmHash_1_1 instance = (access.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) ? CityAndFarmHash_1_1.INSTANCE : BigEndian.INSTANCE;
            if (len <= 64L) {
                return this.finalize(instance.naHash64(access, input, off, len));
            }
            return instance.uoHash64WithSeeds(access, input, off, len, 0L, this.seed1);
        }
    }
    
    private static class UoSeeded extends AsLongHashFunctionSeeded
    {
        private static final long serialVersionUID = 0L;
        
        private UoSeeded(final long seed0, final long seed1) {
            super(seed0, seed1);
        }
        
        @Override
        public <T> long hash(final T input, final Access<T> access, final long off, final long len) {
            if (access.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
                return CityAndFarmHash_1_1.INSTANCE.uoHash64WithSeeds(access, input, off, len, this.seed0, this.seed1);
            }
            return BigEndian.INSTANCE.uoHash64WithSeeds(access, input, off, len, this.seed0, this.seed1);
        }
    }
}
