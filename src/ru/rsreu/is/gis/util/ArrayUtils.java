package ru.rsreu.is.gis.util;

final public class ArrayUtils {
    private ArrayUtils() {
    }

    public static void reverse(byte[] bytes) {
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
    }

    public static byte[] concat(byte[]... arrays) {
        byte[] result;
        int resultArrayLength = 0;

        for (byte[] array : arrays) {
            resultArrayLength += array.length;
        }
        result = new byte[resultArrayLength];

        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }

        return result;
    }
}
