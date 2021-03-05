package com.example.storage.modules.sys.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * SHA1加密算法
 *
 * @author arvin 采用新浪微博加密算法
 */
public class CryptoUtils {
    private final static int[] ABCDE = {0x67452301, 0xefcdab89, 0x98badcfe,
            0x10325476, 0xc3d2e1f0};
    // 摘要数据存储数组
    private static int[] digestInt = new int[5];
    // 计算过程中的临时数据存储数组
    private static int[] tmpData = new int[80];

    // 计算sha-1摘要
    private static int process_input_bytes(byte[] bytedata) {
        // 初试化常量
        System.arraycopy(ABCDE, 0, digestInt, 0, ABCDE.length);
        // 格式化输入字节数组，补10及长度数据
        byte[] newbyte = byteArrayFormatData(bytedata);
        // 获取数据摘要计算的数据单元个数
        int MCount = newbyte.length / 64;
        // 循环对每个数据单元进行摘要计算
        for (int pos = 0; pos < MCount; pos++) {
            // 将每个单元的数据转换成16个整型数据，并保存到tmpData的前16个数组元素中
            for (int j = 0; j < 16; j++) {
                tmpData[j] = byteArrayToInt(newbyte, (pos * 64) + (j * 4));
            }
            // 摘要计算函数
            encrypt();
        }
        return 20;
    }

    // 格式化输入字节数组格式
    private static byte[] byteArrayFormatData(byte[] bytedata) {
        // 补0数量
        int zeros = 0;
        // 补位后总位数
        int size = 0;
        // 原始数据长度
        int n = bytedata.length;
        // 模64后的剩余位数
        int m = n % 64;
        // 计算添加0的个数以及添加10后的总长度
        if (m < 56) {
            zeros = 55 - m;
            size = n - m + 64;
        } else if (m == 56) {
            zeros = 63;
            size = n + 8 + 64;
        } else {
            zeros = 63 - m + 56;
            size = (n + 64) - m + 64;
        }
        // 补位后生成的新数组内容
        byte[] newbyte = new byte[size];
        // 复制数组的前面部分
        System.arraycopy(bytedata, 0, newbyte, 0, n);
        // 获得数组Append数据元素的位置
        int l = n;
        // 补1操作
        newbyte[l++] = (byte) 0x80;
        // 补0操作
        for (int i = 0; i < zeros; i++) {
            newbyte[l++] = (byte) 0x00;
        }
        // 计算数据长度，补数据长度位共8字节，长整型
        long N = (long) n * 8;
        byte h8 = (byte) (N & 0xFF);
        byte h7 = (byte) ((N >> 8) & 0xFF);
        byte h6 = (byte) ((N >> 16) & 0xFF);
        byte h5 = (byte) ((N >> 24) & 0xFF);
        byte h4 = (byte) ((N >> 32) & 0xFF);
        byte h3 = (byte) ((N >> 40) & 0xFF);
        byte h2 = (byte) ((N >> 48) & 0xFF);
        byte h1 = (byte) (N >> 56);
        newbyte[l++] = h1;
        newbyte[l++] = h2;
        newbyte[l++] = h3;
        newbyte[l++] = h4;
        newbyte[l++] = h5;
        newbyte[l++] = h6;
        newbyte[l++] = h7;
        newbyte[l++] = h8;
        return newbyte;
    }

    private static int f1(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    private static int f2(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private static int f3(int x, int y, int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private static int f4(int x, int y) {
        return (x << y) | x >>> (32 - y);
    }

    // 单元摘要计算函数
    private static void encrypt() {
        for (int i = 16; i <= 79; i++) {
            tmpData[i] = f4(tmpData[i - 3] ^ tmpData[i - 8] ^ tmpData[i - 14]
                    ^ tmpData[i - 16], 1);
        }
        int[] tmpABCDE = new int[5];
        for (int i1 = 0; i1 < tmpABCDE.length; i1++) {
            tmpABCDE[i1] = digestInt[i1];
        }
        for (int j = 0; j <= 19; j++) {
            int tmp = f4(tmpABCDE[0], 5)
                    + f1(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3]) + tmpABCDE[4]
                    + tmpData[j] + 0x5a827999;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = f4(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
        for (int k = 20; k <= 39; k++) {
            int tmp = f4(tmpABCDE[0], 5)
                    + f2(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3]) + tmpABCDE[4]
                    + tmpData[k] + 0x6ed9eba1;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = f4(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
        for (int l = 40; l <= 59; l++) {
            int tmp = f4(tmpABCDE[0], 5)
                    + f3(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3]) + tmpABCDE[4]
                    + tmpData[l] + 0x8f1bbcdc;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = f4(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
        for (int m = 60; m <= 79; m++) {
            int tmp = f4(tmpABCDE[0], 5)
                    + f2(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3]) + tmpABCDE[4]
                    + tmpData[m] + 0xca62c1d6;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = f4(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
        for (int i2 = 0; i2 < tmpABCDE.length; i2++) {
            digestInt[i2] = digestInt[i2] + tmpABCDE[i2];
        }
        for (int n = 0; n < tmpData.length; n++) {
            tmpData[n] = 0;
        }
    }

    // 4字节数组转换为整数
    private static int byteArrayToInt(byte[] bytedata, int i) {
        return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16)
                | ((bytedata[i + 2] & 0xff) << 8) | (bytedata[i + 3] & 0xff);
    }

    // 整数转换为4字节数组
    private static void intToByteArray(int intValue, byte[] byteData, int i) {
        byteData[i] = (byte) (intValue >>> 24);
        byteData[i + 1] = (byte) (intValue >>> 16);
        byteData[i + 2] = (byte) (intValue >>> 8);
        byteData[i + 3] = (byte) intValue;
    }

    // 将字节转换为十六进制字符串
    private static String byteToHexString(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    // 将字节数组转换为十六进制字符串
    private static String byteArrayToHexString(byte[] bytearray) {
        String strDigest = "";
        for (int i = 0; i < bytearray.length; i++) {
            strDigest += byteToHexString(bytearray[i]);
        }
        return strDigest;
    }

    // 计算sha-1摘要，返回相应的字节数组
    public static byte[] getDigestOfBytes(byte[] byteData) {
        process_input_bytes(byteData);
        byte[] digest = new byte[20];
        for (int i = 0; i < digestInt.length; i++) {
            intToByteArray(digestInt[i], digest, i * 4);
        }
        return digest;
    }

    // 传入字符串，计算sha-1摘要，返回相应的十六进制字符串
    public static String getDigestOfString(String data) {
        return byteArrayToHexString(getDigestOfBytes(data.getBytes()));
    }

    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String hashPassword(String plainPassword) {
        byte[] salt = RandomUtils.nextBytes(SALT_SIZE);
        PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, HASH_INTERATIONS, 256);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Hex.encodeHexString(ArrayUtils.addAll(salt, hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param hash          密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String hash) {
        try {
            byte[] raw = Hex.decodeHex(hash.toCharArray());
            byte[] salt = ArrayUtils.subarray(raw, 0, SALT_SIZE);
            PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, HASH_INTERATIONS, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] recalcHash = skf.generateSecret(spec).getEncoded();
            return MessageDigest.isEqual(ArrayUtils.subarray(raw, SALT_SIZE, raw.length), recalcHash);
        } catch (DecoderException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (InvalidKeySpecException e) {
            return false;
        }
    }

}