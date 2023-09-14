package com.example.pdfgeneratorconcept;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ScrollView scrollView;
    EditText authIntimationNo,authUHIDNo,authDateOfAdmission,authDateOfDischarge,authPolicyNumber,authPatientName,authAge;
    LinearLayout saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView=findViewById(R.id.scrollView);
        saveButton=findViewById(R.id.saveButton);
        authIntimationNo = findViewById(R.id.authIntimationNo);
        authUHIDNo = findViewById(R.id.authUHIDNo);
        authDateOfAdmission = findViewById(R.id.authDateOfAdmission);
        authDateOfDischarge = findViewById(R.id.authDateOfDischarge);
        authPolicyNumber = findViewById(R.id.authPolicyNumber);
        authPatientName = findViewById(R.id.authPatientName);
        authAge = findViewById(R.id.authAge);

        // Set example text for authIntimationNo
        authIntimationNo.setText("INT123456");
        // Set example text for authUHIDNo
        authUHIDNo.setText("UHID789012");
        // Set example text for authDateOfAdmission
        authDateOfAdmission.setText("2023-09-10");
        // Set example text for authDateOfDischarge
        authDateOfDischarge.setText("2023-09-15");
        // Set example text for authPolicyNumber
        authPolicyNumber.setText("POL123456");
        // Set example text for authPatientName
        authPatientName.setText("Saranya");
        // Set example text for authAge
        authAge.setText("35");


        saveButton.setOnClickListener(l->{
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }else {
                ScrollViewtoPdf.generatePdf(this,scrollView);
            }
        });
    }


    private void createAndSavePDF() {
        // Create a PdfDocument instance
        PdfDocument pdfDocument = new PdfDocument();

        // Initialize a page number
        int pageNumber = 1;

        // Calculate the total height of the ScrollView's content
        int totalContentHeight = scrollView.getChildAt(0).getHeight();

        // Get the height of the ScrollView
        int scrollViewHeight = scrollView.getHeight();

        // Loop through the content and generate pages
        while (true) {
            // Define the PageInfo for the current page
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                    scrollView.getWidth(), scrollViewHeight, pageNumber).create();

            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            // Capture the content of the ScrollView and draw it on the canvas
            Bitmap bitmap = Bitmap.createBitmap(
                    scrollView.getWidth(), scrollViewHeight, Bitmap.Config.ARGB_8888);
            Canvas contentCanvas = new Canvas(bitmap);

            // Scroll the ScrollView to the correct position for this page
            int scrollY = scrollViewHeight * (pageNumber - 1);
            scrollView.scrollTo(0, scrollY);

            // Capture the content of the ScrollView
            scrollView.draw(contentCanvas);
            canvas.drawBitmap(bitmap, 0, 0, null);

            pdfDocument.finishPage(page);

            // Increment the page number
            pageNumber++;

            // Calculate the next scroll position
            int nextScrollY = scrollViewHeight * pageNumber;
            System.out.println("Value at nextScroll: "+nextScrollY);
            // Scroll to the next position
            if (nextScrollY < totalContentHeight) {
                scrollView.scrollTo(0, nextScrollY);
            } else {
                break; // Exit the loop if there is no more content
            }
        }

        // Get the path for the downloads directory
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "pdfgeneratorSample" + Calendar.getInstance().getTimeInMillis() + ".pdf";

        File pdfFile = new File(filePath);

        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }









}

