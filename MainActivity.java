package com.example.invoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button PrintInvoice;
    Button paybtn;
    List<String> List1,List2,List3;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    SimpleDateFormat datePatternformat = new SimpleDateFormat("dd-MM-yy hh:mm a");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List1=new ArrayList<>();
        List2=new ArrayList<>();
        List3=new ArrayList<>();
        PrintInvoice = findViewById(R.id.button);

        PrintInvoice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                DatabaseReference reference1 = FirebaseDatabase.getInstance("https://my-application-01-6c9de-default-rtdb.firebaseio.com/").getReference("cart");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            List1.clear();
                            List2.clear();
                            List3.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String Name = snapshot1.child("name").getValue().toString();
                                String Quantity = snapshot1.child("qty").getValue().toString();
                                String TotalPrice  = snapshot1.child("price").getValue().toString();
                                List1.add(Name);
                                List2.add(Quantity);
                                List3.add(TotalPrice);
                                PdfDocument document = new PdfDocument();
                                Paint paint = new Paint();
                                Paint forLine = new Paint();
                                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
                                PdfDocument.Page myPage = document.startPage(myPageInfo);
                                Canvas canvas = myPage.getCanvas();
                                int y1=135;
                                int y2=135;
                                int y3=135;
                                int TotalAmount=0;
                                paint.setTextSize(15.5f);
                                paint.setColor(Color.rgb(0, 50, 250));
                                canvas.drawText("Pharma Medical & Distributors", 20, 20, paint);
                                paint.setTextSize(13.5f);
                                canvas.drawText("INVOICE", 20, 40, paint);
                                forLine.setStyle(Paint.Style.STROKE);
                                forLine.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
                                forLine.setStrokeWidth(2);
                                canvas.drawLine(20, 65, 230, 65, forLine);
                                canvas.drawText("Customer Name: "+"Rihan", 20, 80, paint);
                                canvas.drawLine(20, 90, 230, 90, forLine);
                                canvas.drawText("Items Purchased:", 20, 105, paint);
                                canvas.drawText("Item", 20, 135, paint);
                                canvas.drawText("Quantity", 90, 135, paint);
                                paint.setTextAlign(Paint.Align.RIGHT);
                                canvas.drawText("Amount in(₹)", 230, 135, paint);
                                paint.setTextAlign(Paint.Align.LEFT);
//                              For Displaying Name
                                for(int i=0;i<List1.size();i++)
                                {
                                    y1=y1+20;
                                    String Element=List1.get(i);
                                    canvas.drawText(""+Element,20,y1,paint);

                                }
//                              For Displaying Quantity
                                for(int i=0;i<List2.size();i++)
                                {
                                    y2=y2+20;
                                    String quantity=List2.get(i);
                                    canvas.drawText(""+quantity,120,y2,paint);

                                }
                                paint.setTextAlign(Paint.Align.RIGHT);
//                               For Displaying Amount
                                for(int i=0;i<List3.size();i++)
                                {
                                    y3=y3+20;
                                    String amount=List3.get(i);
                                    canvas.drawText(""+amount,230,y3,paint);
                                    int amount1=Integer.parseInt(amount);
                                    int temp=amount1;
                                    TotalAmount+=temp;
                                }
                                canvas.drawLine(20, y3+20, 230, y3+20, forLine);
                                paint.setTextSize(12f);
                                paint.setTextAlign(Paint.Align.RIGHT);
                                canvas.drawText("Total: "+TotalAmount, 230, y3+40, paint);
                                paint.setTextAlign(Paint.Align.RIGHT);
                                paint.setTextAlign(Paint.Align.LEFT);
                                paint.setTextSize(8.5f);
                                canvas.drawText("Date:" + datePatternformat.format(new Date().getTime()), 20, y3+38, paint);
                                paint.setTextAlign(Paint.Align.CENTER);
                                paint.setTextSize(12f);
                                canvas.drawText("Thank You!!", canvas.getWidth() / 2, y3+70, paint);
                                document.finishPage(myPage);
                                File file = new File(MainActivity.this.getExternalFilesDir("/"), "Pharma Medical & Distributors File.pdf");
                                try
                                {

                                    document.writeTo(new FileOutputStream(file));
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                document.close();
                                Toast.makeText(MainActivity.this, "Printed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,PdfActivity.class).putExtra("path",file.getAbsolutePath()));
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

}



