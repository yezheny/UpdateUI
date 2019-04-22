package example.glh.updateui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.codec.binary.Base64;


public class StringUtils {

	public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

	public static String formatDouble(double d, DecimalFormat decimalFormat) {
		double temp = d;
		BigDecimal bd = new BigDecimal(temp);
		temp = bd.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return decimalFormat.format(temp);
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null || str2 == null)
			return false;
		return str1.equals(str2);
	}

	public static boolean isContains(String str1, String str2) {

		return str1.contains(str2);

	}

	public static String deleteLast(String str1) {
		return str1.substring(0, str1.length() - 1);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null || str2 == null)
			return false;
		return str1.equalsIgnoreCase(str2);
	}

	public static String[] getSplit(String str, String cha) {
		return str.split(cha);
	}

	public static String formatFloat(float f, DecimalFormat decimalFormat) {
		float temp = f;
		BigDecimal bd = new BigDecimal(f);
		temp = bd.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
		return decimalFormat.format(temp);
	}

	public static String formatDouble(double d) {
		return formatDouble(d, new DecimalFormat("###0.0"));
	}

	public static String formatFloat(float f) {
		return formatFloat(f, new DecimalFormat("###0.0"));
	}

	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");
			s = s.replaceAll("[.]$", "");
		}
		return s;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String stringToBase64(String str) {
		String strBase64 = Base64
				.encodeToString(str.getBytes(), Base64.NO_WRAP);// (bytes,Base64.DEFAULT);
		return strBase64;
	}
	
	public static Bitmap Base64ToImage(String strImage) throws Exception {
		
		
		byte[]  encodeBase64 = Base64.decode(strImage.getBytes("UTF-8"), Base64.NO_WRAP);
		 
		return Bytes2Bimap(encodeBase64);
	}
	
	public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

	public static String strBase64ToStr(String strBase64) {
		String str = new String(Base64.decode(strBase64.getBytes(),
				Base64.DEFAULT));// (bytes,Base64.DEFAULT);
		return str;
	}

	public static boolean getIndexOf(String str1, String str2){
        return str1.indexOf(str2) != -1;
	}

	/**
	 * 加密
	 * @param res
	 * @param secret
     * @return
     */
	public static String encrypt(String res, char secret) {
		char[] resChar = res.toCharArray();
		for(int i = 0; i < resChar.length; i++) {
			resChar[i] = (char)(resChar[i]^secret); //通过异或运算进行加密
		}

		return resChar.toString();
	}

	/**
	 * 解密
	 * @param res
	 * @param secret
     * @return
     */
	public static String uncrypt(String res, char secret) {
		char[] resChar = res.toCharArray();
		for(int i = 0; i < resChar.length; i++) {
			resChar[i] = (char)(resChar[i]^secret); //通过异或运算进行加密
		}

		return resChar.toString();
	}

	public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//确定计算方法
		MessageDigest md5= MessageDigest.getInstance("MD5");
		/*BASE64Encoder base64en = new BASE64Encoder();
		//加密后的字符串
		String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));*/
		String newstr = Base64.encodeToString(md5.digest(str.getBytes("utf-8")), Base64.NO_WRAP);
		return newstr;
	}
}
