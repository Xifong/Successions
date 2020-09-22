package successions.xifongchristian;

public class DefaultModel implements IModel{
    private IFamModel familyModel;
    private int yearOffset;

    DefaultModel(IFamModel familyModel, int yearOffset){
        this.familyModel = familyModel;
        this.yearOffset = yearOffset;
    }

    @Override
    public IFamModel getFamilyModel() {
        return familyModel;
    }

    @Override
    public void setYearOffset(int yearOffset) {
        this.yearOffset = yearOffset;
    }

    @Override
    public int getYearOffset() {
        return yearOffset;
    }
}
