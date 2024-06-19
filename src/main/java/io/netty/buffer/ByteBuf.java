package io.netty.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public abstract class ByteBuf {
    public abstract ByteBuf writeBoolean(boolean value);

    public abstract ByteBuf writeByte(int value);

    public abstract ByteBuf writeShort(int value);

    public abstract ByteBuf writeMedium(int value);

    public abstract ByteBuf writeInt(int value);

    public abstract ByteBuf writeLong(long value);

    public abstract ByteBuf writeChar(int value);

    public abstract ByteBuf writeFloat(float value);

    public abstract ByteBuf writeDouble(double value);

    public abstract ByteBuf writeBytes(ByteBuf src);

    public abstract ByteBuf writeBytes(ByteBuf src, int length);

    public abstract ByteBuf writeBytes(ByteBuf src, int srcIndex, int length);

    public abstract ByteBuf writeBytes(byte[] src);

    public abstract ByteBuf writeBytes(byte[] src, int srcIndex, int length);

    public abstract ByteBuf writeBytes(ByteBuffer src);

    public abstract int  writeBytes(InputStream in, int length) throws IOException;

    public abstract int  writeBytes(ScatteringByteChannel in, int length) throws IOException;


    public abstract boolean readBoolean();

    public abstract byte readByte();

    public abstract short readUnsignedByte();

    public abstract short readShort();

    public abstract int readUnsignedShort();

    public abstract int readMedium();

    public abstract int readUnsignedMedium();

    public abstract int readInt();

    public abstract long readUnsignedInt();

    public abstract long readLong();

    public abstract char readChar();

    public abstract float readFloat();

    public abstract double readDouble();

    public abstract ByteBuf readBytes(int length);

    public abstract ByteBuf readBytes(ByteBuf dst);

    public abstract ByteBuf readBytes(ByteBuf dst, int length);

    public abstract ByteBuf readBytes(ByteBuf dst, int dstIndex, int length);

    public abstract ByteBuf readBytes(byte[] dst);

    public abstract ByteBuf readBytes(byte[] dst, int dstIndex, int length);

    public abstract ByteBuf readBytes(ByteBuffer dst);

    public abstract ByteBuf readBytes(OutputStream out, int length) throws IOException;

    public abstract int readBytes(GatheringByteChannel out, int length) throws IOException;
}
