package toys.xifongchristian;

import java.util.ArrayList;
import java.util.Iterator;

public interface IFamModel extends Iterable{
    boolean isFresh();

    int addPersonageAt(int id, IPersonage personage);

    void markDead(int id);

    boolean hasSpouse(int id);

    ArrayList<IPersonage> getChildrenOf(int id);

    IPersonage getSpouseOf(int id);

    IPersonage getPersonAt(int id);

    Iterator iterator();
}
