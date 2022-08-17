package com.example.lk_readcvs.Util;

import android.text.format.DateUtils;
import android.widget.Toast;

import com.example.lk_readcvs.MyApplication;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CreateKml {
    public void createKml(String filePath, Task task) {
        Element root = DocumentHelper.createElement("kml");  //根节点是kml
        Document document = DocumentHelper.createDocument(root);
        document.setXMLEncoding("UTF-8");
        //给根节点kml添加属性
        root.addNamespace("xmlns", "http://www.opengis.net/kml/2.2");
        root.addNamespace("gx", "http://www.google.com/kml/ext/2.2");
        //给根节点kml添加子节点  Document
        Element documentElement = root.addElement("Document");
        Element folderDe = documentElement.addElement("Folder");
        Element PlacemarkDe = folderDe.addElement("Placemark");

        Element styleE = PlacemarkDe.addElement("Style");
        Element LineStyle = styleE.addElement("LineStyle");
        LineStyle.addElement("color").addText("ed0000ff");
        LineStyle.addElement("width").addText("5");

        List<TrackPoint> tps = task.getTrackPointList();
        Element trackE = PlacemarkDe.addElement("gx:Track");

        for (TrackPoint tp : tps) {
            trackE.addElement("gx:coord").addText(tp.getLongitude() + " " + tp.getLatitude() + " " + tp.getAltitude());
//            trackE.addElement("when").setText(DateUtils.specialDateString(tp.getTime()));
            trackE.addElement("when").setText(tp.getTime());
        }
        try {
            Writer fileWriter = new FileWriter(filePath);
            //换行
            OutputFormat format = new OutputFormat();
            format.setEncoding("UTF-8");
            format.setNewlines(true);
            // 生成缩进
            format.setIndent(true);
            //dom4j提供了专门写入文件的对象XMLWriter
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
            Toast.makeText(MyApplication.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MyApplication.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
