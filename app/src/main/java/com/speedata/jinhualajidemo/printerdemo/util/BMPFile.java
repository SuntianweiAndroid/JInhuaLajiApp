//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.speedata.jinhualajidemo.printerdemo.util;

import android.util.Log;
import java.io.FileOutputStream;

public class BMPFile {
    private static final int BITMAPFILEHEADER_SIZE = 14;
    private static final int BITMAPINFOHEADER_SIZE = 40;
    private byte[] bfType = new byte[]{(byte)66, (byte)77};
    private int bfSize = 0;
    private int bfReserved1 = 0;
    private int bfReserved2 = 0;
    private int bfOffBits = 62;
    private int biSize = 40;
    private int biWidth = 0;
    private int biHeight = 0;
    private int biPlanes = 1;
    private int biBitCount = 1;
    private int biCompression = 0;
    private int biSizeImage = 0;
    private int biXPelsPerMeter = 0;
    private int biYPelsPerMeter = 0;
    private int biClrUsed = 2;
    private int biClrImportant = 2;
    private byte[] bitmap;
    int scanLineSize = 0;
    private byte[] colorPalette = new byte[]{(byte)0, (byte)0, (byte)0, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1};

    public BMPFile() {
    }

    public void saveBitmap(FileOutputStream fos, byte[] imagePix, int parWidth, int parHeight) {
        try {
            this.save(fos, imagePix, parWidth, parHeight);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    private void save(FileOutputStream fos, byte[] imagePix, int parWidth, int parHeight) {
        try {
            this.convertImage(imagePix, parWidth, parHeight);
            this.writeBitmapFileHeader(fos);
            this.writeBitmapInfoHeader(fos);
            this.writePixelArray(fos);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    private boolean convertImage(byte[] imagePix, int parWidth, int parHeight) {
        this.bitmap = imagePix;
        //
        this.bfSize = 62 + (parWidth + 31) / 32 * 4 * parHeight;
        this.biWidth = parWidth;
        this.biHeight = parHeight;
        this.scanLineSize = (parWidth * 1 + 31) / 32 * 4;
        return true;
    }

    private void writeBitmapFileHeader(FileOutputStream fos) {
        try {
            fos.write(this.bfType);
            fos.write(this.intToDWord(this.bfSize));
            fos.write(this.intToWord(this.bfReserved1));
            fos.write(this.intToWord(this.bfReserved2));
            fos.write(this.intToDWord(this.bfOffBits));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private void writeBitmapInfoHeader(FileOutputStream fos) {
        try {
            fos.write(this.intToDWord(this.biSize));
            fos.write(this.intToDWord(this.biWidth));
            fos.write(this.intToDWord(this.biHeight));
            fos.write(this.intToWord(this.biPlanes));
            fos.write(this.intToWord(this.biBitCount));
            fos.write(this.intToDWord(this.biCompression));
            fos.write(this.intToDWord(this.biSizeImage));
            fos.write(this.intToDWord(this.biXPelsPerMeter));
            fos.write(this.intToDWord(this.biYPelsPerMeter));
            fos.write(this.intToDWord(this.biClrUsed));
            fos.write(this.intToDWord(this.biClrImportant));
            fos.write(this.colorPalette);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private void writePixelArray(FileOutputStream fos) {
        try {
            for(int e = this.biHeight; e > 0; --e) {
                for(int k = (e - 1) * this.scanLineSize; k < (e - 1) * this.scanLineSize + this.scanLineSize; ++k) {
                    fos.write(this.bitmap[k] & 255);
                }
            }
        } catch (Exception var4) {
            Log.e("BMPFile", var4.toString());
        }
    }

    private byte[] intToWord(int parValue) {
        byte[] retValue = new byte[]{(byte)(parValue & 255), (byte)(parValue >> 8 & 255)};
        return retValue;
    }

    private byte[] intToDWord(int parValue) {
        byte[] retVa1lue = new byte[]{(byte)(parValue & 255), (byte)(parValue >> 8 & 255), (byte)(parValue >> 16 & 255), (byte)(parValue >> 24 & 255)};
        return retVa1lue;
    }
}
