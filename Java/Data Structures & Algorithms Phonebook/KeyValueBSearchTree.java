package oy.tol.tra;

import java.util.concurrent.atomic.AtomicInteger;

public class KeyValueBSearchTree<K extends Comparable<K>,V> implements Dictionary<K, V> {

    // This is the BST implementation, KeyValueHashTable has the hash table implementation
    private TreeNode<K,V> root = null;
    private int count;
    private int maxDepth;
    private int maxCollisionChainLength;

    public KeyValueBSearchTree(){
        root = null;
        count = 0;
        maxDepth = 0;
        maxCollisionChainLength = 0;
    }

    @Override
    public Type getType() {
       return Type.BST;
    }
 
    @Override
    public int size() {
        
        return count;
    }



    /**
     * Prints out the statistics of the tree structure usage.
     * Here you should print out member variable information which tell something about
     * your implementation.
     * <p>
     * For example, if you implement this using a hash table, update member variables of the class
     * (int counters) in add(K) whenever a collision happen. Then print this counter value here. 
     * You will then see if you have too many collisions. It will tell you that your hash function
     * is good or bad (too much collisions against data size).
     */
    @Override
    public String getStatus() {
        StringBuilder newbuilder = new StringBuilder();
        newbuilder.append("Count of tree nodes: " + count + "\n");
        newbuilder.append("Max depth: " + maxDepth + "\n");
        newbuilder.append("Max collision chain length: " + maxCollisionChainLength + "\n");
        return newbuilder.toString();
    }

    @Override
    public boolean add(K key, V value) throws IllegalArgumentException, OutOfMemoryError {
        
        if(key == null || value == null){
            throw new IllegalArgumentException("Key or Value cannot be null");
        }
        if(root == null){
            root = new TreeNode<K,V>(key, value);
            count += 1;
            maxDepth = 1;
            maxCollisionChainLength = 0;
            return true;
        }
        else{
            TreeNode.addDepth = 1;
            maxCollisionChainLength = 0;
            int added = root.insert(key, value, key.hashCode());
            if(added > 0){
                count += 1;
                maxDepth = Math.max(TreeNode.addDepth, maxDepth);
                maxCollisionChainLength = Math.max(TreeNode.collisionChainLength, maxCollisionChainLength);
                return true;
            }
        }
        return false;
    }

    @Override
    public V find(K key) throws IllegalArgumentException {
       
        if(key == null){
            throw new IllegalArgumentException("Key can't be null");
        }
        if(root == null){
            return null;
        }
        else{
            return root.find(key, key.hashCode()); //tästä jatkuu
        }
    }

    @Override
    public void ensureCapacity(int size) throws OutOfMemoryError {
        //Not needed
    }

    @Override
    @java.lang.SuppressWarnings({"unchecked"})
    public Pair<K,V> [] toSortedArray() {
       
        if(root == null){
            return null;
        }
        Pair<K,V> [] returnArray = (Pair<K,V> []) new Pair[count];
        AtomicInteger addIndex = new AtomicInteger(-1);
        root.toSortedArray(returnArray, addIndex);
        Algorithms.fastSort(returnArray);
        return returnArray;
    }
    
      @Override
      public void compress() throws OutOfMemoryError {
        //Not needed
      }
}
