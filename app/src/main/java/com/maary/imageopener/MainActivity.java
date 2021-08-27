package com.maary.imageopener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.github.chrisbanes.photoview.PhotoView;

public class MainActivity extends AppCompatActivity {

    Uri imageUri;
    PhotoView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                imageUri = intent.getData();

                LinearLayout container = findViewById(R.id.container);

                HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
                ScrollView verticalScrollView = new ScrollView(this);
                imageView = new PhotoView(this);

                //Vertical Scroll View
                LinearLayout.LayoutParams vLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                verticalScrollView.setLayoutParams(vLayoutParams);
                verticalScrollView.setFillViewport(true);

                //Horizontal Scroll View
                LinearLayout.LayoutParams hLayoutParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                horizontalScrollView.setLayoutParams(hLayoutParams);
                horizontalScrollView.setFillViewport(true);

                LinearLayout.LayoutParams hImageLayoutParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);

                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(hLayoutParams);
                imageView.setImageURI(imageUri);

                int width = imageView.getDrawable().getIntrinsicWidth();
                int height = imageView.getDrawable().getIntrinsicHeight();
                if (width < height) {
                    container.addView(verticalScrollView, vLayoutParams);
                    verticalScrollView.addView(imageView);
                } else {
                    container.addView(horizontalScrollView, hImageLayoutParams);
                    horizontalScrollView.addView(imageView);
                }


                imageView.setOnApplyWindowInsetsListener((view, windowInsets) -> {
                    imageView.setPadding(
                            0,
                            MainActivity.this.getWindowManager().getDefaultDisplay().getCutout().getSafeInsetTop(),
                            0,
                            windowInsets.getSystemWindowInsetBottom()
                    );
                    return windowInsets.consumeSystemWindowInsets();
                });

            }
        }
    }
}