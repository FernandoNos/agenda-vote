package app.biz;

import app.enums.TransactionCode;
import app.model.Associate;
import app.model.Message;
import app.repositories.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.TransactionAnnotationParser;

import java.util.List;

@Component
public class AssociateBO {

    @Autowired
    private AssociateRepository associateRepository;

    public Associate create(Associate associate)  {
        if(associate.getName() == null || associate.getName().isEmpty()){
            throw new IllegalArgumentException("The associate MUST have a name!");
        }
            Associate newAssociate = associateRepository.save(associate);
            return newAssociate;
    }

    public Associate findAssociate(String associateId) {

        Associate associate =  associateRepository.findById(associateId);
        if(associate == null){
            throw new IllegalArgumentException(TransactionCode.ASSOCIATE_NOT_FOUND.getMessage(associateId));
        }
        return associate;
    }
}
