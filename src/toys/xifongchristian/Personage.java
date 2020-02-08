package toys.xifongchristian;

import org.apache.commons.math3.distribution.GammaDistribution;

import java.util.HashMap;

public abstract class Personage implements IPersonage {
    protected Attributes attributes;
    protected PersonageRegistry registry;
    protected static GammaDistribution ageDistributionUtil = new GammaDistribution(6, 0.55);
    protected  int id;

    Personage(Attributes attributes, PersonageRegistry registry, int id){
        this.attributes = attributes;
        this.registry = registry;
        this.id = id;
    }

    public boolean enactBehaviour(){
        age();
        return !die();
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
            registry.markDead(id);
            return true;
        }
        return false;
    }
}
