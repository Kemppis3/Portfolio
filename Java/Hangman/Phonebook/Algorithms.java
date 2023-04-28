package oy.tol.tra;
import java.util.function.Predicate;

public class Algorithms {
    
   public static class ModeSearchResult<T> {
      public T theMode;
      public int count = 0;
   }

   public static <T extends Comparable<T>> void sort(T [] array){

      int i = 0;
      while(i < array.length-1){
           int j = 0;
           while(j < array.length-1){
              if(array[j].compareTo(array[j+1]) > 0){
              swap(array, j, j+1);
              j++;
              }
              else{
                 j++;
                 continue;
              }
           }
        i++;
      }
  }
  
  public static <T> void reverse(T [] array) {
    
    
   int i = 0;
   while (i < array.length/2) {
      swap(array, i, array.length-i-1);
      i++;
   }
}

   public static <T> void swap(T [] array, int first, int second){

      T temp = array[first];
      array[first] = array[second];
      array[second] = temp;

    }

   public static <T extends Comparable<T>> ModeSearchResult<T> findMode(T [] array) {
      ModeSearchResult<T> result = new ModeSearchResult<>();
      

      if(array == null || array.length <= 1){
         result.theMode = null;
         result.count = -1;
      }
      else{
      sort(array);
      int calc = 1;
      result.count = 1;
      T tempMode = array[0];
      
      for(int i = 1; i < array.length; i++){
         
         if(array[i].compareTo(tempMode) == 0){
            calc++;
         }
         else{
               if(calc > result.count){
                  result.count = calc;
                  result.theMode = tempMode;
               }
            tempMode = array[i];
            calc = 1;
         }
      }
      if(calc > result.count){
         result.count = calc;
         result.theMode = tempMode;
      }
   }
   return result;
   }

public static <T> int partitionByRule(T [] array, int count, Predicate<T> rule) {


   if(array == null || array.length <= 1){
      return -1;
   }

   else{
      int index = 0;
      for(; index < count; index++){
         if(rule.test(array[index])){
         break;
      }
    }
      if(index == count){
         return count;
      }
   int nextIndex = index + 1;
   while(nextIndex < count){
      if(!rule.test(array[nextIndex])) {
         swap(array, index, nextIndex);
         index++;
      }
      nextIndex++;
    }
   return index;
}
}

public static <T extends Comparable<T>> int binarySearch(T aValue, T [] fromArray, int fromIndex, int toIndex) {
	
   if(fromIndex == toIndex){
      if(fromArray[fromIndex].compareTo(aValue) == 0){
         return fromIndex;
      }
      else{
         return -1;
      }
   } 
   else{
      int middleIndex = fromIndex + (toIndex - fromIndex) / 2;
      if(fromArray[middleIndex].compareTo(aValue) >= 0){
         return binarySearch(aValue, fromArray, fromIndex, middleIndex);
      }
      else{
         return binarySearch(aValue, fromArray, middleIndex + 1, toIndex);
      }
   }
}

public static <E extends Comparable<E>> void fastSort(Comparable<E>[] array){
   quicksort(array,0, array.length - 1);

}


private static <E extends Comparable<E>> void quicksort(Comparable<E>[] array, int low, int high){
   if(low < high){
      int partIndex = partition(array, low, high);
      quicksort(array, low, partIndex - 1);
      quicksort(array, partIndex + 1, high);
      
   }
}
@java.lang.SuppressWarnings({"unchecked"})
public static <E extends Comparable<E>> int partition(Comparable<E> [] partArray, int low, int high){
   E pivotValue = (E)partArray[high];
   int i = low - 1;
   for(int j = low; j < high; j++){
      if(partArray[j].compareTo(pivotValue) < 0){
         i++;
         swap(partArray, i, j);
      }
   }
   swap(partArray, i + 1, high);
   return i + 1;
}


}
