package toys.xifongchristian;

public class DefaultPersonage extends Personage {

    DefaultPersonage(Attributes attributes, PersonageRegistry registry){
        super(attributes, registry);
    }

    @Override
    public boolean enactBehaviour(){
        //marriage_status should not be an attribute - should get it from the registry.
        if(!registry.isMarried(id)) {
            System.out.println("Person " + getId() + " will try for marriage.");
            marry();
        }
        if(registry.isMarried(id)) {
            System.out.println("Person " + getId() + " will try for a child.");
            beget();
        }
        age();
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

        System.out.println("Person " + getId() + " has marriage potential: " + baseUtil);

        return 0.5;
        //return baseUtil * ageDistributionUtil.density(age) * 10;
    }

    private void marry(){
        double prob = marriageProb();
        System.out.println("Person " + getId() + " has marriage probability: " + prob);
        if(doesHappen(prob)){
            System.out.println("###########Person " + getId() + " is marrying.###########");
            registry.addSpouse(id);
        }
    }

    private double begetProb(){
        //temporary
        return 0.25;
    }

    private void beget(){
        if(doesHappen(begetProb())){
            System.out.println("###########Person " + getId() + " is having a child.###########");
            registry.addChild(id);
        }
    }
}
