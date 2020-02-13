package toys.xifongchristian;

import java.util.concurrent.Phaser;

public class PersonageThread implements Runnable {
    private IPersonage personage;
    private Phaser phaser;

    PersonageThread(IPersonage personage, Phaser phaser){
        this.personage = personage;
        this.phaser = phaser;
    }

    public void run(){
        System.out.println("Deregestering person " + personage.getId() + "; Phaser waiting on: " + phaser.getUnarrivedParties());
        phaser.arriveAndAwaitAdvance();
        while(true){
            if(phaser.isTerminated()) {
                break;
            }
            System.out.println("Person " + personage.getId() + " is to act.");
            boolean stillAlive = personage.enactBehaviour();
            System.out.println("Person " + personage.getId() + " has acted.");
            if (!stillAlive) {
                System.out.println("Deregestering person " + personage.getId() + "; Phaser waiting on: " + phaser.getUnarrivedParties());
                phaser.arriveAndDeregister();
                break;
            }
            System.out.println("Deregestering person " + personage.getId() + "; Phaser waiting on: " + phaser.getUnarrivedParties());
            phaser.arriveAndAwaitAdvance();
        }
    }
}
