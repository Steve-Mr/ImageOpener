package com.maary.imageopener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Point deviceBounds = Util.getDeviceBounds(MainActivity.this);
        int device_height = deviceBounds.y;
        int device_width = deviceBounds.x;

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null){
            if (type.startsWith("image/")){
                try {
                    bitmap = Util.getBitmap(intent, MainActivity.this);

                    int width = bitmap.getWidth();
                    Log.e("width", String.valueOf(width));

                    LinearLayout container = findViewById(R.id.container);

                    HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
                    ScrollView verticalScrollView = new ScrollView(this);
                    ImageView imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setAdjustViewBounds(true);
                    imageView.setId(View.generateViewId());

                    Boolean isVertical = Util.isVertical(device_height, device_width, bitmap);

                    int bitmap_full_width = bitmap.getWidth();
                    int bitmap_full_height = bitmap.getHeight();
                    int desired_width;
                    int desired_height;
                    if (isVertical) {
                        desired_width = device_width;
                        float scale = (float) device_width / bitmap_full_width;
                        desired_height = (int) (scale * bitmap_full_height);

                        //bitmap = Bitmap.createScaledBitmap(bitmap, desired_width, desired_height, true);

                        //Vertical Scroll View
                        LinearLayout.LayoutParams vLayoutParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        );
                        verticalScrollView.setLayoutParams(vLayoutParams);
                        verticalScrollView.setFillViewport(true);

                        ViewGroup.LayoutParams vImageLayoutParams =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        imageView.setLayoutParams(vLayoutParams);

                        container.addView(verticalScrollView, vImageLayoutParams);
                        verticalScrollView.addView(imageView);

                    } else {
                        desired_height = device_height;
                        float scale = (float) device_height / bitmap_full_height;
                        desired_width = (int) (scale * bitmap_full_width);

                        //bitmap = Bitmap.createScaledBitmap(bitmap, desired_width, desired_height, true);

                        //Horizontal Scroll View
                        LinearLayout.LayoutParams hLayoutParams =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                        horizontalScrollView.setLayoutParams(hLayoutParams);
                        horizontalScrollView.setFillViewport(true);

                        LinearLayout.LayoutParams hImageLayoutParams =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                        imageView.setLayoutParams(hLayoutParams);

                        container.addView(horizontalScrollView, hImageLayoutParams);
                        horizontalScrollView.addView(imageView);

                    }

                    imageView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                            imageView.setPadding(
                                    0,//windowInsets.getSystemWindowInsetLeft(),
                                    MainActivity.this.getWindowManager().getDefaultDisplay().getCutout().getSafeInsetTop(),
                                    0,//windowInsets.getSystemWindowInsetRight(),
                                    windowInsets.getSystemWindowInsetBottom()
                            );
                            return windowInsets.consumeSystemWindowInsets();
                        }
                    });

                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}