package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import android.os.Bundle;

import java.util.Random;

import static android.graphics.Color.HSVToColor;
import static android.graphics.Color.RGBToHSV;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;
import static com.example.td1.R.id.imagetest;
import android . renderscript . Allocation;
//import androidx . support . v8 . renderscript . Allocation ;
import androidx.renderscript.*;


import com.android.rssample.ScriptC_gray;


public class MainActivity extends AppCompatActivity {

    //******************************* MES FONCTIONS  **************************************

    //*************************************************************************************
    //*********     TP1    ****************************************************************
    public void toGray(Bitmap bmp)
    {
        for (int i = 0; i < bmp.getWidth();i++)
        {
            for (int j = 0; j < bmp.getHeight();j++)
            {
                int pixel = bmp.getPixel(i,j);
                int red = red(pixel);
                int green = green(pixel);
                int blue = blue(pixel);
                int grey = (int) Math.round(0.3*red + 0.59*green + 0.11*blue);
                bmp.setPixel(i,j,rgb(grey,grey,grey));


            }
        }
    }

    //**************************************************************************************
    public void toGray2(Bitmap bmp)
    {
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            int grey = (int) Math.round(0.3*red + 0.59*green + 0.11*blue);
            pixels[i] = rgb(grey,grey,grey);
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
    //*************************************************************************************
    //*********     TP2    ****************************************************************

    public void colorize (Bitmap bmp)
    {
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        float[] hsv = new float[3];

        Random r = new Random();
        int h = r.nextInt(360 );

        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            RGBToHSV(red ,green,blue,hsv);
            //TO DO hue change
            hsv[0]=h;
            //*****************
            pixels[i] = HSVToColor(hsv);
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
    //**************************************************************************************
    public void keepRed (Bitmap bmp)
    {
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        float[] hsv = new float[3];


        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            RGBToHSV(red ,green,blue,hsv);
            //TO DO hue change
            if ( !((hsv[0] < 40 )| (hsv[0] > 320 ) ) ){
                hsv[1] = 0;
            }
            pixels[i] = HSVToColor(hsv);
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
    //*************************************************************************************
    //*********     TP3    ****************************************************************

    public void lowContraste (Bitmap bmp)
    {
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            if (red <128 ){red+= 40;  }
            else {red -= 40;}

            if (green <128 ){green+= 40;  }
            else {green -= 40;  }
            if (blue <128 ){blue+= 40;  }
            else{blue -= 40;  }

            pixels[i] = rgb(red,green,blue);

        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
    //**************************************************************************************
    public void contrasteExtensionLineaire (Bitmap bmp)
    {
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        //******************************
        //find min and max of each canal

        //initialisation
        int premierPixel = pixels[0];
        int premierRed = red(premierPixel);
        int premierGreen = green(premierPixel);
        int premierBlue = blue(premierPixel);
        int minRed = premierRed;
        int minGreen = premierGreen;
        int minBlue = premierBlue;
        int maxRed = premierRed;
        int maxGreen = premierGreen;
        int maxBlue = premierBlue;
        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            if (red < minRed ) {minRed=red;}
            if (green < minGreen ) {minGreen=green;}
            if (blue < minBlue ) {minBlue=blue;}
            if (red > maxRed ) {maxRed=red;}
            if (green > maxGreen ) {maxGreen=green;}
            if (blue > maxBlue ) {maxBlue=blue;}
        }

        //******************************
        // set up LUT
        int[] redLUT= new int[256];
        for (int ng = 0;ng <256;ng++){
            redLUT[ng]=(255*(ng-minRed))/(maxRed-minRed);
        }
        int[] greenLUT= new int[256];
        for (int ng = 0;ng <256;ng++){
            greenLUT[ng]=(255*(ng-minGreen))/(maxGreen-minGreen);
        }
        int[] blueLUT= new int[256];
        for (int ng = 0;ng <256;ng++){
            blueLUT[ng]=(255*(ng-minBlue))/(maxBlue-minBlue);
        }
        //******************************
        //change value for more contrast
        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);

            int newRed = redLUT[red];
            int newGreen = greenLUT[green];
            int newBlue = blueLUT[blue];

            pixels[i] = rgb(newRed,newGreen,newBlue);
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
    //**************************************************************************************
    public void hsvContraste (Bitmap bmp)
    {
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        float[] hsv = new float[3];
        float[] newhsv = new float [3];

        // init find value min & max
        int ppixel = pixels[0];
        int pred = red(ppixel);
        int pgreen = green(ppixel);
        int pblue = blue(ppixel);
        RGBToHSV(pred ,pgreen,pblue,hsv);
        float minV = hsv[2];
        float maxV = hsv[2];
        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            RGBToHSV(red ,green,blue,hsv);
            if (hsv[2] < minV) { minV = hsv[2];}
            if (hsv[2] > maxV) { maxV = hsv[2];}
        }

        //LUT of value
        float[] LUT= new float[256];
        for (int ng = 0;ng <256;ng++){
            LUT[ng]= ((float)(ng-(     ((int)((minV)*255)) )   ))/((float)((int)((maxV-minV)*255)));
        }

        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            RGBToHSV(red ,green,blue,hsv);
            newhsv[0] = hsv[0];
            newhsv[1] = hsv[1];
            newhsv[2]=LUT[(int)(hsv[2]*255)];
            pixels[i] = HSVToColor(newhsv);
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    public int[] getHistogramme(Bitmap bmp){
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        float[] hsv = new float[3];
        int[] histogrammeTable= new int[256];
        //initialisation de la table
        for (int i = 0; i < 256;i++)
        {
            histogrammeTable[i]=0;
        }

        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            RGBToHSV(red ,green,blue,hsv);
            int value=Math.round(hsv[2]*255);
            //Log.i("HG", "value: "+value);
            histogrammeTable[value]++;
        }
        return histogrammeTable;


    }

    public int[] getHistogrammeCumule(Bitmap bmp){
        int[] histogrammeCumule = new int[256];
        int[] histogrammeTable = getHistogramme(bmp);
        int sum=0;
        for (int i = 0; i < 256;i++)
        {
            sum+=histogrammeTable[i];
            histogrammeCumule[i]=sum;
            //Log.i("HC", "HC[i]: "+histogrammeCumule[i]);
        }
        return histogrammeCumule;
    }
    
    public void displayHistogramme(Bitmap bmp){
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        int[] histogrammeTable = getHistogramme(bmp);
        //on test avec un histogramme bidon
        int max=1;
        for (int i=0; i<256;i++){
            if (histogrammeTable[i]>max){
                max=histogrammeTable[i];
            }
        }
        //histogrammeTable = getHistogramme(bmp);

        float[] hsv = new float[3];
        hsv[0]=120;
        hsv[1]=1;
        hsv[2]=1;

        int myColor = rgb(200,0,0);

        //float countperpixelH = Math.round(bmp.getHeight()/256);
        //float countperpixelW = Math.round(bmp.getWidth()/256);

        for (int i = 0; i < 256;i++){
            for (int j=0; j < (histogrammeTable[i]*256)/max; j++) {
                pixels[i+((255-j)*bmp.getWidth())] = myColor;
                //pixels[i+j*255] = myColor;
            }
        }

        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

    public void egalisationHistogramme (Bitmap bmp)
    {
        int[] pixels= new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        int[] histogrammeTable = getHistogramme(bmp);
        int[] histogrammeCumule = getHistogrammeCumule(bmp);
        Log.i("HC", "HC[0]: "+histogrammeCumule[0]);
        Log.i("HC", "HC[100]: "+histogrammeCumule[100]);
        Log.i("HC", "HC[255]: "+histogrammeCumule[255]);
        float[] hsv = new float[3];

        for (int i = 0; i < pixels.length;i++)
        {
            int pixel = pixels[i];
            int red = red(pixel);
            int green = green(pixel);
            int blue = blue(pixel);
            RGBToHSV(red ,green,blue,hsv);
            int value=Math.round(hsv[2]*255);
            int c=histogrammeCumule[value];
            int n =bmp.getWidth()*bmp.getHeight();
            hsv[2]=(c)/n;
            Log.i("HG", "c: "+c);
            Log.i("HG", "value: "+hsv[2]);
            pixels[i] = HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }
    //*************************************************************************************
    //*********     TP4    ****************************************************************

    public void invert (Bitmap bmp) {

    }
    /**
    private void toGrayRS ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
        android.renderscript.RenderScript rs = RenderScript. create ( this ) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation . createFromBitmap ( rs , bmp ) ;
        Allocation output = Allocation . createTyped ( rs , input.getType () ) ;
        // 3) Creer le script
        ScriptC_gray grayScript = new ScriptC_gray ( rs ) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
        grayScript . forEach_toGray ( input , output ) ;
        // 7) Recuperer les donnees des Allocation (s)
        output . copyTo ( bmp ) ;
        // 8) Detruire le context , les Allocation (s) et le script
        input . destroy () ; output . destroy () ;
        grayScript . destroy () ; rs . destroy () ;
    }
**/
    //**************************************************************************************
    //**************************************************************************************



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView tv = (TextView) findViewById(R.id.txtHello);
        Button bg = (Button) findViewById(R.id.bGray);
        Button bc = (Button) findViewById(R.id.bColorize);
        Button br = (Button) findViewById(R.id.bRed);
        Button bc1 = (Button) findViewById(R.id.bContraste1);
        Button blc = (Button) findViewById(R.id.bLowContraste);



        //Log.i("CV", "message onCreate");


        ImageView img = (ImageView) findViewById(imagetest);
        BitmapDrawable d = (BitmapDrawable) img.getDrawable();
        final Bitmap bitmap = d.getBitmap();
        //Bitmap b = BitmapFactory.decodeFile("res/drawable/test.png");
        String width = Integer.toString(bitmap.getWidth());
        String height = Integer.toString(bitmap.getHeight());
        tv.setText("Width: "+width+" Height: "+height);


        final Bitmap bitmap2 = bitmap.copy(bitmap.getConfig(),true);
        /**
        for (int i = 0; i < bitmap.getWidth();i++){
            bitmap2.setPixel(i,32, 0x00ff44);
        }**/
        getHistogramme(bitmap2);
        bg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                toGray(bitmap2);
            }
        });

        bc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                colorize(bitmap2);
            }
        });

        br.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                keepRed(bitmap2);
            }
        });

        bc1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //contrasteExtensionLineaire(bitmap2);
                hsvContraste(bitmap2);
            }
        });
        blc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                egalisationHistogramme(bitmap2);
                displayHistogramme(bitmap2);
            }
        });






        img.setImageBitmap(bitmap2);
    }



}
