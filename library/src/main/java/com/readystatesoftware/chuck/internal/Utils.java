package com.readystatesoftware.chuck.internal;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * <p>description：
 * <p>===============================
 * <p>creator：lixiancheng
 * <p>create time：2018/5/7 下午4:27
 * <p>===============================
 * <p>reasons for modification：
 * <p>Modifier：
 * <p>Modify time：
 * <p>@version
 */

public class Utils {
    public static boolean setAnimatedImageUriToFrescoView(final SimpleDraweeView sdv, Uri uri, final Context context, final boolean isFixedSize) {

        BaseControllerListener<ImageInfo> listener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (!isFixedSize) {
                    int width = imageInfo.getWidth();
                    int height = imageInfo.getHeight();
                    int viewWidth = getScreenWidth(context);
                    int viewHeight = (int) (1.0 * viewWidth * height / width);
                    ViewGroup.LayoutParams layoutParams = sdv.getLayoutParams();
                    layoutParams.width = viewWidth - 2 * dp2px(context, 16);
                    layoutParams.height = viewHeight;
                    sdv.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }
        };

        DraweeController controller = Fresco
                .newDraweeControllerBuilder()
                .setControllerListener(listener)
                .setOldController(sdv.getController())
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        sdv.setController(controller);
        return true;

    }
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }
}
