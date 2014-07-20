package com.example.imageview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ImageView iv_image;
	static Bitmap newbmp;
	static TextView mytext;
	private GridView gridView = null;
	static Bitmap smallpic;
	private MyAdapter adapter = null;
	private SimpleAdapter adapter1 = null;
	private List<HashMap<String, Object>> list = null;
	private HashMap<String, Object> map = null;
	private String data[] = { "图片1", "图片2", "图片3", "图片4", "图片5", "图6", "图片7",
			"图片8", "图片9", };
	ImagePiece lastbitmap = null;
	Button buttonGetPicture;
	Button buttonStart;
	static List<ImagePiece> myimagelist;
	ImagePiece imagearray[] = new ImagePiece[9];
	Bitmap blankimage;
	boolean gamesuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonGetPicture = (Button) findViewById(R.id.get_picture);
		buttonStart = (Button) findViewById(R.id.startgame);
		buttonStart.setVisibility(View.INVISIBLE);

		 iv_image = (ImageView)findViewById(R.id.image);
		// iv_image.setVisibility(View.GONE);
		buttonGetPicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/*");
				startActivityForResult(openAlbumIntent, 0);
			}
		});
		buttonStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// setdata();
				startgame();

			}
		});
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		int width = metric.widthPixels; // 瀹藉害锛圥X锛�
		int height = metric.heightPixels; // 楂樺害锛圥X)
	}

	int[] changegame(int[] serialnum) {
		/*
		 * { int i=0; int sum=0, tmpnum,tmpindex; for(i=0;i<=8;i++){
		 * if(serialnum[i]!=0){ for(int j=i+1;j<=8;j++){
		 * if(serialnum[j]<serialnum[i]&&serialnum[j]!=0){ sum++; } } } }
		 * if(sum%2!=0){ tmpnum = serialnum[0]; tmpindex=0; for(int
		 * j=1;j<=8;j++){ if(tmpnum<serialnum[j]){
		 * serialnum[tmpindex]=serialnum[j]; serialnum[j]=tmpnum; break; } else
		 * if(tmpnum<serialnum[j]){ tmpnum=serialnum[j]; tmpindex = j; } } }}
		 */
		int count = 0;
		int direction[] = new int[20];
		int zeroposition = 8;
		int tmp;
		int movenum = 0;
		for (int i = 0; i <= 8; i++) {
			if (serialnum[i] == 0) {
				zeroposition = i;
				break;
			}
		}
		Random rdm = new Random(System.currentTimeMillis());

		while (count < 20) {
			direction[count++] = Math.abs(rdm.nextInt()) % 4;
		}
		count = 0;
		while (movenum < 30) {
			if (count == 20) {
				count = 0;
			}
			int cd = direction[count];
			if (cd == 0) {
				if (zeroposition - 3 >= 0) {
					serialnum[zeroposition] = serialnum[zeroposition - 3];
					serialnum[zeroposition - 3] = 0;
					zeroposition = zeroposition - 3;
					movenum++;
				}
			} else if (cd == 1) {
				if (zeroposition + 3 <= 8) {
					serialnum[zeroposition] = serialnum[zeroposition + 3];
					serialnum[zeroposition + 3] = 0;
					zeroposition = zeroposition + 3;
					movenum++;
				}
			} else if (cd == 2) {
				if (zeroposition != 0 && zeroposition != 3 && zeroposition != 6) {
					serialnum[zeroposition] = serialnum[zeroposition - 1];
					serialnum[zeroposition - 1] = 0;
					zeroposition = zeroposition - 1;
					movenum++;
				}
			} else if (cd == 3) {
				if (zeroposition != 2 && zeroposition != 5 && zeroposition != 8) {
					serialnum[zeroposition] = serialnum[zeroposition + 1];
					serialnum[zeroposition + 1] = 0;
					zeroposition = zeroposition + 1;
					movenum++;
				}
			}
			count++;
		}
		return serialnum;
	}

	void startgame() {
		// int serialnum[]={1,2,3,4,5,6,7,8,0};
		gridView.setFocusable(false);
		gridView.setClickable(false);
		gridView.setFocusableInTouchMode(false);

		int serialnum[] = { 7, 6, 3, 5, 2, 8, 4, 1, 0 };
		boolean exist[] = new boolean[9];
		boolean flag = false;
		int count = 0, num;
		for (int i = 0; i < 9; i++) {
			exist[i] = false;
		}
		Random rdm = new Random(System.currentTimeMillis());
		/*
		 * while(count<9){
		 * 
		 * num = Math.abs(rdm.nextInt())%9;
		 * 
		 * if(exist[num]==false){ serialnum[count] = num; exist[num]=true;
		 * count++; } }
		 */
		serialnum = changegame(serialnum);
		ImagePiece tmpimagepiece = new ImagePiece();

		BaseAdapter la = (BaseAdapter) gridView.getAdapter();
		Resources res = getResources();
		Drawable blankdrawble = res.getDrawable(R.drawable.blank);
		blankimage = BitmapFactory.decodeResource(res, R.drawable.blank);

		ImagePiece tmp = new ImagePiece();
		tmp.bitmap = blankimage;
		tmp.index = 0;
	//	myimagelist.set(8, tmp);
		
		for (int i = 0; i < 9; i++) {
			if (serialnum[i] != 0) {
				imagearray[i] = myimagelist.get(serialnum[i] - 1);
			} else {
				imagearray[i] = tmp;
			}
		}

		(la).notifyDataSetChanged();
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 传递选中的position到adapter里去重绘GridView当前列的背景色
				// gView.setAdapter(new dataAdapter(position));
				BaseAdapter la = (BaseAdapter) gridView.getAdapter();

				int up, down, left, right;
				up = position - 3;
				down = position + 3;
				left = position - 1;
				right = position + 1;
				int direction[] = { position - 3, position + 3, position - 1,
						position + 1 };
				boolean changeok = false;
				int changenum = 0;
				for (int i = 0; i <= 3; i++) {
					if (direction[i] >= 0 && direction[i] <= 8) {
						if (imagearray[direction[i]].index == 0) {
							changenum = direction[i];
							changeok = true;
							break;
						}
					}
				}
				ImagePiece tmpimagepiece = new ImagePiece();
				if (changeok == true) {
					tmpimagepiece = imagearray[changenum];

					imagearray[changenum] = imagearray[position];
					imagearray[position] = tmpimagepiece;
				//	(la).notifyDataSetChanged();
				}
				for (int i = 0; i < 8; i++) {
					if (imagearray[i].index != i + 1) {
						gamesuccess = false;
						break;
					}
					gamesuccess = true;
				}
				if (gamesuccess) {
					imagearray[8] = lastbitmap;
					
					buttonStart.setText(R.string.try_again);
					gridView.setClickable(false);
					gridView.setFocusable(false);
					gridView.setFocusableInTouchMode(false);
				}
				(la).notifyDataSetChanged();
			}

		});
	}

	void setdata() {
		myimagelist = split(smallpic, 3, 3);
		if (myimagelist == null)
			return;

		for (int i = 0; i < 9; i++) {
			imagearray[i] = myimagelist.get(i);
		}
		lastbitmap = new ImagePiece();
		lastbitmap = myimagelist.get(8);
		Resources res = getResources();
		Drawable blankdrawble = res.getDrawable(R.drawable.blank);
		blankimage = BitmapFactory.decodeResource(res, R.drawable.blank);
		// Bitmap bitmap = drawable.getBitmap();
		lastbitmap = myimagelist.get(8);
		ImagePiece tmppiece = new ImagePiece();
		tmppiece.bitmap=blankimage;
		tmppiece.index = 0;
		//imagearray[8].bitmap = blankimage;
		//imagearray[8].index = 0;
		imagearray[8]=tmppiece;
		gridView = (GridView) findViewById(R.id.grid_view);
		//list = new ArrayList<HashMap<String, Object>>();
		// avoid the default color of the gridview frame
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
				ContentResolver resolver = getContentResolver();

				Uri originalUri = data.getData();

				Cursor cursor = getContentResolver().query(originalUri, null,
						null, null, null);

				// 第一行第二列保存路径strRingPath
				cursor.moveToFirst();
				String strRingPath = cursor.getString(1);
				cursor.close();

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(strRingPath, options);
				int imageHeight = options.outHeight;
				int imageWidth = options.outWidth;
				String imageType = options.outMimeType;

				// try {
				// Bitmap photo=null;
				// Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
				// originalUri);
				Bitmap photo = decodeSampledBitmapFromResource(strRingPath,
						400, 400);
				if (photo != null) {
					// 涓洪槻姝㈠師濮嬪浘鐗囪繃澶у鑷村唴瀛樻孩鍑猴紝杩欓噷鍏堢缉灏忓師鍥炬樉绀猴紝鐒跺悗閲婃斁鍘熷Bitmap鍗犵敤鐨勫唴瀛�
					// int SCALE =1;
					smallpic = ImageCrop(photo, false);

					// photo.recycle();
					 iv_image.setVisibility(View.VISIBLE);
					 iv_image.setImageBitmap(smallpic);
				}
				// }
				/*
				 * catch (FileNotFoundException e) { e.printStackTrace(); }
				 * catch (IOException e) { e.printStackTrace(); }
				 */
				setdata();
				buttonStart.setText(R.string.start_game);
				buttonStart.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}
	}

	public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {

		if (bitmap == null) {
			return null;
		}

		int w = bitmap.getWidth(); // 寰楀埌鍥剧墖鐨勫锛岄珮
		int h = bitmap.getHeight();
		int wh = w > h ? h : w;// 瑁佸垏鍚庢墍鍙栫殑姝ｆ柟褰㈠尯鍩熻竟闀�

		int retX = w > h ? (w - h) / 2 : 0;// 鍩轰簬鍘熷浘锛屽彇姝ｆ柟褰㈠乏涓婅x鍧愭爣
		int retY = w > h ? 0 : (h - w) / 2;

		Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
				false);
		if (isRecycled && bitmap != null && !bitmap.equals(bmp)
				&& !bitmap.isRecycled()) {
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
		int count = 1;
		for (int i = 0; i < yPiece; i++) {
			for (int j = 0; j < xPiece; j++) {
				ImagePiece piece = new ImagePiece();
				piece.index = j + i * xPiece;
				int xValue = j * pieceWidth;
				int yValue = i * pieceHeight;
				piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
						pieceWidth, pieceHeight);
				piece.index = count++;
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
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String path,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
}
