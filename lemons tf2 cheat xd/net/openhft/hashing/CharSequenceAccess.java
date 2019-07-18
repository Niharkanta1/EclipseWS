// 
// Decompiled by Procyon v0.5.36
// 

package net.openhft.hashing;

import java.nio.ByteOrder;

abstract class CharSequenceAccess extends Access<CharSequence>
{
    public static CharSequenceAccess charSequenceAccess(final ByteOrder order) {
        return (order == ByteOrder.LITTLE_ENDIAN) ? LittleEndianCharSequenceAccess.INSTANCE : BigEndianCharSequenceAccess.INSTANCE;
    }
    
    public static CharSequenceAccess nativeCharSequenceAccess() {
        return charSequenceAccess(ByteOrder.nativeOrder());
    }
    
    private static int ix(final long offset) {
        return (int)(offset >> 1);
    }
    
    static long getLong(final CharSequence input, final long offset, final int char0Off, final int char1Off, final int char2Off, final int char3Off) {
        final int base = ix(offset);
        final long char0 = input.charAt(base + char0Off);
        final long char2 = input.charAt(base + char1Off);
        final long char3 = input.charAt(base + char2Off);
        final long char4 = input.charAt(base + char3Off);
        return char0 | char2 << 16 | char3 << 32 | char4 << 48;
    }
    
    static long getUnsignedInt(final CharSequence input, final long offset, final int char0Off, final int char1Off) {
        final int base = ix(offset);
        final long char0 = input.charAt(base + char0Off);
        final long char2 = input.charAt(base + char1Off);
        return char0 | char2 << 16;
    }
    
    private CharSequenceAccess() {
    }
    
    @Override
    public int getInt(final CharSequence input, final long offset) {
        return (int)this.getUnsignedInt(input, offset);
    }
    
    @Override
    public int getUnsignedShort(final CharSequence input, final long offset) {
        return input.charAt(ix(offset));
    }
    
    @Override
    public int getShort(final CharSequence input, final long offset) {
        return (short)input.charAt(ix(offset));
    }
    
    static int getUnsignedByte(final CharSequence input, final long offset, final int shift) {
        return Primitives.unsignedByte(input.charAt(ix(offset)) >> shift);
    }
    
    @Override
    public int getByte(final CharSequence input, final long offset) {
        return (byte)this.getUnsignedByte(input, offset);
    }
    
    private static class LittleEndianCharSequenceAccess extends CharSequenceAccess
    {
        private static final CharSequenceAccess INSTANCE;
        
        private LittleEndianCharSequenceAccess() {
            super(null);
        }
        
        @Override
        public long getLong(final CharSequence input, final long offset) {
            return CharSequenceAccess.getLong(input, offset, 0, 1, 2, 3);
        }
        
        @Override
        public long getUnsignedInt(final CharSequence input, final long offset) {
            return CharSequenceAccess.getUnsignedInt(input, offset, 0, 1);
        }
        
        @Override
        public int getUnsignedByte(final CharSequence input, final long offset) {
            return CharSequenceAccess.getUnsignedByte(input, offset, ((int)offset & 0x1) << 3);
        }
        
        @Override
        public ByteOrder byteOrder(final CharSequence input) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        
        static {
            INSTANCE = new LittleEndianCharSequenceAccess();
        }
    }
    
    private static class BigEndianCharSequenceAccess extends CharSequenceAccess
    {
        private static final CharSequenceAccess INSTANCE;
        
        private BigEndianCharSequenceAccess() {
            super(null);
        }
        
        @Override
        public long getLong(final CharSequence input, final long offset) {
            return CharSequenceAccess.getLong(input, offset, 3, 2, 1, 0);
        }
        
        @Override
        public long getUnsignedInt(final CharSequence input, final long offset) {
            return CharSequenceAccess.getUnsignedInt(input, offset, 1, 0);
        }
        
        @Override
        public int getUnsignedByte(final CharSequence input, final long offset) {
            return CharSequenceAccess.getUnsignedByte(input, offset, (((int)offset & 0x1) ^ 0x1) << 3);
        }
        
        @Override
        public ByteOrder byteOrder(final CharSequence input) {
            return ByteOrder.BIG_ENDIAN;
        }
        
        static {
            INSTANCE = new BigEndianCharSequenceAccess();
        }
    }
}
