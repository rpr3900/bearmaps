package bearmaps;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private int size;
    ArrayList<PriorityNode> values;
    TreeMap<T, PriorityNode> hash;

    public ArrayHeapMinPQ() {
        size = 0;
        values = new ArrayList<>();
        values.add(null);
        hash =  new TreeMap<>();
    }

    /**
     * Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present.
     * You may assume that item is never null.
     *
     * @param item
     * @param priority
     **/
    public void add(T item, double priority) {
        if (hash.containsKey(item)) {
            throw new IllegalArgumentException("Item is already present");
        }
        size += 1;
        PriorityNode newNode = new PriorityNode(item, priority, size);
        implement(newNode);
        arrangeUp(size);
        arrangeDown(1);
    }

    private void implement(PriorityNode node) {
        hash.put(node.getItem(), node);
        values.add(node);
    }

    /** moves the node up to its correct position in the heap.
     *
     * @param i
     * @param i
     */
    public void arrangeUp(int i) {
        int parent = parent(i);
        while (i > 1 && lowerPriority(parent, i) == i) {
            swap(i, parent);
            i = parent(i);
            parent = parent(i);
        }
    }

    /** moves the node down to its correct position in the heap.
     *
     * @param index
     */
    public void arrangeDown(int index) {
        while (leftChild(index) <= size) {
            int rightKid = rightChild(index);
            int leftKid = leftChild(index);
            if (leftKid < size && lowerPriority(leftKid, rightKid) == rightKid) {
                leftKid++;
            }
            if (lowerPriority(index, leftKid) != leftKid) {
                break;
            }
            swap(index, leftKid);
            index = leftKid;
        }
    }

    /** if needed, it will exchange two nodes when rearranging.
     *
     * @param i
     * @param p
     */
    private void swap(int i, int p) {
        PriorityNode node1 = indexValue(i);
        PriorityNode node2 = indexValue(p);
        values.set(i, node2);
        values.set(p, node1);
        values.get(i).setId(i);
        values.get(p).setId(p);
    }

    /** checks which node has a lower priority
     *
     * @param priority1
     * @param priority2
     * @return It will returns the index that has the value with the
     * smallest priority. It will be either null, index1, or index2.
     */
    private int lowerPriority(int priority1, int priority2) {
        PriorityNode node1 = indexValue(priority1);
        PriorityNode node2 = indexValue(priority2);

        if (node1 == null) {
            return priority2;
        } else if (node2 == null) {
            return priority1;
        } else if (node1.priority < node2.priority) {
            return priority1;
        } else {
            return priority2;
        }
    }

    /** returns the parent of a node.
     *
     * @param k
     * @return
     */
    private int parent(int k) {
        return k / 2;
    }

    /** returns the left child of a node.
     *
     * @param k
     * @return
     */
    private int leftChild(int k) {
        return k * 2;
    }

    /** returns the right child of a node.
     *
     * @param k
     * @return
     */
    private int rightChild(int k) {
        return k * 2 + 1;
    }

    /* Returns true if the PQ contains the given item. */
    public boolean contains(T item) {

        return hash.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("Priority Queue is empty");
        }
        return values.get(1).getItem();
    }

    /** gets the values of the node at the given index
     * @param index
     * @return index
     */
    private PriorityNode indexValue(int index) {
        if (index >= values.size()) {
            return null;
        }
        return values.get(index);
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T removeSmallest() {
        if (size() == 0) {
            throw new RuntimeException("Priority Queue is empty");
        }
        PriorityNode target = values.get(1);
        swap(1, size);
        values.remove(size--);
        hash.remove(target.getItem());
        arrangeDown(1);
        return target.getItem();
    }

    /* Returns the number of items in the PQ. */
    public int size() {
        return size;
    }

    /** Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist.
     * @param item
     * @param priority
     * */
    public void changePriority(T item, double priority) {
        if (!hash.containsKey(item)) {
            throw new NoSuchElementException("Item does not exist");
        }
        PriorityNode p = hash.get(item);
        p.setPriority(priority);
        int parent = parent(p.getId());
        PriorityNode par = values.get(parent);
        if (par == null) {
            arrangeDown(p.getId());
        } else if (par.getPriority() > p.getPriority()) {
            arrangeUp(p.getId());
        } else {
            arrangeDown(p.getId());
        }
    }

    private class PriorityNode {
        private T element;
        private double priority;
        private int id;

        PriorityNode(T e, double p, int location) {
            this.element = e;
            this.priority = p;
            this.id = location;
        }

        T getItem() {
            return element;
        }

        int getId() {
            return id;
        }

        void setId(int id) {
            this.id = id;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }
    }
}

