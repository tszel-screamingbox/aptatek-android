package com.aptatek.aptatek.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

public class PinCode {

    private final char[] passwordChars;
    private final byte[] passwordBytes;
    private boolean cleared = false;

    public PinCode(final char[] password) {
        this.passwordChars = Arrays.copyOf(password, password.length);
        this.passwordBytes = toBytes(this.passwordChars);
    }

    public PinCode(final byte[] password) {
        this.passwordBytes = Arrays.copyOf(password, password.length);
        this.passwordChars = toChars(this.passwordBytes);
    }

    public byte[] getBytes() {
        if (cleared) {
            throw new RuntimeException("PinCode cleared.");
        }
        return Arrays.copyOf(passwordBytes, passwordBytes.length);
    }

    public char[] getChars() {
        if (cleared) {
            throw new RuntimeException("PinCode cleared.");
        }
        return Arrays.copyOf(passwordChars, passwordChars.length);
    }

    public void clear() {
        cleared = true;
        Arrays.fill(passwordBytes, (byte) 0);
        Arrays.fill(passwordChars, '*');
    }

    private byte[] toBytes(final char[] chars) {
        final CharBuffer charBuffer = CharBuffer.wrap(chars);
        final ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        final byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }

    private char[] toChars(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        final CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
        final char[] chars = Arrays.copyOfRange(charBuffer.array(), charBuffer.position(), charBuffer.limit());
        Arrays.fill(charBuffer.array(), '*');
        return chars;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PinCode)) {
            return false;
        }
        final PinCode pinCode = (PinCode) o;
        return Arrays.equals(passwordBytes, pinCode.getBytes())
                && Arrays.equals(passwordChars, pinCode.getChars());
    }

    @Override
    public int hashCode() {
        return Objects.hash(passwordBytes, passwordChars);
    }
}
