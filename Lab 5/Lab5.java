/*************************************************************************
*
* Lab5 - This program creates leaf-based 2-3 trees, randomly 
*        choosing:
*          (1) the number of insertions to perform (up to
*              NUM_INSERTS), and
*          (2) the values to insert (values <= MAX_DATA_ITEM).
*        The insertion method does not permit duplicates,
*        so the number of values actually inserted may
*        differ from the random number of insertions chosen.
*
*        For each of the leaf-based 2-3 trees created:
*
*        It prints out the tree after the tree is created using
*        an inorder traversal, so you can _see_ what's in
*        the 2-3 tree.
*
*        Then the program does two batches of searches on the tree:
*          (1) It searches for values that were inserted into
*              the tree (it stores inserted values in an array,
*              so that it randomly chooses an index in the array
*              to tell it what value to search for).  These
*              searches should all succeed.
*          (2) It then searches for random values.  Since they
*              are randomly-chosen values, most of them are not
*              in the tree, so most of these searches will fail.
*
***************************************************************************/

import java.io.*;
import java.util.*;

public class Lab5
{
  /********************************************************************
  * A bunch of named constants that control the 2-3 tree testing.     *
  ********************************************************************/
  private static final int NUM_TREES = 5; // The number of trees to build and search.
  private static final int NUM_INSERTS = 25; // The maximum number of random inesrts 
                                          // to do in each tree.
  private static final int MAX_DATA_ITEM = 500; // The largest a randomly-chosen
                                          // data item can be.
  private static final int NUM_SUCCESSFUL_SEARCHES = 10; // The number of searches
                                          // to do for items we know we inserted.
  private static final int NUM_RANDOM_SEARCHES = 10; // The number of searches
                                          // to do for randomly-chosen items
                                          // (most of which won't be in the tree).

  /***************************************************
  * main                                             *
  *                                                  *
  * Purpose: Construct a number of leaf-based        *
  *           2-3 trees, performing searches on      *
  *           each tree.                             *
  ***************************************************/
  public static void main( String[] args )
  {
    System.out.println( "\n\nCOMP 2140 Lab 5   Fall 2016" );
    System.out.println( "Searching in leaf-based 2-3 trees." );
    System.out.println( "==================================\n" );

    treeTester();

    System.out.println( "\nProgram ends.\n" );
  } 

  /********************************************************************
  * treeTester                                                        *
  *                                                                   *
  * This method does the actual work of constructing and searching    *
  * a number of leaf-based 2-3 trees.                                 *
  ********************************************************************/
  private static void treeTester()
  {
    TwoThreeTree tree;
    Random generator = new Random();
    int numInserts;
    int[] insertedDataItems = new int[ NUM_INSERTS ];
    int idx, randomItem, successfulSearches;

    // Test on NUM_TREES different leaf-based 2-3 trees.
    for ( int i = 0; i < NUM_TREES; i++ )
    {

      // Construct a tree:
      // Do a random number of insertion into an empty leaf-based 2-3 tree
      // (no more than NUM_INSERTS insertions).
      tree = new TwoThreeTree();
      numInserts = generator.nextInt( NUM_INSERTS ) + 1;
      for ( int j = 0; j < numInserts; j++ )
      {
        insertedDataItems[j] = generator.nextInt( MAX_DATA_ITEM );
        tree.insert( insertedDataItems[j] );
      }
      
      // Print out the data stored in the tree (in an inorder traversal)
      System.out.println( "\nData items in Tree " + i + ": " );
      System.out.println(   "---------------------" );
      tree.printTree();

      // Now see if searches work.

      // First, search for some data items that were inserted into the tree.
      // Print out only searches that fail (they indicate a problem in the code).
      System.out.println( "\n\nSearching for items that were inserted:" );
      successfulSearches = 0;
      for ( int j = 0; j < NUM_SUCCESSFUL_SEARCHES; j++ )
      {
        // Randomly choose one of the items that was inserted into the tree.
        idx = generator.nextInt( numInserts );
        if ( !tree.containsDataItem( insertedDataItems[idx] ) )
          System.out.println( "*** ERROR: Search says that " + insertedDataItems[idx] 
                              + " is not in the tree." );
        else
          successfulSearches++;
      } // end for j
      System.out.println( "    " + successfulSearches + " out of " 
        + NUM_SUCCESSFUL_SEARCHES
        + " searches for inserted items were successful." );

      // Second, search for some random data items (which may or may not
      // have been inserted in the tree).
      System.out.println( "\nSearching for random items (should mostly fail):" );
      for ( int j = 0; j < NUM_RANDOM_SEARCHES; j++ )
      {
        // Randomly choose an item.
        randomItem = generator.nextInt( MAX_DATA_ITEM );
        if ( tree.containsDataItem( randomItem ) )
          System.out.println( "   A search for " + randomItem + " succeeded."  );
        else
          System.out.println( "   A search for " + randomItem + " failed."  );
      } // end for j
      System.out.println( "\n==================================\n" );
    } // end for i 

  } // end treeTester

} // end class Lab5


