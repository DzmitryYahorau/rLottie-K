package com.yahoraustudio.rlottie_poc;

import android.content.Context;
import android.view.animation.Animation;
import android.widget.ImageView;


import java.util.HashMap;

public class RLottieImageView extends ImageView {

    private HashMap<String, Integer> layerColors;
    private RLottieDrawable drawable;
    private ImageReceiver imageReceiver;
    private boolean autoRepeat;
    private boolean attachedToWindow;
    private boolean playing;
    private boolean startOnAttach;
    private Integer layerNum;
    private boolean onlyLastFrame;
    public boolean cached;
    private boolean reverse;

    public RLottieImageView(Context context) {
        super(context);
    }
//
//    public void clearLayerColors() {
//        layerColors.clear();
//    }
//
//    public void setLayerNum(Integer layerNum) {
//        this.layerNum = layerNum;
//        if (this.imageReceiver != null) {
//            this.imageReceiver.setLayerNum(layerNum);
//        }
//    }
//

    public void setAnimation(int resId, int w, int h, int[] colorReplacement) {
        setAnimation(new RLottieDrawable(resId, "" + resId, MainActivityKt.getDp(w), MainActivityKt.getDp(h), false, colorReplacement));
    }

    //
//    public void setOnAnimationEndListener(Runnable r) {
//        if (drawable != null) {
//            drawable.setOnAnimationEndListener(r);
//        }
//    }
//
    public void setAnimation(RLottieDrawable lottieDrawable) {
        if (drawable == lottieDrawable) {
            return;
        }
        if (imageReceiver != null) {
//            imageReceiver.onDetachedFromWindow();
            imageReceiver = null;
        }
        drawable = lottieDrawable;
        drawable.setMasterParent(this);
        if (autoRepeat) {
            drawable.setAutoRepeat(1);
        }
//        if (layerColors != null) {
//            drawable.beginApplyLayerColors();
//            for (HashMap.Entry<String, Integer> entry : layerColors.entrySet()) {
//                drawable.setLayerColor(entry.getKey(), entry.getValue());
//            }
//            drawable.commitApplyLayerColors();
//        }
        drawable.setAllowDecodeSingleFrame(true);
        setImageDrawable(drawable);
    }

    //
//
//    public void setOnlyLastFrame(boolean onlyLastFrame) {
//        this.onlyLastFrame = onlyLastFrame;
//    }
//
//    public void setReverse() {
//        if (drawable != null) {
//            drawable.setPlayInDirectionOfCustomEndFrame(true);
//            drawable.setCurrentFrame(drawable.getFramesCount());
//            drawable.setCustomEndFrame(0);
//        }
//    }
//
    protected void onLoaded() {

    }

    //
//    public void clearAnimationDrawable() {
//        if (drawable != null) {
//            drawable.stop();
//        }
//        if (imageReceiver != null) {
//            imageReceiver.onDetachedFromWindow();
//            imageReceiver = null;
//        }
//        drawable = null;
//        setImageDrawable(null);
//    }
//
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
//        if (imageReceiver != null) {
//            imageReceiver.onAttachedToWindow();
//            if (playing) {
//                imageReceiver.startAnimation();
//            }
//        }
        if (drawable != null) {
            drawable.setCallback(this);
            if (playing) {
                drawable.start();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;
        if (drawable != null) {
            drawable.stop();
        }
//        if (imageReceiver != null) {
//            imageReceiver.onDetachedFromWindow();
//        }
    }
//
//    public boolean isPlaying() {
//        return drawable != null && drawable.isRunning();
//    }
//
    public void setAutoRepeat(boolean repeat) {
        autoRepeat = repeat;
    }
//
//    public void setProgress(float progress) {
//        if (drawable != null) {
//            drawable.setProgress(progress);
//        }
//    }
//
//    public ImageReceiver getImageReceiver() {
//        return imageReceiver;
//    }
//
//    @Override
//    public void setImageResource(int resId) {
//        super.setImageResource(resId);
//        drawable = null;
//    }
//
    public void playAnimation() {
        if (drawable == null) {
            return;
        }
        playing = true;
        drawable.start();
    }

    public void stopAnimation() {
        if (drawable == null && imageReceiver == null) {
            return;
        }
        playing = false;
        if (attachedToWindow) {
            if (drawable != null) {
                drawable.stop();
            }
            if (imageReceiver != null) {
//                imageReceiver.stopAnimation();
            }
        } else {
            startOnAttach = false;
        }
    }

    public RLottieDrawable getAnimatedDrawable() {
        return drawable;
    }
}
