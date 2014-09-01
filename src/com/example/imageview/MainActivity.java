package com.example.imageview;

import java.util.List;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

		iv_image = (ImageView) findViewById(R.id.image);
		// iv_image.setVisibility(View.GONE);
		Resources res = getResources();
		blankimage = BitmapFactory.decodeResource(res, R.drawable.blank);
		mGameHelper = new GameHelper(this);
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
				gridView.setFocusable(false);
				gridView.setClickable(false);
				gridView.setFocusableInTouchMode(false);
				mGameHelper.startgame();

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

				Cursor cursor = getContentResolver().query(originalUri, null,
						null, null, null);

				// 第一行第二列保存路径strRingPath
				cursor.moveToFirst();
				String strRingPath = cursor.getString(1);
				cursor.close();

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(strRingPath, options);
				Bitmap photo = ImageHelper.decodeSampledBitmapFromResource(
						strRingPath, 400, 400);
				if (photo != null) {
					smallpic = ImageHelper.ImageCrop(photo, false);

					iv_image.setVisibility(View.VISIBLE);
					iv_image.setImageBitmap(smallpic);
				}
				setdata();
				buttonStart.setText(R.string.start_game);
				buttonStart.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}
	}

}