/********************************************************************
*********************************************************************
* class TwoThreeTree                                                *
*********************************************************************
********************************************************************/
class TwoThreeTree
{

  /********************************************************************
  *********************************************************************
  * private class TwoThreeNode    (inside class TwoThreeTree)         *
  *********************************************************************
  ********************************************************************/
  private class TwoThreeNode
  {
    /****** Instance variables (seven of them!) *******/

    // The parent (is only null in the root)
    public TwoThreeNode parent;

    // A data item.
    // Used only in leaves --- contains random junk in interior nodes
    // (so make sure you don't look at it in interior nodes).
    public int dataItem;

    // The index value(s) used only to guide searches.
    // Used only in interior nodes --- these contain random
    // junk in leaves (so make sure you that don't look at 
    // them when you are at a leaf).
    public int indexValue; // the only index value in a 2-child node or
                           // the smaller index value in a 3-child node
                           // (contains random junk in a leaf)
    public int largerIndexValue; // the second, larger index value in a 3-child node
                                 // (contains random junk in a 2-child node or leaf)


    // The children (are all null if this node is a leaf containing a data item).
    public TwoThreeNode left; // a pointer to the leftmost child
    public TwoThreeNode middle; // a pointer to the middle child if 3 children
                                 // (if only 2 children, this pointer is null)
    public TwoThreeNode right; // a pointer to the rightmost child

    /********************************************************************
    * Constructor #1                                                    *
    *                                                                   *
    * PURPOSE:  To construct a leaf, which contains only a data item.   *
    ********************************************************************/
    public TwoThreeNode( int datum )
    {
      dataItem = datum;
      parent = null; // will be set later when we know what it is
      left = middle = right = null;

      // Nobody should ever look at these values, so they can be random junk!
      indexValue = generator.nextInt( 100000 );
      largerIndexValue = generator.nextInt( 100000 );
    }

    /********************************************************************
    * Constructor #2                                                    *
    *                                                                   *
    * PURPOSE:  To construct an interior node that has exactly two      *
    *           children (left and right) and exactly one index value   *
    *           (which must be stored in indexValue).                   *
    ********************************************************************/
    public TwoThreeNode( int idxVal, TwoThreeNode l, TwoThreeNode r )
    {
      indexValue = idxVal;
      parent = null; // will be set later when we know what it is
      left = l;
      right = r;
      middle = null;
      l.parent = this;
      r.parent = this;

      // Nobody should ever look at these values, so they can be random junk!
      largerIndexValue = generator.nextInt( 100000 );
      dataItem = generator.nextInt( 100000 );
    }

    /********************************************************************
    * printTree                                                         *
    *                                                                   *
    * PURPOSE:  To recursively print the data items stored in the       *
    *           leaves of the subtree rooted at this (the implicit      *
    *           parameter) by performing an inorder traversal.          *
    * NOTE:  No index values (stored in interior nodes) are printed.    *
    *        The only printing is done at the leaves, which store the   *
    *        data items.                                                *
    ********************************************************************/
    public void printTree()
    {
      if ( this.left == null ) // this is a leaf (no children)
        System.out.print( this.dataItem + " " );
      else // this is an interior node (has two or three children)
      {
        this.left.printTree();
        if ( this.middle != null )
          this.middle.printTree();
        this.right.printTree();
      }
    } // end printTree()

