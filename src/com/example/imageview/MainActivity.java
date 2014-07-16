package com.example.imageview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	ImageView iv_image;
	static Bitmap newbmp;
	static TextView mytext;
	  private GridView gridView=null;  
	  static Bitmap smallpic;
	private MyAdapter adapter=null;  
    private SimpleAdapter adapter1=null;  
    private List<HashMap<String,Object>> list=null;  
    private HashMap<String,Object> map=null;  
    private String data[]={"图片1","图片2","图片3","图片4","图片5","图6","图片7","图片8","图片9",  
            };  
    private int   imgId[]={R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,  
            R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,  
            R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a,R.drawable.a};
    static List<ImagePiece> myimagelist;
     ImagePiece imagearray[]=new ImagePiece[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonGetPicture = (Button)findViewById(R.id.get_picture);
        Button buttonstart =  (Button)findViewById(R.id.startgame);
         iv_image = (ImageView)findViewById(R.id.image);
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
        buttonstart.setOnClickListener(
        		new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						setdata();
						
					}
				});
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
      

        int width = metric.widthPixels;  // 瀹藉害锛圥X锛�
        int height = metric.heightPixels;  // 楂樺害锛圥X)
         mytext = (TextView)findViewById(R.id.mytext); 
        mytext.setText("width:"+width+";"+"height:"+height);
    }

    void setdata(){
    	myimagelist = split(smallpic, 3, 3);
    	if(myimagelist==null)
    		return;
    	
    	
    	for(int i=0;i<9;i++){
    		imagearray[i]=myimagelist.get(i);
    	}
    	gridView=(GridView) findViewById(R.id.grid_view);  
        list=new ArrayList<HashMap<String,Object>>(); 
        
  adapter=new MyAdapter(this, data, imagearray);  
        
        gridView.setAdapter(adapter);  
          
          
          
      //  使用SimpeAdapter添加数据  
      /* for(int i=0;i<9;i++){  
              
            map=new HashMap<String, Object>();  
              
            map.put("text", data[i]);  
            map.put("img", imgId[i]);  
           list.add(map);  
              
        }  
        //adapter1=new SimpleAdapter(this, list, R.layout.gridview_item, new String[]{"text","img"}, new int[]{R.id.gridview_text,R.id.gridview_img});  
       // gridView.setAdapter(adapter1);
        * */ 
        
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
    	
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case 0:
                ContentResolver resolver = getContentResolver();
                
                Uri originalUri = data.getData();
                  
                Cursor cursor = getContentResolver().query(originalUri, null,   
                		null, null, null); 
                		 
                		//第一行第二列保存路径strRingPath
                		cursor.moveToFirst();   
                		String strRingPath = cursor.getString(1);  
                		cursor.close();
                        mytext.setText(strRingPath);
                        
                        
                        BitmapFactory.Options options = new BitmapFactory.Options();  
                        options.inJustDecodeBounds = true;  
                        BitmapFactory.decodeFile(strRingPath, options);  
                        int imageHeight = options.outHeight;  
                        int imageWidth = options.outWidth;  
                        String imageType = options.outMimeType;  
                        mytext.setText(imageHeight+" "+imageWidth);
                
             //   try {
                	//Bitmap photo=null;
                    //Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                	Bitmap photo=decodeSampledBitmapFromResource(strRingPath,  
                            800, 800) ;
                    if (photo != null) {
                        //涓洪槻姝㈠師濮嬪浘鐗囪繃澶у鑷村唴瀛樻孩鍑猴紝杩欓噷鍏堢缉灏忓師鍥炬樉绀猴紝鐒跺悗閲婃斁鍘熷Bitmap鍗犵敤鐨勫唴瀛�
                    	//int SCALE =1;
                  	smallpic = ImageCrop(photo,false);
                        
                      //  photo.recycle();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
                        iv_image.setImageBitmap(smallpic);
                    }
             //   }
                /*
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                
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
            bitmap.recycle();  
            bitmap = null;  
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
    
    public static int calculateInSampleSize(BitmapFactory.Options options,  
            int reqWidth, int reqHeight) {  
        // 源图片的高度和宽度  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
        if (height > reqHeight || width > reqWidth) {  
            // 计算出实际宽高和目标宽高的比率  
            final int heightRatio = Math.round((float) height / (float) reqHeight);  
            final int widthRatio = Math.round((float) width / (float) reqWidth);  
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高  
            // 一定都会大于等于目标的宽和高。  
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
        }  
        return inSampleSize;  
    }  
    public static Bitmap decodeSampledBitmapFromResource(String  path, 
            int reqWidth, int reqHeight) {  
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(path, options);  
        // 调用上面定义的方法计算inSampleSize值  
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);  
        // 使用获取到的inSampleSize值再次解析图片  
        options.inJustDecodeBounds = false;  
        return BitmapFactory.decodeFile(path, options);  
    }  
}
