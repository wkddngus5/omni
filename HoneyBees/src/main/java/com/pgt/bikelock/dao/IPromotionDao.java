package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.PromotionVO;


public interface IPromotionDao {
    String TABLE_NAME ="t_promotion";
    String COLUMN_ID="id";
    String COLUMN_CITY_ID="city_id";
    String COLUMN_CREATED ="created";
    String COLUMN_UPDATED ="updated";
    String COLUMN_POLICY ="policy";
    String COLUMN_IS_ACTIVATED ="is_activated";



    boolean insert(PromotionVO promotionVO);


    PromotionVO findById(String id);


    boolean update(String id, String policy, Boolean isActivated);


    boolean delete(String id);

    PromotionVO findByCityId(String  cityId);

}
