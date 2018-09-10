package app.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

public class Associate {

    @Id
    private String id;
    @JsonProperty("name")
    private String name;

    public String getName(){ return name;}

    public void setName(String name){
        this.name = name;
    }
    public void setId(String id){this.id = id;}

    public String getId(){ return id;}

    public String toString(){
        return "{" +
                "id:"+id
                +",name:"+name
                +"}";
    }

}

