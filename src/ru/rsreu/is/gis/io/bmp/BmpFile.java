package ru.rsreu.is.gis.io.bmp;

import ru.rsreu.is.gis.util.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class BmpFile {

    private BitMapFileHeader fileHeader;
    private BitMapInfoHeader infoHeader;

    public BmpFile(File file) {
        this(file.toPath());
    }

    public BmpFile(Path path) {
        try {
            byte[] imageBytes = Files.readAllBytes(path);
            byte[] fileHeaderBytes = Arrays.copyOfRange(imageBytes, 0, BitMapFileHeader.length());
            byte[] infoHeaderBytes = Arrays.copyOfRange(imageBytes, BitMapFileHeader.length(),
                    BitMapFileHeader.length() + BitMapInfoHeader.length());

            fileHeader = SerializationUtils.deserialize(fileHeaderBytes, BitMapFileHeader.class, ByteOrder.LITTLE_ENDIAN);
            infoHeader = SerializationUtils.deserialize(infoHeaderBytes, BitMapInfoHeader.class, ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
