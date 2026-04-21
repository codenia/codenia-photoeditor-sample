package com.codenia.photoeditorsample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Register callbacks on all Android versions (AndroidX insets works with minSdk 26).
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState)
            {
                // Only enable EdgeToEdge if this Activity is a ComponentActivity (avoids ClassCastException).
                if (activity instanceof ComponentActivity)
                {
                    EdgeToEdge.enable((ComponentActivity) activity);
                }

                applyEdgeToEdge(activity);
            }

            @Override public void onActivityStarted(@NonNull Activity activity) {}
            @Override public void onActivityResumed(@NonNull Activity activity) {}
            @Override public void onActivityPaused(@NonNull Activity activity) {}
            @Override public void onActivityStopped(@NonNull Activity activity) {}
            @Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}
            @Override public void onActivityDestroyed(@NonNull Activity activity) {}
        });
    }

    private void applyEdgeToEdge(Activity activity)
    {
        try
        {
            Window window = activity.getWindow();

            if (window != null)
            {
                // Enable true edge-to-edge: content can draw behind system bars.
                WindowCompat.setDecorFitsSystemWindows(window, false);

                View decorView = window.getDecorView();
                View contentView = decorView.findViewById(android.R.id.content);

                if (contentView != null)
                {
                    // Prefer the actual layout root (child 0 of android.R.id.content) if available.
                    View rootView = contentView;
                    if (contentView instanceof android.view.ViewGroup)
                    {
                        android.view.ViewGroup vg = (android.view.ViewGroup) contentView;
                        if (vg.getChildCount() > 0 && vg.getChildAt(0) != null)
                        {
                            rootView = vg.getChildAt(0);
                        }
                    }

                    // Preserve any padding already defined in XML (so we don't break layouts).
                    final int baseLeft = rootView.getPaddingLeft();
                    final int baseTop = rootView.getPaddingTop();
                    final int baseRight = rootView.getPaddingRight();
                    final int baseBottom = rootView.getPaddingBottom();

                    ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) ->
                    {
                        // Insets for system bars (status + navigation) and display cutout (notch).
                        Insets bars = insets.getInsets(
                                WindowInsetsCompat.Type.systemBars()
                                        | WindowInsetsCompat.Type.displayCutout()
                        );

                        // IMPORTANT: Ignore IME insets so the keyboard overlays the UI
                        // instead of resizing/adding bottom padding to the content.
                        int bottom = bars.bottom;

                        // Apply insets as additional padding while keeping existing XML padding.
                        v.setPadding(
                                baseLeft + bars.left,
                                baseTop + bars.top,
                                baseRight + bars.right,
                                baseBottom + bottom
                        );

                        // Do NOT consume insets, otherwise child views may not receive them.
                        return insets;
                    });

                    // Request insets so they're applied immediately.
                    ViewCompat.requestApplyInsets(rootView);
                }
            }
        }
        catch (Exception ex)
        {
            // Log or handle error if needed
        }
    }
}
