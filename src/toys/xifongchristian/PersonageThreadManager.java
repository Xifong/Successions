package toys.xifongchristian;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PersonageThreadManager {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(4);

    private PersonageRegistry registry;
    private Phaser phaser;
    private volatile boolean phaseCanActuallyStart = false;

    private ArrayList<PersonageThread> newPeople;

    PersonageThreadManager(PersonageRegistry registry, int cycles){
        this.registry = registry;
        newPeople = new ArrayList<>();
        phaser = new Phaser(1){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("Flag reset.");
                phaseCanActuallyStart = false;
                System.out.println("During1 phaser phase: " + phase);
                System.out.println("During1 phaser parties: " + phaser.getRegisteredParties());
                new Thread(() -> bringNewIn()).start();
                registry.manageHierarchy();
                System.out.println("During2 phaser phase: " + phase);
                System.out.println("During2 phaser parties: " + phaser.getRegisteredParties());
                if(phaser.getRegisteredParties() == 0){
                    return true;
                }
                return phase >= cycles;
            }
        };
    }

    public void addPersonage(IPersonage personage){
        newPeople.add(new PersonageThread(personage, phaser, this));
    }

    public void begin(){
        System.out.println("Initial phaser phase: " + phaser.getPhase());
        System.out.println("Initial phaser parties: " + phaser.getRegisteredParties());
        phaser.arriveAndDeregister();
        System.out.println("End phaser phase: " + phaser.getPhase());
        System.out.println("End phaser parties: " + phaser.getRegisteredParties());
    }

    private void bringNewIn(){
        System.out.println("No of people being added: " + newPeople.size());
        for(PersonageThread personageThread : newPeople){
            phaser.register();
            System.out.println("Adding phaser parties: " + phaser.getRegisteredParties());
            threadPool.submit(personageThread);
        }
        newPeople.clear();
        phaseCanActuallyStart = true;
        System.out.println("Flag set.");

    }

    public boolean personagesCanGo(){
        return phaseCanActuallyStart;
    }
}
