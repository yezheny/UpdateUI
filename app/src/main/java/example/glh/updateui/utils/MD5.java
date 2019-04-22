package example.glh.updateui.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5 {
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 得到参数加密后的MD5值
     *
     * @return 32byte MD5 Value
     */
//    public static String getMD5(String inStr) {
//        byte[] inStrBytes = inStr.getBytes();
//        try {
//            MessageDigest MD = MessageDigest.getInstance("MD5");
//            MD.update(inStrBytes);
//            byte[] mdByte = MD.digest();
//            char[] str = new char[mdByte.length * 2];
//            int k = 0;
//            for (int i = 0; i < mdByte.length; i++) {
//                byte temp = mdByte[i];
//                str[k++] = hexDigits[temp >>> 4 & 0xf];
//                str[k++] = hexDigits[temp & 0xf];
//            }
//            return new String(str);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String getMD5(String info)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

    //密码加密 与php加密一致
    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) {
                result = "0" + result;
            }
        }
        return result;
    }

    /**
     * 字符串转MD5
     * 与上面的方法实现方式有所不同（采用StringBuffer，对传过来的str直接修改，易于数据的加密，防止监听内存）
     *
     * @param str
     * @return str
     */
    public static String Md5(String str) {
        if (str != null && !str.equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(hexDigits[(md5Byte[i] & 0xff) / 16]);
                    sb.append(hexDigits[(md5Byte[i] & 0xff) % 16]);
                }
                str = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * 获取随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    public static String toMD5(String source) {
        MessageDigest md;
        StringBuffer sb = new StringBuffer();
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte[] data = md.digest();
            int index;
            for (byte b : data) {
                index = b;
                if (index < 0) index += 256;
                if (index < 16) sb.append("0");
                sb.append(Integer.toHexString(index));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}

