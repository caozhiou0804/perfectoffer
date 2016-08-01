package com.dh.perfectoffer.dhutils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore;

public class PicChooseUtil {

	public static final int GETPIC_FROM_SYSTEM = 0;
	public static final int GETPIC_FROM_LOCAL = 1;
	public static final int GETPIC_FROM_CAMARA = 2;

	private static final long IMG_MAX_SIZE = 2097152; // 2G

	public PicChooseUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void getPicFromLocal(Activity parentActivity) {
		Intent native_intent = new Intent(Intent.ACTION_PICK, null);
		native_intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		parentActivity.startActivityForResult(native_intent, GETPIC_FROM_LOCAL);

	}

	public static void getPicFromCamara(Activity parentActivity) {
		parentActivity.startActivityForResult(turnToCamera(),
				GETPIC_FROM_CAMARA);
	}

	/**
	 * 调用照相机
	 * 
	 * @return
	 */
	public static Intent turnToCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		return intent;
	}

	public static Bitmap setImage2Roate(Context context, String imgpath) {
		Bitmap bitmap = null;
		// /**
		// * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
		// */
		// // int degree = readPictureDegree(imgpath);

		Options options = new Options();

		options.inSampleSize = 1;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inJustDecodeBounds = false;
		options.inTempStorage = new byte[32767];
		try {
			// bitmap = CompressImageUtils.loadImageFromPath(imgpath);

			File file = new File(imgpath);
			Uri mImageCaptureUri = Uri.fromFile(file);
			bitmap = BitmapFactory.decodeStream(context.getApplicationContext()
					.getContentResolver().openInputStream(mImageCaptureUri),
					null, options);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// /**
		// * 把图片旋转为正的方向
		// */
		// bitmap = rotaingImageView(degree, bitmap);

		return bitmap;
	}

	public static Bitmap getSmallPicFromPath(Context context,
			String tempImgPath, int h, int w) {
		Bitmap small_bitmap = setImage2Roate(context, tempImgPath);
		small_bitmap = MyThumbnailUtils.extractThumbnail(small_bitmap, h, w);
		return small_bitmap;
	}

}
