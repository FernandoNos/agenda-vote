package app.model;

import app.enums.VoteCode;
import app.utils.DateHandler;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class VotingResult {

    @JsonProperty("norm_title")
    private String normTitle;
    @JsonProperty("votes")
    private HashMap<VoteCode,Integer> votes;
    @JsonProperty("closing_date")
    private Date closingDate;
    @JsonProperty("norm_description")
    private String description;

    public VotingResult(Agenda agenda){
        this.normTitle = agenda.getTitle();
        this.votes = computeVotes(agenda);
        this.closingDate = DateHandler.addMinute(agenda.getCreationDate(), agenda.getDuration());
        this.description = agenda.getDescription();
    }
    private HashMap<VoteCode,Integer> computeVotes(Agenda agenda){
        HashMap<VoteCode, Integer> voteCount = new HashMap<VoteCode, Integer>();
        for(VoteCode voteCode : VoteCode.values()){
            HashSet<String> votes = agenda.getListOfVotes(voteCode);
            int count = votes==null?0:votes.size();
            voteCount.put(voteCode, count);
        }
        return voteCount;
    }

    public String toString(){
        return "[" +
                "norm_title:"+normTitle
                +",votes:"+votes
                +",description:"+description
                +",closing_date:"+closingDate
                +"description:"+description
                +"]";
    }
}
