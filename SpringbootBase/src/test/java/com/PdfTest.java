package com;

import com.lyf.util.PdfUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class PdfTest {

    String[] arr = {  "109320199","张馨月" };

    @Test
    public void pdf() throws IOException {
        ByteArrayOutputStream baos = null;
        FileOutputStream out = null;
        try {
            for (int i = 0; i < arr.length; i+=2) {
                Map<String,Object> data = new HashMap<>();
                data.put("name", arr[i+1]);
                baos = PdfUtil.createPDF(data, "zhengshu.ftl");
                String fileName = arr[i]+".pdf";
                File file = new File(fileName);
                out = new FileOutputStream(file);
                baos.writeTo(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(baos!=null){
                baos.close();
            }
            if(out != null){
                out.close();
            }
        }
    }

}
