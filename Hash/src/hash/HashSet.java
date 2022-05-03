/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hash;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author HP
 */
public  class HashSet<T> extends AbstractCollection<T> implements Set<T> {

    private static final int DEFAULT_TABLE_SIZE = 101  ;
    private int currentSize = 0 ;
    private int occupied = 0 ;
    private int modCount = 0 ;
    private HashEntry  array[] ;    
    
    
    /**
     This is the implementation of the HashSetIterator.
     It maintains a notion of a current position and of course the implicit 
     reference to the HashSet.
     */
    private class HashSetIterator implements Iterator<T> {
       
        private int expectedModCount = modCount ;
        private int currentPos = -1 ;
        private int visited = 0 ;
     
        @Override
        public boolean hasNext() {
             if( expectedModCount != modCount )
                 throw new ConcurrentModificationException() ;
         
            return visited != size() ;
        }

        @Override
        public T next() {
           if( !hasNext()) throw new NoSuchElementException() ;
           do{
               currentPos++ ;
           }while(currentPos < array.length && !isActive(array,currentPos)) ;
           
           visited++ ;
           return (T)array[currentPos].element ; 
        }
        public void remove( ) {
            if(expectedModCount != modCount ) throw new ConcurrentModificationException() ;
            if(currentPos == -1 || !isActive(array,currentPos )) throw new IllegalStateException() ;
            
            array[ currentPos ].isActive = false ;
            currentSize-- ;
            visited-- ;
            modCount++ ;
            expectedModCount++ ;
         } 
    }
    
    private static class HashEntry implements java.io.Serializable {
        
        public Object element ; // the element 
        public boolean isActive ; // false if marked deleted
        
        public HashEntry (Object e) {
           // this(e,true );
           this.element = e ;
           this.isActive = true ;
        } 
        public HashEntry (Object e, boolean i) {
            this.element = e ;
            this.isActive = i ;
        } 
    }
 
    // Construct an empty HashSet.
    public HashSet() {
       // allocatedArray(DEFAULT_TABLE_SIZE) ;
       // clear() ;
       array = new HashEntry[DEFAULT_TABLE_SIZE] ;
        
    }
    // Construct a HashSet from any collection.
    public HashSet(Collection<? extends T > other) {
        
        // allocateArray (nextPrime(other.size()*2)) ;
        // clear () ;
        array = new HashEntry[nextPrime(other.size()*2)] ;
        
        for( T val : other) {
            add( val )  ;
        }
    }
   
    @Override
    public Iterator<T> iterator() {
       return new HashSetIterator() ;
    }
    /**
     Tests if some item is in this collection.
     @param x any object.
     @return true if this collection conatains an item equal to x .
     */
    public boolean contains(Object x ) {
        return isActive(array , findPos((T)x)) ;
    }
    
    /**
     Tests if item in pos is active.
     @param pos a position in the hash table.
     @param arr the HashEntry array (can be oldArray during rehash).
     @return true if this position is active 
     */
    public static boolean isActive( HashEntry [] arr , int pos ) {
       
        return arr[pos] != null && arr[pos].isActive  ;
    }
    /**
     It checks if x is in the set.If it is, it return reference to the matching object; otherwise it return null 
     @param x the object to search for.
     @return if contains(x) is false, the return value is null ;
     otherwise, the return value is the object that causes contains(x) to return true
     */
    public T getMatch(T x){
        
        int currentPos = findPos(x) ;
        if (isActive(array,currentPos ) ) return (T)array[ currentPos ].element  ;
       
        return null ;
    }
    /**
     Remove an item from this collection.
     @param x any object
     @return true if this item was removed from the cllection.
     */
    public boolean remove(Object x ) {
       int currentPos = findPos((T)x) ;
       if(!isActive(array,currentPos)) return false;
       
       array[currentPos].isActive = false ;
       currentSize-- ;
       modCount++ ;
       
       if( currentSize < array.length/8) rehash() ;
       
       return true ;
    }
    /*
     Change the size of this collection to zero.  
    */
    public void clear() {
        currentSize = occupied = 0 ;
        modCount++ ;
        for(int i=0;i<array.length ;i++) 
            array[i] = null ;
        
    }
    /**
     Adds an item to this collection.
     @param x an obkect.
     @return true if this item was added to the collection.
     */
    public boolean add(T x) {
        
        int currentPos = findPos(x ) ;
        if(isActive(array,currentPos))
            return false ;
        
        if(array[currentPos] == null ) 
            occupied++;
        
        array[currentPos] = new HashEntry(x,true) ;
        currentSize++ ;
        modCount++ ;
        
        if(occupied > array.length/2)
            rehash() ;
        
        return true ; 
    }
    /**
     private routine to perform rehashing.
     Can be called by both add remove.
     */
    private void rehash() {
      HashEntry[] oldArray = array ;
        
        // Create a new, empty table 
        array = new HashEntry[nextPrime(4*size())] ;
      //  System.arraycopy(oldArray,0, array, 0,oldArray.length) ;
        currentSize = 0 ;
        occupied = 0 ;
        
        // Copy table over 
        for(int i=0;i<oldArray.length;i++) 
            if(isActive(oldArray,i)) 
                add((T)oldArray[i].element) ;   
    }
    /**
     Method that performs quadratic probing resolution.
     @param x the item to search for 
     @return the position where the seacrch terminates.
     */
    private int findPos(T x ) {
        
        int offset = 1 ;
        int currentPos = (x == null) ? 0 : Math.abs(x.hashCode()%array.length) ;
       
        while(array[currentPos] != null ){
            if(x == null ) {
                if(array[currentPos].element == null ) break ;
            }
            else if(x.equals(array[currentPos].element))
                break ;
        
        currentPos += offset ;  // Compute its ith probe 
        offset += 2 ;
        if(currentPos >= array.length) // Implement the mod
            currentPos -= array.length ;
    }
        return currentPos ;
    }
    private void allocateArray( int arraySize ) {
        array = new HashEntry[ arraySize] ;
    }
    private static int nextPrime( int n) {
       
        if(n%2 == 0) n++ ;
        
        for(; !isPrime(n) ; n += 2) ;
        
        return n ;      
    }
    private static boolean isPrime(int n) {
        
        if(n<=1) return false ;
        
        for(int i=2;i<n/2;i++) 
            if(n%i==0) return false ;
        
        return true ;
    }
    @Override
    public int size() {
        return currentSize ;  
    }
   
   
   }
