package ru.rsreu.is.gis.io.bmp;

import ru.rsreu.is.gis.util.SerializationUtils;

public class BitMapInfoHeader {
    public final int biSize = 40; // ������ ���������
    public int biWidth;
    public int biHeight;
    public final short biPlains = 1;
    public short biBitCount;
    public int biCompression = 0;      // � ������
    public int biSizeImage = 0;        // ����
    public int biXPelsPerMeter = 160;  // ��������
    public int biYPelsPerMeter = 160;  // ������
    public int biBeClrUsed = 0;        // ����
    public int biBeClrImportant = 0;   // ������

    public static int length() {
        return SerializationUtils.length(BitMapInfoHeader.class);
    }
}
