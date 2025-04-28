package com.yahoraustudio.rlottie_poc;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AndroidUtilities {
    public static final Rect rectTmp2 = new Rect();
    public static ThreadLocal<byte[]> readBufferLocal = new ThreadLocal<>();
    public static ThreadLocal<byte[]> bufferLocal = new ThreadLocal<>();

    public static volatile DispatchQueue globalQueue = new DispatchQueue("globalQueue");

    public static String readRes(File path, int rawRes) {
        int totalRead = 0;
        byte[] readBuffer = readBufferLocal.get();
        if (readBuffer == null) {
            readBuffer = new byte[64 * 1024];
            readBufferLocal.set(readBuffer);
        }
        InputStream inputStream = null;
        try {
            if (path != null) {
                inputStream = new FileInputStream(path);
            } else {
                inputStream = ApplicationLoader.Companion.getApplicationLoaderInstance().getResources().openRawResource(rawRes);
            }
            int readLen;
            byte[] buffer = bufferLocal.get();
            if (buffer == null) {
                buffer = new byte[4096];
                bufferLocal.set(buffer);
            }
            while ((readLen = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                if (readBuffer.length < totalRead + readLen) {
                    byte[] newBuffer = new byte[readBuffer.length * 2];
                    System.arraycopy(readBuffer, 0, newBuffer, 0, totalRead);
                    readBuffer = newBuffer;
                    readBufferLocal.set(readBuffer);
                }
                if (readLen > 0) {
                    System.arraycopy(buffer, 0, readBuffer, totalRead, readLen);
                    totalRead += readLen;
                }
            }
        } catch (Throwable e) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable ignore) {

            }
        }

        return new String(readBuffer, 0, totalRead);
    }

    public static String readRes(int rawRes) {
        return readRes(null, rawRes);
    }

    public static String readRes(File path) {
        return readRes(path, 0);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (ApplicationLoader.Companion.getApplicationLoaderInstance() == null) {
            return;
        }
        if (delay == 0) {
            ApplicationLoader.Companion.getApplicationHandler().post(runnable);
        } else {
            ApplicationLoader.Companion.getApplicationHandler().postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        if (ApplicationLoader.Companion.getApplicationHandler() == null) {
            return;
        }
        ApplicationLoader.Companion.getApplicationHandler().removeCallbacks(runnable);
    }

    public static int dp(int value) {
        return (int) (value * Resources.getSystem().getDisplayMetrics().density);
    }
    public static void recycleBitmaps(List<Bitmap> bitmapToRecycle) {
        if (Build.VERSION.SDK_INT <= 23) {
            // cause to crash:
            // /system/lib/libskia.so (SkPixelRef::unlockPixels()+3)
            // /system/lib/libskia.so (SkBitmap::freePixels()+14)
            // /system/lib/libskia.so (SkBitmap::setPixelRef(SkPixelRef*, int, int)+50)
            // /system/lib/libhwui.so (android::uirenderer::ResourceCache::recycleLocked(SkBitmap*)+30)
            // /system/lib/libhwui.so (android::uirenderer::ResourceCache::recycle(SkBitmap*)+20)
            // gc recycle it automatically
            return;
        }
        if (bitmapToRecycle != null && !bitmapToRecycle.isEmpty()) {
            ArrayList<WeakReference<Bitmap>> bitmapsToRecycleRef = new ArrayList<>();
            for (int i = 0; i < bitmapToRecycle.size(); i++) {
                Bitmap bitmap = bitmapToRecycle.get(i);
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmapsToRecycleRef.add(new WeakReference<>(bitmap));
                }
            }
            AndroidUtilities.runOnUIThread(() -> AndroidUtilities.globalQueue.postRunnable(() -> {
                for (int i = 0; i < bitmapsToRecycleRef.size(); i++) {
                    Bitmap bitmap = bitmapsToRecycleRef.get(i).get();
                    bitmapsToRecycleRef.get(i).clear();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        try {
                            bitmap.recycle();
                        } catch (Exception e) {
//                            FileLog.e(e);
                        }
                    }
                }
            }), 36);
        }
    }

    public static int clamp(int value, int maxValue, int minValue) {
        return Math.max(Math.min(value, maxValue), minValue);
    }

    public static long clamp(long value, long maxValue, long minValue) {
        return Math.max(Math.min(value, maxValue), minValue);
    }

    public static float clamp(float value, float maxValue, float minValue) {
        if (Float.isNaN(value)) {
            return minValue;
        }
        if (Float.isInfinite(value)) {
            return maxValue;
        }
        return Math.max(Math.min(value, maxValue), minValue);
    }

    public static float clamp01(float value) {
        return clamp(value, 1f, 0f);
    }

    public static double clamp(double value, double maxValue, double minValue) {
        if (Double.isNaN(value)) {
            return minValue;
        }
        if (Double.isInfinite(value)) {
            return maxValue;
        }
        return Math.max(Math.min(value, maxValue), minValue);
    }

}
