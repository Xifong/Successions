package toys.xifongchristian;

public class DefaultPersonage extends Personage {

    DefaultPersonage(Attributes attributes, PersonageRegistry registry, int id){
        super(attributes, registry, id);
    }

    @Override
    public boolean enactBehaviour(){
        //marriage_status should not be an attribute - should get it from the registry.
        System.out.println("0");
        if(!registry.isMarried(id)) {
            System.out.println("A thread will marry.");
            marry();
        }
        System.out.println("A thread has finished marriage phase.");
        if(registry.isMarried(id)) {
            System.out.println("I am married.");
            beget();
        }
        System.out.println("A thread has finished spawn phase.");
        age();
        System.out.println("A thread has aged and is to enter death phase.");
        return !die();
    }

    private double marriageProb(){
        double wealth = attributes.getAttribute("wealth");
        double charm = attributes.getAttribute("charm");
        double looks = attributes.getAttribute("looks");
        double age = attributes.getAttribute("age");

        double wealthUtil = standardSigmoid(wealth);
        double charmUtil = standardSigmoid(charm);
        double looksUtil = standardSigmoid(looks);

        double baseUtil =  (wealthUtil + charmUtil + looksUtil)/3;

        return baseUtil * ageDistributionUtil.density(age) * 10;
    }

    private void marry(){
        if(doesHappen(marriageProb())){
            System.out.println("###########I am marrying.###########");
            registry.addSpouse(id);
        }
    }

    private double begetProb(){
        //temporary
        return 0.15;
    }

    private void beget(){
        if(doesHappen(begetProb())){
            System.out.println("###########I am having a child.###########");
            registry.addChild(id);
        }
    }
}
