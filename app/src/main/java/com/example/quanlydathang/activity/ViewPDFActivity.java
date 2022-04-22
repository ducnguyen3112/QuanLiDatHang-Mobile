package com.example.quanlydathang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.quanlydathang.MainActivity;
import com.example.quanlydathang.R;
import com.example.quanlydathang.utils.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;

import java.io.File;

public class ViewPDFActivity extends AppCompatActivity {
    PDFView viewPdf;
    String path;
    Toolbar toolbar;
    Button sendMail;

    int id;

    public ViewPDFActivity() {
    }

    public ViewPDFActivity(PDFView viewPdf, String path) {
        this.viewPdf = viewPdf;
        this.path = path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdfactivity);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        setControl();
        setActionBar();

        Intent intent = new Intent(ViewPDFActivity.this,PDFDonHangActivity.class);
        path = getIntent().getStringExtra("path");
        id = getIntent().getIntExtra("id",1);
        File file = new File(path);
        viewPdf.fromFile(file)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .enableAnnotationRendering(true)
                .defaultPage(0)
                .scrollHandle(null)
                .password(null)
                .load();

        handleClickSendMail();
    }

    private void setControl() {
        viewPdf = findViewById(R.id.viewPdf);
        sendMail = findViewById(R.id.sendMail);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(ViewPDFActivity.this, PDFDonHangActivity.class);
                main.putExtra("id",id);
                startActivityForResult(main, Constants.RESULT_PDF_ACTIVITY);
            }
        });
    }

     private void handleClickSendMail() {
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(path);
                if(file.exists()){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                    intent.setType("application/pdf");
                    startActivity(intent);
                }
            }
        });
    }
}