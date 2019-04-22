package example.glh.updateui.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public class GetSign {

    private static String sign;

    public static String getSign() {

        Date date = new Date();
        long str = date.getTime();
        String num = getRandomString(5);
        String s = "API595654866dklnULdnld";

        try {
            sign = MD5.md5(SHA1.encode(str + num + s));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // DebugFlags.logD("签名"+str+"=="+num+"=="+sign);

        return str + "," + num + "," + sign;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 使用sha-1方式进行加密
     *
     * @return
     */
    public static String digest(String content) {
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest msgDitest = MessageDigest.getInstance("SHA-1");
            msgDitest.update(content.getBytes());
            byte[] digests = msgDitest.digest();
            //将每个字节转为16进制
            for (int i = 0; i < digests.length; i++) {
                builder.append(Integer.toHexString(digests[i] & 0xff + 8));//+8为加盐操作
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
