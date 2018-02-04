package com.huruwo.mapview.mapview;



/**
 * @author: liuwan
 * @date: 2018-01-26
 * @action:
 */
public class DaoHepler {


    public static DaoSession getAppDao() {
        // 创建数据
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(AppAplication.getContext(), "zte.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();

        return daoSession;
    }
}
