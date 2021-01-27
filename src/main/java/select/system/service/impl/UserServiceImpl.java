package select.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import select.base.Constants;
import select.base.Result;
import select.constants.BaseEnums;
import select.system.dao.UserMapper;
import select.system.dto.User;
import select.system.service.UserService;
import select.util.PageBean;
import select.util.Results;
import select.util.TokenUtil;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author yeyuting
 * @create 2021/1/25
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper ;

    @Autowired
    TokenUtil tokenUtil ;

    public User selectByName(String username) {
      return  userMapper.selectByName(username) ;
    }

    public User selectById(int id){
        return userMapper.selectById(id) ;
    }

    public List<User> selectAll(){
        return userMapper.selectAll() ;
    }

    public boolean insertOne(User user) {
        return userMapper.insertOne(user) ;
    }

    public boolean insertMany(List<User> userList) {
        return userMapper.insertMany(userList) ;
    }
    public boolean updateOne(User user){
        return userMapper.updateOne(user) ;
    }

    public boolean deleteById(int id){
        return userMapper.deleteById(id) ;
    }

    public List<User> SelectByStartIndexAndPageSize(int startIndex , int pageSize) {
        return userMapper.SelectByStartIndexAndPageSize(startIndex,pageSize) ;
    }

    public List<User> selectByMap(Map<String ,Object> map){
        return userMapper.selectByMap(map) ;
    }

    public List<User> SelectByPageBean(PageBean pageBean) {
        return userMapper.SelectByPageBean(pageBean) ;
    }

    public List<User> selectByLike(Map<String , Object> map){
        return userMapper.selectByLike(map) ;
    }

    public Result loginCheck(User user , HttpServletResponse response){
        User user1 = userMapper.selectByName(user.getUserName()) ;
        if(user1 == null ){
            return Results.failure("用户不存在") ;
        }else if(!user1.getPassword().equals(user.getPassword())){
            return Results.failure("密码输入错误！") ;
        }
        String token = tokenUtil.generateToken(user1) ;
        System.out.println("token:" + token);
        Jedis jedis = new Jedis("localhost" , 6379) ;
        jedis.set(user1.getUserName() , token) ;
        jedis.expire(user1.getUserName() , Constants.TOKEN_EXPIRED_TIME) ;
        jedis.set(token , user1.getUserName()) ;
        jedis.expire(token , Constants.TOKEN_EXPIRED_TIME) ;
        Long currentTime = System.currentTimeMillis() ;
        jedis.set(user1.getUserName()+token ,currentTime.toString()) ;
        System.out.println("---"+ jedis.get(user1.getUserName()));
        System.out.println("--"+ jedis.get(token));

        jedis.close();
        return Results.successWithData(user1);
    }



}
