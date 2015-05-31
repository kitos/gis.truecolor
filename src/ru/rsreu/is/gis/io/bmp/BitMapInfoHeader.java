package ru.rsreu.is.gis.io.bmp;

import ru.rsreu.is.gis.util.SerializationUtils;

public class BitMapInfoHeader {
    public int biSize = 40; // размер заголовка
    public int biWidth;
    public int biHeight;
    public final short biPlains = 1;
    public short biBitCount;
    public int biCompression = 0;      // в данной
    public int biSizeImage = 0;        // лабе
    public int biXPelsPerMeter = 160;  // значения
    public int biYPelsPerMeter = 160;  // должны
    public int biBeClrUsed = 0;        // быть
    public int biBeClrImportant = 0;   // такими

    public static int length() {
        return SerializationUtils.length(BitMapInfoHeader.class);
    }
}
