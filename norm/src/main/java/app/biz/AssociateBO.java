package app.biz;

import app.enums.TransactionCode;
import app.model.Associate;
import app.repositories.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class AssociateBO {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final String CLASS_NAME = this.getClass().getName();

    @Autowired
    private AssociateRepository associateRepository;

    public Associate create(Associate associate)  {
        LOG.info(CLASS_NAME+" - Request to create Associate "+associate);

        if(associate.getName() == null || associate.getName().isEmpty()){
            throw new IllegalArgumentException(TransactionCode.INVALID_ASSOCIATE.getMessage());
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