    public TwoThreeNode findLeaf( int searchItem )
  {
      TwoThreeNode result = root;
      
      if (left == null && middle == null && right == null) // base case (at a leaf)
        result = this;
      
      if (middle == null){ // only 2 children
        if (left != null && searchItem < indexValue) // check left child
          result = left.findLeaf(searchItem);
        else if (right != null && searchItem >= indexValue) // check right child
          result = right.findLeaf(searchItem);
      } // if
      
      else{ // has 3 children
        if (left != null && searchItem < indexValue) // check left child
        result  = left.findLeaf(searchItem);
        else if (middle != null && searchItem >= indexValue && searchItem < largerIndexValue) // check middle child
          result  = middle.findLeaf(searchItem);
        else if (right != null && searchItem >= largerIndexValue) // check right child
          result  = right.findLeaf(searchItem);
      }// else

      return result;     
      
    } // end findLeaf

  } // end class TwoThreeNode
  

  /********************************************************************
  *********************************************************************
  * Back to class TwoThreeTree                                        *
  *********************************************************************
  ********************************************************************/

  private TwoThreeNode root;

  /****** A shared random number generator used by node constructors and insert *****/

  private static Random generator = new Random();


  /********************************************************************
  * Constructor                                                       *
  *                                                                   *
  * PURPOSE:  To construct an empty leaf-based 2-3 tree.              *
  ********************************************************************/
  public TwoThreeTree()
  {
    root = null;
  }

  /********************************************************************
  * containsDataItem                                                  *
  *                                                                   *
  * PURPOSE:  To search the leaf-based 2-3 tree for searchItem.       *
  *           It returns true if the tree contains searchItem in a    *
  *           leaf (i.e., contains a data item == searchItem) and     *
  *           false otherwise.                                        *
  * NOTE:  findLeaf does the work of searching.  This method simply   *
  *        checks the leaf lsearch that the search reaches to see     *
  *        if it contain searchItem.                                  *
  ********************************************************************/
  public boolean containsDataItem( int searchItem )
  {
    TwoThreeNode lsearch = null;

    if ( root != null )
      lsearch = findLeaf( searchItem );

    return lsearch != null && lsearch.dataItem == searchItem;
  }


  /********************************************************************
  * insert                                                            *
  *                                                                   *
  * PURPOSE:  To newDataItem into the leaf-based 2-3 tree.            *
  ********************************************************************/
  public void insert( int newDataItem )
  {
    int largestIdxVal;
    TwoThreeNode newLeaf;
    TwoThreeNode existingLeaf; // used when the tree contains only one leaf
    TwoThreeNode lsearch; // used when the tree contains at least one interior node

    if ( root == null ) // Special case: emtpy tree
    {
      root = new TwoThreeNode( newDataItem );
    }
    else if ( root.left == null ) // Special case: 1-leaf tree (the root only)
    {
      if ( newDataItem != root.dataItem ) // no duplicates!
      {
        // Construct a new parent (the new root) and a new leaf.
        existingLeaf = root;

        // First, construct the new leaf.
        newLeaf = new TwoThreeNode( newDataItem ); 

        // Now, construct the new root (parent of the new leaf and the existing leaf).
        // Should the new leaf be the left or right sibling of the existing leaf?
        if ( newDataItem < existingLeaf.dataItem )
        {
          root = new TwoThreeNode( existingLeaf.dataItem, newLeaf, existingLeaf );
        }
        else
        {
          root = new TwoThreeNode( newDataItem, existingLeaf, newLeaf );
        }
      }
    }
    else  // Normal case: already at least two leaves and one interior node.
    {
      lsearch = findLeaf( newDataItem );

      if ( lsearch.dataItem != newDataItem ) // no duplicates!
      {
        newLeaf = new TwoThreeNode( newDataItem );
        if ( lsearch.parent.middle == null ) // parent has only two children
          easyInsertLeaf( lsearch, newLeaf );
        else
          splitAndPushUpAtLeaves( lsearch, newLeaf );
      }
      
    }
  } // end insert


