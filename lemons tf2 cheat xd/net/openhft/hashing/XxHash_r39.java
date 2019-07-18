// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.nio.ByteOrder;

class XxHash_r39
{
    private static final XxHash_r39 INSTANCE;
    private static final XxHash_r39 NATIVE_XX;
    private static final long P1 = -7046029288634856825L;
    private static final long P2 = -4417276706812531889L;
    private static final long P3 = 1609587929392839161L;
    private static final long P4 = -8796714831421723037L;
    private static final long P5 = 2870177450012600261L;
    
    private XxHash_r39() {
    }
    
     <T> long fetch64(final Access<T> access, final T in, final long off) {
        return access.getLong(in, off);
    }
    
     <T> long fetch32(final Access<T> access, final T in, final long off) {
        return access.getUnsignedInt(in, off);
    }
    
     <T> int fetch8(final Access<T> access, final T in, final long off) {
        return access.getUnsignedByte(in, off);
    }
    
    long toLittleEndian(final long v) {
        return v;
    }
    
    int toLittleEndian(final int v) {
        return v;
    }
    
    short toLittleEndian(final short v) {
        return v;
    }
    
    public <T> long xxHash64(final long seed, final T input, final Access<T> access, long off, final long length) {
        long remaining = length;
        long hash;
        if (remaining >= 32L) {
            long v1 = seed - 7046029288634856825L - 4417276706812531889L;
            long v2 = seed - 4417276706812531889L;
            long v3 = seed;
            long v4 = seed + 7046029288634856825L;
            do {
                v1 += this.fetch64(access, input, off) * -4417276706812531889L;
                v1 = Long.rotateLeft(v1, 31);
                v1 *= -7046029288634856825L;
                v2 += this.fetch64(access, input, off + 8L) * -4417276706812531889L;
                v2 = Long.rotateLeft(v2, 31);
                v2 *= -7046029288634856825L;
                v3 += this.fetch64(access, input, off + 16L) * -4417276706812531889L;
                v3 = Long.rotateLeft(v3, 31);
                v3 *= -7046029288634856825L;
                v4 += this.fetch64(access, input, off + 24L) * -4417276706812531889L;
                v4 = Long.rotateLeft(v4, 31);
                v4 *= -7046029288634856825L;
                off += 32L;
                remaining -= 32L;
            } while (remaining >= 32L);
            hash = Long.rotateLeft(v1, 1) + Long.rotateLeft(v2, 7) + Long.rotateLeft(v3, 12) + Long.rotateLeft(v4, 18);
            v1 *= -4417276706812531889L;
            v1 = Long.rotateLeft(v1, 31);
            v1 *= -7046029288634856825L;
            hash ^= v1;
            hash = hash * -7046029288634856825L - 8796714831421723037L;
            v2 *= -4417276706812531889L;
            v2 = Long.rotateLeft(v2, 31);
            v2 *= -7046029288634856825L;
            hash ^= v2;
            hash = hash * -7046029288634856825L - 8796714831421723037L;
            v3 *= -4417276706812531889L;
            v3 = Long.rotateLeft(v3, 31);
            v3 *= -7046029288634856825L;
            hash ^= v3;
            hash = hash * -7046029288634856825L - 8796714831421723037L;
            v4 *= -4417276706812531889L;
            v4 = Long.rotateLeft(v4, 31);
            v4 *= -7046029288634856825L;
            hash ^= v4;
            hash = hash * -7046029288634856825L - 8796714831421723037L;
        }
        else {
            hash = seed + 2870177450012600261L;
        }
        hash += length;
        while (remaining >= 8L) {
            long k1 = this.fetch64(access, input, off);
            k1 *= -4417276706812531889L;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= -7046029288634856825L;
            hash ^= k1;
            hash = Long.rotateLeft(hash, 27) * -7046029288634856825L - 8796714831421723037L;
            off += 8L;
            remaining -= 8L;
        }
        if (remaining >= 4L) {
            hash ^= this.fetch32(access, input, off) * -7046029288634856825L;
            hash = Long.rotateLeft(hash, 23) * -4417276706812531889L + 1609587929392839161L;
            off += 4L;
            remaining -= 4L;
        }
        while (remaining != 0L) {
            hash ^= this.fetch8(access, input, off) * 2870177450012600261L;
            hash = Long.rotateLeft(hash, 11) * -7046029288634856825L;
            --remaining;
            ++off;
        }
        return finalize(hash);
    }
    
    private static long finalize(long hash) {
        hash ^= hash >>> 33;
        hash *= -4417276706812531889L;
        hash ^= hash >>> 29;
        hash *= 1609587929392839161L;
        hash ^= hash >>> 32;
        return hash;
    }
    
