package com.example.scoutconcordia.FileAccess;

import android.content.res.Resources;
import android.util.Log;

import com.example.scoutconcordia.Activities.MapsActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileAccesser
{
    DES encryptionAccesser;
    
    public FileAccesser()
    {
        encryptionAccesser = new DES();
    }
    
}
