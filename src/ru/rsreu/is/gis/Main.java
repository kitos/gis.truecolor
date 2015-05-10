package ru.rsreu.is.gis;

import ru.rsreu.is.gis.truecolor.bmp.BitMapFileHeader;
import ru.rsreu.is.gis.truecolor.bmp.BitMapInfoHeader;
import ru.rsreu.is.gis.truecolor.SerializationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

    public static void main(String... args) {
        try {
            Path output = Paths.get("./dst.bmp");

            BitMapFileHeader fileHeader = new BitMapFileHeader();
            fileHeader.bfSize = 640 * 480 * 3 + fileHeader.bfOffbits;
            byte[] fileHeaderBytes = SerializationUtils.serialize(fileHeader);
            Files.write(output, fileHeaderBytes);

            BitMapInfoHeader infoHeader = new BitMapInfoHeader();
            infoHeader.biHeight = 480;
            infoHeader.biWidth = 640;
            infoHeader.biBitCount = 24;
            byte[] infoHeaderBytes = SerializationUtils.serialize(infoHeader);
            Files.write(output, infoHeaderBytes, StandardOpenOption.APPEND);

            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            process(bytes);
            Files.write(output, bytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void process(byte[] bytes) {
        for (int i = 0; i < bytes.length; i += 3) {
            // invert red
            bytes[i + 2] = (byte) (bytes[i + 2] ^ 0b11111111);

            // swap green and blue
            byte temp = bytes[i + 1];
            bytes[i + 1] = bytes[i];
            bytes[i] = temp;
        }
    }
}