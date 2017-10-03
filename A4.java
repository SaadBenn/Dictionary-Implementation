//-----------------------------------------
// NAME: Saad B Mushtaq 
// STUDENT NUMBER: 7785430
// COURSE: COMP 2140, SECTION: A01
// INSTRUCTOR: Yang Wang
// ASSIGNMENT: assignment # 4
// 
// REMARKS: Implementing ADT Dictionary
//
//-----------------------------------------

import java.io.*;
import java.util.*;
  
/***********************************************************************
************************************************************************
*  The driver class.
*
*  Processes the two files and prints the result on to teh console
***********************************************************************
************************************************************************/
class A4 {
  
  public static final int tableSize = 9851;
  public static Node table[] = new Node[tableSize];
  public static int uniqueWords = 0;
  public static int count = 1;
  
  /******************************************************************
   * Takes in files as command line arguments and calls processFile
   * for further processing.
   ******************************************************************/
  public static void main ( String args[] ) {
    
     if ( args.length < 2 ) {
       System.out.println( "Arguments must be supplied" );
       System.out.println( "Usage: java App <arg0> <arg1>" );
       System.exit( 1 );
     } // if
     
     // optionally, check that there are exactly 2 arguments
     if ( args.length > 2)  {
       System.out.println( "Too many arguments" );
       System.out.println( "Usage: java App <arg0> <arg1>" );
       System.exit( 1 );
     } // if
     
     // Print output headers.
     System.out.println( );
     System.out.println("Comp 2140  Assignment 4   Winter 2017");
     System.out.println("Spell checking an essay.\n\n");
     
     File dict = new File( args[0] );
     processFile( dict, args[0] , args );
     
     File errors = new File( args[1] );
     processFile( errors, args[1], args );
     
     System.out.println("\nNumber of unique words in the dictionary: " + uniqueWords );
     System.out.println("\nProgram ended normally.");
 
  } // main

  
  /******************************************************************
   * Reads in each line, and splits it and then calls tokennizer()
   *
   * @param      file      The file pointer
   * @param      argument  The argument we are dealing with
   * @param      args      array of the command line arguments
   ******************************************************************/
  public static void processFile( File file, String argument, String[] args ) {
    
    int lineNum = 0;
    String line;
    String[] tokens; 
     
    try {
      
      Scanner input  = new Scanner ( file ); 
      line = input.nextLine();
      
      while ( input.hasNextLine() ) {
        lineNum++;
        
        if ( line.length() > 0 ) { // calls the functions only if no blank line
          
          line = line.trim(); 
          line = line.toLowerCase();
          tokens = line.trim().split( "[^a-z']+" ); 
          tokenizer( tokens, argument, args, lineNum );
          
        } // if
        
        line = input.nextLine();
      } // while
        
    } catch ( Exception ex ) {
      System.out.println( ex.getMessage() );
      ex.printStackTrace();
    } // catch
    
  } // processFile
  
  
  /***********************************************************************************************
   * Looks at the tokens one by one and map it onto an array index
   * then depending on teh argument we are dealing with, if it is
   * file 1 then we insert it otherwise if it is file 2 then we are checking
   * for errors.
   * 
   * @param      file      The file pointer
   * @param      argument  The argument we are dealing with
   * @param      args      array of the command line arguments
   **********************************************************************************************/
  public static void tokenizer( String tokens[], String argument, String[] args, int lineNum  ) {
    
    int index;
    
    for ( int i = 0; i < tokens.length; i++ ) {
      
      if ( tokens[i].length() > 0 ) {
        
        int hashCode = hash( tokens[i] ); // get the hashCode
        index = compressionMap( hashCode ); // get the index
        
        boolean result = search( index, tokens[i] );
        
        if ( !result  ) {
          
          if ( argument.equals( args[0] ) ) { // if file 1 then insert
            
            insert( index, tokens[i] ); // insert the word
            uniqueWords++;
            
          } else {
            
            if ( argument.equals( args[1] ) ) { // print to the console
              
               System.out.printf( "%d: \"%s\" on line [%d] is misspelt.\n\n", count, tokens[i], lineNum );
               count++;
            } // inner if
          } // else  
        } // inner if
      } // outer if
    } // for loop
    
  } // tokenizer
  
 
 /**********************************************************************
  * Takes in the word and looks at each char of the word and calculates
  * hashing code using polynomial hashing horner method.
  *
  * @param      word  The word
  *
  * @return     { the unique hashCode }
  **********************************************************************/
  public static int hash( String word ) {
      
    int a = 13;
    int hashCode = word.charAt(0);
  
    for (int i = 1; i < word.length(); i++ ) { 
       hashCode = ( (hashCode * a + (int) word.charAt(i) ) % tableSize );
    } // for loop
      
    return hashCode;
  } // hash
    
  
  /**********************************************************
   * Takes in the hashcode and maps it onto the array index
   *
   * @param      hashCode  The hash code
   *
   * @return     { the index of array }
   **********************************************************/
  public static int compressionMap( int hashCode ) {
    int index;
    index = hashCode % tableSize;
      
    return index;
  } // compressionMap
  
  
  /***********************************************************************
   * Inserts the word depending on whether it is the first word in the
   * nodes, if it is not then we just point the new node to previous one 
   *
   * @param      index  The index of teh array
   * @param      word   The word
   ***********************************************************************/
  public static void insert( int index, String word ) {
    
    if ( table[index] != null ) {

      table[index] = new Node( word, table[index] );

    } else {

      table[index] = new Node( word, null );
    }
  } // insert
  
  
  /*******************************************************************
   * Searches the index of trhe array for the expected word
   *
   * @param      index     The index of teh array
   * @param      expected  The expected word
   *
   * @return     { true or false depending on whether it is present }
   *******************************************************************/
  public static boolean search( int index, String expected ) {
    
    boolean result = false;
    Node curr = table[index];
    
    while ( curr != null && !result ) {
      if ( curr.getWord().equals(expected) ) {
        result = true;
      }
      curr = curr.getNext();
    }
    
    return result;
  } // search
    
     
} // class A4

/***********************************************************************
************************************************************************
*  The Node class.
*
*  Ordinary linked-list nodes, storing an Object item (so it can
*  be used to store any type of data.
***********************************************************************
************************************************************************/
class Node {
  
  private Object word;
  private Node next;

  public Node ( Object word, Node next ) {
    this.word = word;
    this.next = next;
  } // Node constructor
  
  // getters and setters functions
  public Object getWord() { return word; } // getWord

  public Node getNext() { return next; } // getNext

  public void setNext( Node next ) { this.next = next; } // setNext
  
} // class Node