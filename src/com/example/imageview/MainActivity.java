package com.example.imageview;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONObject;

import com.example.imageview.rankList;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ImageView iv_image;
	static Bitmap newbmp;
	static TextView mytext;
	public GridView gridView = null;
	static Bitmap smallpic;
	public GameHelper mGameHelper;
	private MyAdapter adapter = null;
	private String data[] = new String[9];
	ImagePiece lastbitmap = null;
	Button buttonGetPicture;
	Button buttonStart;
	Button buttonStartRank;
	static List<ImagePiece> myimagelist;
	ImagePiece imagearray[] = new ImagePiece[9];
	Bitmap blankimage;
	boolean gamesuccess = false;
	
	 int timecount = 0;
	    Handler handler = new Handler();
	    String jsonString = "";
	    String urlLocalPrefix = "http://192.168.191.1/test";
	    String urlInternetPrefix ="http://loveriver.sinaapp.com";
	    String urlPrefix = urlInternetPrefix;
	    int timeScore=timecount;
	    Handler uiHandler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 1:
	               

	                break;
	            case 2:
	                Toast.makeText(getApplicationContext(), jsonString,
	                        Toast.LENGTH_LONG).show();
	            }
	            super.handleMessage(msg);
	        }
	    };
	    Toast succeedtoast;
	    Runnable runnable = new Runnable() {
	        public void run() {
	            timecount++;
	            handler.postDelayed(this, 1000);
	        }
	    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonGetPicture = (Button) findViewById(R.id.get_picture);
		buttonStart = (Button) findViewById(R.id.startgame);
		buttonStartRank =(Button)findViewById(R.id.startrank);
		buttonStart.setVisibility(View.INVISIBLE);
	    buttonStartRank.setVisibility(View.INVISIBLE);
	    
        handler.postDelayed(runnable, 1000);


		iv_image = (ImageView) findViewById(R.id.image);
		// iv_image.setVisibility(View.GONE);
		Resources res = getResources();
		blankimage = BitmapFactory.decodeResource(res, R.drawable.blank);
		mGameHelper = new GameHelper(this);
		buttonGetPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/jpeg");
				openAlbumIntent.addCategory(Intent.CATEGORY_OPENABLE); 
				if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){                  
			        startActivityForResult(openAlbumIntent, 0);    
			}else{                
			        startActivityForResult(openAlbumIntent, 0);   
			} 
			}
		});
		buttonStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gridView.setFocusable(false);
				gridView.setClickable(false);
				gridView.setFocusableInTouchMode(false);
				mGameHelper.startgame();
				timecount = 0;
			}
		});
		
		  buttonStartRank.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View arg0) {
	                startRankList();
	            }
	        });
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		int width = metric.widthPixels;
		int height = metric.heightPixels;
	}

	void setdata() {
		myimagelist = ImageHelper.split(smallpic, 3, 3);
		if (myimagelist == null)
			return;

		for (int i = 0; i < 9; i++) {
			imagearray[i] = myimagelist.get(i);
		}
		lastbitmap = new ImagePiece();
		lastbitmap = myimagelist.get(8);
		Resources res = getResources();
		blankimage = BitmapFactory.decodeResource(res, R.drawable.blank);
		lastbitmap = myimagelist.get(8);
		ImagePiece tmppiece = new ImagePiece();
		tmppiece.bitmap = blankimage;
		tmppiece.index = 0;

		imagearray[8] = tmppiece;
		gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new MyAdapter(this, data, imagearray);

		gridView.setAdapter(adapter);

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
			    
				Uri originalUri = data.getData();
				String strRingPath=ImageHelper.getPath(this, originalUri);
				//String strRingPath=DocumentsContract.getDocumentId(originalUri);
				//String strRingPath=Build.VERSION.SDK_INT+"";
				//String strRingPath= ImageHelper.selectImage(this,data);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(strRingPath, options);
				Bitmap photo = ImageHelper.decodeSampledBitmapFromResource(
						strRingPath, 400, 400);
				
				if (photo != null) 
				{
					smallpic = ImageHelper.ImageCrop(photo, false);

					iv_image.setVisibility(View.VISIBLE);
					iv_image.setImageBitmap(smallpic);
				}
				setdata();
				buttonStart.setText(R.string.start_game);
			//	buttonStart.setText(strRingPath);
				buttonStart.setVisibility(View.VISIBLE);
                buttonStartRank.setText(R.string.start_rank);

				buttonStartRank.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}
	}
	  public void startRankList(){
	        Intent intent = new Intent();
	        Bundle b = new Bundle();
	        String rankListUrl = urlPrefix + "/jsonarray.php";
	        b.putString("urlRankList", rankListUrl);
	        intent.putExtras(b);
	        intent.setClass(MainActivity.this, rankList.class);
	        startActivityForResult(intent, 0);
	    }
	  public void postJsonToServer(final String urlpath) {
	         timeScore=timecount;
	        new Thread() {
	            public void run() {
	                URL url;
	                try {
	                    url = new URL(urlpath);
	                    /* 封装子对象 */

	                    SimpleDateFormat formatter = new SimpleDateFormat(
	                            "yyyy-MM-dd  HH:mm:ss");
	                    // Date curDate = new Date(System.currentTimeMillis());//
	                    // 获取当前时间
	                    Date curDate = new Date();// 获取当前时间
	                    String str = formatter.format(curDate);
	                    // long timelong = curDate.getTime();
	                    // str="hello";
	                    JSONObject ClientKey = new JSONObject();
	                    ClientKey.put("user", getInfo());
	                    ClientKey.put("game_time", timeScore);
	                    ClientKey.put("create_time", str);
	                    HttpClient httpClient = new DefaultHttpClient();
	                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
	                    // 读取超时
	                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000    );
	                    LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	                    params.add(new BasicNameValuePair("postjson", ClientKey
	                            .toString()));
	                    HttpPost postMethod = new HttpPost(urlpath);
	                    postMethod.setEntity(new UrlEncodedFormEntity(params,
	                            "utf-8")); // 将参数填入POST Entity中

	                    HttpResponse response = httpClient.execute(postMethod); // 执行POST方法
	                    /* 把JSON数据转换成String类型使用输出流向服务器写 */
	                    int resCode = response.getStatusLine().getStatusCode();// 获取响应码
	                    // //获取响应内容
	                    StringBuilder strContent = new StringBuilder();

	                    if (resCode == 200) {

	                        BufferedReader buffer = new BufferedReader(
	                                new InputStreamReader(response.getEntity()
	                                        .getContent()));
	                        for (String s = buffer.readLine(); s != null; s = buffer
	                                .readLine()) {
	                            strContent.append(s);
	                        }
	                        String newstr = new String(strContent.toString()
	                                .getBytes(), "UTF-8");
	                        

	                        JSONObject json = new JSONObject(newstr);
	                        String user = json.getString("user");

	                        int game_time = json.getInt("game_time");
	                        String create_time = json.getString("create_time");
	                        

	                        String errorstr = "error";
	                        if (!errorstr.equals(user)) {
	                            jsonString = "恭喜完成拼图，用时" + timeScore
	                                    + "秒。在所有玩家中名列前茅，快去看看成绩吧";
	                        } else {
	                            jsonString = "恭喜完成拼图，用时" + timeScore
	                                    + "秒。虽然没有名列前茅，但是也可以看看成绩";

	                        }
	                        Message message2 = new Message();
	                        message2.what = 2;
	                        uiHandler.sendMessage(message2);
	                    }
	                   // dialog();

	                }

	                catch (Exception e) {
	                    jsonString = "恭喜完成拼图，用时" + timeScore
	                            + "秒。你可以连接网络将成绩上传。";
	                    Message message2 = new Message();
	                    message2.what = 2;
	                    uiHandler.sendMessage(message2);
	                    e.printStackTrace();
	                }
	            }
	        }.start();

	    }
	  public void handleGameSuccess(){
	  Toast.makeText(getApplicationContext(), "拼图完成",
	          Toast.LENGTH_SHORT).show();
	  String urlpath = urlPrefix+"/postjson.php";
      postJsonToServer(urlpath);
	  }
	  private String getInfo() {
	        String mtype = android.os.Build.MODEL; // 手机型号
	        String mtyb = android.os.Build.BRAND;// 手机品牌
	        String text = mtype + " " + mtyb + " ";
	        // edit.setText(text);
	        return text;
	    }
	    
}
