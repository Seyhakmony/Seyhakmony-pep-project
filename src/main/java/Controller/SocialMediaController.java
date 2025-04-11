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

    public void messagesCreate(Context context) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Message messageTemp = map.readValue(context.body(), Message.class);


            Message createMessage = messageService.creatMessage(messageTemp);
            
            if(createMessage == null || createMessage.getMessage_text() == ""){
                context.status(400);
                context.result("");
            }
            else{
                context.status(200);

                context.json(map.writeValueAsString(createMessage));
            }

    }

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

    public void messagesAll(Context context){
        List<Message> allMessages = messageService.getAllMessage();


        if(allMessages == null){
            context.status(400);
        }
        else{
            context.status(200);
            context.json(allMessages);
        }
    }

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