    public static LongHashFunction asLongHashFunctionWithoutSeed() {
        return AsLongHashFunction.SEEDLESS_INSTANCE;
    }
    
    public static LongHashFunction asLongHashFunctionWithSeed(final long seed) {
        return new AsLongHashFunctionSeeded(seed);
    }
    
    static {
        INSTANCE = new XxHash_r39();
        NATIVE_XX = (LongHashFunction.NATIVE_LITTLE_ENDIAN ? XxHash_r39.INSTANCE : BigEndian.INSTANCE);
    }
    
    private static class BigEndian extends XxHash_r39
    {
        private static final BigEndian INSTANCE;
        
        private BigEndian() {
            super(null);
        }
        
        @Override
         <T> long fetch64(final Access<T> access, final T in, final long off) {
            return Long.reverseBytes(super.fetch64(access, in, off));
        }
        
        @Override
         <T> long fetch32(final Access<T> access, final T in, final long off) {
            return (long)Integer.reverseBytes(access.getInt(in, off)) & 0xFFFFFFFFL;
        }
        
        @Override
        long toLittleEndian(final long v) {
            return Long.reverseBytes(v);
        }
        
        @Override
        int toLittleEndian(final int v) {
            return Integer.reverseBytes(v);
        }
        
        @Override
        short toLittleEndian(final short v) {
            return Short.reverseBytes(v);
        }
        
        static {
            INSTANCE = new BigEndian();
        }
    }
    
    private static class AsLongHashFunction extends LongHashFunction
    {
        public static final AsLongHashFunction SEEDLESS_INSTANCE;
        private static final long serialVersionUID = 0L;
        
        private Object readResolve() {
            return AsLongHashFunction.SEEDLESS_INSTANCE;
        }
        
        public long seed() {
            return 0L;
        }
        
        @Override
        public long hashLong(long input) {
            input = XxHash_r39.NATIVE_XX.toLittleEndian(input);
            long hash = this.seed() + 2870177450012600261L + 8L;
            input *= -4417276706812531889L;
            input = Long.rotateLeft(input, 31);
            input *= -7046029288634856825L;
            hash ^= input;
            hash = Long.rotateLeft(hash, 27) * -7046029288634856825L - 8796714831421723037L;
            return finalize(hash);
        }
        
        @Override
        public long hashInt(int input) {
            input = XxHash_r39.NATIVE_XX.toLittleEndian(input);
            long hash = this.seed() + 2870177450012600261L + 4L;
            hash ^= input * -7046029288634856825L;
            hash = Long.rotateLeft(hash, 23) * -4417276706812531889L + 1609587929392839161L;
            return finalize(hash);
        }
        
        @Override
        public long hashShort(short input) {
            input = XxHash_r39.NATIVE_XX.toLittleEndian(input);
            long hash = this.seed() + 2870177450012600261L + 2L;
            hash ^= (input & 0xFF) * 2870177450012600261L;
            hash = Long.rotateLeft(hash, 11) * -7046029288634856825L;
            hash ^= (input >> 8 & 0xFF) * 2870177450012600261L;
            hash = Long.rotateLeft(hash, 11) * -7046029288634856825L;
            return finalize(hash);
        }
        
        @Override
        public long hashChar(final char input) {
            return this.hashShort((short)input);
        }
        
        @Override
        public long hashByte(final byte input) {
            long hash = this.seed() + 2870177450012600261L + 1L;
            hash ^= input * 2870177450012600261L;
            hash = Long.rotateLeft(hash, 11) * -7046029288634856825L;
            return finalize(hash);
        }
        
        @Override
        public long hashVoid() {
            return finalize(2870177450012600261L);
        }
        
        @Override
        public <T> long hash(final T input, final Access<T> access, final long off, final long len) {
            final long seed = this.seed();
            if (access.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
                return XxHash_r39.INSTANCE.xxHash64(seed, input, access, off, len);
            }
            return BigEndian.INSTANCE.xxHash64(seed, input, access, off, len);
        }
        
        static {
            SEEDLESS_INSTANCE = new AsLongHashFunction();
        }
    }
    
    private static class AsLongHashFunctionSeeded extends AsLongHashFunction
    {
        private final long seed;
        private final long voidHash;
        
        private AsLongHashFunctionSeeded(final long seed) {
            this.seed = seed;
            this.voidHash = finalize(seed + 2870177450012600261L);
        }
        
        @Override
        public long seed() {
            return this.seed;
        }
        
        @Override
        public long hashVoid() {
            return this.voidHash;
        }
    }
}
