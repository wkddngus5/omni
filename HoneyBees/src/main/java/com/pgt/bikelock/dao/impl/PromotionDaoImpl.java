package com.pgt.bikelock.dao.impl;

import com.pgt.bikelock.dao.IPromotionDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.PromotionVO;

import java.sql.*;
import java.util.*;


public class PromotionDaoImpl implements IPromotionDao {

    @Override
    public boolean insert(PromotionVO promotion) {

        Connection conn = DataSourceUtil.getConnection();
        String sql="insert INTO t_promotion (city_id,policy,is_activated,created,updated) values (?,?,?,?,?) ";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String promotionId = null;
        String id = null;
        try {
            pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, promotion.getCityId());
            pstmt.setString(2,promotion.getPolicy());
            pstmt.setBoolean(3, promotion.getIsActivated());
            pstmt.setLong(4, promotion.getCreated().getTime() / 1000);
            pstmt.setLong(5, promotion.getUpdated().getTime() / 1000);
            if(pstmt.executeUpdate()>0){
                rs= pstmt.getGeneratedKeys();
                if(rs.next()){
                    id = rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DataSourceUtil.close(pstmt, conn);
        }
        return id == null;
    }

    @Override
    public PromotionVO findById(String id) {
        Connection conn = DataSourceUtil.getConnection();
        String sql="SELECT * FROM t_promotion where id = ?" ;
        PromotionVO  promotion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            while(rs.next()){
                promotion = new PromotionVO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DataSourceUtil.close(rs,pstmt, conn);
        }
        return promotion;
    }

    @Override
	public PromotionVO findByCityId(String  cityId) {
        Connection conn = DataSourceUtil.getConnection();
        String sql="SELECT * FROM t_promotion where city_id = ? and is_activated = ?" ;
        PromotionVO  promotion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cityId);
            pstmt.setInt(2, 1);
            rs = pstmt.executeQuery();
            while(rs.next()){
                promotion = new PromotionVO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DataSourceUtil.close(rs,pstmt, conn);
        }
        return promotion;
    }


    @Override
    public boolean update(String id, String policy, Boolean isActivated) {
        Connection conn = DataSourceUtil.getConnection();
        String sql="update t_promotion set policy = ?,is_activated = ?,updated= ? where id = ? ";

        PreparedStatement pstmt = null;
        boolean flag = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, policy);
            pstmt.setBoolean(2,isActivated);
            pstmt.setLong(3, (new java.util.Date()).getTime()/1000);
            if(pstmt.executeUpdate()>0) flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DataSourceUtil.close(pstmt, conn);
        }
        return flag;
    }

    @Override
    public boolean delete(String id) {
        // TODO Auto-generated method stub
        Connection conn = DataSourceUtil.getConnection();
        String sql="delete from t_promotion where id = ? ";

        PreparedStatement pstmt = null;
        boolean flag = false;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            if(pstmt.executeUpdate()>0) flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            DataSourceUtil.close(pstmt, conn);
        }
        return flag;
    }
}
