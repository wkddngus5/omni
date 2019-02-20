package com.pgt.bikelock.vo;

import com.pgt.bikelock.dao.IPromotionDao;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.PromotionDaoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class PromotionVO {

    private String id;
    private String cityId;
    private String policy;
    private Boolean isActivated;
    private Date created;
    private Date updated;



    public PromotionVO(){
    }

    public PromotionVO(ResultSet rst) throws SQLException {
        this.id = BaseDao.getString(rst, IPromotionDao.COLUMN_ID);
        this.cityId = BaseDao.getString(rst, IPromotionDao.COLUMN_CITY_ID);
        this.policy = BaseDao.getString(rst, IPromotionDao.COLUMN_POLICY);
        this.isActivated = BaseDao.getLong(rst, IPromotionDao.COLUMN_IS_ACTIVATED) == 1;
        this.created = BaseDao.getTimestamp(rst, IPromotionDao.COLUMN_CREATED);
        this.updated = BaseDao.getTimestamp(rst, IPromotionDao.COLUMN_UPDATED);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }


}
