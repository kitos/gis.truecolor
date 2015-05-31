package ru.rsreu.is.gis.util;

public class FilterUtils {

    public static byte[][] contur = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}},
            contur2 = {
                    {-1, -1, -1},
                    {-1, 9, -1},
                    {-1, -1, -1}},
            contur3 = {
                    {1, -2, 1},
                    {-2, 5, -2},
                    {1, -2, 1}};

    public static byte[][] reliefNorth = {
            {1, 1, 1},
            {1, -1, 1},
            {-1, -1, -1}},
            reliefEast = {
                    {-1, 1, 1},
                    {-1, -1, 1},
                    {-1, 1, 1}},
            reliefSouth = {
                    {-1, -1, -1},
                    {1, -1, 1},
                    {1, 1, 1}},
            reliefWest = {
                    {1, 1, -1},
                    {1, -1, 1},
                    {1, 1, -1}};

    public static void process(byte[] bytes) {
        for (int i = 0; i < bytes.length; i += 3) {
            // invert red
            bytes[i + 2] = (byte) (bytes[i + 2] ^ 0b11111111);

            // swap green and blue
            byte temp = bytes[i + 1];
            bytes[i + 1] = bytes[i];
            bytes[i] = temp;
        }
    }

    public static byte[] applyMask(byte[] bytes, byte[][] mask, int imageWidth) {
        byte[] result = new byte[bytes.length];
        int sum;
        for (int i = 0; i < bytes.length; i++) {
            sum = 0;
            try {
                sum += toUnsignedInt(bytes[i - imageWidth - 1]) * mask[0][0];
                sum += toUnsignedInt(bytes[i - imageWidth]) * mask[0][1];
                sum += toUnsignedInt(bytes[i - imageWidth + 1]) * mask[0][2];

                sum += toUnsignedInt(bytes[i - 1]) * mask[1][0];
                sum += toUnsignedInt(bytes[i]) * mask[1][1];
                sum += toUnsignedInt(bytes[i + 1]) * mask[1][2];

                sum += toUnsignedInt(bytes[i + imageWidth - 1]) * mask[2][0];
                sum += toUnsignedInt(bytes[i + imageWidth]) * mask[2][1];
                sum += toUnsignedInt(bytes[i + imageWidth + 1]) * mask[2][2];

            } catch (ArrayIndexOutOfBoundsException e) {
                sum = bytes[i];
            }

            result[i] = (byte) ((sum < 0) ? 0 : ((sum > 255) ? 255 : sum));
        }
        return result;
    }

    public static int toUnsignedInt(byte b) {
        return b & 0xFF;
    }

    /**
     * Генерирует палитру для 8-ми битного bmp
     *
     * @return
     */
    public static byte[] generateColors() {
        byte[] arr = new byte[256 * 4];
        for (int i = 0; i < 256 * 4; i += 4) {
            byte b = (byte) (i / 4);
            arr[i] = b;
            arr[i + 1] = b;
            arr[i + 2] = b;
            arr[i + 3] = 0;
        }
        return arr;
    }
}
