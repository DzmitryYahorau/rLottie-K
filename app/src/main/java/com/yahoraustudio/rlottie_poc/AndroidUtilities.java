package com.yahoraustudio.rlottie_poc;

import android.graphics.Rect;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
}