  /********************************************************************
  * easyInsertLeaf                                                    *
  *                                                                   *
  * PURPOSE:  To insert newLeaf as a sibling of leaf lsearch.         *
  *           Note that this task is different from adding a new      *
  *           interior node child as a sibling because at the leaves  *
  *           we look at data items and at interior nodes we look at  *
  *           index values.                                           *
  * GUARANTEE:  lsearch's parent has only TWO children and ONE index  *
  *             value (so it has room for the new leaf child) and     *
  *             newLeaf does not contain a duplicate value.           *
  ********************************************************************/
  private void easyInsertLeaf( TwoThreeNode lsearch, TwoThreeNode newLeaf )
  {
    TwoThreeNode lsparent = lsearch.parent; // to save typing

    if ( lsearch == lsparent.left )
    {
      // We're going to add a new smaller index value in the parent
      // (the existing index value will be the larger one).
      lsparent.largerIndexValue = lsparent.indexValue;
      if ( newLeaf.dataItem < lsearch.dataItem )
      {
        lsparent.indexValue = lsearch.dataItem;
        lsparent.middle = lsearch;
        lsparent.left = newLeaf;
      }
      else
      {
        lsparent.indexValue = newLeaf.dataItem;
        lsparent.middle = newLeaf;
      }
    }
    else
    {
      // We're going to add a new larger index value in the parent
      // (the existing index value will be the smaller
      // one, so it should stay in lsparent.indexValue).
      if ( newLeaf.dataItem < lsearch.dataItem )
      {
        lsparent.largerIndexValue = lsearch.dataItem;
        lsparent.middle = newLeaf;
      }
      else
      {
        lsparent.largerIndexValue = newLeaf.dataItem;
        lsparent.middle = lsearch;
        lsparent.right = newLeaf;
      }
    }
    newLeaf.parent = lsparent;
  } // end easyInsertLeaf



  /********************************************************************
  * splitAndPushUpAtLeaves                                            *
  *                                                                   *
  * PURPOSE:  To insert newLeaf beside leaf lsearch.                  *
  *           Note that this task is different from adding a new      *
  *           interior node child beside another interior node        *
  *           because, at the leaves, we look at data items, while    *
  *           at interior nodes we look at index values.              *
  * GUARANTEE:  lsearch's parent already has THREE children and       *
  *             TWO index values (so the parent has NO room for the   *
  *             new leaf child) and newLeaf does not contain a        *
  *             duplicate value.  Leaf newLeaf belongs immediately    *
  *             beside lsearch, but we don't know whether it belongs  *
  *             to the left or the right of lsearch.                  *
  ********************************************************************/
  private void splitAndPushUpAtLeaves( TwoThreeNode lsearch, TwoThreeNode newLeaf )
  {
    TwoThreeNode originalParent = lsearch.parent;
    TwoThreeNode newParent; // split the existing parent ==> create a new parent

    if ( lsearch == originalParent.right )
    {
      // lsearch and newLeaf will be children of newParent.
      // originalParent will keep its existing left child and 
      // middle child (which becomes its right child) and its
      // existing smaller index value (which is already in indexValue).
      if ( newLeaf.dataItem > lsearch.dataItem )
      {
        // lsearch and newLeaf are the left and right child of the 
        // new parent, respectively.
        newParent = new TwoThreeNode( newLeaf.dataItem, lsearch, newLeaf );
      }
      else
      {
        // newLeaf and lsearch are the left and right child of the 
        // new parent, respectively.
        newParent = new TwoThreeNode( lsearch.dataItem, newLeaf, lsearch );
      }
      // The original parent gets its existing left and middle children
      // and its existing smaller index value (in indexValue already).
      originalParent.right = originalParent.middle;
    }
    else if ( lsearch == originalParent.middle )
    {
      // Only the larger of lsearch or newLeaf will be a (left) child of newParent.
      // The right child of newParent will be originalParent.right.
      // originalParent will keep its existing left child
      // and its right child will be the smaller of lsearch or newLeaf.
      if ( newLeaf.dataItem > lsearch.dataItem )
      {
        // newLeaf and originalParent.right will be the left and right 
        // children of the new parent, respectively.
        newParent = new TwoThreeNode( originalParent.right.dataItem, newLeaf,
                    originalParent.right );

        originalParent.right = lsearch;
      }
      else
      {
        // lsearch and originalParent.right are the left and right children
        // of the new parent, respectively.
        newParent = new TwoThreeNode( originalParent.right.dataItem, lsearch,
                    originalParent.right );

        originalParent.right = newLeaf;
        newLeaf.parent = originalParent;
        originalParent.indexValue = newLeaf.dataItem;
      }
    }
    else  // lsearch is originalParent's left child
    {
      // originalParent gets children lsearch and newLeaf (not which is left child yet)
      // newParent gets originalParent's current middle and right children.
      newParent = new TwoThreeNode( originalParent.largerIndexValue, 
                                    originalParent.middle, originalParent.right );
      if ( newLeaf.dataItem < lsearch.dataItem )
      {
        originalParent.left = newLeaf;
        originalParent.right = lsearch;
        originalParent.indexValue = lsearch.dataItem;
      }
      else
      {
        originalParent.left = lsearch;
        originalParent.right = newLeaf;
        originalParent.indexValue = newLeaf.dataItem;
      }
      newLeaf.parent = originalParent;
    }
    // originalParent has become a two-child node
    originalParent.largerIndexValue = generator.nextInt( 100000 );
    originalParent.middle = null;
    
    // Now we need to insert newParent into the tree
    if ( root == originalParent ) // Special case: original parent has no parent
    {
      // The tree grows taller: create a new root above originalParent and newParent
      root = new TwoThreeNode( newParent.left.dataItem, originalParent, newParent );
    }
    else // original parent has a parent
    {
      if ( originalParent.parent.middle == null ) // parent has room!
        easyInsertInteriorNode( originalParent, newParent, newParent.left.dataItem );
      else // parent does NOT have room, must split it and push up
        splitAndPushUpAtInteriorNode( originalParent, newParent, newParent.left.dataItem );
    }

  } // end splitAndPushUpAtLeaves


