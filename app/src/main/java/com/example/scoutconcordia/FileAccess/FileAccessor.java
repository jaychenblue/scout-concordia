package com.example.scoutconcordia.FileAccess;

import android.content.res.Resources;
import android.util.Log;

import com.example.scoutconcordia.DataStructures.LinkedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class FileAccessor
{
    DES encryptionAccesser;
    String fileName;
    
    public FileAccessor()
    {
        encryptionAccesser = new DES();
        fileName = null;
    }
    
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
    // Used first to be able to read the file
    public void decryptFile()
    {
        InputStream readme = null;
        OutputStream writeToMe = null;
        try
        {
            readme = encryptionAccesser.getResources().openRawResource(
            encryptionAccesser.getResources().getIdentifier(fileName, "raw", encryptionAccesser.getPackageName()));
            writeToMe = new FileOutputStream(new File(encryptionAccesser.getFilesDir().getAbsoluteFile(), fileName));
            DES.decryptFile(readme, writeToMe);
        }
        catch (FileNotFoundException fnf)
        {
            Log.println(Log.WARN, "FileAccessor", "The output file was moved during the transfer");
        }
        catch (Resources.NotFoundException e)
        {
            Log.println(Log.WARN, "FileAccessor", "The input file could not be located");
        }
    }
    
    // returns an array with every line as a string from the file
    public Object[] obtainContents()
    {
        LinkedList<String> contents = new LinkedList<String>("");
        Scanner reader = null;
        try
        {
            reader = new Scanner(new File(encryptionAccesser.getFilesDir().getAbsoluteFile(), fileName));
            while (reader.hasNextLine())
            {
                contents.add(reader.nextLine());
            }
        }
        catch (FileNotFoundException fnf)
        {
            Log.println(Log.WARN, "FileAccessor", "The file has not yet been decrypted");
        }
        return contents.toArray();
    }
    
    // used when done to make sure no information is potentially leaked
    public boolean closeFile()
    {
        File deleteMe = new File(encryptionAccesser.getFilesDir().getAbsoluteFile(), fileName);
        return deleteMe.delete();
    }
}
