package com.codenia.photoeditorsample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * A lightweight ImageView that supports:
 * - Fit-to-view initial placement (centered)
 * - Pinch-to-zoom with min/max scale clamping
 * - Drag/pan with bounds clamping
 * - Double-tap toggle between "fit" and "1:1 (original pixel size)".
 * <p>
 * Notes:
 * - "Original size" means a matrix scale of 1.0 (image pixels == screen pixels).
 * - If the image is smaller than the view and gets upscaled by fit, minScale is clamped to <= 1.0
 *   so that 1:1 is always reachable via double-tap.
 */
public class ZoomImageView extends AppCompatImageView {

    // Matrix used to transform (scale/translate) the image content.
    private final Matrix imageMatrixInternal = new Matrix();

    // Reusable array for reading matrix values.
    private final float[] matrixValues = new float[9];

    // Pinch zoom detector.
    private final ScaleGestureDetector scaleGestureDetector;

    // Scale constraints (matrix scale).
    private float minScale = 1f;
    private float maxScale = 4f;

    // Drag state.
    private float lastTouchX;
    private float lastTouchY;
    private boolean isDragging = false;

    // Double-tap detection (minimal, no GestureDetector to keep it compact).
    private long lastTapTimeMs = 0L;
    private float lastTapX;
    private float lastTapY;
    private static final long DOUBLE_TAP_TIMEOUT_MS = 260L;
    private static final float DOUBLE_TAP_SLOP_PX = 32f;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // We fully control the image transform via matrix.
        setScaleType(ScaleType.MATRIX);

