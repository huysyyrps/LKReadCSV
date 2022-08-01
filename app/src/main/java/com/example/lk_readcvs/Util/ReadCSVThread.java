package com.example.lk_readcvs.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ReadCSVThread extends Thread {
    String fileName;
    String folder;
    ReadCSVCallBack readCSVCallBack;
    public ReadCSVThread(String folder, String fileName, ReadCSVCallBack readCSVCallBack) {
        this.folder = folder;
        this.fileName = fileName;
        this.readCSVCallBack = readCSVCallBack;
    }
    @Override
    public void run() {
//        File inFile = new File(folder + fileName);
        File inFile = new File(folder , fileName);
        if (inFile.exists()){
            Log.e("XXX","11111");
        }
        List<String> listData = new ArrayList<>();
        String inString;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "GBK"));
            while ((inString = reader.readLine()) != null) {
                listData.add(URLDecoder.decode(inString, "UTF-8"));
//                sBuffer.append(inString);
//                Log.e("line -->",inString);
            }
            readCSVCallBack.success(listData);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            readCSVCallBack.fail();
        }
    }
}