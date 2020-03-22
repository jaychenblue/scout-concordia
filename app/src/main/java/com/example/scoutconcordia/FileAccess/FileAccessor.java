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
    
    public FileAccessor()
    {
        encryptionAccesser = new DES();
    }
    
    public void decryptFile(String fileName)
    {
        InputStream readme = null;
        OutputStream writeToMe = null;
        try
        {
            readme = encryptionAccesser.getResources().openRawResource(
            encryptionAccesser.getResources().getIdentifier(fileName, "raw", encryptionAccesser.getPackageName()));
            writeToMe = new FileOutputStream(new File(encryptionAccesser.getFilesDir().getAbsoluteFile(), fileName));
            //DES.encryptFile(readme, writeToMe);
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
}
