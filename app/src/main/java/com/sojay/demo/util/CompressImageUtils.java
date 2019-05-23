package com.sojay.demo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.sojay.demo.article.ArticleActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by larvea on 2018/12/4.
 * 图片压缩工具类
 * 图片最大限制    2*1024*1024  B
 */
public class CompressImageUtils {

    private CompressListener listener;

    /**
     * @param imagePath
     * @return file
     * @throws Exception
     */
    public static ByteArrayOutputStream compressImage(String imagePath) throws IOException {

        // Get bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        // Get bitmap output stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        // Compress bitmap into ByteArrayOutputStream
        int compressRatio = 80;
        while (byteArrayOutputStream.toByteArray().length / 1024f > (2 * 1024)) {
            byteArrayOutputStream.reset();
            compressRatio -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressRatio, byteArrayOutputStream);
        }
        return byteArrayOutputStream;
    }

    public static File compressImage2FileSync(String path) {
        // Get bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > width) {
            while (width / inSampleSize > ArticleActivity.SCREENWIDTH) {
                inSampleSize *= 2;
            }
        } else {
            while (height / inSampleSize > ArticleActivity.SCREENHEIGHT) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        // Get bitmap output stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int compressRatio = 100;
        do {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressRatio, byteArrayOutputStream);
            compressRatio -= 10;
        }
        while (byteArrayOutputStream.toByteArray().length / 1024f > (2 * 1024) && compressRatio > 20);     //限制图片最大不超过2m   质量压缩不得小于20
        String imageName = new File(path).getName();
        int dotIndex = imageName.lastIndexOf('.');
        String suffix = "jpg";
        if (dotIndex != -1) {
            suffix = imageName.substring(dotIndex, imageName.length());
        }
        String prefix = imageName.substring(0, dotIndex);
        if (prefix.length() < 3) {
            prefix = "TMP_" + prefix;
        }
        try {
            File tempFile = File.createTempFile(prefix, suffix);
            FileOutputStream fos = new FileOutputStream(tempFile);
            byteArrayOutputStream.writeTo(fos);
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            fos.flush();
            fos.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return new File(path);
        }
    }

    public void compressImage2FileAsync(String path, CompressListener listener) {
        this.listener = listener;
        new CompressImageTask().execute(path);
    }

    private class CompressImageTask extends AsyncTask<String, Void, File> {
        @Override
        protected File doInBackground(String... params) {
            return compressImage2FileSync(params[0]);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (listener != null) {
                listener.onSuccess(file);
            }
        }
    }

    /**
     * 判断照片是否旋转
     *
     * @param originpath
     * @return
     */
    public static String amendRotatePhoto(String originpath) {

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);
        if (angle == 0)
            return originpath;
        // 把原图压缩后得到Bitmap对象
        Bitmap bmp = getCompressPhoto(originpath);


        // 修复图片被旋转的角度
        Bitmap bitmap = rotaingImageView(angle, bmp);

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(bitmap, originpath);
    }

    /**
     * 获取旋转角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
        Log.i("Image旋转--", degree + "");
        return degree;
    }

    /**
     * 根据file path得到bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getCompressPhoto(String path) {
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public static String savePhotoToSD(Bitmap mbitmap, String fileName) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(fileName);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 旋转bitmap
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public interface CompressListener {
        void onSuccess(File file);
    }
}