  /********************************************************************
  * easyInsertInteriorNode                                            *
  *                                                                   *
  * PURPOSE:  To insert new interior node newNode as a sibling of     *
  *           interior node existing.                                 *
  *           Note that this task is different from adding a new      *
  *           leaf node child as a sibling because at the leaves, we  *
  *           look at data items, and, at interior nodes, we look at  *
  *           index values.                                           *
  * GUARANTEE:  existing's parent has only TWO children and ONE index *
  *             value (so it has room for the new child).             *
  *             Furthermore, splitIndexValue is the correct index     *
  *             value to use to distinguish between existingChild     *
  *             and newChild.                                         *
  ********************************************************************/
  private void easyInsertInteriorNode( TwoThreeNode existing, TwoThreeNode newNode,
                                       int splitValue )
  {
    TwoThreeNode eparent = existing.parent; // to save typing

    if ( existing == eparent.left )
    {
      // We're going to add a new smaller index value in the parent
      // (the existing index value will be the larger one).
      eparent.largerIndexValue = eparent.indexValue;
      eparent.indexValue = splitValue;
      if ( newNode.indexValue < existing.indexValue )
      {
        eparent.middle = existing;
        eparent.left = newNode;
      }
      else
      {
        // existing is already eparent's left child
        eparent.middle = newNode;
      }
    }
    else // existing is its parent's right child
    {
      // We're going to add a new larger index value in the parent
      // (the existing index value will be the smaller
      // one, so it should stay in eparent.indexValue).
      eparent.largerIndexValue = splitValue;
      if ( newNode.indexValue < existing.indexValue )
      {
        // existing is already eparent's right child
        eparent.middle = newNode;
      }
      else
      {
        eparent.middle = existing;
        eparent.right = newNode;
      }
    }
    newNode.parent = eparent;
  } // end easyInsertInteriorNode


