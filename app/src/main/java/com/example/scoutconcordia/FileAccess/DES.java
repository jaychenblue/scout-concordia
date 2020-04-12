package com.example.scoutconcordia.FileAccess;

import android.util.Log;

import com.example.scoutconcordia.DataStructures.LinkedList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DES
{
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    //private static KeyGenerator keygenerator;
    //static {
    //    try {
    //        keygenerator = KeyGenerator.getInstance("DES");
    //    } catch (NoSuchAlgorithmException e) {
    //        e.printStackTrace();
    //    }
    //}
    //final private static SecretKey myDesKey = keygenerator.generateKey();

    private static String desKey;
    private static byte[] keyBytes;
    private static SecretKeyFactory factory;
    private static SecretKey myDesKey;

    DES() {
        this.desKey = "0123456789abcdef";
        this.keyBytes = hexToByte(this.desKey);
        {
            try {
                this.factory = SecretKeyFactory.getInstance("DES");
                this.myDesKey = this.factory.generateSecret(new DESKeySpec(this.keyBytes));
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
    }

    /** Method for decrypting a file. Requires an input stream and an output stream **/
    public Object[] decryptFile(InputStream readFromMe)
    {
        Scanner reader = null;
        LinkedList<String> returnMe = new LinkedList<String>("");
        try
        {
            Cipher desCipher = Cipher.getInstance("DES");
            reader = new Scanner(readFromMe);

            // reads from the input file and outputs decrypted text to the output file
            while (reader.hasNextLine())
            {
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                String decryptMe = reader.nextLine();

                byte[] textDecrypted = desCipher.doFinal(hexToByte(decryptMe));
                returnMe.add(new String(textDecrypted));
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
        }
        return returnMe.toArray();
    }

//    /** Method for encrypting a file. Requires an input stream and an output stream **/
//    public void encryptFile(InputStream readFromMe, OutputStream writeToMe)
//    {
//        Scanner reader = null;
//        PrintWriter writer = null;
//        try
//        {
//            Cipher desCipher = Cipher.getInstance("DES");
//            reader = new Scanner(readFromMe);
//            writer = new PrintWriter(writeToMe);
//
//            // reads from the input file and outputs encrypted text to the output file
//            while (reader.hasNextLine())
//            {
//                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
//                String encryptMe = reader.nextLine();
//                byte[] textEncrypted = desCipher.doFinal(encryptMe.getBytes("UTF-8"));
//                writer.println(bytesToHex(textEncrypted));
//            }
//        }
//        catch(Exception e)
//        {
//            Log.println(Log.WARN, "decrypting", "An Exception occured");
//        }
//        finally
//        {
//            if (reader != null)
//                reader.close();
//            if (writer != null)
//                writer.close();
//        }
//    }

    /** Helper method for converting an array of bytes to hex characters **/
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /** Helper method for converting hex characters to an array of bytes **/
    private static byte[] hexToByte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
