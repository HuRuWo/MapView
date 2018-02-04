package com.huruwo.mapview.mapview;

import android.content.Context;
import android.support.annotation.RawRes;
import android.util.Log;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author: liuwan
 * @date: 2018-02-01
 * @action:
 */
public class XmlUtil {


    public static List<PathItem> getXmlValue(Context context, @RawRes final int raw_res, String map_name) {


        List<PathItem> pathlists = new ArrayList<>();

        try {
            // DocumentBuilder对象

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            //打开输入流
            InputStream is = context.getResources().openRawResource(raw_res);


            // 获取文档对象
            Document doc = db.parse(is);

            //根节点
            Element element = doc.getDocumentElement();

            //Log.e("android:viewportWidth",element.getAttribute("android:viewportWidth"));
            //Log.e("android:viewportHeight",element.getAttribute("android:viewportHeight"));

          float  map_w = Float.valueOf(element.getAttribute("android:viewportWidth"));
          float  map_h = Float.valueOf(element.getAttribute("android:viewportHeight"));

            //获取path元素节点集合
            NodeList paths = doc.getElementsByTagName("path");


            MapBean mapBean = new MapBean(null, map_name, map_w, map_h);
            long id = DaoHepler.getAppDao().insert(mapBean);


            if (id > 0) {
                Log.e("item", "MapBean 新增成功");

                for (int i = 0; i < paths.getLength(); i++) {
                    // 取出每一个元素
                    Element node = (Element) paths.item(i);
                    //得到android:pathData属性值
                    // Log.e("android:pathData",personNode.getAttribute("android:pathData"));
                   // Log.e("android:fillColor", node.getAttribute("android:fillColor"));
//                Log.e("android:strokeColor",personNode.getAttribute("android:strokeColor"));
//                Log.e("android:strokeWidth",personNode.getAttribute("android:strokeWidth"));
                  //name
                    String pathValue = node.getAttribute("android:pathData");
                    String colorValue = node.getAttribute("android:fillColor");
                    String nameValue=node.getAttribute("android:name");

                    PathItem item = new PathItem(null, pathValue, false, nameValue, Constans.MAP_TYPE_CITY, colorValue,Constans.STATE_WELL, id);
                    item.setPath(PathParser.createPathFromPathData(pathValue));

                    long end = DaoHepler.getAppDao().insert(item);
                    if (end > 0) {
                        Log.e("item", "PathItem 新增成功");
                    } else {

                    }

                    pathlists.add(item);


                }
            }


        } catch (Exception e) {

        }

        return pathlists;
    }

}
