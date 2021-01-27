package select.system.dto;

/**
 * @author yeyuting
 * @create 2021/1/25
 */

public class User {
    int id ;
    String userName ;
    String password ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
