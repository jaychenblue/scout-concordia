package com.example.scoutconcordia;

import com.example.scoutconcordia.FileAccess.FileAccessor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileAccessorTest
{

    FileAccessor fileAccessor;
    byte[] data;
    InputStream input;

    @BeforeEach
    public void beforeEach() throws Exception
    {
        fileAccessor = new FileAccessor();
        data = "1,2,3,4,5".getBytes();
        input = new ByteArrayInputStream(data);
    }

    @AfterEach
    public void afterEach() throws Exception
    {
        fileAccessor = null;
        data = null;
        input = null;
    }

    @Test
    public void testObtainContents()
    {
        fileAccessor.setInputStream(input);
        fileAccessor.decryptFile(false);
        assertNotNull(fileAccessor.obtainContents());
        assertEquals("1,2,3,4,5", fileAccessor.obtainContents()[0]);
    }
}
