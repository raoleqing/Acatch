package catc.tiandao.com.match.utils;

import android.util.Base64;
import android.util.Xml;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {

    public final static String KEY = "haolikeji";
    //private final static String HEX = "0123456789ABCDEF";
    private final static String TRANSFORMATION = "DES/CBC/PKCS5Padding";//DES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private final static String ALGORITHM = "DES";//DES是加密方式
    private static byte[] DESIV = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB,
            (byte) 0xCD, (byte) 0xEF };


    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     */
    public static String encode(String key, String data) {
        return encode(key, data.getBytes());
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     */
    public static String encode(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(DESIV);


            cipher.init(Cipher.ENCRYPT_MODE, getRawKey(key), iv);
            byte[] bytes = cipher.doFinal(data);

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }


    // 对密钥进行处理
    private static Key getRawKey(String key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }





    /**
     * 获取编码后的值
     *
     * @param key
     * @param data
     * @return
     */
    public static String decode(String key, String data) {
        return decode(key, Base64.decode(data, Base64.DEFAULT));
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     */
    public static String decode(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(DESIV);
            cipher.init(Cipher.DECRYPT_MODE, getRawKey(key), iv);
            byte[] original = cipher.doFinal(data);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            return null;
        }
    }




}
