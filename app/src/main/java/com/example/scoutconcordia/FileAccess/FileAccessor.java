package com.example.scoutconcordia.FileAccess;

import android.content.res.Resources;
import android.util.Log;

import com.example.scoutconcordia.DataStructures.LinkedList;

import java.io.InputStream;
import java.util.Scanner;

public class FileAccessor
{
    InputStream fileInput;
    Object[] contents;
    
    public FileAccessor()
    {
        fileInput = null;
        contents = null;
    }
    
    public void setInputStream(InputStream fileInput)
    {
        this.fileInput = fileInput;
    }
    
    // Used first to be able to read the file
    public void decryptFile(boolean isEncrypted)
    {
        DES decrypter = new DES();
        Scanner reader = null;
        InputStream readme = null;
        LinkedList<String> contents = new LinkedList<String>("");
        try
        {
            //writeToMe = new FileOutputStream(new File(MapsActivity.this.getFilesDir().getAbsoluteFile(), fileName));
            if (isEncrypted)
                this.contents = decrypter.decryptFile(fileInput);
            else
            {
                //readme = getResources().openRawResource(getResources().getIdentifier(fileName, "raw", getPackageName()));  // we can read the file directly
                reader = new Scanner(fileInput);
                while (reader.hasNextLine())
                {
                    contents.add(reader.nextLine());
                }
                this.contents = contents.toArray();
            }
        }
        catch (Resources.NotFoundException e)
        {
            Log.println(Log.WARN, "FileAccessor", "The input file could not be located");
        }
    }
    
    // returns an array with every line as a string from the file
    public String[] obtainContents()
    {
        String[] returnMe = new String[contents.length];
        for (int i = 0; i < returnMe.length; i++)
        {
            returnMe[i] = (String)contents[i];
        }
        return returnMe;
    }
    
    // used when done to make sure no information is potentially leaked
    public void resetObject()
    {
        fileInput = null;
        contents = null;
    }
}