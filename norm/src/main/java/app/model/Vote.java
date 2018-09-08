package app.model;
import app.enums.VoteCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

public class Vote {

    @Id
    private String id;
    @JsonProperty("associate_id")
    private String associateId;
    @JsonProperty("vote")
    private VoteCode vote;

    public Vote(String associateId, VoteCode vote){
        this.associateId = associateId;
        this.vote = vote;
    }
    public String getAssociateId(){ return associateId;}
    public VoteCode getvote(){return vote;}

    public void setVote(VoteCode vote){ this.vote = vote;}
    public void setAssociateId(String associateId){
        this.associateId = associateId;
    }

    public String getId(){ return id;}

    public String toString(){
        return "[" +
                "id:"+id
                +",associate_id:"+associateId
                +",vote_code:"+vote
                +"]";
    }

}

