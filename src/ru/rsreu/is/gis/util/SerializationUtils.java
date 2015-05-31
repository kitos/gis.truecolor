package ru.rsreu.is.gis.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

final public class SerializationUtils {

    public static int length(Class clazz) {
        int length = 0;

        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldClass = field.getType();
            if (fieldClass.isAssignableFrom(int.class)) {
                length += Integer.BYTES;
            } else if (fieldClass.isAssignableFrom(short.class)) {
                length += Short.BYTES;
            } else {
                String message = String.format("Object type is not supported: %s", fieldClass.getName());
                throw new RuntimeException(message);
            }
        }
        return length;
    }

    /**
     * Преобразует объект (его поля) в массив байтов, пригодных для записи в файл.
     *
     * @param structure
     * @return
     */
    public static byte[] serialize(Object structure, ByteOrder byteOrder) {
        byte[] arr = new byte[0];
        for (Field field : structure.getClass().getDeclaredFields()) {
            Class<?> fieldClass = field.getType();
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(structure);
                Method toByteArray = SerializationUtils.class.getDeclaredMethod("toByteArray", fieldClass, ByteOrder.class);
                byte[] bytes = (byte[]) toByteArray.invoke(null, fieldValue, byteOrder);

                arr = ArrayUtils.concat(arr, bytes);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("", e);
            } catch (NoSuchMethodException e) {
                String message = String.format("Object type is not supported: %s", fieldClass.getName());
                throw new RuntimeException(message);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Something went wrong", e);
            }
        }
        return arr;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz, ByteOrder byteOrder) {
        T deserializedObject = null;
        try {
            deserializedObject = clazz.newInstance();
            int offset = 0;
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Class<?> fieldClass = field.getType();

                // if'ы можно заменить рефлексией как в #serialize
                if (fieldClass.isAssignableFrom(int.class)) {
                    int value = ByteBuffer.wrap(Arrays.copyOfRange(bytes, offset, offset += Integer.BYTES))
                            .order(byteOrder)
                            .getInt();
                    field.set(deserializedObject, value);

                } else if (fieldClass.isAssignableFrom(short.class)) {
                    short value = ByteBuffer.wrap(Arrays.copyOfRange(bytes, offset, offset += Short.BYTES))
                            .order(byteOrder)
                            .getShort();
                    field.set(deserializedObject, value);

                } else {
                    String message = String.format("Object type is not supported: %s", fieldClass.getName());
                    throw new RuntimeException(message);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            String message = String.format("Can't create instance of %s", clazz.getName());
            throw new RuntimeException(message, e);
        }

        return deserializedObject;
    }

    /**
     * Используется в {@link #serialize}
     *
     * @param i
     * @param byteOrder
     * @return
     */
    private static byte[] toByteArray(int i, ByteOrder byteOrder) {
        return ByteBuffer.allocate(Integer.BYTES).order(byteOrder).putInt(i).array();
    }

    /**
     * Используется в {@link #serialize}
     *
     * @param i
     * @param byteOrder
     * @return
     */
    private static byte[] toByteArray(short i, ByteOrder byteOrder) {
        return ByteBuffer.allocate(Short.BYTES).order(byteOrder).putShort(i).array();
    }
}
