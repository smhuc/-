package com.wangtian.message.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wangtian.message.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @Author Archy Wang
 * @Date 2017/12/17
 * @Description 封装glide
 *
 */

public class GlideUtils {

    public static void loadCirclePic(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CenterCrop(context), new CropCircleTransformation(context))
                .error(R.drawable.icon_nopic)
                .into(imageView);
    }

    public static void loadRectPic(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CenterCrop(context),new RoundedCornersTransformation(context, 5, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    public static void loadNormalPic(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).error(R.drawable.empty_image).into(imageView);
    }

    public static void loadLocalPic(Context context, int resourceId, ImageView imageView){
        Glide.with(context).load(resourceId).into(imageView);
    }
    public static void loadLocalCirclePic(Context context, int resourceId, ImageView imageView){
        Glide.with(context)
                .load(resourceId)
                .bitmapTransform(new CenterCrop(context), new CropCircleTransformation(context))
                .into(imageView);
    }

    public static void loadLocalRectPic(Context context, int resourceId, ImageView imageView){
        Glide.with(context)
                .load(resourceId)
                .bitmapTransform(new CenterCrop(context),new RoundedCornersTransformation(context, 5, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    public static void loadArtRectPic(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CenterCrop(context),new RoundedCornersTransformation(context, 12, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }


}
