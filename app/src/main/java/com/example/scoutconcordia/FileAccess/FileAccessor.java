package com.example.scoutconcordia.FileAccess;

import android.content.res.Resources;
import android.util.Log;
import com.example.scoutconcordia.DataStructures.LinkedList;
import java.io.InputStream;
import java.util.Scanner;

/** Class used to read and access files from the res/raw/ folder. */
public class FileAccessor
{
    InputStream fileInput;
    Object[] contents;

    /** Default constructor that initializes the fileInput and its contents to null. */
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

    /** Used to decrypt a file from the input stream
     * @param isEncrypted Boolean signaling if the file being used as input is encrypted or not.
     */
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

    /** Obtains the contents of the input stream and returns them as an array of strings.
     * @return An array of strings representing the contents of the file.
     */
    public String[] obtainContents()
    {
        String[] returnMe = new String[contents.length];
        for (int i = 0; i < returnMe.length; i++)
        {
            returnMe[i] = (String)contents[i];
        }
        return returnMe;
    }
    
    /** This method is used when done to make sure no information is potentially leaked. */
    public void resetObject()
    {
        fileInput = null;
        contents = null;
    }
}