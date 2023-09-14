package com.example.pdfgeneratorconcept;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class ScrollViewtoPdf {



    public static void generatePdf(Context context, ScrollView scrollView) {
        // Create a PDF document
        PdfDocument pdfDocument = new PdfDocument();



        // Get the scroll view's dimensions
        int scrollViewWidth = scrollView.getWidth();
        int scrollViewHeight = scrollView.getChildAt(0).getHeight();



        // Calculate the number of pages based on the scroll view's content height
        int pageHeight = scrollViewHeight; // A4 page height in points
        int pageCount = (int) Math.ceil((float) scrollViewHeight / pageHeight);



        // Create a page info with the desired page dimensions
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(scrollViewWidth, pageHeight, 1).create();



        for (int i = 0; i < pageCount; i++) {
            // Create a new page
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);



            // Draw the scroll view content onto the page's canvas
            Canvas canvas = page.getCanvas();
            int pageTop = -pageHeight * i;
            canvas.translate(0, pageTop);
            scrollView.draw(canvas);



            // Finish the page
            pdfDocument.finishPage(page);
        }



        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "pdfgeneratorSample" + Calendar.getInstance().getTimeInMillis() + ".pdf";
        // Create a file to save the PDF
        File pdfFile = new File(filePath);



        try {
            // Create an output stream for the PDF file
            FileOutputStream outputStream = new FileOutputStream(pdfFile);



            // Write the PDF document to the output stream
            pdfDocument.writeTo(outputStream);



            // Close the output stream and the PDF document
            outputStream.close();
            pdfDocument.close();



            // Show a toast indicating the PDF file path
            Toast.makeText(context, "PDF saved to " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
