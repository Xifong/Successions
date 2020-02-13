package toys.xifongchristian;

import java.util.concurrent.Phaser;

public class PersonageThreadManager implements SimInstance{
    //Lets just do away with the phaser and turn PersonageThreads into tasks that get executed on a pool
    private Phaser phaser;
    private boolean terminationTime = false;

    PersonageThreadManager(PersonageRegistry registry, int cycles, Controller controller){
        phaser = new Phaser(1){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                registry.manageHierarchy();
                if(phaser.getRegisteredParties() == 0 || phase >= cycles || terminationTime){
                    System.out.println("Simulation stopped.");
                    controller.notifySimDetailsChanged();
                    return true;
                }
                System.out.println("The year " + phaser.getPhase() + " begins.");
                controller.notifySimDetailsChanged();
                return false;
            }
        };
    }

    public void addPersonage(IPersonage personage){
        phaser.register();
        new Thread(new PersonageThread(personage, phaser)).start();
        System.out.println("Submitted task");
    }

    public void begin(){
        phaser.arriveAndDeregister();
    }

    public int interruptSim(){
        phaser.register();
        terminationTime = true;
        return phaser.getPhase();
    }

    public int getCurrentYear(){
        if(phaser.isTerminated()){
            return -1;
        }
        return phaser.getPhase();
    }
}
