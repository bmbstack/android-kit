package com.bmbstack.kit.app.binding;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageBindingAdapter {

    @BindingAdapter(value = {"imageUrl"}, requireAll = false)
    public static void bindImageUrl(ImageView imageView, String imageUrl) {
        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(
                new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL)
        );

        Glide.with(imageView)
                .load(imageUrl)
                .transition(withCrossFade(400))
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(imageView);
    }

    @BindingAdapter({"imageWidth", "imageHeight"})
    public static void bindImageSize(ImageView imageView, int imageWidth, int imageHeight) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = imageWidth;
        params.height = imageHeight;
        imageView.setLayoutParams(params);
    }
}
