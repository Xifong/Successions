package toys.xifongchristian;

import java.util.ArrayList;
import java.util.List;

public class PersonageRegistry {

    private PersonageThreadManager threadManager;
    private IFamModel model;
    private List<Integer> toMarkDead;

    PersonageRegistry(IFamModel model){
        this.model = model;
        toMarkDead = new ArrayList<>();
        if(model.isFresh()) {
            Attributes attributes = new Attributes(0, 18, 100, 100, 100);
            model.addPersonageAt(-1,
                    PersonageFactory.newPersonage(PersonageFactory.PersonEnum.DEFAULT, attributes, this));
        }
    }

    public void setThreadManager(PersonageThreadManager threadManager){
        this.threadManager = threadManager;
        for(Object personage : model){
            threadManager.addPersonage((IPersonage) personage);
        }
    }

    private void addPersonageAt(int id, Attributes attributes, PersonageFactory.PersonEnum type){
        IPersonage newPersonage = PersonageFactory.newPersonage(type, attributes, this);
        model.addPersonageAt(id, newPersonage);
        System.out.println("Child Node created at " + id + ".");
        threadManager.addPersonage(newPersonage);
    }

    public synchronized void addSpouse(int id){
        //Temporary - May want attribute factory
        Attributes attributes = new Attributes(1, 18, 100, 100, 100);
        addPersonageAt(id, attributes, PersonageFactory.PersonEnum.CONSORT);
    }

    public synchronized void addChild(int id){
        //Temporary - May want attribute factory
        Attributes attributes = new Attributes(0, 0, 100, 100, 100);
        addPersonageAt(id, attributes, PersonageFactory.PersonEnum.DEFAULT);
    }

    public void markDead(int id){
        toMarkDead.add(id);
    }

    public boolean isMarried(int id){
        return model.hasSpouse(id);
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
