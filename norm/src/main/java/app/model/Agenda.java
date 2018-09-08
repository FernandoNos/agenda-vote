package app.model;
import app.enums.VoteCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;


public class Agenda {

    @Id
    private String id;
    @JsonProperty("norm_description")
    private String description;
    @JsonProperty("norm_title")
    private String title;
    @JsonProperty("norm_duration")
    private int duration = 0;
    private Map<VoteCode, HashSet<String>> votes;
    private Date creationDate;


    public Agenda(){
        votes = new HashMap<VoteCode, HashSet<String>>();
    }

    public String getDescription(){ return description;}
    public String getTitle(){return title;}
    public int getDuration(){return duration;}

    public  void addVote(String associateId, VoteCode vote){
        HashSet<String> list = votes.get(vote);
        if(list == null){
            list = new HashSet<String>();
            votes.put(vote, list);
        }
        list.add(associateId);
    }

    public boolean hasVote(VoteCode vote,String associateId){

        for(HashSet<String> ids : votes.values()) {
                if(ids.contains(associateId))
                    return true;
        }
        return false;
    }

    public Map<VoteCode, HashSet<String>> getVotes(){
        return new HashMap<VoteCode, HashSet<String>>(votes);
    }

    public HashSet<String> getListOfVotes(VoteCode voteCode){
        return votes.get(voteCode);
    }

    public void setDescription(String description){
        this.description = description;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }
    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }
    public Date getCreationDate(){return creationDate;}
    public String getId(){ return id;}
    public String toString(){
        return "[" +
                "id:"+id
                +",title:"+title
                +",description:"+description
                +",duration:"+duration
                +"creation_date:"+creationDate
                +"]";
    }

}

