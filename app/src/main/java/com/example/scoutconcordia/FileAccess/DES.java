package com.example.scoutconcordia.FileAccess;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class DES extends FragmentActivity
{
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
    
    public DES() { }

    /** Method for decrypting a file. Requires an input stream and an output stream **/
    public static void decryptFile(InputStream readFromMe, OutputStream writeToMe)
    {
        Scanner reader = null;
        PrintWriter writer = null;
        try
        {
            Cipher desCipher = Cipher.getInstance("DES");
            reader = new Scanner(readFromMe);
            writer = new PrintWriter(writeToMe);

            // reads from the input file and outputs decrypted text to the output file
            while (reader.hasNextLine())
            {
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                String decryptMe = reader.nextLine();

                byte[] textDecrypted = desCipher.doFinal(hexToByte(decryptMe));
                writer.println(new String(textDecrypted));
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

    /** Method for encrypting a file. Requires an input stream and an output stream **/
    public static void encryptFile(InputStream readFromMe, OutputStream writeToMe)
    {
        Scanner reader = null;
        PrintWriter writer = null;
        try
        {
            Cipher desCipher = Cipher.getInstance("DES");
            reader = new Scanner(readFromMe);
            writer = new PrintWriter(writeToMe);

            // reads from the input file and outputs encrypted text to the output file
            while (reader.hasNextLine())
            {
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
