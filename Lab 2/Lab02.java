import java.io.*;
import java.util.*;


public class Lab02
{
  // the size of a larger list for testing
  private static final int LIST_SIZE = 100;

  public static void main( String[] args )
  {
    System.out.println( "\n\nCOMP 2140 Lab 02 (Week of October 17, 2016)\n" );

    runTests();

    System.out.println( "\nLab 02 program ends\n\n" );
  } // end main

  // Create three different lists and run multiple tests on each one (with different n)
  private static void runTests( )
  {
    LinkedList list = new LinkedList();
    Random generator = new Random(System.nanoTime());

    // Try an empty list
    testList( list, 0, 0 ); // should produce FALSE
    testList( list, 0, -1 ); // should produce TRUE

    // Try a list with one number
    list.insertAtFront( generator.nextInt( LIST_SIZE*100 ) );
    testList( list, 1, 2 ); // should produce FALSE
    testList( list, 1, 1 ); // should produce FALSE
    testList( list, 1, 0 ); // should produce TRUE

    // Fill the list with LIST_SIZE numbers (already has 1, so add LIST_SIZE-1 more)
    for ( int i = 0; i < LIST_SIZE-1; i++ )
    {
      list.insertAtFront( generator.nextInt( LIST_SIZE*100 ) );
    }
    testList( list, LIST_SIZE, LIST_SIZE/2 ); // should produce TRUE
    testList( list, LIST_SIZE, LIST_SIZE+1 ); // should produce FALSE
  } // end runTests
  

  // Try both the iterative and the recursive method on this list
  // (which contains size items) with this n.
  // Print out what each method returns (and whether it is the correct
  // result) and how long it took.
  private static void testList( LinkedList list, int size, int n )
  {
    long start, stop, elapsed;
    boolean iterative, recursive;

    System.out.println( "\n\n*************************\n\nThe list:" );
    list.printList();

    // Try the iterative hasMoreThan
    start = System.nanoTime();
    iterative = list.hasMoreThanIterative( n );
    stop = System.nanoTime();
    elapsed = stop - start;
    System.out.println("\nTime for the iterative hasMoreThan method: "+ elapsed 
          + " nanoseconds.");
    if ( iterative )
    {
      if ( size <= n )
        System.out.print( "ERROR: " );
      System.out.println( "The iterative hasMoreThan method says that "
        + "the list with " + size + " item(s) has more than " 
        + n + " item(s)." );
    }
    else
    {
      if ( n < size )
        System.out.print( "ERROR: " );
      System.out.println( "The iterative hasMoreThan method says "
        + "that the list with " + size 
        + " item(s) does NOT have more than " + n + " item(s)." );
    }


    // Try the recursive hasMoreThan
    start = System.nanoTime();
    recursive = list.hasMoreThanRecursive( n );
    stop = System.nanoTime();
    elapsed = stop - start;
    System.out.println("\nTime for the recursive hasMoreThan method: "+ elapsed 
          + " nanoseconds.");
    if ( recursive )
    {
      if ( size <= n )
        System.out.print( "ERROR: " );
      System.out.println( "The recursive hasMoreThan method says that "
        + "the list with " + size + " item(s) has more than " 
        + n + " item(s)." );
    }
    else
    {
      if ( n < size )
        System.out.print( "ERROR: " );
      System.out.println( "The recursive hasMoreThan method says "
        + "that the list with " + size 
        + " item(s) does NOT have more than " + n + " item(s)." );
    }
  } // end testList

  
} // end class Lab02

/****************** LinkedList class *********************/

class LinkedList
{

  /****************** Node class *********************/
  private class Node // inside the linked list class
  {
    public int item;
    public Node next;

    public Node( int newItem, Node newNext ) // constructor
    {
      item = newItem;
      next = newNext;
    }

    // The usual accessors and mutators (in case you really want to use them).
    // These methods are unnecessary because you can directly access item and next
    // for any node that you have a pointer to without using accessors or mutators
    // anywhere in the LinkedList class (because item and next are declared public
    // in the private Node class inside the LinkedList class).

    public int getItem()
    { 
      return item;
    }

    public Node getNext()
    {
      return next;
    }

    public void setNext( Node newNext )
    {
      next = newNext;
    }

    // Other Node methods

    // Recursively determine if there are more than n items
    // stored in this node and all the non-dummy nodes after this.
    public boolean hasMoreThanRecursive( int n )
    {

/*************** YOU WRITE THIS METHOD ******************/
      boolean result = false;
      if (n < 1) // base case 1
        result = true;
      
      else if (n >= 1 && this.next.next != null) // recursive case
        result = this.next.hasMoreThanRecursive(n-1);
      
      else if (n >= 1 && this.next == null) // base case 2
        result = false; 
      
      return result;
      
    } // end hasMoreThanRecursive


    // Iteratively determine if there are more than n items
    // stored in this node and all the non-dummy nodes after this.
    public boolean hasMoreThanIterative( int n )
    {

/*************** YOU WRITE THIS METHOD ******************/
      //Node prev = this;
      Node curr = top.next;
      int count = 0;
      boolean result = true;
      
      while (count <= n && curr.next != null){
        //prev = curr;
        curr = curr.next;
        count++;     
      
      }//while
      
      return count > n;
    }// end hasMoreThanIterative

  } // end class Node

  /********* Returning to class LinkedList ************/

  private Node top; // a pointer to the first node in the list (when it's not empty)

  public LinkedList( ) // constructor creates the dummy nodes
  {
    top = new Node( 0, new Node( 0, null ) ); // empty list
    // because the list is UNordered, the item in a dummy node should never be used.
  }

  // Insert a node at the front of the list
  public void insertAtFront( int newItem )
  {
    top.next = new Node( newItem, top.next );
  }

  // Print out the list (10 numbers per line)
  public void printList( )
  {
    int count = 1;
    Node curr;

    if ( top.next.next == null )
      System.out.println( "List is empty" );
    else
    {
      curr = top.next; // first non-dummy node (if there are any)
      while ( curr.next != null )
      {
        System.out.print( " " + curr.item );
        count++;
        if ( count % 10 == 0 )
          System.out.println();
        curr = curr.next;
      }
      if ( count % 10 != 0 )
        System.out.println();
    }
  }

  // public driver method for hasMoreThanRecursive
  public boolean hasMoreThanRecursive( int n )
  {
    boolean moreThanN = (n < 0); // correct answer for an empty list

    if ( top.next.next != null ) // at least one non-dummy node in the list
      moreThanN = (top.next).hasMoreThanRecursive( n );

    return moreThanN;
  }

  // public driver method for hasMoreThanIterative
  public boolean hasMoreThanIterative( int n )
  {
    boolean moreThanN = (n < 0); // correct answer for an empty list

    if ( top.next.next != null ) // at least one non-dummy node in the list
      moreThanN = (top.next).hasMoreThanIterative( n );

    return moreThanN;
  }

} // end class LinkedList