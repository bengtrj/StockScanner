package com.stockscanner.pivotal.pivotalstockscanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stockscanner.pivotal.pivotalstockscanner.api.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class ProductScannedActivity extends AppCompatActivity {

    private static final Map<String, ProductData> productsDB = new HashMap<String, ProductData>() {{
        put("5390691017889", new ProductData("Protein Bar", "Fulfill", "Cookies & Cream", 3));
        put("5390691017964", new ProductData("Protein Bar", "Fulfill", "Chocolate Caramel & Cookie Dough", 3));
        put("016000502666", new ProductData("Cereal Bar", "Natural Valley", "Crunchy Oats & Honey", 3));
        put("016000502642", new ProductData("Cereal Bar", "Natural Valley", "Crunchy Canadian Maple Syrup", 3));
        put("5391500913552", new ProductData("Rice Cakes", "Bunalun Organic", "Yoghurt & Orange", 3));
        put("5391518520070", new ProductData("Kale Crunchies", "Natasha's", "Lemon & Pepper", 2));
        put("5391518520063", new ProductData("Kale Crunchies", "Natasha's", "Spicy Tomato", 4));
    }};
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;

    private String scannedBarCode;
    private TextView productDescription;
    private TextView productDescriptionSubtitle;
    private TextView productStockLeft;
    private ImageView productImage;
    private View progressBar;
    private final ServiceImpl service = new ServiceImpl();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Returning product...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        productDescription = findViewById(R.id.productDescription);
        productDescriptionSubtitle = findViewById(R.id.productDescriptionSubtitle);
        productStockLeft = findViewById(R.id.productStockLeft);
        productImage = findViewById(R.id.productImage);
        progressBar = findViewById(R.id.progressBar);

        scannedBarCode = getIntent().getStringExtra("barcode");
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ProductData productData = productsDB.get(scannedBarCode);
        if (checkPermissions()) {
            updateStockQuantity(productData);
        }

        if (productData == null) {
            Toast.makeText(this, "Product not found in database!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            productDescription.setText(productData.getBrand() + " " + productData.getType());
            productDescriptionSubtitle.setText(productData.getFlavor());
            productImage.setImageResource(getResources().getIdentifier("img_" + scannedBarCode, "drawable", this.getPackageName()));
        }

    }

    private void updateStockQuantity(final ProductData productData) {
        new Thread() {
            @Override
            public void run() {
                final int stockQuantity = service.getStockQuantity(scannedBarCode);
                productData.setQuantityInStock(stockQuantity);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        productStockLeft.setText(getResources().getQuantityString(R.plurals.stock_left, stockQuantity, stockQuantity));
                    }
                }, 1000);
            }
        }.start();
    }

    private boolean checkPermissions() {
        String internetPermission = Manifest.permission.INTERNET;
        boolean granted = ContextCompat.checkSelfPermission(this, internetPermission) == PackageManager.PERMISSION_GRANTED;

        if (!granted) {
            ActivityCompat.requestPermissions(this, new String[]{internetPermission}, MY_PERMISSIONS_REQUEST_INTERNET);
        }

        return granted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                if (isGranted(grantResults)) {
                    updateStockQuantity(productsDB.get(scannedBarCode));
                } else {
                    checkPermissions();
                }
            }
        }
    }

    private boolean isGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

}
