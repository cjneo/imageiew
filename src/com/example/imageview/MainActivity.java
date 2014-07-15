package com.example.imageview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	ImageView iv_image;
	Bitmap newbmp;
	static TextView mytext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonGetPicture = (Button)findViewById(R.id.get_picture);
        Button buttonstart =  (Button)findViewById(R.id.startgame);
        // iv_image = (ImageView)findViewById(R.id.image);
        buttonGetPicture.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
	                    openAlbumIntent.setType("image/*");
	                    startActivityForResult(openAlbumIntent, 0);
					}
				}
        		);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);


        int width = metric.widthPixels;  // 宽度（PX）
        int height = metric.heightPixels;  // 高度（PX)
         mytext = (TextView)findViewById(R.id.mytext); 
        mytext.setText("width:"+width+";"+"height:"+height);
    }


    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Bitmap smallpic;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case 0:
                ContentResolver resolver = getContentResolver();
                //照片的原始资源地址
                Uri originalUri = data.getData();
                try {
                    //使用ContentProvider通过URI获取原始图片
                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    if (photo != null) {
                        //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    	//int SCALE =1;
                    	smallpic = ImageCrop(photo,false);
                        //释放原始图片占用的内存，防止out of memory异常发生
                       // photo.recycle();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
                   //     iv_image.setImageBitmap(smallpic);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
            default:
                break;
            }
        }
    }
    public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled)  
    {  
  
        if (bitmap == null)  
        {  
            return null;  
        }  
  
        int w = bitmap.getWidth(); // 得到图片的宽，高  
        int h = bitmap.getHeight();  
        mytext.setText("width:"+w+";"+"height:"+h);
        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长  
  
        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标  
        int retY = w > h ? 0 : (h - w) / 2;  
  
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,  
                false);  
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)  
                && !bitmap.isRecycled())  
        {  
          //  bitmap.recycle();  
          //  bitmap = null;  
        }  
  
        // 下面这句是关键  
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,  
                    // false);  
    }  
    
    public static List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {    
        
        List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);    
        int width = bitmap.getWidth();    
        int height = bitmap.getHeight();    
        int pieceWidth = width / 3;    
        int pieceHeight = height / 3;    
        for (int i = 0; i < yPiece; i++) {    
            for (int j = 0; j < xPiece; j++) {    
                ImagePiece piece = new ImagePiece();    
                piece.index = j + i * xPiece;    
                int xValue = j * pieceWidth;    
                int yValue = i * pieceHeight;    
                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,    
                        pieceWidth, pieceHeight);    
                pieces.add(piece);    
            }    
        }    
    
        return pieces; 
    
    }
}
