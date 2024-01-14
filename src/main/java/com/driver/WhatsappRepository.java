package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception{

        if(userMobile.contains(mobile)){
            throw new Exception ("User already Exist");
        }
        List<User> userList = new ArrayList<>();
        User user = new User(name, mobile);
        userList.add(user);
        return "SUCCESS";
    }
    public Group createGroup(List<User> users){
        List<User> listOfUser = users;
        User admin = listOfUser.get(0);
        String groupName = "";
        int noOfParticipants = listOfUser.size();
        if(listOfUser.size() == 2){
            groupName = listOfUser.get(1).getName();
        }else{
            customGroupCount += 1;
            groupName = "Group " +  customGroupCount;
        }

        Group group = new Group(groupName, noOfParticipants);
        adminMap.put(group, admin);
        groupUserMap.put(group,users);
        return group;
    }

    public int createMessage(String content){
        messageId += 1;
        Message message = new Message(messageId, content);
        return messageId;
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{

        if(adminMap.containsKey(group)){
            List<User> users = groupUserMap.get(group);
            boolean found = false;
            for(User user : users){
                if(user.equals(sender)) {
                    found = true;
                    break;
                }
            }
            if(found){
                senderMap.put(message, sender);
                if(groupUserMap.containsKey(group)){
                    if(groupMessageMap.get(group)!= null){
                        List<Message> messages = groupMessageMap.get(group);
                        messages.add(message);
                        groupMessageMap.put(group, messages);
                        return messages.size();
                    }else{
                        List<Message> newMsg = groupMessageMap.get(group);
                        newMsg.add(message);
                        groupMessageMap.put(group, newMsg);
                        newMsg.size();
                    }
                }
            }
            throw new Exception("You are not allowed to send message");
        }
        throw new Exception("Group does not exist");

    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{

       if(!groupUserMap.containsKey(group)){
           throw new Exception("Group does not exist");
       }
       if(!approver.getMobile().equals(adminMap.get(group).getMobile())){
           throw new Exception("Approver does not have rights");
       }
       List<User> userList = new ArrayList<>(groupUserMap.get(group));
       for(User val : userList){
           if(val.getMobile().equals(user.getMobile())){
               adminMap.put(group, user);
               return "SUCCESS";
           }
       }
       throw new Exception("User is not a participant");
    }

    public int removeUser(User user) throws Exception{

        for(Group x : groupUserMap.keySet()){
            List<User> userList = new ArrayList<>(groupUserMap.get(x));
            for(User u : userList){
                if(u.getMobile().equals(user.getMobile())){
                    if(adminMap.get(x).getMobile().equals(user.getMobile())){
                        throw new Exception("Cannot remove admin");
                    }else {
                        List<Message> msgList = new ArrayList<>(groupMessageMap.get(x));
                        for(Message m : msgList){
                            if(senderMap.get(m).getMobile().equals(user.getMobile())){
                                msgList.remove(m);
                                senderMap.remove(m);
                            }
                        }
                        groupUserMap.get(x).remove(user);
                        x.setNumberOfParticipants(x.getNumberOfParticipants()-1);
                        return groupUserMap.get(x).size()+groupMessageMap.get(x).size()+senderMap.size();
                    }
                }
            }
        }
        throw new Exception("User not found");
    }

    public String findMessage(Date start, Date end, int K) throws Exception{
        List<Message> msgList = new ArrayList<>();
        for(Message message : senderMap.keySet()){
            if(message.getTimestamp().compareTo(start)>0 && message.getTimestamp().compareTo(end)<0){
                msgList.add(message);
            }
        }
        Collections.sort(msgList, (a, b)->a.getTimestamp().compareTo(b.getTimestamp()));
        if(K > msgList.size()){
            throw new Exception("K is greater than the number of messages");
        }
        return msgList.get(K-1).getContent();
    }


}
