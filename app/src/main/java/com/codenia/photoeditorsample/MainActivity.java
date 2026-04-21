package com.codenia.photoeditorsample;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.codenia.photoeditor.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sample activity that demonstrates how to integrate {@link PhotoEditorView}.
 * <p>
 * Integration notes: Add the AAR and required AndroidX dependencies in app/build.gradle.
 * </p>
 *
 * <p>This sample shows how to:</p>
 * <ul>
 *   <li>Pick an image from the gallery</li>
 *   <li>Decode it off the UI thread</li>
 *   <li>Edit it using {@link PhotoEditorView}</li>
 *   <li>Save the result via the Storage Access Framework</li>
 * </ul>
 */
@SuppressWarnings({"unused", "CommentedOutCode"})
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PhotoEditorSample";
    private static final int MAX_PREVIEW_DIM_PX = 3000;
    private RelativeLayout rootLayout;
    private LinearLayout controlsLayout;
    private ZoomImageView imageView;
    private Button btnSaveImage;
    private PhotoEditorView photoEditorView;

    /**
     * Adds a new {@link PhotoEditorView} to the UI and configures it with sample options.
     *
     * @param bitmap Source image to edit.
     */
    private void showPhotoEditorView(@NonNull Bitmap bitmap) {
        try {

            // Create a new PhotoEditorView instance.
            photoEditorView = new PhotoEditorView(this);

            // Set the source image to be edited.
            // The editor creates its own internal copy and does not modify or recycle
            // the original bitmap. The caller remains responsible for managing it.
            photoEditorView.setImageBitmap(bitmap);

            // A valid license key unlocks the full version of the Software and removes the watermark from exported images.
            // The license key is generated based on the application ID (bundle ID) and must only be used with the licensed application.
            // https://www.codenia-photoeditor.com/purchase
            //
            // photoEditorView.setLicenseKey("YOUR_LICENSE_KEY");

            /*
             * Optional:
             * Configure the filters shown in the editor UI, including which filters
             * are enabled and how they are labeled.
             */
            /*
            FilterSettings filterSettings = new FilterSettings(
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("Filter 1", true),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("Filter 2", true),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("Filter 3", true),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false),
                    new FilterSetting("", false)
            );

            photoEditorView.setFilterSettings(filterSettings);
            */

            /*
             * Optional:
             * Disables the filter intensity slider in the editor UI.
             */
            // photoEditorView.setFilterIntensityAdjustable(false);

            /*
             * Optional:
             * Configure the visual style of filter thumbnails in the filter selection UI.
             *
             * If not set, the default style is SQUARE_WITH_CAPTION_STYLE2.
             */
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.ROUNDED_NO_CAPTION_NO_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.ROUNDED_NO_CAPTION_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.CIRCLE_NO_CAPTION_NO_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.CIRCLE_NO_CAPTION_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.SQUARE_NO_CAPTION_NO_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.SQUARE_NO_CAPTION_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.ROUNDED_WITH_CAPTION_NO_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.ROUNDED_WITH_CAPTION_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.CIRCLE_WITH_CAPTION_NO_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.CIRCLE_WITH_CAPTION_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.SQUARE_WITH_CAPTION_STYLE1_NO_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.SQUARE_WITH_CAPTION_STYLE1_GAP);
            // photoEditorView.setFilterThumbnailStyle(PhotoEditorView.FilterThumbnailStyle.SQUARE_WITH_CAPTION_STYLE2);

            /*
             * Optional:
             * Specify which editing tools are available in the editor UI.
             *
             * If not set, the Photo Editor shows all available tools.
             */
            /*
            photoEditorView.setTools(
                PhotoEditorView.Tool.CROP,
                PhotoEditorView.Tool.ROTATE,
                PhotoEditorView.Tool.FILTER,
                PhotoEditorView.Tool.TEXT,
                PhotoEditorView.Tool.STICKER
            );
            */

            /*
             * Optional:
             * Provide a custom set of sticker resources to be shown in the sticker tool.
             *
             * If not set, the Photo Editor uses the three example stickers.
             */
            /*
            photoEditorView.setStickerResourceIds(
                new int[]{
                    com.codenia.photoeditor.R.drawable.photo_editor_sticker_3,
                    com.codenia.photoeditor.R.drawable.photo_editor_sticker_2,
                    com.codenia.photoeditor.R.drawable.photo_editor_sticker_1
                }
            );
            */

            /*
             * Optional:
             * Configure the aspect ratio options available in the crop tool.
             * If not set, the Photo Editor uses the default aspect ratio options.
             */
            /*
            photoEditorView.setCropAspectRatioOptions(Arrays.asList(
                AspectRatioOption.free(),
                AspectRatioOption.original(),
                AspectRatioOption.fixed(1, 1, "1 : 1")
            ));
            */

            /*
             * Optional:
             * Provide a custom list of fonts for the text tool, including
             * display names and corresponding font resources.
             *
             * If not set, the Photo Editor uses the default font set.
             */
            /*
            int[] NAME_RES = new int[]{
                com.codenia.photoeditor.R.string.photo_editor_font_20_kode_mono,
                com.codenia.photoeditor.R.string.photo_editor_font_15_sour_gummy,
                com.codenia.photoeditor.R.string.photo_editor_font_12_petrona
            };

            int[] FONT_RES = new int[]{
                com.codenia.photoeditor.R.font.photo_editor_font_20_kode_mono_variable_font_wght,
                com.codenia.photoeditor.R.font.photo_editor_font_15_sour_gummy_variable_font_wdth_wght,
                com.codenia.photoeditor.R.font.photo_editor_font_12_petrona_variable_font_wght
            };

            photoEditorView.setAvailableFonts(NAME_RES, FONT_RES);
            */

            /*
             * Optional:
             * Disable the exit confirmation dialog shown when leaving the editor.
             */
            // photoEditorView.setShowExitConfirmationMessage(false);

            /*
             * Register a listener to receive editor actions.
             *
             * - onDone: called when editing is completed and the final bitmap is available
             * - onCancel: called when the editor is closed without applying changes
             * - onError: called when an error occurs during editing
             */
            photoEditorView.setOnEditorActionListener(new PhotoEditorView.OnEditorActionListener() {
                @Override
                public void onDone(Bitmap resultBitmap) {
                    // Use a downscaled preview for the ImageView to reduce memory usage.
                    imageView.setImageBitmap(scaleToMaxDim(resultBitmap, MAX_PREVIEW_DIM_PX));
                    removePhotoEditorView();

                    // Keep a reference for saving.
                    // If the editor reuses/recycles this bitmap internally,
                    // store a copy instead.
                    editedBitmap = resultBitmap;

                    btnSaveImage.setVisibility(VISIBLE);
                }

                @Override
                public void onCancel() {
                    removePhotoEditorView();
                }

                @Override
                public void onError(@NonNull String message) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    removePhotoEditorView();
                }
            });

            // Add the PhotoEditorView to the root layout.
            rootLayout.addView(photoEditorView);

        } catch (IllegalArgumentException e) {
            // Invalid usage of the API
            removePhotoEditorView();
            Log.e(TAG, "Failed to initialize PhotoEditorView", e);
        }
    }

    /**
     * Removes the {@link PhotoEditorView} from the root layout and resets UI state.
     */
    private void removePhotoEditorView() {
        if (photoEditorView == null) return;

        photoEditorView.setOnEditorActionListener(null);

        if (photoEditorView.getParent() instanceof ViewGroup) {
            ((ViewGroup) photoEditorView.getParent()).removeView(photoEditorView);
        }

        photoEditorView = null;
        controlsLayout.setVisibility(VISIBLE);
    }

    private void handleOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (photoEditorView != null) {
                    if (photoEditorView.handleBackPressed()) {
                        return;
                    }
                }

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("")
                        .setMessage(R.string.photo_editor_exit_confirmation_message)
                        .setPositiveButton(R.string.photo_editor_yes_button, (dialog, which) ->
                        {
                            setEnabled(false);
                            getOnBackPressedDispatcher().onBackPressed();
                        })
                        .setNegativeButton(R.string.photo_editor_no_button, (dialog, which) ->
                        {
                            //
                        })
                        .show();
            }
        });
    }

    /**
     * Holds the last edited bitmap (for sample purposes).
     */
    @Nullable
    private Bitmap editedBitmap;

    boolean sourceImageIsPNG = false;

    private final ExecutorService decodeExecutor = Executors.newSingleThreadExecutor();

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private final ActivityResultLauncher<Intent> saveImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() != RESULT_OK || result.getData() == null) return;

                        Uri uri = result.getData().getData();
                        if (uri == null) return;

                        if (editedBitmap == null || editedBitmap.isRecycled()) {
                            Toast.makeText(this, "No image to save", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        writeBitmapToUri(uri, editedBitmap);
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);
        controlsLayout = findViewById(R.id.controlsLayout);
        imageView = findViewById(R.id.imageView);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveImage = findViewById(R.id.btnSaveImage);
        btnSaveImage.setVisibility(INVISIBLE);

        setupPickImageLauncher();

        btnSelectImage.setOnClickListener(v -> openGallery());
        btnSaveImage.setOnClickListener(v -> saveImage());

        handleOnBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop background decoding.
        decodeExecutor.shutdownNow();
        editedBitmap = null;
    }

    /**
     * Registers the gallery picker using the Activity Result API.
     */
    private void setupPickImageLauncher() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                        controlsLayout.setVisibility(VISIBLE);
                        return;
                    }

                    Uri uri = result.getData().getData();
                    if (uri == null) return;

                    sourceImageIsPNG = isPng(uri);

                    decodeExecutor.execute(() -> {
                        Bitmap decoded = decodeFullResBitmap(uri);

                        runOnUiThread(() -> {
                            if (decoded != null) {
                                showPhotoEditorView(decoded);
                            } else {
                                Toast.makeText(this, "Failed to decode image", Toast.LENGTH_SHORT).show();
                                controlsLayout.setVisibility(VISIBLE);
                            }
                        });
                    });
                }
        );
    }

    private boolean isPng(Uri uri) {
        if (uri == null) return false;

        ContentResolver r = this.getContentResolver();

        // check MIME type first
        String type = r.getType(uri);
        if (type != null) return "image/png".equalsIgnoreCase(type);

        // fallback: check file extension
        String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (ext != null && !ext.isEmpty())
            return "png".equalsIgnoreCase(ext);

        // fallback for content:// URIs (get display name)
        try (Cursor c = r.query(uri, null, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                int i = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (i != -1) {
                    String name = c.getString(i);
                    int dot = name.lastIndexOf('.');
                    return dot != -1 && name.substring(dot + 1).equalsIgnoreCase("png");
                }
            }
        }

        return false;
    }

    /**
     * Opens the system gallery picker.
     */
    private void openGallery() {
        imageView.setImageBitmap(null);
        btnSaveImage.setVisibility(INVISIBLE);
        controlsLayout.setVisibility(INVISIBLE);
        editedBitmap = null;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    /**
     * Decodes the selected image at full resolution.
     *
     * <p>Note: Full-res decoding can be memory intensive. For production apps, consider sampling
     * down to a target size if the original is very large.</p>
     *
     * @param uri Image URI chosen by the user.
     * @return Decoded {@link Bitmap}, or {@code null} on failure.
     */
    @Nullable
    private Bitmap decodeFullResBitmap(@NonNull Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // API 28+
                ImageDecoder.Source src = ImageDecoder.createSource(getContentResolver(), uri);
                return ImageDecoder.decodeBitmap(
                        src,
                        (decoder, info, s) -> decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE)
                );
            } else {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try (InputStream is = getContentResolver().openInputStream(uri)) {
                    return BitmapFactory.decodeStream(is, null, o);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "decodeFullResBitmap failed", e);
            return null;
        }
    }

    /**
     * Launches the system "Create document" UI so the user can choose a destination and filename.
     * The selected document URI is then used to write {@link #editedBitmap}.
     */
    private void saveImage() {
        if (editedBitmap == null || editedBitmap.isRecycled()) {
            Toast.makeText(this, "No image to save", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (sourceImageIsPNG) {
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_TITLE, "edited_image.png");
        } else {
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_TITLE, "edited_image.jpg");
        }

        saveImageLauncher.launch(intent);
    }

    /**
     * Writes a bitmap to the given URI using the {@link android.content.ContentResolver}.
     *
     * @param uri    Destination URI chosen by the user.
     * @param bitmap Bitmap to write.
     */
    private void writeBitmapToUri(@NonNull Uri uri, @NonNull Bitmap bitmap) {
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out == null) throw new IOException("Failed to open output stream");

            if (sourceImageIsPNG) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }

            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Error saving image", e);
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Scales {@code src} so that {@code max(width, height) <= maxDim}, keeping the aspect ratio.
     * Only downsizes; returns {@code src} unchanged if it is already small enough.
     *
     * @param src    Source bitmap.
     * @param maxDim Maximum dimension in pixels.
     * @return A scaled bitmap or the original bitmap if no scaling is needed.
     */
    @SuppressWarnings("SameParameterValue")
    @Nullable
    private static Bitmap scaleToMaxDim(@Nullable Bitmap src, int maxDim) {
        if (src == null || maxDim <= 0) return src;

        int w = src.getWidth();
        int h = src.getHeight();
        int longest = Math.max(w, h);

        if (longest <= maxDim) return src;

        double scale = maxDim / (double) longest;
        int nw = Math.max(1, (int) Math.round(w * scale));
        int nh = Math.max(1, (int) Math.round(h * scale));

        return Bitmap.createScaledBitmap(src, nw, nh, true);
    }
}