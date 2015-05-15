package ru.rsreu.is.gis;

import ru.rsreu.is.gis.truecolor.SerializationUtils;
import ru.rsreu.is.gis.truecolor.bmp.BitMapFileHeader;
import ru.rsreu.is.gis.truecolor.bmp.BitMapInfoHeader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

    public static final int IMAGE_WIDTH = 640;
    public static final int IMAGE_HEIGHT = 480;

    private static byte[][] contur = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}},
            contur2 = {
                    {-1, -1, -1},
                    {-1, 9, -1},
                    {-1, -1, -1}},
            contur3 = {
                    {1, -2, 1},
                    {-2, 4, -2},
                    {1, -2, 1}};

    private static byte[][] reliefNorth = {
            {1, 1, 1},
            {1, -2, 1},
            {-1, -1, -1}},
            reliefEast = {
                    {-1, 1, 1},
                    {-1, -2, 1},
                    {-1, 1, 1}},
            reliefSouth = {
                    {-1, -1, -1},
                    {1, -2, 1},
                    {1, 1, 1}},
            reliefWest = {
                    {1, 1, -1},
                    {1, -2, 1},
                    {1, 1, -1}};

    private static byte[][][] masks = {contur, contur2, contur3, reliefNorth, reliefEast, reliefSouth, reliefWest};

    public static void main(String... args) {
        try {
            Path output = Paths.get("./dst.bmp");

            BitMapFileHeader fileHeader = new BitMapFileHeader();
            fileHeader.bfSize = IMAGE_WIDTH * IMAGE_HEIGHT + fileHeader.bfOffbits + 256 * 4;
            fileHeader.bfOffbits = 54 + 256 * 4;
            byte[] fileHeaderBytes = SerializationUtils.serialize(fileHeader);
            Files.write(output, fileHeaderBytes);

            BitMapInfoHeader infoHeader = new BitMapInfoHeader();
            infoHeader.biHeight = IMAGE_HEIGHT;
            infoHeader.biWidth = IMAGE_WIDTH;
            infoHeader.biBitCount = 8;
            byte[] infoHeaderBytes = SerializationUtils.serialize(infoHeader);
            Files.write(output, infoHeaderBytes, StandardOpenOption.APPEND);

            byte[] arr = generateColors();
            Files.write(output, arr, StandardOpenOption.APPEND);

            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            bytes = applyMask(bytes, contur2);
            Files.write(output, bytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] generateColors() {
        byte[] arr = new byte[256 * 4];
        for (int i = 0; i < 256 * 4; i += 4) {
            byte b = (byte) (i / 4);
            arr[i] = b;
            arr[i + 1] = b;
            arr[i + 2] = b;
            arr[i + 3] = b;
        }
        return arr;
    }

    private static byte[] applyMask(byte[] bytes, byte[][] mask) {
        byte[] result = new byte[bytes.length];
        int sum;
        for (int i = 0; i < bytes.length; i++) {
            sum = 0;
            try {
                sum += bytes[i - IMAGE_WIDTH - 1] * mask[0][0];
                sum += bytes[i - IMAGE_WIDTH] * mask[0][1];
                sum += bytes[i - IMAGE_WIDTH + 1] * mask[0][2];

                sum += bytes[i - 1] * mask[1][0];
                sum += bytes[i] * mask[1][1];
                sum += bytes[i + 1] * mask[1][2];

                sum += bytes[i + IMAGE_WIDTH - 1] * mask[2][0];
                sum += bytes[i + IMAGE_WIDTH] * mask[2][1];
                sum += bytes[i + IMAGE_WIDTH + 1] * mask[2][2];

            } catch (ArrayIndexOutOfBoundsException e) {
                sum = bytes[i] * mask[1][1];
            }

            result[i] = (byte) ((sum < 0) ? 0 : ((sum > 255) ? 255 : sum));
        }
        return result;
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