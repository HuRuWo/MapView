package com.huruwo.mapview.mapview;

import android.graphics.Path;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * @author: 蜗牛
 * @date: 2017/6/1 10:13
 * @desc:
 */
@Entity
public class PathItem {
    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXmlvalue() {
        return this.xmlvalue;
    }

    public void setXmlvalue(String xmlvalue) {
        this.xmlvalue = xmlvalue;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getMapid() {
        return this.mapid;
    }

    public void setMapid(long mapid) {
        this.mapid = mapid;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Id(autoincrement = true) // id自增长
    private Long id;

    @Transient
    private Path path;
    private String xmlvalue;
    private boolean isSelect;
    private String title;
    private int type;
    private String color;
    private int state;

    private long mapid; // 外键

    @Generated(hash = 128200156)
    public PathItem(Long id, String xmlvalue, boolean isSelect, String title,
            int type, String color, int state, long mapid) {
        this.id = id;
        this.xmlvalue = xmlvalue;
        this.isSelect = isSelect;
        this.title = title;
        this.type = type;
        this.color = color;
        this.state = state;
        this.mapid = mapid;
    }

    @Generated(hash = 420311859)
    public PathItem() {
    }

   
}
