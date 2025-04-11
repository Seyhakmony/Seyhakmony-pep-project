package Service;
import Model.Message;
import DAO.MessageDao;
import java.util.List;


public class MessageService {
    public MessageDao messageDao;

    public MessageService(){
        messageDao = new MessageDao();
    }

    public MessageService(MessageDao m){
        this.messageDao = m;
    }

    public List<Message> getAllMessage(){
        List<Message> temp = messageDao.getAllMessages();
        
        return temp;
    }

      
    public List<Message> getUserMessages (int userId){
        return messageDao.getAllMbyUser(userId);
    }
    
    public Message getMessagebyIdOnly(int id){
        Message temp = messageDao.getMessageByMessage(id);
        if(temp != null){
            return temp;
        }

        return null;

    }


    public Message creatMessage(Message m){
        if(messageDao.getMessageByMessage(m.getMessage_id()) != null){
            return null;
        }

        Message temp = messageDao.createMessage(m);
        return temp;
    }

    public Message updateMessage(Message m, int id){

        if(messageDao.getMessageByMessage(id) != null && !m.getMessage_text().equals("")){
            Message temp = messageDao.updateM(m, id);
            return temp;
        }
        return null;
    }

    
    public boolean deleteMessage(Message m){
        if(messageDao.getMessageByMessage(m.getMessage_id()) != null){
            messageDao.deleteM(m);
            return true;
        }
        return false;
    }

    

}
