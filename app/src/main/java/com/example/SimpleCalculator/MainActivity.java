package com.example.SimpleCalculator;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final String WEBSITE_URL = "https://jivdayaabhiyan.org/app-2-calc"; // Replace with your website URL
    private static final String LOCAL_FILE = "file:///android_asset/your_local_file.html"; // Replace with your local HTML file path
    private static final String TAG = "MainActivity";

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 101;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the WebView
        webView = findViewById(R.id.webView);

        // Enable JavaScript for the entire WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Initialize WebViewClient to handle page navigation
        webView.setWebViewClient(new WebViewClient());

        // Set up Geolocation permissions for the WebView
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, request.getOrigin().toString());
                        if (request.getOrigin().toString().equals(WEBSITE_URL)) {
                            // Check if the permission being requested is CAMERA
                            if (request.getResources()[0].contains("android.permission.CAMERA")) {
                                // Request camera permission
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        CAMERA_PERMISSION_REQUEST_CODE);
                            }
                            // Check if the permission being requested is RECORD_AUDIO
                            else if (request.getResources()[0].contains("android.permission.RECORD_AUDIO")) {
                                // Request microphone permission
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.RECORD_AUDIO},
                                        RECORD_AUDIO_PERMISSION_REQUEST_CODE);
                            }
                            // Check if the permission being requested is ACCESS_FINE_LOCATION
                            else if (request.getResources()[0].contains("android.permission.ACCESS_FINE_LOCATION")) {
                                // Request location permission
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        }
                    }
                });
            }
        });

        // Load the website URL or local file, depending on your needs
        webView.loadUrl(WEBSITE_URL); // Use this line for loading a website

        // Request permissions for CAMERA, RECORD_AUDIO, and LOCATION at the app level
        checkAndRequestPermissions();
    }

    // Enable JavaScript for the WebView
    private void enableJavaScript() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    // Check and request permissions for CAMERA, RECORD_AUDIO, and LOCATION at the app level
    private void checkAndRequestPermissions() {
        // Check and request CAMERA permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }

        // Check and request RECORD_AUDIO permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_PERMISSION_REQUEST_CODE);
        }

        // Check and request LOCATION permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Override onBackPressed to handle WebView navigation
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Navigate back in WebView history if possible
        } else {
            super.onBackPressed(); // Exit the app if there's no history to navigate
        }
    }

    // Override onRequestPermissionsResult to handle permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE || requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE || requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can use the requested feature
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable the feature)
            }
        }
    }
}
