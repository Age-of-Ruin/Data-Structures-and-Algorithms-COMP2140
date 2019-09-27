/*************************************************************************
*
* Lab4 - creates a number of binary trees, randomly choosing
*        (1) whether to insert value in binary search tree (BST)
*            order or not,
*        (2) the size of the tree (up to MAX_TREE_SIZE), and
*        (3) the values to insert.
*        It ensures that the tree has exactly the chosen number
*        of distinct values (i.e., no duplicates are allowed).
*
*        It prints out the tree after the tree is created using
*        an inorder traversal, so you can _see_ whether the tree
*        is in BST order or not.
*
*        Then it uses a student-written method to test whether
*        the tree is in BST order or not, printing out the
*        result returned by the student-written method.
*
***************************************************************************/

import java.util.*;

public class Lab4
{

  /*********************************************************************
  *
  * Global constants
  *
  **********************************************************************/
  private final static int NUM_TREES = 10; // number of trees to create
  private final static int MAX_TREE_SIZE = 30; // max number of values in a tree
  private final static int MAX_NODE_VALUE = 100; // max key value to be inserted into a tree.


  /*********************************************************************
  *
  * Global variable
  *
  **********************************************************************/
  private static Random generator = new Random(System.nanoTime()); // so only ONE random number generator is created (for efficiency)


  /*********************************************************************
  *
  * main
  *
  **********************************************************************/
  public static void main( String[] args )
  {
    BinaryTree tree;
    boolean ordered;
    int treeSize; // number (distinct) values to put in the tree
    int insertedCount; // number of (distinct) values inserted into the tree so far
    int value; // random value to try inserting in the tree

    // Print out titles

    System.out.println( "\n\nCOMP 2140 Lab 4: Testing If Binary Trees are BST Ordered\n"
                          + "--------------------------------------------------------\n" );

    System.out.println( "Note: When a BST is printed out in an inorder traversal,\n      the values will be in sorted order." );

    // Construct NUM_TREES different trees,
    // some ordered and some not, and test
    // the isOrdered method on them.

    for ( int i = 0; i < NUM_TREES; i++ )
    {
      tree = new BinaryTree();

      // Flip a coin to decide if insertions should be
      // done in BST order or randomly in this tree.
      ordered = generator.nextInt( 2 ) == 0;

      // Get a random (non-zero) tree size for this tree.
      treeSize = 1 + generator.nextInt( MAX_TREE_SIZE );

      // Keep trying to insert random values until
      // treeSize distinct values have been inserted.
      insertedCount = 0;
      while ( insertedCount < treeSize )
      {
        value = generator.nextInt( MAX_NODE_VALUE );
        if ( ordered ) // try to insert in BST order
        {
          if ( tree.insert( value ) )
          {
            insertedCount++;
          }
        }
        else // insert, moving randomly left or right
             // IF the value was not already inserted
             // randomly somewhere in the tree
          if ( !tree.somewhereInTheTree( value ) )
          {
            tree.insertRandomly( value );
            insertedCount++;
          }

      } // end while insertedCount < treeSize

      // Print out the tree just created
      System.out.println( "\n\n----------------\nTree number " + i + " with " + treeSize + " values printed by an inorder traversal:\n" );
      tree.printTree();
      if ( ordered )
        System.out.println( "Values were inserted using a correct BST insertion method." );
      else
        System.out.println( "Values were NOT inserted in BST order (just inserted in random places in the tree)." );

      // Use isOrdered() to see if the tree is in BST order
      System.out.print( "\nYour isOrdered() method says that the tree  " );
      if ( tree.isOrdered() )
        System.out.print( "IS" );
      else
        System.out.print( "is NOT" );
      System.out.println( "  in BST order." );

    } // end for i < NUM_TREES

    // Print out a closing method

   
    System.out.println( "\n\n---------------------\nLab 4 ended normally.\n" );
  } // end main

} // end class Lab4



/*************************************************************************
*
* BinaryTree
*
* - A binary tree, which could either have values randomly placed in it,
*   OR could have values inserted in binary search tree (BST) order.
* 
*************************************************************************/