  /********************************************************************
  * splitAndPushUpAtInteriorNode                                      *
  *                                                                   *
  * PURPOSE:  To insert newChild beside interior node existingChild.  *
  *           Note that this task is different from adding a new      *
  *           leaf child beside an existing leaf child, because       *
  *           at the leaves, we look at data items, while at the      *
  *           interior nodes we look at index values.                 *
  * GUARANTEE:  existingChild's parent already has THREE children     *
  *             and TWO index values (so the parent has NO room for   *
  *             newChild) and newChild belongs immediately to the     *
  *             right of existingChild.  Furthermore,                 *
  *             splitIndexValue is the correct index value to use     *
  *             to distinguish between existingChild and newChild.    *
  ********************************************************************/
  private void splitAndPushUpAtInteriorNode( TwoThreeNode existingChild,
                                             TwoThreeNode newChild,
                                             int splitIndexValue )
  {
    TwoThreeNode originalParent = existingChild.parent;
    TwoThreeNode newParent;
    // Temporary arrays to use when splitting a too-full parent
    TwoThreeNode[] fourChildren = new TwoThreeNode[4];
    int[] threeIndexValues = new int[3];
    int i;

    // We have four children (originalParent's three children, plus newChild)
    // and three index values (originalParent's two index values, plus
    // splitIndexValue, which distinguishes between existingChild and newChild).
    // We need them in order so we can figure out what stays in originalParent
    // (the two smaller children and the smaller index value) and what goes
    // to newParent (the two larger children and the largest index value)
    // and which index value is the middle index value that gets pushed up.
    // Since originalParent's three children and two index values are already
    // in sorted order and we know that newChild belongs immediately to the
    // right of existingChild, we can do something similar to the sifting
    // step in insertion sort to sift newChild and splitIndexValue down to
    // their correct positions.
    fourChildren[0] = originalParent.left;
    fourChildren[1] = originalParent.middle;
    fourChildren[2] = originalParent.right;
    threeIndexValues[0] = originalParent.indexValue;
    threeIndexValues[1] = originalParent.largerIndexValue;
    i = 2;
    while ( i > 0 && fourChildren[i] != existingChild )
    {
      threeIndexValues[i] = threeIndexValues[i-1];
      fourChildren[i+1] = fourChildren[i];
      i--;
    }
    fourChildren[i+1] = newChild;
    threeIndexValues[i] = splitIndexValue;
    
    // Now just split
    newParent = new TwoThreeNode( threeIndexValues[2], fourChildren[2], fourChildren[3] );
    originalParent.left = fourChildren[0];
    originalParent.middle = null;
    originalParent.right = fourChildren[1];
    originalParent.indexValue = threeIndexValues[0];
    originalParent.largerIndexValue = generator.nextInt( 100000 );

    if ( newChild == originalParent.left || newChild == originalParent.right )
      newChild.parent = originalParent;
    
    // Now we need to insert newParent into the tree (and push up threeIndexValues[1])
    if ( root == originalParent ) // Special case: original parent has no parent
    {
      // The tree grows taller: create a new root above originalParent and newParent
      root = new TwoThreeNode( threeIndexValues[1], originalParent, newParent );
    }
    else // original parent has a parent
    {
      if ( originalParent.parent.middle == null ) // parent has room!
        easyInsertInteriorNode( originalParent, newParent, threeIndexValues[1] );
      else // parent does NOT have room, must split it and push up
        splitAndPushUpAtInteriorNode( originalParent, newParent, threeIndexValues[1] );
    }
  } // end splitAndPushUpAtInteriorNode

  /********************************************************************
  * printLeaf                                                         *
  *                                                                   *
  * PURPOSE: To print the tree in inorder (just the data, which       *
  *          will be printed in sorted order).                        *
  *          (This is the public driver method.)                      *
  ********************************************************************/
  public void printTree()
  {
    if ( root == null )
      System.out.println( "The 2-3 tree is empty." );
    else
      root.printTree();
  }

  /********************************************************************
  * findLeaf                                                          *
  *                                                                   *
  * PURPOSE: To return a pointer to the leaf (called Lsearch in       *
  *          class notes) that is reached in a search for data item   *
  *          searchItem.  (Because data items are stored only in the  *
  *          the leaves, all searches for any purpose must go all the *
  *          way to the leaves.)                                      *
  ********************************************************************/
  private TwoThreeNode findLeaf( int searchItem )
  {
    TwoThreeNode lSearch = null;
//    TwoThreeNode next = null;
//    
//    if (root != null){
//      next = root;
//      lSearch = null;
//    } //if
//    
//    while (next != null){
//      lSearch = next;
//      
//      if (next.middle == null){ // only 2 children
//        if (searchItem < next.indexValue) // check left child
//          next = next.left;
//        else if (searchItem >= next.indexValue) // check right child
//          next = next.right;
//      } // if
//      
//      else{ // has 3 children
//        if (searchItem < next.indexValue) // check left child
//        next = next.left;
//        else if (searchItem >= next.indexValue && searchItem < next.largerIndexValue) // check middle child
//          next = next.middle;
//        else if (searchItem >= next.largerIndexValue) // check right child
//          next = next.right;
//      }// else
//    } //while
//    
    if (root != null)
      lSearch = root.findLeaf(searchItem);
    
    return lSearch;    
      
  } // end findLeaf
  

} // end class TwoThreeTree
