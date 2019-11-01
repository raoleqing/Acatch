package catc.tiandao.com.match.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 格试转换
 **/
public class StringEscapeUtils {

	public static String HTMLDecode(String escapeStr) {

		String str = "";
		
		try {

			if (escapeStr != null && !escapeStr.equals("")) {
				str = URLDecoder.decode(escapeStr, "utf-8");
			}

			return str;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";

	}

}
