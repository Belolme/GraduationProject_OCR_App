package com.billin.www.graduationproject_ocr.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理相关
 * <p>
 * Created by Billin on 2018/4/14.
 */
public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    public static boolean compressImage(String filePath, String targetPath, int quality, RectF clip) {
        //获取一定尺寸的图片
        Bitmap bm = getSmallBitmap(filePath, clip);

        //获取相片拍摄角度
        int degree = readPictureDegree(filePath);

        //旋转照片角度，防止头像横着显示
        if (degree != 0) {
            bm = rotateBitmap(bm, degree);
        }

        File outputFile = new File(targetPath);
        FileOutputStream out = null;
        try {
            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
                return false;
            }

            out = new FileOutputStream(outputFile, false);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * 压缩图片
     *
     * @param filePath   图片的位置
     * @param targetPath 压缩后存放的位置
     * @param quality    压缩的质量，其取值范围为 [0, 100]
     * @return 是否压缩成功
     */
    public static boolean compressImage(String filePath, String targetPath, int quality) {
        return compressImage(filePath, targetPath, quality, null);
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath, RectF clip) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        //只解析图片边沿，获取宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;

        Bitmap src = BitmapFactory.decodeFile(filePath, options);

        if (clip != null) {
            Matrix matrix = new Matrix();
            matrix.setScale(1f / options.inSampleSize, 1f / options.inSampleSize);

            RectF dstClip = new RectF();
            matrix.mapRect(dstClip, clip);
            return Bitmap.createBitmap(src, (int) dstClip.left, (int) dstClip.top,
                    (int) dstClip.width(), (int) dstClip.height());
        } else {
            return src;
        }
    }


    /**
     * 获取照片角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    /**
     * 旋转照片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
        }

        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
