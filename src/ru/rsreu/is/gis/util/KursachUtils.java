package ru.rsreu.is.gis.util;

import java.util.Arrays;
import java.util.Objects;

final public class KursachUtils {

    // используетс€ как значение уточн€ющего коэффециента,
    // говорит о том, что пиксель не был усреднЄн
    public static final int ODD = -128;

    public static byte[][] encode(byte[] imageBytes, int iterations) {
        return average(new byte[][]{imageBytes, ArrayUtils.empty()}, iterations);
    }

    private static byte[][] average(byte[][] array, int iterations) {
        // усредн€ем указанное количество итераций или пока есть что усредн€ть
        if (iterations > 0 && array[0].length > 1) {
            // усредн€ем полусуммы
            byte[][] encodedArr = average(array[0]);
            // конкатинируем полученные полуразности с полуразност€ми, полученными на предыдущих итераци€х
            encodedArr[1] = ArrayUtils.concat(encodedArr[1], array[1]);
            return average(encodedArr, --iterations);
        } else {
            return array;
        }
    }

    /**
     * –азбивает переданный массив на массив полумумм и полуразностей.
     *
     * @param array
     * @return двумерный массив, первой строкой которого €вл€ютс€ полусуммы, второй - полуразности
     */
    private static byte[][] average(byte[] array) {
        Objects.requireNonNull(array);

        byte[][] res = new byte[2][];
        // если массив нечЄтного размера - округл€ем в большую сторону
        int avgLength = (int) Math.ceil(array.length / 2.0);
        // сюда будем записывать полусуммы (усреднЄнные значени€)
        res[0] = new byte[avgLength];
        // сюда полуразности (уточн€ющие коэффициенты)
        res[1] = new byte[avgLength];

        for (int i = 0; i < res[0].length; ++i) {
            // если массив нечЄтный, его послений элемент не усредн€ем,
            // а в качестве уточн€ющего элемента записываем -128,
            // чтобы при декодировании корректно его (массив) восстановить
            if (array.length % 2 == 1 && i == res[0].length - 1) {
                res[0][i] = array[i * 2];
                res[1][i] = ODD;
            } else {
                res[0][i] = (byte) ((array[i * 2] + array[i * 2 + 1]) / 2);
                res[1][i] = (byte) ((array[i * 2] - array[i * 2 + 1]) / 2);
            }
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
            // вытаскиваем из массива всех полуразностей те, что необходимы дл€ восстановлени€ на текущей итерации
            byte[] diffs = Arrays.copyOf(encodedImageBytes[1], avgs.length);
            boolean isOdd = diffs[diffs.length - 1] == ODD;
            byte[] newAvgs = new byte[avgs.length * 2 - (isOdd ? 1 : 0)];
            // восстанавливаем полусуммы (или, возможно, исходный массив)
            for (int i = 0; i < newAvgs.length; i += 2) {
                if (isOdd && i == newAvgs.length - 1) {
                    newAvgs[i] = avgs[i / 2];
                } else {
                    byte diff = diffs[i / 2];
                    byte avg = avgs[i / 2];

                    int sum = (avg) + (diff);
                    newAvgs[i] =(byte)  sum;//((sum < 0) ? 0 : ((sum > 255) ? 255 : sum));
                    int div = (avg) - (diff);
                    newAvgs[i + 1] = (byte) div;//((div < 0) ? 0 : ((div > 255) ? 255 : div));
                }
            }

            // удал€ем из массива полуразнотей те, что уже использовали
            byte[] diffRests = Arrays.copyOfRange(encodedImageBytes[1], avgs.length, encodedImageBytes[1].length);
            return decode(new byte[][]{newAvgs, diffRests});
        }
    }
}