package com.htetznaing.micustomfont;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Converter {
    public Bitmap textAsBitmap(String text, float textSize, float stroke,
                               int color, Typeface typeface) {

        TextPaint paint = new TextPaint();
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(stroke);
        paint.setTypeface(typeface);

        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);

        StaticLayout staticLayout = new StaticLayout(text, 0, text.length(),
                paint, 435, Layout.Alignment.ALIGN_CENTER, 1.0f,
                1.0f, false);

        int height = 70;

        Bitmap image = Bitmap.createBitmap(435, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawARGB(0xFF, 0xFF, 0xFF, 0xFF);

        staticLayout.draw(canvas);

        return image;

    }

        public boolean saveImage(Bitmap imageData, String filename) {
            File sdIconStorageDir = new File(filename);
            try {
                String filePath = sdIconStorageDir.toString();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                BufferedOutputStream bos = new BufferedOutputStream(
                        fileOutputStream);
                imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                bos.flush();
                bos.close();

            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            }

            return true;
        }
}