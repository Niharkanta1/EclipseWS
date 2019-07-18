// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.nio.ByteOrder;
import sun.nio.ch.DirectBuffer;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

public abstract class LongHashFunction implements Serializable
{
    private static final long serialVersionUID = 0L;
    static final boolean NATIVE_LITTLE_ENDIAN;
    static final byte TRUE_BYTE_VALUE;
    static final byte FALSE_BYTE_VALUE;
    private static StringHash stringHash;
    
    public static LongHashFunction city_1_1() {
        return CityAndFarmHash_1_1.asLongHashFunctionWithoutSeed();
    }
    
    public static LongHashFunction city_1_1(final long seed) {
        return CityAndFarmHash_1_1.asLongHashFunctionWithSeed(seed);
    }
    
    public static LongHashFunction city_1_1(final long seed0, final long seed1) {
        return CityAndFarmHash_1_1.asLongHashFunctionWithTwoSeeds(seed0, seed1);
    }
    
    public static LongHashFunction farmNa() {
        return CityAndFarmHash_1_1.naWithoutSeeds();
    }
    
    public static LongHashFunction farmNa(final long seed) {
        return CityAndFarmHash_1_1.naWithSeed(seed);
    }
    
    public static LongHashFunction farmNa(final long seed0, final long seed1) {
        return CityAndFarmHash_1_1.naWithSeeds(seed0, seed1);
    }
    
    public static LongHashFunction farmUo() {
        return CityAndFarmHash_1_1.uoWithoutSeeds();
    }
    
    public static LongHashFunction farmUo(final long seed) {
        return CityAndFarmHash_1_1.uoWithSeed(seed);
    }
    
    public static LongHashFunction farmUo(final long seed0, final long seed1) {
        return CityAndFarmHash_1_1.uoWithSeeds(seed0, seed1);
    }
    
    public static LongHashFunction murmur_3() {
        return MurmurHash_3.asLongHashFunctionWithoutSeed();
    }
    
    public static LongHashFunction murmur_3(final long seed) {
        return MurmurHash_3.asLongHashFunctionWithSeed(seed);
    }
    
    public static LongHashFunction xx_r39() {
        return XxHash_r39.asLongHashFunctionWithoutSeed();
    }
    
    public static LongHashFunction xx_r39(final long seed) {
        return XxHash_r39.asLongHashFunctionWithSeed(seed);
    }
    
