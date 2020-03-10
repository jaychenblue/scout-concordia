package com.example.scoutconcordia;

import android.util.Log;

import com.google.android.gms.common.util.Hex;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class DES {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static KeyGenerator keygenerator;
    static {
        try {
            keygenerator = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    final private static SecretKey myDesKey = keygenerator.generateKey();


    public static void decryptFile(InputStream readFromMe, OutputStream writeToMe)
    {
        Scanner reader = null;
        PrintWriter writer = null;
        try
        {
            //KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            //SecretKey myDesKey = keygenerator.generateKey();
            Cipher desCipher = Cipher.getInstance("DES");
            reader = new Scanner(readFromMe);
            writer = new PrintWriter(writeToMe);

            while (reader.hasNextLine())
            {
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                String decryptMe = reader.nextLine();

                byte[] textDecrypted = desCipher.doFinal(decryptMe.getBytes("UTF8"));
                writer.println(textDecrypted);

                //byte[] enc = android.util.Base64.decode(decryptMe, android.util.Base64.DEFAULT);
                //byte[] textDecrypted = desCipher.doFinal(enc);
                //byte[] textDecrypted = desCipher.doFinal(decryptMe.getBytes("UTF8"));  //original
                //String writeMe = new String(textDecrypted);
                //writer.println(writeMe);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.println(Log.WARN, "decrypting", "An Exception occured");
        }
        finally
        {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
        }
    }

    public static void encryptFile(InputStream readFromMe, OutputStream writeToMe)
    {
        Scanner reader = null;
        PrintWriter writer = null;
        try
        {
            //KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            //SecretKey myDesKey = keygenerator.generateKey();
            Cipher desCipher = Cipher.getInstance("DES");
            reader = new Scanner(readFromMe);
            writer = new PrintWriter(writeToMe);

            while (reader.hasNextLine())
            {
                //String encryptMe = reader.nextLine();
                //byte[] text = encryptMe.getBytes("UTF8");
                //desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                //byte[] textEncrypted = desCipher.doFinal(text);
                //String writeMe = new String(textEncrypted);
                //writer.println(writeMe);

                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                String encryptMe = reader.nextLine();
                byte[] textEncrypted = desCipher.doFinal(encryptMe.getBytes("UTF-8"));
                writer.println(bytesToHex(textEncrypted));
            }
        }
        catch(Exception e)
        {
            Log.println(Log.WARN, "decrypting", "An Exception occured");
        }
        finally
        {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToByte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
