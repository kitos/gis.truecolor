package ru.rsreu.is.gis.truecolor.bmp;

public class BitMapFileHeader {
    public final short bfType = 0x4d42; // 'BM'
    public int bfSize; // размер файла в байтах
    public short bfReserved1 = 0;
    public short bfReserved2 = 0;
    public int bfOffbits = 54; // смещение в битах до начала изображения BitMapInfoHeader(40) + BitMapFileHeader(14)
}
