package com.manarat.manaratlibrary;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GuideActivity extends Activity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private TextView tvStatus, tvMessages;
    private StringBuilder messageLog = new StringBuilder();

    // لتجنب تكرار الرسائل
    private String lastArabicMessage = "";

    private final ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getScanRecord() != null && result.getScanRecord().getManufacturerSpecificData() != null) {
                SparseArray<byte[]> data = result.getScanRecord().getManufacturerSpecificData();
                for (int i = 0; i < data.size(); i++) {
                    byte[] rawBytes = data.valueAt(i);
                    try {
                        String decodedMessage = new String(rawBytes, "UTF-8").trim();
                        decodedMessage = decodedMessage.replaceAll("\\p{C}", ""); // إزالة المحارف غير المطبوعة

                        if (decodedMessage.equals(lastArabicMessage)) {
                            return; // تجاهل الرسالة المكررة
                        }
                        lastArabicMessage = decodedMessage;

                        appendMessage("📡 رسالة: " + decodedMessage);
                    } catch (Exception e) {
                        appendMessage("❌ خطأ في فك الترميز");
                    }
                }
            } else {
                String deviceInfo = result.getDevice().getName() + " - " + result.getDevice().getAddress();
                appendMessage("📡 جهاز مكتشف: " + deviceInfo);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // ربط العناصر
        tvStatus = findViewById(R.id.tv_status);
        tvMessages = findViewById(R.id.tv_messages);
        Button btnStartScan = findViewById(R.id.btn_start_scan);
        Button btnStopScan = findViewById(R.id.btn_stop_scan);
        Button btnCopy = findViewById(R.id.btn_copy);
        Button btnClear = findViewById(R.id.btn_clear);
        Button btnShare = findViewById(R.id.btn_share);

        // تهيئة البلوتوث
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "الجهاز لا يدعم البلوتوث", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        // بدء الالتقاط
        btnStartScan.setOnClickListener(v -> {
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "يرجى تفعيل البلوتوث", Toast.LENGTH_SHORT).show();
                return;
            }
            bluetoothLeScanner.startScan(leScanCallback);
            tvStatus.setText("الحالة: جارٍ الالتقاط...");
            appendMessage("✅ بدأ الالتقاط...");
        });

        // إيقاف الالتقاط
        btnStopScan.setOnClickListener(v -> {
            bluetoothLeScanner.stopScan(leScanCallback);
            tvStatus.setText("الحالة: تم الإيقاف");
            appendMessage("⛔ تم إيقاف الالتقاط.");
        });

        // نسخ الرسائل
        btnCopy.setOnClickListener(v -> {
            String text = tvMessages.getText().toString();
            if (text.isEmpty()) return;

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("BLE Messages", text);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "تم النسخ", Toast.LENGTH_SHORT).show();
        });

        // مسح الرسائل
        btnClear.setOnClickListener(v -> {
            messageLog.setLength(0);
            lastArabicMessage = ""; // إعادة التهيئة
            tvMessages.setText("لا توجد رسائل بعد");
        });

        // مشاركة الرسائل
        btnShare.setOnClickListener(v -> {
            String text = tvMessages.getText().toString();
            if (text.isEmpty()) return;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(shareIntent, "مشاركة الرسائل عبر..."));
        });
    }

    private void appendMessage(String message) {
        if (messageLog.length() == 0) {
            messageLog.append("📢 حدث جديد:\n");
        } else {
            messageLog.append("\n📢 حدث جديد:\n");
        }
        messageLog.append(message);
        tvMessages.setText(messageLog.toString());
    }
}
