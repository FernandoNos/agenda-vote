package app.BOTests;

import app.biz.AssociateBO;
import app.controllers.BOFacade;
import app.model.Associate;
import app.model.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Component
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AssociateBOTest {

    private List<Associate> associates;

    @Autowired
    private BOFacade boFacade;

    @Autowired
    private AssociateBO associateBO;

    @Before
    public void createAssociates(){
        associates = new ArrayList<Associate>();
        for(int i = 0;i<10;i++){
            Associate aux = new Associate();
            aux.setName("Associate "+i);
            Message ms =  boFacade.create(aux);
            associates.add((Associate)ms.getObject());
        }
    }

    @Test
    public void positive_findAssociate() {
        Associate associate = associateBO.findAssociate(associates.get(0).getId());
        assertEquals(associate.getId(),associates.get(0).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void negative_findAssociate(){
        Associate associate = associateBO.findAssociate("-1");

    }

    public List<Associate> getAssociates(){ return this.associates;}

}
