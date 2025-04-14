package Service;
import Model.Message;
import DAO.MessageDao;
import java.util.List;

/*
 * The MessageService class provides mainly the business logic/validation logic for message
 */
public class MessageService {
    public MessageDao messageDao;

    /*
     * Default constructor
     */
    public MessageService(){
        messageDao = new MessageDao();
    }

    /*
     * constructor allowiing for dependency injection of a custom MessageDao object
     */
    public MessageService(MessageDao m){
        this.messageDao = m;
    }

    /*
     * getAllMessage retrieves all messages from the database
     * @return  List<Message> A list of message objects representing all messages in the database
     */
    public List<Message> getAllMessage(){
        List<Message> temp = messageDao.getAllMessages();
        
        return temp;
    }

    /*
     * getUserMessages retrieves all messages posted by a specific user based on their user ID
     * @param userId The id of the user whose messages are to be retrieved
     * @return List<Message> A list of message objects representing all messages posted by the specified user
     */
    public List<Message> getUserMessages (int userId){
        return messageDao.getAllMbyUser(userId);
    }
    

    /*
     * getMessagebyIdOnly retrieves a specific message by its ID
     * @param id The id of the message to be retrieved
     * @return Message The message object if found or null if no message exist 
     */
    public Message getMessagebyIdOnly(int id){
        Message temp = messageDao.getMessageByMessage(id);
        if(temp != null){
            return temp;
        }

        return null;

    }

    /*
     * creatMessage adds a new message to the database
     * @param m The message object containing the details of the message to be created.
     * @return message The newly created message object or null if validation fails
     *
     */
    public Message creatMessage(Message m){
        if(messageDao.getMessageByMessage(m.getMessage_id()) != null || m.getMessage_text() == null || m.getMessage_text().trim().isEmpty() || m.getMessage_text().length() > 255){
            return null;
        }

        Message temp = messageDao.createMessage(m);
        return temp;
    }


    /*
     * updateMessage updates an existing message in the database
     * 
     * @param m The message object containing the updated message details
     * @param id The id of the message to be updated
     * @return Message the updated message object or null if the update conditions aren't successful
     */
    public Message updateMessage(Message m, int id){

        if(messageDao.getMessageByMessage(id) != null && !m.getMessage_text().trim().isEmpty() && m.getMessage_text().length() <= 255){
            Message temp = messageDao.updateM(m, id);
            return temp;
        }
        return null;
    }

    
    /*
     * deleteMessage deletes a message from the database
     * @param m the message object containing the details of the message to be deleted.
     * @return boolean as it returns true if the message was deleted or false if the message does not exist
     */
    public boolean deleteMessage(Message m){
        if(messageDao.getMessageByMessage(m.getMessage_id()) != null){
            messageDao.deleteM(m);
            return true;
        }
        return false;
    }

    

}
