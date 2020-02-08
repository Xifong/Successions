package toys.xifongchristian;

public class PersonageFactory{
    public static IPersonage newPersonage(PersonEnum type, Attributes attributes, int id, PersonageRegistry registry){
        switch(type){
            case DEFAULT:
                return new DefaultPersonage(attributes, registry, id);
            case CONSORT:
                return new ConsortPersonage(attributes, registry, id);
        }
        return null;
    }

    public enum PersonEnum{
        DEFAULT,
        CONSORT;
    }
}
