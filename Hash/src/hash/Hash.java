/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hash;

/**
 *
 * @author HP
 */

       


public class Hash {

    public static int hash(String key, int tableSize) {
        
        int hashVal = 0 ;
        for(int i=0;i<key.length();i++) 
            hashVal = (hashVal*128 + key.charAt(i))%tableSize ;
        
        return hashVal ;
    }
    //a faster hash fuction that takes advantage of overflow 
    /**
      A hash routine for String objects.
     *@param key the String to hash 
     * @param tableSize the size of the hash table.
     * @return the hash value.
     */
    public static int hash2(String key, int tableSize) {
        
        int hashVal = 0 ;
        
        for(int i=0;i<key.length();i++) 
            hashVal = 37*hashVal + key.charAt(i) ;
        
        hashVal %= tableSize ;
        
        if(hashVal<0) hashVal += tableSize ;
        
        return hashVal ;
        
    }
    // A poor hash function when tableSize is large 
    
    public static int hash3(String key, int tableSize) {
        
        int hashVal = 0 ;
        
        for(int i=0;i<key.length() ;i++) 
             hashVal += key.charAt(i) ;
        
        return hashVal%tableSize ;
    }
    /*
    Method to find a prime number at least as large as n.
    @param n the starting number (must be positive).
    @param a prime number larger or equal to n . 
    */
    private static int nextPrime(int n) {
        
        if( n%2 ==0 ) n++ ;
        
        for(; !isPrime(n); n +=2) ;
        
        return n ;
    }
    private static boolean isPrime(int n) {
        
        if(n<=1) return false ;
        for(int i=2;i<n/2;i++) 
            if(n%i==0) return  false ;
        
        return true ;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
// Excerpt of String class hashCode 

  /*
public final class String {
   private int hash = 0 ;
   
   public int hashCode(){
       if(hash != 0 ) return hash ;
       
       for( int i=0;i< length() ;i++) 
            hash = hash*31 +(int)charAt(i) ;
       
       return hash ;
   }
}
*/


