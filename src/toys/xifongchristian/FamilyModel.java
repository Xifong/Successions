package toys.xifongchristian;

import java.util.*;

public class FamilyModel implements Iterable{
    //Assumes 1-1 asymmetric pairing. One way spouse relation.
    private int lastId = -1;

    private class Node{
        private int id;
        private boolean live;

        private IPersonage satelliteData;
        private Node spouse;
        private ArrayList<Node> children;

        Node(){
            id = ++lastId;
            live = true;
        }

        private int addTo(){
            Node newNode = new Node();
            if(!hasSpouse()){
                spouse = newNode;
            }
            else {
                children.add(newNode);
            }
            return newNode.id;
        }

        private boolean hasSpouse(){
            return spouse == null;
        }
    }

    private Node root;

    public void setRoot(IPersonage personage){
        lastId = -1;
        root = new Node();
        root.satelliteData = personage;
    }

    public void attachPersonage(int id, IPersonage personage){
        getNodeAt(id).satelliteData = personage;
    }

    public int addNodeAt(int parentId){
        Node parent = getNodeAt(parentId);
        return parent.addTo();
    }

    public ArrayList<IPersonage> getChildrenOf(int id){
        ArrayList<IPersonage> children = new ArrayList<>();
        for(Node childNode : getNodeAt(id).children){
            children.add(childNode.satelliteData);
        }
        return children;
    }

    public boolean hasSpouse(int id){
        System.out.println("2");
        return getNodeAt(id).hasSpouse();
    }

    public IPersonage getSpouseOf(int id){
        return getNodeAt(id).spouse.satelliteData;
    }

    public IPersonage getPersonAt(int id){
        return getNodeAt(id).satelliteData;
    }

    public void markDead(int id){
        getNodeAt(id).live = false;
    }

    private Node getNodeAt(int id){
        System.out.println("3");
        if(id > lastId){
            throw new IndexOutOfBoundsException("This id has not been used yet: " + id);
        }
        Node current = root;
        ArrayList<Node> fringe = new ArrayList<>();
        while(true){
            if(current.id == id){
                return current;
            }
            if(current.spouse.id == id){
                return current.spouse;
            }
            fringe.addAll(root.children);
            current = fringe.get(0);
            fringe.remove(0);
        }
    }

    @Override
    public Iterator iterator() {
        return new Traverser();
    }

    private class Traverser implements Iterator{
        Queue<Node> queue = new LinkedList<>();

        Traverser(){
            queue.add(root);
        }

        @Override
        public boolean hasNext() {
            return !(queue.peek() == null);
        }

        @Override
        public IPersonage next() {
            Node current = queue.remove();
            if(current.spouse != null) {
                queue.add(current.spouse);
                if (!current.children.isEmpty()) {
                    queue.addAll(current.children);
                }
            }
            return current.satelliteData;
        }
    }
}
