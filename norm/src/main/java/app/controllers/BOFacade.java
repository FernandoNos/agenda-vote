package app.controllers;

import app.biz.AssociateBO;
import app.enums.TransactionCode;
import app.model.Associate;
import app.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BOFacade {



    @Autowired
    private AssociateBO associateBO;



    public Message create(Associate associate){
        Message message = new Message();
        try {
            Associate newAssociate = associateBO.create(associate);
            message.setMessage(TransactionCode.SUCCESS);
            message.setObject(newAssociate);
        }catch(Exception e){
            message.setMessage(e.getMessage());
        }
        return message;
    }

    public Message findAssociate(String associateId){
        Message message = new Message();
        try {
            Associate newAssociate = associateBO.findAssociate(associateId);
            message.setMessage(TransactionCode.SUCCESS);
            message.setObject(newAssociate);
        }catch(IllegalArgumentException e){
            message.setMessage(TransactionCode.ASSOCIATE_NOT_FOUND);
            message.setMessage(e.getMessage());
        }catch(Exception e){
            message.setMessage(TransactionCode.ERROR);
        }
        return message;

    }


}
