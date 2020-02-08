package toys.xifongchristian;

public class Main {
    public static void main(String[] args){
        PersonageRegistry registry = new PersonageRegistry();
        initialiser(registry);
        PersonageThreadManager threadManager = new PersonageThreadManager(registry, 5);
        registry.setThreadManager(threadManager);
        threadManager.begin();
    }
    private static void initialiser(PersonageRegistry registry){

    }
}
