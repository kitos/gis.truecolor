package ru.rsreu.is.gis.truecolor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final public class SerializationUtils {
    private SerializationUtils() {
    }

    /**
     * Преобразует объект (его поля) в массив байтов, пригодных для записи в файл.
     *
     * @param structure
     * @return
     */
    public static byte[] serialize(Object structure) {
        byte[] arr = new byte[0];
        for (Field field : structure.getClass().getDeclaredFields()) {
            Class<?> fieldClass = field.getType();
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(structure);
                Method toByteArray = SerializationUtils.class.getDeclaredMethod("toByteArray", fieldClass);
                byte[] bytes = (byte[]) toByteArray.invoke(null, fieldValue);

                arr = ArrayUtils.concat(arr, bytes);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("", e);
            } catch (NoSuchMethodException e) {
                String message = String.format("Object type is not supported: %s", fieldClass.getName());
                throw new RuntimeException(message);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Something went wrong");
            }
        }
        return arr;
    }

    /**
     * Используется в {@link #serialize}
     *
     * @param i
     * @return
     */
    private static byte[] toByteArray(int i) {
        byte[] bytes = {(byte) ((i >> 24) & 0xff),
                (byte) ((i >> 16) & 0xff),
                (byte) ((i >> 8) & 0xff),
                (byte) (i & 0xff)};
        ArrayUtils.reverse(bytes);
        return bytes;
    }

    /**
     * Используется в {@link #serialize}
     *
     * @param i
     * @return
     */
    private static byte[] toByteArray(short i) {
        byte[] bytes = {(byte) ((i >> 8) & 0xff),
                (byte) (i & 0xff)};
        ArrayUtils.reverse(bytes);
        return bytes;
    }

    /**
     * Используется в {@link #serialize}
     *
     * @param s
     * @return
     */
    private static byte[] toByteArray(String s) {
        return s.getBytes();
    }
}
