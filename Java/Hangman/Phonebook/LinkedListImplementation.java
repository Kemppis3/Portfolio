package oy.tol.tra;

public class LinkedListImplementation<E> implements LinkedListInterface<E> {

   private class Node<T> {
      Node(T data) {
         element = data;
         next = null;
      }
      T element;
      Node<T> next;
      @Override
      public String toString() {
         return element.toString();
      }
   }

   private Node<E> head;
   private int size;

   public LinkedListImplementation(){
      head = null;
      size = 0;
   }

   public boolean isEmpty(){
      return size == 0;
   }


   @Override
   public void add(E element) throws NullPointerException, LinkedListAllocationException {
      
      if(element == null){
         throw new NullPointerException();
      }
      if(head == null){
         head = new Node<E>(element);
         size++;
      }
      else{
         Node<E> current = head;
         while(current.next != null){
            current = current.next;
         }
         current.next = new Node<E>(element);
         size++;
      }
   }

   @Override
   public void add(int index, E element) throws NullPointerException, LinkedListAllocationException, IndexOutOfBoundsException {
      
      if(element == null){
         throw new NullPointerException();
      }
      if(index > size || index < 0){
         throw new IndexOutOfBoundsException();
      }
      if(index == 0){
         Node<E> newNode = new Node<E>(element);
         newNode.next = head;
         head = newNode;
         size++;
      } else{
          Node<E> previousNode = null;
          Node<E> currentNode = head;
          int counter = 0;
          while(counter < index){
            previousNode = currentNode;
            currentNode = currentNode.next;
            counter++;
          }
          Node<E> newNode = new Node<E>(element);
          previousNode.next = newNode;
          newNode.next = currentNode;
          size++;
      }

   }

   @Override
   public boolean remove(E element) throws NullPointerException {
      
      if(element == null){
         throw new NullPointerException();
      }
      if(head == null){
         return false;
      }
      Node<E> removableNode = new Node<E>(element);
      Node<E> previousNode = null;
      Node<E> currentNode = head;
      int counter = 0;
      while(counter < size){
         if(removableNode.equals(currentNode) == true){
            if(previousNode == null){
               head = currentNode.next;
               size--;
            }
            else{
               head = previousNode;
               head.next = currentNode.next;
               size--;
            }
            return true;
         }
         else{
            previousNode = currentNode;
            currentNode = currentNode.next;
            counter++;
         }
      }
      return false;
   }
   
   @Override
   public E remove(int index) throws IndexOutOfBoundsException {
      
      if(index < 0 || index >= size){
         throw new IndexOutOfBoundsException();
      }
      E removed = null;
      if(index == 0){
         removed = head.element;
         head = head.next;
         size--;
      }
      else{
         int counter = 1;
         Node<E> currentNode = head.next;
         Node<E> previousNode = head;
         while(currentNode != null){
            if(counter == index){
               removed = currentNode.element;
               previousNode.next = currentNode.next;
               size--;
               break;
            }
            counter++;
            previousNode = currentNode;
            currentNode = currentNode.next;
         }
      }
      return removed;
   }

   @Override
   public E get(int index) throws IndexOutOfBoundsException {
      
      if(index < 0 || index >= size()){
         throw new IndexOutOfBoundsException();
      }
      E element = null;
      if(index == 0){
         element = head.element;
      }
      else{
         int counter = 1;
         Node<E> tempNode = head.next;
         while(counter < index){
            tempNode = tempNode.next;
            counter++;
         }
         element = tempNode.element;
      }
      return element;
   }

   @Override
   public int indexOf(E element) throws NullPointerException {
      
      if(element == null){
         throw new NullPointerException();
      }
      if(head == null){
         return -1;
      }
      if(element.equals(head.element) == true){
         return 0;
      }

      int counter = 1;
      Node<E> tempNode = head.next;
      while(counter < size){
         if(element.equals(tempNode.element) == true){
            return counter;
         }
         counter++;
         tempNode = tempNode.next;
      }
      return -1;
   }

   @Override
   public int size() {
      
      return size;
   }

   @Override
   public void clear() {
     
      head = null;
      size = 0;
   }

   @Override
   public void reverse() {
      
      // This method is not needed in doing the task in the README.md.
      Node<E> previousNode = null;
      Node<E> nextNode = null;
      Node<E> currentNode = head;
      while(currentNode != null){
         nextNode = currentNode.next;
         currentNode.next = previousNode;
         previousNode = currentNode;
         currentNode = nextNode;
      }
      head = previousNode;
   }

   @Override
   public String toString() {
      
      StringBuilder strbuilder = new StringBuilder();
      strbuilder.append("[");
      if(size > 0){
         int index = 0;
         Node<E> tmpNode = head;
         while(index < size){
            strbuilder.append(tmpNode.element);
            index++;
            tmpNode = tmpNode.next;
            if(index < size){
               strbuilder.append(", ");
            }
         }
      }
      strbuilder.append("]");
      return strbuilder.toString();
   }
   
}
