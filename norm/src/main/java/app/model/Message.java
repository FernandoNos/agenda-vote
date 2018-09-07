package app.model;

import app.enums.TransactionCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;


public  class Message {

    @JsonProperty("code")
    private int code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("transaction_details")
    private Object object;


    public Object getObject(){
        return object;
    }

    public void setMessage(String message){
        this.message = message;
    }
    public void setErrorId(int code){
        this.code = code;
    }

    public void setMessage(TransactionCode transactionCode){
        this.code = transactionCode.getCode();
        this.message = transactionCode.getMessage();
    }

    public int getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
    public void setObject(Object object){
        this.object = object;
    }

    public String toString(){
        return "[" +
                "code:"+code
                +",message:"+message
                +",object:"+object
                +"]";
    }
}