        scaleGestureDetector = new ScaleGestureDetector(context,
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {

                    @Override
                    public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
                        // When scaling begins, stop dragging to avoid conflicts.
                        isDragging = false;
                        return true;
                    }

                    @Override
                    public boolean onScale(@NonNull ScaleGestureDetector detector) {
                        if (getDrawable() == null) return false;

                        final float currentScale = getCurrentScale();
                        float scaleFactor = detector.getScaleFactor();
                        float targetScale = currentScale * scaleFactor;

                        // Clamp scale to [minScale, maxScale].
                        if (targetScale < minScale) scaleFactor = minScale / currentScale;
                        if (targetScale > maxScale) scaleFactor = maxScale / currentScale;

                        // Scale around the gesture focal point.
                        imageMatrixInternal.postScale(scaleFactor, scaleFactor,
                                detector.getFocusX(), detector.getFocusY());

                        // Keep image inside view bounds.
                        clampToBounds();

                        setImageMatrix(imageMatrixInternal);
                        return true;
                    }
                });
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        fitImageToView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        fitImageToView();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the scale detector inspect all events.
        scaleGestureDetector.onTouchEvent(event);

        if (getDrawable() == null) return true;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                isDragging = true;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Drag only when we are not currently pinching.
                if (isDragging && !scaleGestureDetector.isInProgress()) {
                    float dx = event.getX() - lastTouchX;
                    float dy = event.getY() - lastTouchY;

                    imageMatrixInternal.postTranslate(dx, dy);
                    clampToBounds();
                    setImageMatrix(imageMatrixInternal);

                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                // Handle double-tap only if this wasn't part of a pinch gesture.
                if (!scaleGestureDetector.isInProgress()) {
                    handleDoubleTap(event);
                }
                isDragging = false;

                // Final bounds clamp.
                clampToBounds();
                setImageMatrix(imageMatrixInternal);
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                isDragging = false;
                clampToBounds();
                setImageMatrix(imageMatrixInternal);
                break;
            }
        }

        return true;
    }

    /**
     * Performs a double-tap toggle:
     * - If currently at (approximately) minScale (fit), zoom to 1:1 (matrix scale 1.0) clamped to maxScale.
     * - Otherwise, zoom back to minScale (fit).
     */
    private void handleDoubleTap(MotionEvent event) {
        final long nowMs = event.getEventTime();
        final float x = event.getX();
        final float y = event.getY();

        final boolean isCloseToLastTap =
                squaredDistance(x, y, lastTapX, lastTapY) <= (DOUBLE_TAP_SLOP_PX * DOUBLE_TAP_SLOP_PX);

        if (nowMs - lastTapTimeMs <= DOUBLE_TAP_TIMEOUT_MS && isCloseToLastTap) {
            final float currentScale = getCurrentScale();

            // 1:1 pixel scale in matrix coordinates.
            final float oneToOneScale = Math.min(maxScale, 1f);

            // Toggle target scale.
            final float targetScale =
                    (Math.abs(currentScale - minScale) < 0.001f) ? oneToOneScale : minScale;

            // Apply scale around the tap position.
            final float scaleFactor = targetScale / currentScale;
            imageMatrixInternal.postScale(scaleFactor, scaleFactor, x, y);

            clampToBounds();
            setImageMatrix(imageMatrixInternal);

            // Reset tap state so triple-taps don't retrigger immediately.
            lastTapTimeMs = 0L;
        } else {
            lastTapTimeMs = nowMs;
            lastTapX = x;
            lastTapY = y;
        }
    }

    /**
     * Fits the image into the view while preserving aspect ratio and centering it.
     * Also defines minScale as:
     * - min(fitScale, 1.0) so that 1:1 is always reachable even if the fit scale would upscale a small image.
     */
    private void fitImageToView() {
        final Drawable drawable = getDrawable();
        if (drawable == null || getWidth() == 0 || getHeight() == 0) return;

        final float viewWidth = getWidth();
        final float viewHeight = getHeight();

        final float drawableWidth = drawable.getIntrinsicWidth();
        final float drawableHeight = drawable.getIntrinsicHeight();
        if (drawableWidth <= 0 || drawableHeight <= 0) return;

        imageMatrixInternal.reset();

        final float fitScale = Math.min(viewWidth / drawableWidth, viewHeight / drawableHeight);

        final float translateX = (viewWidth - drawableWidth * fitScale) * 0.5f;
        final float translateY = (viewHeight - drawableHeight * fitScale) * 0.5f;

        imageMatrixInternal.postScale(fitScale, fitScale);
        imageMatrixInternal.postTranslate(translateX, translateY);

        // "Fit" should be the minimum, but never above 1:1 to allow original size.
        minScale = Math.min(fitScale, 1f);

        // Ensure maxScale is not below minScale.
        if (maxScale < minScale) maxScale = minScale;

        setImageMatrix(imageMatrixInternal);
    }

    /**
     * @return Current matrix scale (X axis). For uniform scaling, X==Y.
     */
    private float getCurrentScale() {
        imageMatrixInternal.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    /**
     * Computes the current displayed rectangle of the drawable after applying the matrix.
     */
    private RectF getDisplayedDrawableRect() {
        final Drawable drawable = getDrawable();
        final RectF rect = new RectF(0, 0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        imageMatrixInternal.mapRect(rect);
        return rect;
    }

    /**
     * Clamps translation so the image stays within the view bounds.
     * - If the image is smaller than the view, it is centered.
     * - If larger, it is constrained so no empty gaps appear.
     */
    private void clampToBounds() {
        final RectF rect = getDisplayedDrawableRect();
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();

        float dx = 0f;
        float dy = 0f;

        // Horizontal clamp / center.
        if (rect.width() <= viewWidth) {
            dx = (viewWidth - rect.width()) * 0.5f - rect.left;
        } else {
            if (rect.left > 0f) dx = -rect.left;
            if (rect.right < viewWidth) dx = viewWidth - rect.right;
        }

        // Vertical clamp / center.
        if (rect.height() <= viewHeight) {
            dy = (viewHeight - rect.height()) * 0.5f - rect.top;
        } else {
            if (rect.top > 0f) dy = -rect.top;
            if (rect.bottom < viewHeight) dy = viewHeight - rect.bottom;
        }

        imageMatrixInternal.postTranslate(dx, dy);
    }

    private static float squaredDistance(float x1, float y1, float x2, float y2) {
        final float dx = x1 - x2;
        final float dy = y1 - y2;
        return dx * dx + dy * dy;
    }
}
