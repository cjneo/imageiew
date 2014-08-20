package com.example.imageview;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageHelper{
/**
	 * result:create a picture from a exist picture 
	 * 从原有图像中裁剪出一个正方形图像
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

	/*从一张存在的图片中切分图像成x*y个格子
	 * 并返回
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
	 * 计算对图片进行缩放的比率 
	 */
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
	
	/*
	 * 返回经过缩放之后的图片
	 * 第一次调用decodefile时并不真正loadpicture,
	 * 只获得相应得参数
	 */
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