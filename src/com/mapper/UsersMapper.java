package com.mapper;

import java.util.ArrayList;
import java.util.Map;

import com.po.Users;

public interface UsersMapper {

  /**
   * 添加用户信息
   * 
   * @param param
   */
  public void insertUsers(Map<String, Object> param);

  /**
   * 删除用户信息
   * 
   * @param id
   */
  public void deleteUsers(int id);

  /**
   * 修改用户信息
   * 
   * @param param
   */
  public void updateUsers(Map<String, Object> param);

  /**
   * 查询用户信息(后台)
   * 
   * @return ArrayList<Users>
   */
  public ArrayList<Users> searchUsers();

  /**
   * 根据用户名称查询用户信息(后台)
   * 
   * @return ArrayList<Users>
   */
  public ArrayList<Users> searchUsersByUsername(String username);

  /**
   * 根据编号查询用户信息
   * 
   * @param id
   * @return Users
   */
  public Users searchUsersById(int id);

  /**
   * 根据用户名和密码查询用户是否存在
   * 
   * @param param
   * @return Users
   */
  public Users getUserByUsernameAndPassword(Map<String, Object> param);

}