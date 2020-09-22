package successions.xifongchristian;

public class SimStarter{

    private Options options;
    private IModel model;

    SimStarter(Options options, IModel model){
        this.options = options;
        this.model = model;
    }

    public PersonageThreadManager getSimInstance(Controller controller){
        PersonageRegistry registry = new PersonageRegistry(model.getFamilyModel());
        PersonageThreadManager threadManager = new PersonageThreadManager(registry, options.getCycles(), controller);
        registry.setThreadManager(threadManager);

        return threadManager;
    }
}
