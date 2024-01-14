package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {
    private WhatsappRepository WR = new WhatsappRepository();

    public String createUser(String name, String mobile) throws Exception{
        return WR.createUser(name, mobile);
    }

    public Group createGroup(List<User> users){
        return WR.createGroup(users);
    }
    public int createMessage(String content){
        return WR.createMessage(content);
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{

        return WR.sendMessage(message, sender, group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{

        return WR.changeAdmin(approver, user, group);
    }
    public int removeUser(User user) throws Exception{

        return WR.removeUser(user);
    }
    public String findMessage(Date start, Date end, int K) throws Exception{

        return WR.findMessage(start, end, K);
    }


}
