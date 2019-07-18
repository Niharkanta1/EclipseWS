// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.nio.ByteOrder;

class MurmurHash_3
{
    private static final MurmurHash_3 INSTANCE;
    private static final MurmurHash_3 NATIVE_MURMUR;
    private static final long C1 = -8663945395140668459L;
    private static final long C2 = 5545529020109919103L;
    
    private MurmurHash_3() {
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
    
    int toLittleEndianShort(final int unsignedShort) {
        return unsignedShort;
    }
    
    public <T> long hash(final long seed, final T input, final Access<T> access, long offset, final long length) {
        long h1;
        long h2;
        long remaining;
        long k1;
        long k2;
        for (h1 = seed, h2 = seed, remaining = length; remaining >= 16L; remaining -= 16L, h1 ^= mixK1(k1), h1 = Long.rotateLeft(h1, 27), h1 += h2, h1 = h1 * 5L + 1390208809L, h2 ^= mixK2(k2), h2 = Long.rotateLeft(h2, 31), h2 += h1, h2 = h2 * 5L + 944331445L) {
            k1 = this.fetch64(access, input, offset);
            k2 = this.fetch64(access, input, offset + 8L);
            offset += 16L;
        }
        if (remaining > 0L) {
            k1 = 0L;
            k2 = 0L;
            switch ((int)remaining) {
                case 15: {
                    k2 ^= (long)access.getUnsignedByte(input, offset + 14L) << 48;
                }
                case 14: {
                    k2 ^= (long)access.getUnsignedByte(input, offset + 13L) << 40;
                }
                case 13: {
                    k2 ^= (long)access.getUnsignedByte(input, offset + 12L) << 32;
                }
                case 12: {
                    k2 ^= (long)access.getUnsignedByte(input, offset + 11L) << 24;
                }
                case 11: {
                    k2 ^= (long)access.getUnsignedByte(input, offset + 10L) << 16;
                }
                case 10: {
                    k2 ^= (long)access.getUnsignedByte(input, offset + 9L) << 8;
                }
                case 9: {
                    k2 ^= access.getUnsignedByte(input, offset + 8L);
                }
                case 8: {
                    k1 ^= this.fetch64(access, input, offset);
                    break;
                }
                case 7: {
                    k1 ^= (long)access.getUnsignedByte(input, offset + 6L) << 48;
                }
                case 6: {
                    k1 ^= (long)access.getUnsignedByte(input, offset + 5L) << 40;
                }
                case 5: {
                    k1 ^= (long)access.getUnsignedByte(input, offset + 4L) << 32;
                }
                case 4: {
                    k1 ^= Primitives.unsignedInt(this.fetch32(access, input, offset));
                    break;
                }
                case 3: {
                    k1 ^= (long)access.getUnsignedByte(input, offset + 2L) << 16;
                }
                case 2: {
                    k1 ^= (long)access.getUnsignedByte(input, offset + 1L) << 8;
                }
                case 1: {
                    k1 ^= access.getUnsignedByte(input, offset);
                }
                case 0: {
                    break;
                }
                default: {
                    throw new AssertionError((Object)"Should never get here.");
                }
            }
            h1 ^= mixK1(k1);
            h2 ^= mixK2(k2);
        }
        return finalize(length, h1, h2);
    }
    
    private static long finalize(final long length, long h1, long h2) {
        h1 ^= length;
        h2 ^= length;
        h1 += h2;
        h2 += h1;
        h1 = fmix64(h1);
        h2 = fmix64(h2);
        h1 += h2;
        return h1;
    }
    
    private static long fmix64(long k) {
        k ^= k >>> 33;
        k *= -49064778989728563L;
        k ^= k >>> 33;
        k *= -4265267296055464877L;
        k ^= k >>> 33;
        return k;
    }
    
    private static long mixK1(long k1) {
        k1 *= -8663945395140668459L;
        k1 = Long.rotateLeft(k1, 31);
        k1 *= 5545529020109919103L;
        return k1;
    }
    
    private static long mixK2(long k2) {
        k2 *= 5545529020109919103L;
        k2 = Long.rotateLeft(k2, 33);
        k2 *= -8663945395140668459L;
        return k2;
    }
    
    public static LongHashFunction asLongHashFunctionWithoutSeed() {
        return AsLongHashFunction.INSTANCE;
    }
    
    public static LongHashFunction asLongHashFunctionWithSeed(final long seed) {
        return new AsLongHashFunctionSeeded(seed);
    }
    
    static {
        INSTANCE = new MurmurHash_3();
        NATIVE_MURMUR = (LongHashFunction.NATIVE_LITTLE_ENDIAN ? MurmurHash_3.INSTANCE : BigEndian.INSTANCE);
    }
    
    private static class BigEndian extends MurmurHash_3
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
        
        @Override
        int toLittleEndianShort(final int unsignedShort) {
            return (unsignedShort & 0xFF) << 8 | unsignedShort >> 8;
        }
        
        static {
            INSTANCE = new BigEndian();
        }
    }
    
    private static class AsLongHashFunction extends LongHashFunction
    {
        public static final AsLongHashFunction INSTANCE;
        private static final long serialVersionUID = 0L;
        
        private Object readResolve() {
            return AsLongHashFunction.INSTANCE;
        }
        
        long seed() {
            return 0L;
        }
        
        long hashNativeLong(final long nativeLong, final long len) {
            final long h1 = mixK1(nativeLong);
            final long h2 = 0L;
            return finalize(len, h1, h2);
        }
        
        @Override
        public long hashLong(final long input) {
            return this.hashNativeLong(MurmurHash_3.NATIVE_MURMUR.toLittleEndian(input), 8L);
        }
        
        @Override
        public long hashInt(final int input) {
            return this.hashNativeLong(Primitives.unsignedInt(MurmurHash_3.NATIVE_MURMUR.toLittleEndian(input)), 4L);
        }
        
        @Override
        public long hashShort(final short input) {
            return this.hashNativeLong(MurmurHash_3.NATIVE_MURMUR.toLittleEndianShort(Primitives.unsignedShort(input)), 2L);
        }
        
        @Override
        public long hashChar(final char input) {
            return this.hashNativeLong(MurmurHash_3.NATIVE_MURMUR.toLittleEndianShort(input), 2L);
        }
        
        @Override
        public long hashByte(final byte input) {
            return this.hashNativeLong(Primitives.unsignedByte(input), 1L);
        }
        
        @Override
        public long hashVoid() {
            return 0L;
        }
        
        @Override
        public <T> long hash(final T input, final Access<T> access, final long off, final long len) {
            final long seed = this.seed();
            if (access.byteOrder(input) == ByteOrder.LITTLE_ENDIAN) {
                return MurmurHash_3.INSTANCE.hash(seed, input, access, off, len);
            }
            return BigEndian.INSTANCE.hash(seed, input, access, off, len);
        }
        
        static {
            INSTANCE = new AsLongHashFunction();
        }
    }
    
    private static class AsLongHashFunctionSeeded extends AsLongHashFunction
    {
        private static final long serialVersionUID = 0L;
        private final long seed;
        private transient long voidHash;
        
        private AsLongHashFunctionSeeded(final long seed) {
            this.seed = seed;
            this.voidHash = finalize(0L, seed, seed);
        }
        
        @Override
        long seed() {
            return this.seed;
        }
        
        @Override
        long hashNativeLong(final long nativeLong, final long len) {
            final long seed = this.seed;
            final long h1 = seed ^ mixK1(nativeLong);
            final long h2 = seed;
            return finalize(len, h1, h2);
        }
        
        @Override
        public long hashVoid() {
            return this.voidHash;
        }
    }
}
