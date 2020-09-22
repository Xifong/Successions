package successions.xifongchristian;


public class PersonageRunnable{
    private IPersonage personage;
    private PersonageThreadManager manager;

    PersonageRunnable(IPersonage personage, PersonageThreadManager manager){
        this.personage = personage;
        this.manager = manager;
    }

    public int run(){
        System.out.println("Person " + personage.getId() + " is to act.");
        boolean stillAlive = personage.enactBehaviour();
        System.out.println("Person " + personage.getId() + " has acted.");
        if (!stillAlive) {
            System.out.println("Removing person " + personage.getId() + ".");
            manager.removePersonage(personage);
            return 0;
        }
        return 1;
    }
}
