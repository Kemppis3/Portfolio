package oy.tol.tra;

public class KeyValueHashTable<K extends Comparable<K>, V> implements Dictionary<K, V> {

    private int capacity = 0;
    private int size = 0;
    private int collisionCount = 0;
    private int maxProbingCount = 0;
    private int pairsUpdated = 0;
    private int reallocations = 0;
    private Pair<K,V> keyvalueArray [] = null;
    private static final int DEFAULT_CAPACITY = 20;
    private static final double LOAD_FACTOR = 0.65;


    public KeyValueHashTable(int capacity) throws OutOfMemoryError {
    try{
        this.capacity = capacity;
        ensureCapacity(capacity);
    }catch(OutOfMemoryError o){
        throw new OutOfMemoryError("Out of memory");
        }
    }

    public KeyValueHashTable() throws OutOfMemoryError {
        try{
        ensureCapacity(DEFAULT_CAPACITY);
    } catch(OutOfMemoryError o){
        throw new OutOfMemoryError("Out of memory");
        }
    }

    private int indexFor(K key, int hashMod){
        return (((key.hashCode() + hashMod) & 0x7fffffff) % capacity);
    }

    
    @SuppressWarnings("unchecked")
    private void reallocate(double newCapacity){
        Pair<K,V> oldArray[] = keyvalueArray;
        newCapacity = Math.round(newCapacity);
        keyvalueArray = (Pair<K,V>[]) new Pair[(int)newCapacity];
        int oldCapacity = capacity;
        size = 0;
        collisionCount = 0;
        capacity = (int)newCapacity;
        for(int index = 0; index < oldCapacity; index++){
            if(oldArray[index] != null){
                add(oldArray[index].getKey(), oldArray[index].getValue());
            }
        }
        reallocations++;
    }

    @Override
    public Type getType() {
        return Type.HASHTABLE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void ensureCapacity(int size) throws OutOfMemoryError {
        
        if(size < DEFAULT_CAPACITY){
            size = DEFAULT_CAPACITY;
        }
        keyvalueArray = (Pair<K,V>[]) new Pair[size];
        capacity = size;
    }

    @Override
    public int size() {
        
        return size;
    }

    /**
     * Prints out the statistics of the hash table.
     * Here you should print out member variable information which tell something
     * about your implementation.
     * <p>
     * For example, if you implement this using a hash table, update member
     * variables of the class (int counters) in add() whenever a collision
     * happen. Then print this counter value here.
     * You will then see if you have too many collisions. It will tell you that your
     * hash function is not good.
     */
    @Override
    public String getStatus() {
        
        StringBuilder newbuilder = new StringBuilder();
        newbuilder.append("Capacity: " + capacity + "\n");
        newbuilder.append("Size: " + size + "\n");
        newbuilder.append("Collion count: " + collisionCount + "\n");
        newbuilder.append("Max Probing count: " + maxProbingCount + "\n");
        newbuilder.append("Pairs Updated: " + pairsUpdated + "\n");
        newbuilder.append("Reallocations: " + reallocations + "\n");
        newbuilder.append("Load Factor: " + LOAD_FACTOR + "\n");
        newbuilder.append("Filled to: " + (double)size / (double)capacity * 100 + "%\n");
        return newbuilder.toString();
    }

    @Override
    public boolean add(K key, V value) throws IllegalArgumentException, OutOfMemoryError {
        
        if(key == null || value == null){
            throw new IllegalArgumentException("Key or value cannot be null");
        }
        
        int index = 0;
        int hashModifier = 0;
        int currProbCount = 0;
        boolean added = false;
        try{
            if(size > (capacity * LOAD_FACTOR)){
                reallocate(capacity * (1.0 / LOAD_FACTOR));
            }
        do {
            index = indexFor(key, hashModifier);
            if(keyvalueArray[index] == null){
                keyvalueArray[index] = new Pair<K,V>(key,value);
                added = true;
                size++;
            } else if(!keyvalueArray[index].getKey().equals(key)){
                hashModifier++;
                collisionCount++;
                currProbCount++;
            } else {
                keyvalueArray[index].setvalue(value);
                added = true;
                pairsUpdated++;
            }
        } while(!added);
        maxProbingCount = Math.max(maxProbingCount, currProbCount);
        return added;
    } catch (Exception e){
        throw new OutOfMemoryError("Out of memory");
    }
}

    @Override
    public V find(K key) throws IllegalArgumentException {
       
        if(key == null){
            throw new IllegalArgumentException("Key cannot be null");
        }
        boolean finished = false;
        V result = null;
        int hashMod = 0;
        do {
            int index = indexFor(key, hashMod);
            if(keyvalueArray[index] != null){
                if(keyvalueArray[index].getKey().equals(key)){
                    result = keyvalueArray[index].getValue();
                    finished = true;
                } else {
                    hashMod++;
                }
            } else {
                finished = true;
            }
        } while(!finished);
        
        return result;
    }

    @Override
    @java.lang.SuppressWarnings({"unchecked"})
    public Pair<K,V> [] toSortedArray() {
        
        Pair<K,V> [] toReturn = (Pair<K,V> []) new Pair[size];
        int addIndex = 0;
        for(int index = 0; index < capacity; index++){
            if(keyvalueArray[index] != null){
                toReturn[addIndex++] = new Pair<K,V>(keyvalueArray[index].getKey(), keyvalueArray[index].getValue());
            }
        }
        Algorithms.fastSort(toReturn);
        return toReturn;
      }

    @Override
    public void compress() throws OutOfMemoryError {
        
        int indexOfFirstNull = Algorithms.partitionByRule(keyvalueArray, size, element -> element == null);
        reallocate(indexOfFirstNull);
    }
 

}
