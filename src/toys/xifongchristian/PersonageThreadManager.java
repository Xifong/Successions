package toys.xifongchristian;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PersonageThreadManager {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private Phaser phaser;

    PersonageThreadManager(PersonageRegistry registry, int cycles){
        phaser = new Phaser(1){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                registry.manageHierarchy();
                System.out.println("During phaser phase: " + phase);
                System.out.println("During phaser parties: " + phaser.getRegisteredParties());
                if(phaser.getRegisteredParties() == 0 || phase >= cycles){
                    threadPool.shutdown();
                    return true;
                }
                return false;
            }
        };
    }

    public void addPersonage(IPersonage personage){
        phaser.register();
        threadPool.submit(new PersonageThread(personage, phaser));
    }

    public void begin(){
        System.out.println("Initial phaser phase: " + phaser.getPhase());
        System.out.println("Initial phaser parties: " + phaser.getRegisteredParties());
        phaser.arriveAndDeregister();
        System.out.println("End phaser phase: " + phaser.getPhase());
        System.out.println("End phaser parties: " + phaser.getRegisteredParties());
    }
}
