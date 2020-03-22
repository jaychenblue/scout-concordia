package com.example.scoutconcordia.FileAccess;

import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    
    // used when done to make sure no information is potentially leaked
    public boolean closeFile()
    {
        File deleteMe = new File(encryptionAccesser.getFilesDir().getAbsoluteFile(), fileName);
        return deleteMe.delete();
    }
}
