package com.huruwo.mapview.mapview;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * @author: liuwan
 * @date: 2018-02-01
 * @action:
 */
@Entity
public class MapBean {

    @Id(autoincrement = true) // id自增长
    private Long id;

    @Unique
    private String map_name;

    private float  map_w;
    private float  map_h;


    @ToMany(referencedJoinProperty = "mapid")
    private List<PathItem> pathItems;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1184987114)
    private transient MapBeanDao myDao;
    
    @Generated(hash = 361777663)
    public MapBean(Long id, String map_name, float map_w, float map_h) {
        this.id = id;
        this.map_name = map_name;
        this.map_w = map_w;
        this.map_h = map_h;
    }
    @Generated(hash = 412228366)
    public MapBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMap_name() {
        return this.map_name;
    }
    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }
    public float getMap_w() {
        return this.map_w;
    }
    public void setMap_w(float map_w) {
        this.map_w = map_w;
    }
    public float getMap_h() {
        return this.map_h;
    }
    public void setMap_h(float map_h) {
        this.map_h = map_h;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 54088814)
    public List<PathItem> getPathItems() {
        if (pathItems == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PathItemDao targetDao = daoSession.getPathItemDao();
            List<PathItem> pathItemsNew = targetDao._queryMapBean_PathItems(id);
            synchronized (this) {
                if (pathItems == null) {
                    pathItems = pathItemsNew;
                }
            }
        }
        return pathItems;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1420568320)
    public synchronized void resetPathItems() {
        pathItems = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1841862255)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapBeanDao() : null;
    }

}
