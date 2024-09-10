package com.string.german.string.v1;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *     This entire implementation is futile since java doesn't give me any way to allocate anything on the stack
 * </p>
 *
 * <p>
 *  The German String class attempts to treat strings as a sequence of characters that are conformant to specific
 *  behaviour specified in detail in the link provided at the bottom.
 * </p>
 * <p>German Strings offer specific advantages such as faster comparisons.</p>
 * @see <a href="https://cedardb.com/blog/german_strings/">German Strings</a>
 */
@Deprecated
public class GermanString {

    private final byte[] repr;

    public byte[] getRepr() {
        return this.repr;
    }

    private static final int GERMAN_STR_MAX_LEN = 12;
    private static final int BIT_256_BYTE_32 = 32;
    private static final int INT_BYTES = 4;

    /**
     * constructor used to instantiate a instance of a GermanString from a specified character sequence
     * @param charSequence sequence of characters
     */
    public GermanString(char[] charSequence) {
        int strLen = charSequence.length;
        if (GERMAN_STR_MAX_LEN >= strLen) {
            // 256 bit repr => 32 bit len + 224 bit = 224/16 = 14 chars
            // str size limit ~ 4GB (2^32 length)
            this.repr = new byte[BIT_256_BYTE_32];
            byte[] strLenByteRepr = ByteBuffer.allocate(INT_BYTES).putInt(strLen).array();
            System.arraycopy(strLenByteRepr, 0, this.repr, 0, INT_BYTES);

            Charset charset = StandardCharsets.UTF_8;
            CharsetEncoder encoder = charset.newEncoder();
            try {
                ByteBuffer byteBuffer = encoder.encode(CharBuffer.wrap(charSequence));
                int byteBufferLength = byteBuffer.remaining();
                byteBuffer.get(this.repr, INT_BYTES, byteBufferLength);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else {
            throw new RuntimeException("german string implementation for lengths greater than " + GERMAN_STR_MAX_LEN +
                    "is not implemented yet");
        }
    }

    /**
     *
     * @param object object against which equality check needs to be performed
     * @return true if object equals this else false
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GermanString gs)) {
            return false;
        }

        byte[] gsRepr = gs.getRepr();

        ByteBuffer gsReprBuffer = ByteBuffer.wrap(gsRepr, 0, INT_BYTES);
        int gsLen = gsReprBuffer.getInt();
        ByteBuffer thisReprBuffer = ByteBuffer.wrap(this.repr, 0, INT_BYTES);
        int thisLen = thisReprBuffer.getInt();

        if (gsLen != thisLen) return false;

        for (int i = INT_BYTES; i < gsLen; i++) {
            if (gsRepr[i] != this.repr[i]) return false;
        }

        return true;
    }
}
