package com.example.imageview;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageHelper{
/**
	 * result:create a picture from a exist picture 
	 * ��ԭ��ͼ���вü���һ��������ͼ��
	 * @param bitmap :the picture you want to cut
	 * @param isRecycled
	 * @return
	 */
	public static Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {

		if (bitmap == null) {
			return null;
		}

		int w = bitmap.getWidth(); 
		int h = bitmap.getHeight();
		int wh = w > h ? h : w;
		int retX = w > h ? (w - h) / 2 : 0;
		int retY = w > h ? 0 : (h - w) / 2;

		Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
				false);
		if (isRecycled && bitmap != null && !bitmap.equals(bmp)
				&& !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}

		return bmp;
	}

	/*��һ�Ŵ��ڵ�ͼƬ���з�ͼ���x*y������
	 * ������
	 * 
	 */
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

	/*
	 * �����ͼƬ�������ŵı��� 
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// ԴͼƬ�ĸ߶ȺͿ��
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�
			// һ��������ڵ���Ŀ��Ŀ�͸ߡ�
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	/*
	 * ���ؾ�������֮���ͼƬ
	 * ��һ�ε���decodefileʱ��������loadpicture,
	 * ֻ�����Ӧ�ò���
	 */
	public static Bitmap decodeSampledBitmapFromResource(String path,
			int reqWidth, int reqHeight) {
		// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// �������涨��ķ�������inSampleSizeֵ
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	
}