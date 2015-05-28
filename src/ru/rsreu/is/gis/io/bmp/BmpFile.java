package ru.rsreu.is.gis.io.bmp;

import ru.rsreu.is.gis.util.ArrayUtils;
import ru.rsreu.is.gis.util.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class BmpFile {

    public static final int COLOR_MAP_SIZE_8_BIT = 256 * 4;

    private BitMapFileHeader fileHeader;
    private BitMapInfoHeader infoHeader;
    private byte[] colorMap;
    private byte[] pixels;

    public BmpFile(File file) {
        this(file.toPath());
    }

    public BmpFile(Path path) {
        try {
            byte[] imageBytes = Files.readAllBytes(path);
            byte[] fileHeaderBytes = Arrays.copyOfRange(imageBytes, 0, BitMapFileHeader.length());
            int endOfHeaders = BitMapFileHeader.length() + BitMapInfoHeader.length();
            byte[] infoHeaderBytes = Arrays.copyOfRange(imageBytes, BitMapFileHeader.length(), endOfHeaders);

            fileHeader = SerializationUtils.deserialize(fileHeaderBytes, BitMapFileHeader.class, ByteOrder.LITTLE_ENDIAN);
            infoHeader = SerializationUtils.deserialize(infoHeaderBytes, BitMapInfoHeader.class, ByteOrder.LITTLE_ENDIAN);

            if (infoHeader.biBitCount == 8) {
                int endOfColorMap = BitMapFileHeader.length() + BitMapInfoHeader.length() + COLOR_MAP_SIZE_8_BIT;
                colorMap = Arrays.copyOfRange(imageBytes, endOfHeaders, endOfColorMap);
                pixels = Arrays.copyOfRange(imageBytes, endOfColorMap, imageBytes.length);
            } else {
                // к сожалению прога работает только с 8-ми битными изображениями
                throw new RuntimeException("Bit depth is not supported, try");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] bytes() {
        return ArrayUtils.concat(SerializationUtils.serialize(fileHeader, ByteOrder.LITTLE_ENDIAN),
                SerializationUtils.serialize(infoHeader, ByteOrder.LITTLE_ENDIAN),
                colorMap, pixels);
    }

    public BitMapFileHeader getFileHeader() {
        return fileHeader;
    }

    public BitMapInfoHeader getInfoHeader() {
        return infoHeader;
    }

    public byte[] getColorMap() {
        return colorMap;
    }

    public void setColorMap(byte[] colorMap) {
        this.colorMap = colorMap;
    }

    public byte[] getPixels() {
        return pixels;
    }

    public void setPixels(byte[] pixels) {
        this.pixels = pixels;
    }
}
