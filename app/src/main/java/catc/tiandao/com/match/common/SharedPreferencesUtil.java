package catc.tiandao.com.match.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import catc.tiandao.com.match.MatchApplication;

/***
 简单的存储类
 * **/
public class SharedPreferencesUtil {


    public static final String OPEN_PERMISSIONS = "open_permissions";
    private static final String SHARED_PATH = "User_data";
	public static final String USER_KEY = "user_key";

	/**
	 * 拿到SharedPreferences
	 **/
	public static SharedPreferences getDefaultSharedPreferences(Context context) {
		return context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
	}
	
	public static void putInt(Context context, String key, int value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getInt(key, 0);
	}

	public static int getInt2(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getInt(key, -1);
	}
	
	public static void putLong(Context context, String key, long value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public static long getLong(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getLong(key, 0);
	}
	
	public static void putString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public static String getString(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getString(key, null);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}


	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(key, defValue);
	}
	
	public static void removeKey(Context context, String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.remove(key);
		edit.commit();
	}

	//保存序列化对象
	public static void saveBean(Object object,String key) {
		SharedPreferences mSharedPreferences = MatchApplication.getInstance()
				.getSharedPreferences("base64",
						Context.MODE_PRIVATE);
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);

			String personBase64 = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			Editor editor = mSharedPreferences.edit();
			editor.putString(key, personBase64);
			editor.commit();
		}
		catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (oos != null)
					oos.close();
				if(baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//获取序列化对象
	public static Object getBean(String key) {
		ObjectInputStream ois = null;
		ByteArrayInputStream bais = null;
		try {
			SharedPreferences mSharedPreferences = MatchApplication.getInstance()
					.getSharedPreferences("base64",
							Context.MODE_PRIVATE);
			String personBase64 = mSharedPreferences.getString(key, "");
			byte[] base64Bytes = Base64.decode(personBase64, Base64.DEFAULT);//
			bais = new ByteArrayInputStream(base64Bytes);
			ois = new ObjectInputStream(bais);
			Object object = ois.readObject();
			return object;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			try {
				if (ois != null)
					ois.close();
				if (bais != null)
					bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**存储对象*/
	private static void put(Context context, String key, Object obj)
			throws IOException
	{
		if (obj == null) {//判断对象是否为空
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream    oos  = null;
		oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		// 将对象放到OutputStream中
		// 将对象转换成byte数组，并将其进行base64编码
		String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
		baos.close();
		oos.close();

		putString(context, key, objectStr);
	}

	/**获取对象*/
	private static Object get(Context context, String key)
			throws IOException, ClassNotFoundException
	{
		String wordBase64 = getString(context, key);
		// 将base64格式字符串还原成byte数组
		if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
			return null;
		}
		byte[]               objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream bais     = new ByteArrayInputStream(objBytes);
		ObjectInputStream    ois      = new ObjectInputStream(bais);
		// 将byte数组转换成product对象
		Object obj = ois.readObject();
		bais.close();
		ois.close();
		return obj;
	}

	/**
	 * 存储List集合
	 * @param context 上下文
	 * @param key 存储的键
	 * @param list 存储的集合
	 */
	public static void putList(Context context, String key, List<? extends Serializable> list) {
		try {
			put(context, key, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取List集合
	 * @param context 上下文
	 * @param key 键
	 * @param <E> 指定泛型
	 * @return List集合
	 */
	public static <E extends Serializable> List<E> getList(Context context, String key) {
		try {
			return (List<E>) get(context, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
