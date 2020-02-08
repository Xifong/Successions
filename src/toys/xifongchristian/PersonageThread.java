package toys.xifongchristian;

import java.util.concurrent.Phaser;

public class PersonageThread implements Runnable {
    private IPersonage personage;
    private Phaser phaser;
    private PersonageThreadManager manager;

    PersonageThread(IPersonage personage, Phaser phaser, PersonageThreadManager manager){
        this.personage = personage;
        this.phaser = phaser;
        this.manager = manager;
    }

    public void run(){
        while(true){
            if(manager.personagesCanGo()) {
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
        }
        System.out.println("A thread has terminated");
    }
}
