package toys.xifongchristian;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.List;

public class PersonageRegistry {

    private PersonageThreadManager threadManager;
    private FamilyModel model;
    private List<Integer> toMarkDead;

    PersonageRegistry(){
        model = new FamilyModel();
        Attributes attributes = new Attributes(0, 18, 100, 100, 100);
        model.setRoot(
                PersonageFactory.newPersonage(PersonageFactory.PersonEnum.DEFAULT, attributes, 0, this));
        toMarkDead = new ArrayList<>();
    }

    public void setThreadManager(PersonageThreadManager threadManager){
        this.threadManager = threadManager;
        for(Object personage : model){
            threadManager.addPersonage((IPersonage) personage);
        }
    }

    private void addPersonageAt(int id, Attributes attributes, PersonageFactory.PersonEnum type){
        int newId = model.addNodeAt(id);
        IPersonage newPersonage = PersonageFactory.newPersonage(type, attributes, newId, this);
        model.attachPersonage(newId, newPersonage);
    }

    public void addSpouse(int id){
        //Temporary - May want attribute factory
        Attributes attributes = new Attributes(1, 18, 100, 100, 100);
        addPersonageAt(id, attributes, PersonageFactory.PersonEnum.CONSORT);
    }

    public void addChild(int id){
        //Temporary - May want attribute factory
        Attributes attributes = new Attributes(0, 0, 100, 100, 100);
        addPersonageAt(id, attributes, PersonageFactory.PersonEnum.DEFAULT);
    }

    public void markDead(int id){
        toMarkDead.add(id);
    }

    public boolean isMarried(int id){
        System.out.println("1");
        return model.hasSpouse(id);
    }

    public boolean changed(){
        return false;
    }

    public int size(){
        return 0;
    }

    private void transactDeaths(){
        for(int id : toMarkDead){
            model.markDead(id);
        }
        toMarkDead.clear();
    }

    public void manageHierarchy(){
        transactDeaths();
    }
}
