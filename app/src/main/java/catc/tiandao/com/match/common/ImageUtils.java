package catc.tiandao.com.match.common;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageUtils
{
  private static final String TAG = "BitmapCommonUtils";
  
  private static final String JPEG_FILE_PREFIX = "IMG_";
  private static final String JPEG_FILE_SUFFIX = ".jpg";

  public static Bitmap getUnErrorBitmap(InputStream is, Options options)
  {
     Bitmap bitmap = null;
    try {
       bitmap = BitmapFactory.decodeStream(is, null, options);
    } catch (OutOfMemoryError e) {
       options.inSampleSize += 1;
       return getUnErrorBitmap(is, options);
    }
     return bitmap;
  }

  public static byte[] Bitmap2Bytes(Bitmap bm) {
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     bm.compress(CompressFormat.PNG, 100, baos);
     return baos.toByteArray();
  }

  public static String getBitmapWH(Bitmap bitmap)
  {
     byte[] datas = Bitmap2Bytes(bitmap);
     Options options = new Options();
     options.inJustDecodeBounds = true;
     BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
     return options.outWidth + "X" + options.outHeight;
  }

  public static File getDiskCacheDir(Context context, String uniqueName)
  {
     String cachePath = "mounted".equals(
       Environment.getExternalStorageState()) ? getExternalCacheDir(context)
       .getPath() : context.getCacheDir().getPath();

     return new File(cachePath + File.separator + uniqueName);
  }

  public static int getBitmapSize(Bitmap bitmap)
  {
     return bitmap.getRowBytes() * bitmap.getHeight();
  }

  public static File getExternalCacheDir(Context context)
  {
     String cacheDir = "/Android/data/" + context.getPackageName() + 
       "/cache/";
     return new File(Environment.getExternalStorageDirectory().getPath() + 
      cacheDir);
  }
  
  /**
   * 保存图片
 * @param context
 * @param bm
 * @param
 * @return
 */
  public static String saveBitmap(Context context,Bitmap bm){
	  try {
		  File  f = createImageFile(context);
		  
		  if (f.exists()) {
		   f.delete();
		  }
		   FileOutputStream out = new FileOutputStream(f);
		   bm.compress(CompressFormat.PNG, 90, out);
		   out.flush();
		   out.close();
		   return f.getAbsolutePath();
	  } catch (FileNotFoundException e) {
	   e.printStackTrace();
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	  
	  return null;
  }
  
  /**
 * @param context
 * @param fileName
 * @param bm
 * @return
 */
public static String saveBitmap(Context context,String fileName,Bitmap bm){
	  try {
		  File  f = new File(context.getExternalCacheDir() + "/" + "img/" + fileName);
		  
		  if (f.exists()) {
		   f.delete();
		  }
		   FileOutputStream out = new FileOutputStream(f);
		   bm.compress(CompressFormat.PNG, 90, out);
		   out.flush();
		   out.close();
		   return f.getAbsolutePath();
	  } catch (FileNotFoundException e) {
	   e.printStackTrace();
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	  
	  return null;
  }
  
  /**
   *获取缓存图片路径 
	 * @return
	 */
	public static File getAlbumDir(Context context) {
		
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			storageDir = new File(context.getExternalCacheDir() + "/" + "img");
			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						return null;
					}
				}
			}
		}else{
			storageDir = new File(context.getCacheDir() + "/" + "img");
		}
		return storageDir;
	}


	/**
	 *获取图片路径
	 * @return
	 */
	public static File getAlbumFilesDir(Context context) {

		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			String path = context.getExternalFilesDir(null).getPath();
			storageDir = new File(path + "/" + "img");
			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						return null;
					}
				}
			}
		}else{

			String path = context.getFilesDir().getPath();
			storageDir = new File(path + "/" + "img");
		}
		return storageDir;
	}




	/**
	 * 创建图片
	 * @return
	 * @throws IOException
	 */
	public static File createImageFile(Context context) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir(context);
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	/**
	 * 创建图片
	 * @return
	 * @throws IOException
	 */
	public static File createImageFile(Context context,String fileName) throws IOException {
		// Create an image file name
		File albumF = getAlbumDir(context);
		File imageF = File.createTempFile(fileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	/**
	 * 显示图片
	 * @param mImageView
	 * @param picPath
	 */
	public static void showPic(ImageView mImageView,String picPath) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		Options bmOptions = new Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(picPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);

	}

	public static String getPath(Activity context, Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}



}
