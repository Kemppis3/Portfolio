package oy.tol.tra;

import java.util.concurrent.atomic.AtomicInteger;


public class TreeNode<K extends Comparable<K>, V> {
    
    private LinkedListImplementation <Pair<K,V>> collisionChain = null;
    private Pair<K,V> keyValue;
    private TreeNode<K,V> leftChild = null;
    private TreeNode<K,V> rightChild = null;
    private int hash = 0;
    
    public static int addDepth = 0;
    public static int collisionChainLength = 0;


    public TreeNode(K key, V value){
        collisionChain = null;
        keyValue = new Pair<K,V>(key, value);
        leftChild = null;
        rightChild = null;
        hash = key.hashCode();
    }

    public int insert(K key, V value, int keyToSearch) throws IllegalArgumentException{
        if(key == null || value == null){
            throw new IllegalArgumentException("Key or Value can't be null");
        }
        
        int added = 0;  
        if(keyToSearch < hash){
            if(leftChild == null){
                leftChild = new TreeNode<K,V>(key, value);
                addDepth++;
                added = 1;
            } else{
                added = leftChild.insert(key, value,keyToSearch);
                addDepth++;
            }
        }else if(keyToSearch > hash){
            if(rightChild == null){
                rightChild = new TreeNode<K,V>(key, value);
                addDepth++;
                added = 1;
        } else {
            added = rightChild.insert(key, value,keyToSearch);
            addDepth++;
            }
        } else {
            if(keyValue.getKey().equals(key)){
                keyValue.setvalue(value);
            }else {
                Pair<K,V> toSearch = new Pair<K,V>(key, null);
                if(collisionChain == null){
                    collisionChain = new LinkedListImplementation<>();
                    collisionChain.add(new Pair<K,V>(key, value));
                    added = 1;
                    collisionChainLength = 1;
                } else{
                    int index = collisionChain.indexOf(toSearch);
                    if(index < 0){
                        collisionChain.add(new Pair<K,V>(key, value));
                        added = 1;
                    } else {
                        collisionChain.remove(index);
                        collisionChain.add(new Pair<K,V>(key, value));
                    }
                    collisionChainLength = collisionChain.size();
                }
            }
        }
        return added;
        }

    public V find(K key, int findingHash) throws IllegalArgumentException{
        if(key == null){
            throw new IllegalArgumentException("Key can't be null");
        }
        V result = null;
        if(findingHash < hash){
            if(leftChild != null){
                result = leftChild.find(key, findingHash);
            }
        }else if(findingHash > hash){
            if(rightChild != null){
                result = rightChild.find(key, findingHash);
            }
        } else {
            if(keyValue.getKey().equals(key)){
                return keyValue.getValue();
            } else {
                if(collisionChain != null){
                    Pair<K,V> toSearch = new Pair<K,V>(key, null);
                    int index = collisionChain.indexOf(toSearch);
                    if(index >= 0){
                        return collisionChain.get(index).getValue();
                    }
                }
            }
        }

        return result;
    }

    public void toSortedArray(Pair<K,V> [] array, AtomicInteger addIndex){
        if(leftChild != null){
            leftChild.toSortedArray(array, addIndex);
        }
        
        if(rightChild != null){
            rightChild.toSortedArray(array, addIndex);
        }
        if(collisionChain != null){
            for(int index = 0; index < collisionChain.size(); index++){
                Pair<K,V> item = collisionChain.get(index);
                if (item != null){
                    array[addIndex.incrementAndGet()] = new Pair<K,V>(item.getKey(), item.getValue());
                }
            }
        }
        array[addIndex.incrementAndGet()] = new Pair<K,V>(keyValue.getKey(), keyValue.getValue());
    }

}