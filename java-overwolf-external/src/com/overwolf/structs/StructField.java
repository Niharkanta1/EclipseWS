package com.overwolf.structs;

import java.nio.ByteBuffer;

import com.sun.jna.Pointer;

public class StructField {

	private int size = 0;
	private int offset = 0;
	MemStruct parent;

	public StructField(MemStruct parent, int size) {
		this.parent = parent;
		this.size = size;
		this.offset = parent.SIZE;
		parent.enlarge(size);
	}

	public StructField(MemStruct parent, int size, int offset) {
		this.parent = parent;
		this.size = size;
		this.offset = offset;
		parent.enlarge(size);
		// TODO: FIX?
		if (parent.SIZE < offset + size) {
			parent.SIZE = offset + size;
		}
	}

	public int offset() {
		return this.offset;
	}

	public int size() {
		return this.size;
	}

	public void set(int value) {
		parent.membuf.setInt(parent.OFFSET + this.offset, value);
	}

	public void set(float value) {
		parent.membuf.setFloat(parent.OFFSET + this.offset, value);
	}

	public void set(double value) {
		parent.membuf.setDouble(parent.OFFSET + this.offset, value);
	}

	public void set(long value) {
		parent.membuf.setLong(parent.OFFSET + this.offset, value);
	}

	public void set(boolean value) {
		parent.membuf.setByte(parent.OFFSET + this.offset, value ? (byte) 1 : (byte) 0);
	}

	public void set(short value) {
		parent.membuf.setShort(parent.OFFSET + this.offset, value);
	}

	public void set(char value) {
		parent.membuf.setChar(parent.OFFSET + this.offset, value);
	}

	public void set(Pointer value) {
		parent.membuf.setPointer(parent.OFFSET + this.offset, value);
	}

	public void set(long offset, int value) {
		parent.membuf.setInt(parent.OFFSET + this.offset + offset, value);
	}

	public void set(long offset, float value) {
		parent.membuf.setFloat(parent.OFFSET + this.offset + offset, value);
	}

	public void set(long offset, double value) {
		parent.membuf.setDouble(parent.OFFSET + this.offset + offset, value);
	}

	public void set(long offset, long value) {
		parent.membuf.setLong(parent.OFFSET + this.offset + offset, value);
	}

	public void set(long offset, boolean value) {
		parent.membuf.setByte(parent.OFFSET + this.offset + offset, value ? (byte) 1 : (byte) 0);
	}

	public void set(long offset, short value) {
		parent.membuf.setShort(parent.OFFSET + this.offset + offset, value);
	}

	public void set(long offset, char value) {
		parent.membuf.setChar(parent.OFFSET + this.offset + offset, value);
	}

	public void set(long offset, Pointer value) {
		parent.membuf.setPointer(parent.OFFSET + this.offset + offset, value);
	}

	public ByteBuffer get() {
		return parent.membuf.getByteBuffer(parent.OFFSET + this.offset, this.size);
	}

	public ByteBuffer get(int offset, int size) {
		return parent.membuf.getByteBuffer(parent.OFFSET + this.offset + offset, size);
	}

	public int getInt() {
		return parent.membuf.getInt(parent.OFFSET + this.offset);
	}

	public long getLong() {
		return parent.membuf.getLong(parent.OFFSET + this.offset);
	}

	public float getFloat() {
		return parent.membuf.getFloat(parent.OFFSET + this.offset);
	}

	public double getDouble() {
		return parent.membuf.getDouble(parent.OFFSET + this.offset);
	}

	public Pointer getPointer() {
		return parent.membuf.getPointer(parent.OFFSET + this.offset);
	}

	public short getShort() {
		return parent.membuf.getShort(parent.OFFSET + this.offset);
	}

	public byte getByte() {
		return parent.membuf.getByte(parent.OFFSET + this.offset);
	}

	public boolean getBoolean() {
		return parent.membuf.getByte(parent.OFFSET + this.offset) > 0;
	}

	public char getChar() {
		return parent.membuf.getChar(parent.OFFSET + this.offset);
	}

	public String getString() {
		return parent.membuf.getString(parent.OFFSET + this.offset);
	}

	public int getInt(int offset) {
		return parent.membuf.getInt(parent.OFFSET + this.offset + offset);
	}

	public long getLong(int offset) {
		return parent.membuf.getLong(parent.OFFSET + this.offset + offset);
	}

	public float getFloat(int offset) {
		return parent.membuf.getFloat(parent.OFFSET + this.offset + offset);
	}

	public double getDouble(int offset) {
		return parent.membuf.getDouble(parent.OFFSET + this.offset + offset);
	}

	public Pointer getPointer(int offset) {
		return parent.membuf.getPointer(parent.OFFSET + this.offset + offset);
	}

	public short getShort(int offset) {
		return parent.membuf.getShort(parent.OFFSET + this.offset + offset);
	}

	public byte getByte(int offset) {
		return parent.membuf.getByte(parent.OFFSET + this.offset + offset);
	}

	public boolean getBoolean(int offset) {
		return parent.membuf.getByte(parent.OFFSET + this.offset + offset) > 0;
	}

	public char getChar(int offset) {
		return parent.membuf.getChar(parent.OFFSET + this.offset + offset);
	}

	public String getString(int offset) {
		return parent.membuf.getString(parent.OFFSET + this.offset + offset);
	}

	public int[] getIntArray() {
		return parent.membuf.getIntArray(parent.OFFSET + this.offset, this.size / Integer.BYTES);
	}

	public long[] getLongArray() {
		return parent.membuf.getLongArray(parent.OFFSET + this.offset, this.size / Long.BYTES);
	}

	public float[] getFloatArray() {
		return parent.membuf.getFloatArray(parent.OFFSET + this.offset, this.size / Float.BYTES);
	}

	public double[] getDoubleArray() {
		return parent.membuf.getDoubleArray(parent.OFFSET + this.offset, this.size / Double.BYTES);
	}

	public short[] getShortArray() {
		return parent.membuf.getShortArray(parent.OFFSET + this.offset, this.size / Short.BYTES);
	}

	public byte[] getByteArray() {
		return parent.membuf.getByteArray(parent.OFFSET + this.offset, this.size);
	}

	public char[] getCharArray() {
		return parent.membuf.getCharArray(parent.OFFSET + this.offset, this.size);
	}

}
