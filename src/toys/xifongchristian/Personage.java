package toys.xifongchristian;

import org.apache.commons.math3.distribution.GammaDistribution;

public abstract class Personage implements IPersonage {
    protected Attributes attributes;
    protected PersonageRegistry registry;
    protected static GammaDistribution ageDistributionUtil = new GammaDistribution(6, 1/0.55);
    protected int id;

    Personage(Attributes attributes, PersonageRegistry registry){
        this.attributes = attributes;
        this.registry = registry;
    }

    public boolean enactBehaviour(){
        age();
        return !die();
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    protected double standardSigmoid(double x){
        return 1/(1 + Math.exp(0.08 *(100 - x)));
    }

    protected boolean doesHappen(double prob){
        return Math.random() < prob;
    }

    protected void age(){
        attributes.setAttribute("age", attributes.getAttribute("age") + 1);
    }

    private double deathProb(){
        //Temporary
        return 0.1;
    }

    protected boolean die(){
        if(doesHappen(deathProb())){
            System.out.println("###########Person " + getId() + " is dying.###########");
            registry.markDead(id);
            return true;
        }
        return false;
    }
}
