package ru.rsreu.is.gis.io.bmp;

import ru.rsreu.is.gis.util.SerializationUtils;

final public class BitMapFileHeader {
    public short bfType = 0x4d42; // 'BM'
    public int bfSize; // размер файла в байтах
    public short avgLength = 0; // размер массива усреднённых значений (используется в декодировании вейвлета)
    public short bfReserved2 = 0;
    public int bfOffbits; // смещение в битах до начала изображения BitMapInfoHeader(40) + BitMapFileHeader(14) + Палитра (если есть)

    public static int length() {
        return SerializationUtils.length(BitMapFileHeader.class);
    }
}
