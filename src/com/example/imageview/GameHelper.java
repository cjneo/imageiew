package com.example.imageview;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

public class GameHelper {
	MainActivity mMainActivity;

	public GameHelper(MainActivity cactivity) {
		mMainActivity = cactivity;
	}

	// private GridView gridView = null;

	int[] changegame(int[] serialnum) {

		int count = 0;
		int direction[] = new int[20];
		int zeroposition = 8;
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
		mMainActivity.gridView.setFocusable(false);
		mMainActivity.gridView.setClickable(false);
		mMainActivity.gridView.setFocusableInTouchMode(false);

		int serialnum[] = { 7, 6, 3, 5, 2, 8, 4, 1, 0 };
		boolean exist[] = new boolean[9];
		for (int i = 0; i < 9; i++) {
			exist[i] = false;
		}

		serialnum = changegame(serialnum);
		BaseAdapter la = (BaseAdapter) mMainActivity.gridView.getAdapter();
		Resources res = mMainActivity.getResources();
		mMainActivity.blankimage = BitmapFactory.decodeResource(res,
				R.drawable.blank);

		ImagePiece tmp = new ImagePiece();
		tmp.bitmap = mMainActivity.blankimage;
		tmp.index = 0;
		// myimagelist.set(8, tmp);

		for (int i = 0; i < 9; i++) {
			if (serialnum[i] != 0) {
				mMainActivity.imagearray[i] = mMainActivity.myimagelist
						.get(serialnum[i] - 1);
			} else {
				mMainActivity.imagearray[i] = tmp;
			}
		}

		(la).notifyDataSetChanged();
		mMainActivity.gridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 传递选中的position到adapter里去重绘GridView当前列的背景色
						// gView.setAdapter(new dataAdapter(position));
						BaseAdapter la = (BaseAdapter) mMainActivity.gridView
								.getAdapter();

						/*
						 * It means : up, down, left, right;
						 * 0,1,2
						 * 3,4,5
						 * 6,7,8
						 * 
						 */
						int up=0,down=1,left=2,right=3;
						int direction[] = { position - 3, position + 3,
								position - 1, position + 1 };
						if(position==2||position==5){
						    direction[right]=-1;
						} else if(position==3||position==6){
						    direction[left]=-1;
						}
						    
						boolean changeok = false;
						int changenum = 0;
						for (int i = 0; i <= 3; i++) {
							if (direction[i] >= 0 && direction[i] <= 8) {
								if (mMainActivity.imagearray[direction[i]].index == 0) {
									changenum = direction[i];
									changeok = true;
									break;
								}
							}
						}
						ImagePiece tmpimagepiece = new ImagePiece();
						if (changeok == true) {
							tmpimagepiece = mMainActivity.imagearray[changenum];

							mMainActivity.imagearray[changenum] = mMainActivity.imagearray[position];
							mMainActivity.imagearray[position] = tmpimagepiece;
						}
						for (int i = 0; i < 8; i++) {
							if (mMainActivity.imagearray[i].index != i + 1) {
								mMainActivity.gamesuccess = false;
								break;
							}
							mMainActivity.gamesuccess = true;
						}
						if (mMainActivity.gamesuccess) {
							mMainActivity.imagearray[8] = mMainActivity.lastbitmap;

							mMainActivity.buttonStart
									.setText(R.string.try_again);
							mMainActivity.gridView.setClickable(false);
							mMainActivity.gridView.setFocusable(false);
							mMainActivity.gridView
									.setFocusableInTouchMode(false);
						}
						(la).notifyDataSetChanged();
					}

				});
	}

}