    private static void checkArrayOffs(final int arrayLength, final int off, final int len) {
        if (len < 0 || off < 0 || off + len > arrayLength || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    protected LongHashFunction() {
    }
    
    public abstract long hashLong(final long p0);
    
    public abstract long hashInt(final int p0);
    
    public abstract long hashShort(final short p0);
    
    public abstract long hashChar(final char p0);
    
    public abstract long hashByte(final byte p0);
    
    public abstract long hashVoid();
    
    public abstract <T> long hash(final T p0, final Access<T> p1, final long p2, final long p3);
    
    private long unsafeHash(final Object input, final long off, final long len) {
        return this.hash(input, UnsafeAccess.INSTANCE, off, len);
    }
    
    public long hashBoolean(final boolean input) {
        return this.hashByte(input ? LongHashFunction.TRUE_BYTE_VALUE : LongHashFunction.FALSE_BYTE_VALUE);
    }
    
    public long hashBooleans(@NotNull final boolean[] input) {
        return this.unsafeHash(input, UnsafeAccess.BOOLEAN_BASE, input.length);
    }
    
    public long hashBooleans(@NotNull final boolean[] input, final int off, final int len) {
        checkArrayOffs(input.length, off, len);
        return this.unsafeHash(input, UnsafeAccess.BOOLEAN_BASE + off, len);
    }
    
    public long hashBytes(@NotNull final byte[] input) {
        return this.unsafeHash(input, UnsafeAccess.BYTE_BASE, input.length);
    }
    
    public long hashBytes(@NotNull final byte[] input, final int off, final int len) {
        checkArrayOffs(input.length, off, len);
        return this.unsafeHash(input, UnsafeAccess.BYTE_BASE + off, len);
    }
    
    public long hashBytes(final ByteBuffer input) {
        return this.hashByteBuffer(input, input.position(), input.remaining());
    }
    
    public long hashBytes(@NotNull final ByteBuffer input, final int off, final int len) {
        checkArrayOffs(input.capacity(), off, len);
        return this.hashByteBuffer(input, off, len);
    }
    
    private long hashByteBuffer(@NotNull final ByteBuffer input, final int off, final int len) {
        if (input.hasArray()) {
            return this.unsafeHash(input.array(), UnsafeAccess.BYTE_BASE + input.arrayOffset() + off, len);
        }
        if (input instanceof DirectBuffer) {
            return this.unsafeHash(null, ((DirectBuffer)input).address() + off, len);
        }
        return this.hash(input, ByteBufferAccess.INSTANCE, off, len);
    }
    
    public long hashMemory(final long address, final long len) {
        return this.unsafeHash(null, address, len);
    }
    
    public long hashChars(@NotNull final char[] input) {
        return this.unsafeHash(input, UnsafeAccess.CHAR_BASE, input.length * 2L);
    }
    
    public long hashChars(@NotNull final char[] input, final int off, final int len) {
        checkArrayOffs(input.length, off, len);
        return this.unsafeHash(input, UnsafeAccess.CHAR_BASE + off * 2L, len * 2L);
    }
    
    public long hashChars(@NotNull final String input) {
        return LongHashFunction.stringHash.longHash(input, this, 0, input.length());
    }
    
    public long hashChars(@NotNull final String input, final int off, final int len) {
        checkArrayOffs(input.length(), off, len);
        return LongHashFunction.stringHash.longHash(input, this, off, len);
    }
    
    public long hashChars(@NotNull final StringBuilder input) {
        return this.hashNativeChars(input);
    }
    
    public long hashChars(@NotNull final StringBuilder input, final int off, final int len) {
        checkArrayOffs(input.length(), off, len);
        return this.hashNativeChars(input, off, len);
    }
    
    long hashNativeChars(final CharSequence input) {
        return this.hashNativeChars(input, 0, input.length());
    }
    
    long hashNativeChars(final CharSequence input, final int off, final int len) {
        return this.hash(input, CharSequenceAccess.nativeCharSequenceAccess(), off * 2L, len * 2L);
    }
    
    public long hashShorts(@NotNull final short[] input) {
        return this.unsafeHash(input, UnsafeAccess.SHORT_BASE, input.length * 2L);
    }
    
    public long hashShorts(@NotNull final short[] input, final int off, final int len) {
        checkArrayOffs(input.length, off, len);
        return this.unsafeHash(input, UnsafeAccess.SHORT_BASE + off * 2L, len * 2L);
    }
    
    public long hashInts(@NotNull final int[] input) {
        return this.unsafeHash(input, UnsafeAccess.INT_BASE, input.length * 4L);
    }
    
    public long hashInts(@NotNull final int[] input, final int off, final int len) {
        checkArrayOffs(input.length, off, len);
        return this.unsafeHash(input, UnsafeAccess.INT_BASE + off * 4L, len * 4L);
    }
    
    public long hashLongs(@NotNull final long[] input) {
        return this.unsafeHash(input, UnsafeAccess.LONG_BASE, input.length * 8L);
    }
    
    public long hashLongs(@NotNull final long[] input, final int off, final int len) {
        checkArrayOffs(input.length, off, len);
        return this.unsafeHash(input, UnsafeAccess.LONG_BASE + off * 8L, len * 8L);
    }
    
    static {
        NATIVE_LITTLE_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);
        TRUE_BYTE_VALUE = UnsafeAccess.UNSAFE.getByte(new boolean[] { true }, UnsafeAccess.BOOLEAN_BASE);
        FALSE_BYTE_VALUE = UnsafeAccess.UNSAFE.getByte(new boolean[] { false }, UnsafeAccess.BOOLEAN_BASE);
        try {
            if (System.getProperty("java.vm.name").contains("HotSpot")) {
                final String javaVersion = System.getProperty("java.version");
                if (javaVersion.compareTo("1.7.0_06") >= 0) {
                    if (javaVersion.compareTo("1.9") >= 0) {
                        LongHashFunction.stringHash = UnknownJvmStringHash.INSTANCE;
                    }
                    else {
                        LongHashFunction.stringHash = ModernHotSpotStringHash.INSTANCE;
                    }
                }
                else {
                    LongHashFunction.stringHash = HotSpotPrior7u6StringHash.INSTANCE;
                }
            }
            else {
                LongHashFunction.stringHash = HotSpotPrior7u6StringHash.INSTANCE;
            }
        }
        catch (Throwable t) {}
        finally {
            if (LongHashFunction.stringHash == null) {
                LongHashFunction.stringHash = UnknownJvmStringHash.INSTANCE;
            }
        }
    }
}
