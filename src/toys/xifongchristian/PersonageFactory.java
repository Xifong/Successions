package toys.xifongchristian;

public class PersonageFactory{
    public static IPersonage newPersonage(PersonEnum type, Attributes attributes, PersonageRegistry registry){
        switch(type){
            case DEFAULT:
                return new DefaultPersonage(attributes, registry);
            case CONSORT:
                return new ConsortPersonage(attributes, registry);
        }
        return null;
    }

    public enum PersonEnum{
        DEFAULT,
        CONSORT
    }
}
