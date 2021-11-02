package com.example.designapptest.domain;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.designapptest.R;

public class classFunctionStatic {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int WEEK_MILLIS = 7 * DAY_MILLIS;

    public static void showProgress(Context context, ImageView imageView) {
        Glide.with(context).load(R.drawable.progress_gift_loading_image).into(imageView);
        Log.d("check4", "here");
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "vừa mới đăng";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "khoảng 1 phút trước";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " phút trước";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "khoảng 1 giờ trước";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " giờ trước";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "ngày hôm qua";
        } else if (diff < 7 * DAY_MILLIS) {
            return diff / DAY_MILLIS + " ngày trước";
        } else if (diff < 2 * WEEK_MILLIS) {
            return "khoảng 1 tuần trước";
        } else {
            return diff / WEEK_MILLIS + " tuần trước";
        }
    }
}