class BinaryTree
{


  /*********************************************************************
  *
  * Instance variable
  *
  **********************************************************************/
  private Node root; // A pointer to the root (if the tree's not empty)


  /*********************************************************************
  *
  * Constructor
  *
  **********************************************************************/
  public BinaryTree()  // constructs an empty binary tree
  {
    root = null;
  }


  /**********************************************************************
  * insert
  *
  * Insert a value (key) in binary search tree order.
  *
  * (A "driver" method that handles the special case
  * when the tree is empty, and otherwise calls a
  * Node method called "insertBelow" to do the work.)
  *
  **********************************************************************/
  public boolean insert( int key )
  {
    boolean inserted = false;

    if ( root == null )
    {
      root = new Node( key );
      inserted = true;
    }
    else
      inserted = root.insertBelow( key );

    return inserted;
  }


  /**********************************************************************
  * insertRandomly
  *
  * Insert a value (key) in a new leaf in a random place in the tree.
  *
  * (A "driver" method that handles the special case
  * when the tree is empty, and otherwise calls a
  * Node method called "insertRandomlyBelow" to do the work.)
  *
  **********************************************************************/
  public void insertRandomly( int key )
  {
    if ( root == null )
      root = new Node( key );
    else
      root.insertRandomlyBelow( key );
  }



  /**********************************************************************
  * somewhereInTheTree
  *
  * A search method for a random (non-binary-search-tree) binary tree.
  *
  * (A "driver" method that handles the special case when the
  * tree is empty, and otherwise calls a Node method called
  * "somewhereInTheTree" to do the work.)
  *
  **********************************************************************/
  public boolean somewhereInTheTree( int key )
  {
    boolean result = false;

    if ( root != null )
      result = root.somewhereInTheTree( key );

    return result;
  }


  /**********************************************************************
  * printTree
  *
  * A print method to print a tree in an inorder traversal
  *
  * (A "driver" method that handles the special case when the
  * tree is empty, and otherwise calls a Node method called
  * "printTree" to do the work.)
  *
  **********************************************************************/
  public void printTree()
  {
    if ( root == null )
      System.out.print( "The tree is empty!" );
    else
      root.printTree();
    System.out.println();
  }


  /*********************************************************************
  *
  * isOrdered
  *
  * Tests whether a binary tree is in BST order or not
  * (returns true if it is, and false if it isn't).
  *
  *
  * This public driver method handles the special case when the
  * tree is empty, and otherwise calls a recursive helper method 
  * in the Node class (also called "isOrdered") to do the work.
  *
  **********************************************************************/
  public boolean isOrdered()
  {
    boolean result;
    
    if(this.root == null) //if there is no elements in the list
      result = true; // empty list is 'sorted
    else
      result = root.isOrdered();
  return result;
  }
  
  } // end class BST

/*************************************************************************
* Node
*
* - a Node in a BinaryTree
* - the data stored in a Node is a single int
*
*************************************************************************/
class Node
{

  /*********************************************************************
  *
  * Global variable shared by all Nodes
  *
  **********************************************************************/
  private static Random generator = new Random(System.nanoTime()); // so only ONE random number generator is created (for efficiency)


  /*********************************************************************
  *
  * Instance variables
  *
  **********************************************************************/
  private int data;
  private Node left; // pointer to the left child of the Node
  private Node right; // pointer to the right child of the Node

  

  /*********************************************************************
  *
  * Constructors
  *
  **********************************************************************/
  public Node( int d, Node l, Node r )
  {
    data = d;
    left = l;
    right = r;
  }

  public Node( int d )
  {
    this( d, null, null );
  }

