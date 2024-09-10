package com.string.german.string.v2;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * An entire order of magnitude worse in comparison then java String
 */
@Deprecated
public class GermanString {

    private static final int MAX_BYTES = 16; // Total bytes available (128 bits)
    private static final int LENGTH_BYTES = 1; // 1 byte for storing length
    private static final int DATA_BYTES = MAX_BYTES - LENGTH_BYTES; // Remaining bytes for string data
    private final byte[] buffer = new byte[MAX_BYTES]; // Fixed-size buffer (128 bits)

    public GermanString(String input) {
        byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);

        if (utf8Bytes.length > DATA_BYTES) {
            throw new IllegalArgumentException("Input string is too long to fit in 128 bits.");
        }

        // Store the length of the string in the first byte (8 bits)
        buffer[0] = (byte) (utf8Bytes.length & 0xFF);

        // Copy the UTF-8 bytes into the remaining part of the buffer
        System.arraycopy(utf8Bytes, 0, buffer, LENGTH_BYTES, utf8Bytes.length);
    }

    @Override
    public String toString() {
        // Read the length from the first byte
        int length = buffer[0] & 0xFF; // Convert to unsigned int

        // Convert the data part of the buffer back to a string, using only the valid bytes
        return new String(Arrays.copyOfRange(buffer, LENGTH_BYTES, LENGTH_BYTES + length), StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false; // Different class
        }

        GermanString other = (GermanString) obj;

        // Compare the length byte first
        if (buffer[0] != other.buffer[0]) {
            return false; // Lengths are different
        }

        // Lengths are the same; compare the actual data bytes
        int length = buffer[0] & 0xFF;
        for (int i = 0; i < length; i++) {
            if (buffer[LENGTH_BYTES + i] != other.buffer[LENGTH_BYTES + i]) {
                return false; // Data bytes differ
            }
        }

        return true; // All checks passed; strings are equal
    }
}
