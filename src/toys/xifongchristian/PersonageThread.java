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
        phaser.arriveAndAwaitAdvance();
        while(true){
            if(phaser.isTerminated()) {
                System.out.println("A thread is about to terminate in accordance with phaser termination.");
                break;
            }
            System.out.println("A thread is to act.");
            boolean stillAlive = personage.enactBehaviour();
            System.out.println("A thread has acted.");
            if (!stillAlive) {
                phaser.arriveAndDeregister();
                System.out.println("A thread has deregistered and is about to terminate.");
                break;
            }
            System.out.println("A thread is to arrive.");
            phaser.arriveAndAwaitAdvance();
        }
        System.out.println("A thread has terminated");
    }
}
