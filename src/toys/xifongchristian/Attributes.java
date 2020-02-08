package toys.xifongchristian;

import java.util.HashMap;

public class Attributes {
    private HashMap<String, Integer> hashMap = new HashMap<>();

    Attributes(int type, int age, int wealth, int charm, int looks){
        setAttribute("type", type);
        setAttribute("age", age);
        setAttribute("wealth", wealth);
        setAttribute("charm", charm);
        setAttribute("looks", looks);
    }

    public void setAttribute(String key, Integer value){
        hashMap.put(key, value);
    }

    public Integer getAttribute(String key){
        return hashMap.get(key);
    }

}