  /*********************************************************
  * somewhereInTheTree
  *
  * Decide whether key is in this or any of its descendants
  * by doing a pre-order traversal of the tree rooted at this.
  * (Used when the tree is not in BST order to see if a 
  * random value is already present in the tree or not, so
  * we can avoid inserting duplicates.)
  *
  * Returns true if key is in the tree and false otherwise.
  *
  * Makes use of the lazy evaluation of || (OR) and && (AND)
  * to stop looking any further if it finds key somewhere.
  **********************************************************/ 
  public boolean somewhereInTheTree( int key )
  {
    return ( data == key ) 
           || ( ( left != null ) && left.somewhereInTheTree( key ) )
           || ( ( right != null ) && right.somewhereInTheTree( key ) );
  }

  /*********************************************************
  * insertBelow
  *
  * Insert the key either as a child of this (if this has
  * no left/right child) as appropriate to maintain BST order
  * OR make a recursive call to insert key further down the tree
  * (under the appropriate child of this).
  *
  * Returns true if key is successfully inserted and false if
  * the key is a duplicate (and is therefore not inserted).
  **********************************************************/ 
  public boolean insertBelow( int key )
  {
    boolean inserted = false; // Assume key is a duplicate (not inserted)

    if ( key < data ) // key should be a descendant to the left of this
    {
      if ( left != null ) // this already has a left child
        inserted = left.insertBelow( key );
      else // this doesn't have a left child, so insert here!
      {
        left = new Node( key );
        inserted = true;
      }
    }
    else if ( data < key ) // key should be a descendant to the right of this
    {
      if ( right != null )// this already has a right child
        inserted = right.insertBelow( key );
      else// this doesn't have a right child, so insert here!
      {
        right = new Node( key );
        inserted = true;
      }
    }
    // else if key == data, we're already set to return false
 
    return inserted;
  } // end insertBelow


  /*********************************************************
  * Insert the key in a leaf randomly placed in the tree.
  *
  * Choose to go left or right randomly until we find
  * no child in the chosen direction, where we insert the new
  * leaf.
  *
  * Assumption: The key is NOT in the tree (i.e., we've already
  *             searched the tree and not found the key
  * Doesn't return anything, because it always inserts.
  **********************************************************/ 
  public void insertRandomlyBelow( int key )
  {
    boolean goLeft = generator.nextInt( 2 ) == 0;

    if ( goLeft ) // randomly chose to insert to the left
    {
      if ( left != null ) // move to the left child (which exists)
        left.insertRandomlyBelow( key );
      else // no left child, so insert!
        left = new Node( key );
    }
    else // randomly chose to insert to the right
    {
      if ( right != null ) // move to the right child (which exists)
        right.insertRandomlyBelow( key );
      else // no right child, so insert!
        right = new Node( key );
    }
  } // end insertRandomlyBelow


  /*********************************************************
  * Print the tree in an inorder traversal
  **********************************************************/ 
  public void printTree( )
  {
    if ( left != null )
      left.printTree();

    System.out.print( " " + data );

    if ( right != null )
      right.printTree();
  }


  /*********************************************************
  * isOrdered   (a RECURSIVE method)
  *
  * Returns true if the tree is in binary search tree order
  * and false otherwise.
  **********************************************************/ 
  public boolean isOrdered()
  {
    boolean result = false; 
    
    if (right == null && left == null) // this is a leaf
      result = true; // return true
    else {
      if (left != null)  // left side 
        result = (left.isOrdered() && data > left.getMax());
      
      if (right != null) // right side
        result = (right.isOrdered() && data < right.getMin());
    }//else
    
    return result;    
  }


  /*********************************************************
  * getMax   (a RECURSIVE method)
  *
  * Returns the maximum value stored in the subtree
  * consisting of this and its descendants.
  * (Assumes the tree is in BST order.)
  **********************************************************/ 
  public int getMax()
  {
    int result = data;
    
    if (this.right != null) // go right until end
      result = right.getMax();
    return result;
    
  }


  /*********************************************************
  * getMin   (a RECURSIVE method)
  *
  * Returns the minimum value stored in the subtree
  * consisting of this and its descendants.
  * (Assumes the tree is in BST order.)
  **********************************************************/ 
  public int getMin()
  {
    int result = data;
      
    if (this.left != null) // go left until end
      result = left.getMin();
    return result;
  }


} // end class Node
