package ru.rsreu.is.gis.io.bmp;

import ru.rsreu.is.gis.util.SerializationUtils;

final public class BitMapFileHeader {
    public short bfType = 0x4d42; // 'BM'
    public int bfSize; // ������ ����� � ������
    public short avgLength = 0; // ������ ������� ���������� �������� (������������ � ������������� ��������)
    public short bfReserved2 = 0;
    public int bfOffbits; // �������� � ����� �� ������ ����������� BitMapInfoHeader(40) + BitMapFileHeader(14) + ������� (���� ����)

    public static int length() {
        return SerializationUtils.length(BitMapFileHeader.class);
    }
}
