package successions.xifongchristian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class PersonageThreadManager implements SimInstance, Runnable{
    //got to make sure tasks truly are asynchronous
    private ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private boolean terminationTime = false;

    private List<IPersonage> personages = Collections.synchronizedList(new ArrayList<>());
    private List<Future<Integer>> activeTasks = new ArrayList<>();

    private PersonageRegistry registry;
    private Controller controller;
    private int cycles;
    private int currentCycle;

    PersonageThreadManager(PersonageRegistry registry, int cycles, Controller controller){
        this.registry = registry;
        this.cycles = cycles;
        this.controller = controller;
        currentCycle = 0;
    }

    public synchronized void addPersonage(IPersonage personage){
        personages.add(personage);
        System.out.println("Submitted task, now there are " + personages.size() + " agents to simulate.\n");
    }

    public synchronized void removePersonage(IPersonage personage){
        personages.remove(personage);
    }

    public int getCurrentYear(){
        return currentCycle;
    }

    private void finishUp(){
        threadPool.shutdown();
        controller.setGameBeingPlayed(false);
    }

    private void submitTasks(){
        for(IPersonage personage : personages){
            activeTasks.add(threadPool.submit(() -> new PersonageRunnable(personage, this).run()));
        }
    }

    private boolean onAdvance(){
        registry.manageHierarchy();
        if(personages.isEmpty() || currentCycle >= cycles || terminationTime){
            System.out.println("Simulation stopped.");
            controller.notifySimDetailsChanged();
            return true;
        }
        ++currentCycle;
        System.out.println("The year " + currentCycle + " begins.");
        controller.notifySimDetailsChanged();
        submitTasks();
        return false;
    }

    public void run(){
        while(true) {
            if(onAdvance()){
                finishUp();
                break;
            }
            for(Future<Integer> task : activeTasks){
                try {
                    task.get();
                }catch(InterruptedException interrupt){
                    terminationTime = true;
                    threadPool.shutdown();
                }catch(ExecutionException exException){
                    finishUp();
                }
            }
            activeTasks.clear();
        }
    }
}
