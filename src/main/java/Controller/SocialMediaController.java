package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.*;
import Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();

    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // app.get("example-endpoint", this::exampleHandler);
        
        app.post("/messages", this::messagesCreate);
        app.delete("/messages/{message_id}", this::messageDeletebyId);
        
        app.get("/messages", this::messagesAll);
        app.get("/messages/{message_id}", this::massagesforuser);

        app.patch("/messages/{message_id}", this::updatemessage);

        app.post("/register", this::register);
        app.post("/login", this::login);
        app.get("/accounts/{account_id}/messages", this::useraccountmessages);


        
        return app;
    }

    // /**
    //  * This is an example handler for an example endpoint.
    //  * @param context The Javalin Context object manages information about both the HTTP request and response.
    //  */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    /*
     * messagesCreate will first checks posted_by refers to a real existing user. Then it will create a message and if successful
     * it will the response body should contain a JSON of the message along with status 200. Otherwise return status of 400 if unsucessful.
     */
    public void messagesCreate(Context context) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Message messageTemp = map.readValue(context.body(), Message.class);

            boolean checkUser = accountService.checkUser(messageTemp.getPosted_by());
            if(checkUser){ 
                Message createMessage = messageService.creatMessage(messageTemp);  
                if(createMessage == null){
                    context.status(400);
                    context.result("");
                }
                else{
                    context.status(200);
    
                    context.json(map.writeValueAsString(createMessage));
                }
            }else{
                context.status(400);
                context.result("");
            }


    }

    /*
     * messageDeletebyId will delete mesage given a message_id. 
     * If the message exist then it should be removed from the database with a response of 200.
     * If message did not exist the response will also be 200 but the body should be empty
     */
    public void messageDeletebyId(Context context) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        int byId = Integer.parseInt(context.pathParam("message_id"));

        Message temp = messageService.getMessagebyIdOnly(byId);
        if(temp == null){
            context.status(200);
            return;
        }
        boolean deleteMessageR = messageService.deleteMessage(temp);

        if(deleteMessageR){
            context.status(200);
            if(temp.getMessage_text() != null){
            context.json(map.writeValueAsString(temp));
            }
            else{
                context.status(200);
                context.result("");
            }
        }
        else{
            context.status(200);
        }

    }

    /*
     * messagesAll will return all messages as JSON a list containing all messages retrieved from the database.
     * If there are no messages the list will be empty,
     * Response is 200 by default. 
     */
    public void messagesAll(Context context){
        List<Message> allMessages = messageService.getAllMessage();


        if(allMessages == null){
            context.status(200);
            return;
        }
        else{
            context.status(200);
            context.json(allMessages);
        }
    }

    /*
     * massagesforuser will return the JSON representation of the message identified by the message_id. 
     * It is expected for the response body to simply be empty if there is no such message
     * response is 200 by deafult.
     */
    public void massagesforuser(Context context) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        int byId = Integer.parseInt(context.pathParam("message_id"));
        Message temp = messageService.getMessagebyIdOnly(byId);

        if(temp == null){
            context.status(200);
            return;
        }
        else{
            context.status(200);
            if(temp.getMessage_text() != null){
                context.json(map.writeValueAsString(temp));
            }
            else{
                context.result("");
            }
        }
    }


    /*
     * The request body should contain a new message_text values to replace the message identified by message_id.
     * The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters.
     * If the update is successful, the response body should contain the full updated message with response of 200.
     * if unsucessful then status will be 400. 
     */
    private void updatemessage(Context context) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        int byId = Integer.parseInt(context.pathParam("message_id"));
        
        Message message = map.readValue(context.body(), Message.class);
        
        Message temp = messageService.getMessagebyIdOnly(byId);
        if (temp == null){
            context.status(400);
            return;
        }


        Message updatedMessage = messageService.updateMessage(message, byId);

        if(updatedMessage != null){
            context.status(200);
            if(message.getMessage_text() != null){
                context.json(map.writeValueAsString(updatedMessage));
            }
            else{
                context.status(200);
                context.result("");
            }
        }
        else{
            context.status(400);
            context.result("");
        }

    }

    /*
     * register will will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist.
     * If successful then it return status of 200 along with json of account. If unsuccessful then response will return status of 400.
     */
    private void register(Context context) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Account acc = map.readValue(context.body(), Account.class);

        Account addAcc = accountService.addAccount(acc);
        if(addAcc == null){
            context.status(400);
        }
        else{
            context.status(200);
            context.json(map.writeValueAsString(addAcc));
        }
    }

    /*
     * The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database
     * If unsuccessful the status will be 200, if not then 401
     */
    private void login(Context context) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Account acc = map.readValue(context.body(), Account.class);

        Account logAccount = accountService.loginAccount(acc);

        if(logAccount == null){
            context.status(401);
        }else{
            context.status(200);
            context.json(map.writeValueAsString(logAccount));
        }
    }   


    /*
     * useraccountmessages will get all messages from user given accountID
     * Default return status of 200
     * If successful it will return a list containing all messages posted by a particular user
     * If there are no messages/unsuccessful then it will be empty return
     */
    private void useraccountmessages(Context context) throws JsonProcessingException{
        int userId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> userMessages = messageService.getUserMessages(userId);
        if(userMessages == null){
            context.status(200);
            return;
        }
        else{
            context.status(200);
            context.json(userMessages);

        }

    }



    

}