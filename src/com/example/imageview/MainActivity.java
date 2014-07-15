package com.example.imageview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


http://blog.csdn.net/qq435757399/article/details/8008638

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
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	ImageView iv_image;
	Bitmap newbmp;
	static TextView mytext;
	private MyAdapter adapter=null;  
    private SimpleAdapter adapter1=null;  
    private List<HashMap<String,Object>> list=null;  
    private HashMap<String,Object> map=null;  
    private String data[]={"图片1","图片2","图片3","图片4","图片5","图6","图片7","图片8","图片9",  
            "图片10","图片11","图片12","图片13","图片14","图片15","图片16"};  
    private int   imgId[]={R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,  
            R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,  
            R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a};  
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


        int width = metric.widthPixels;  // 瀹藉害锛圥X锛�
        int height = metric.heightPixels;  // 楂樺害锛圥X)
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
                //鐓х墖鐨勫師濮嬭祫婧愬湴鍧�
                Uri originalUri = data.getData();
                try {
                    //浣跨敤ContentProvider閫氳繃URI鑾峰彇鍘熷鍥剧墖
                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    if (photo != null) {
                        //涓洪槻姝㈠師濮嬪浘鐗囪繃澶у鑷村唴瀛樻孩鍑猴紝杩欓噷鍏堢缉灏忓師鍥炬樉绀猴紝鐒跺悗閲婃斁鍘熷Bitmap鍗犵敤鐨勫唴瀛�
                    	//int SCALE =1;
                    	smallpic = ImageCrop(photo,false);
                        //閲婃斁鍘熷鍥剧墖鍗犵敤鐨勫唴瀛橈紝闃叉out of memory寮傚父鍙戠敓
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
  
        int w = bitmap.getWidth(); // 寰楀埌鍥剧墖鐨勫锛岄珮  
        int h = bitmap.getHeight();  
        mytext.setText("width:"+w+";"+"height:"+h);
        int wh = w > h ? h : w;// 瑁佸垏鍚庢墍鍙栫殑姝ｆ柟褰㈠尯鍩熻竟闀� 
  
        int retX = w > h ? (w - h) / 2 : 0;// 鍩轰簬鍘熷浘锛屽彇姝ｆ柟褰㈠乏涓婅x鍧愭爣  
        int retY = w > h ? 0 : (h - w) / 2;  
  
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,  
                false);  
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)  
                && !bitmap.isRecycled())  
        {  
          //  bitmap.recycle();  
          //  bitmap = null;  
        }  
  
        // 涓嬮潰杩欏彞鏄叧閿� 
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
