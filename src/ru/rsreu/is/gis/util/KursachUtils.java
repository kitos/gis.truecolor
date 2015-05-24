package ru.rsreu.is.gis.util;

import java.util.Arrays;
import java.util.Objects;

public class KursachUtils {

    public static byte[][] encode(byte[] imageBytes, int iterations) {
        return average(new byte[][]{imageBytes, ArrayUtils.empty()}, iterations);
    }

    private static byte[][] average(byte[][] array, int iterations) {
        // усредняем указанное количество итераций или пока есть что усреднять
        if (iterations > 0 && array[0].length > 1) {
            // усредняем полусуммы
            byte[][] encodedArr = average(array[0]);
            // конкатинируем полученные полуразности с полуразностями, полученными на предыдущих итерациях
            encodedArr[1] = ArrayUtils.concat(encodedArr[1], array[1]);
            return average(encodedArr, --iterations);
        } else {
            return array;
        }
    }

    /**
     * Разбивает переданный массив на массив полумумм и полуразностей.
     *
     * @param array
     * @return двумерный массив, первой строкой которого являются полусуммы, второй - полуразности
     */
    private static byte[][] average(byte[] array) {
        Objects.requireNonNull(array);

        byte[][] res = new byte[2][];
        // сюда будем записывать полусуммы (усреднённые значения)
        res[0] = new byte[array.length / 2];
        // сюда полуразности (уточняющие коэффициенты)
        res[1] = new byte[array.length / 2];

        for (int i = 0; i < array.length; i += 2) {
            res[0][i / 2] = (byte) ((array[i] + array[i + 1]) / 2);
            res[1][i / 2] = (byte) ((array[i] - array[i + 1]) / 2);
        }
        return res;
    }

    public static byte[] decode(byte[] encodedImageBytes, int baseSize) {
        byte[] avgs = Arrays.copyOf(encodedImageBytes, baseSize);
        byte[] diffs = Arrays.copyOfRange(encodedImageBytes, baseSize, encodedImageBytes.length);
        return decode(new byte[][]{avgs, diffs});
    }

    public static byte[] decode(byte[][] encodedImageBytes) {
        Objects.requireNonNull(encodedImageBytes);

        if (encodedImageBytes[1].length == 0) {
            return encodedImageBytes[0];
        } else {
            byte[] avgs = encodedImageBytes[0];
            // вытаскиваем из массива всех полуразностей те, что необходимы для восстановления на текущей итерации
            byte[] diffs = Arrays.copyOf(encodedImageBytes[1], avgs.length);
            byte[] newAvgs = new byte[avgs.length * 2];
            // восстанавливаем полусуммы (или, возможно, исходный массив)
            for (int i = 0; i < avgs.length * 2; i += 2) {
                byte diff = diffs[i / 2];
                byte avg = avgs[i / 2];

                newAvgs[i] = (byte) (avg + diff);
                newAvgs[i + 1] = (byte) (avg - diff);
            }

            // удаляем из массива полуразнотей те, что уже использовали
            byte[] diffRests = Arrays.copyOfRange(encodedImageBytes[1], avgs.length, encodedImageBytes[1].length);
            return decode(new byte[][]{newAvgs, diffRests});
        }
    }
}