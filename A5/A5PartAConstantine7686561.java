import java.io.*;
import java.util.*;

/**
* A5SolutionFall2016
*
* COMP 2140
* INSTRUCTOR    Helen Cameron
* ASSIGNMENT    Assignment 5
* @author       Richard Constantine
* @version      5 November 2016
*
* PURPOSE: To edit the A4 solution to use binary trees instead of a hash table.
*/

public class A5PartAConstantine7686561
{

  /***********************************************************************
  * main - Read in the input and process it, printing out any output
  ************************************************************************/
  public static void main( String[] args)
  { 

    // Print output headers.
    System.out.println( );
    System.out.println("Comp 2140  Assignment 5   Fall 2016");
    System.out.println("Evaluating postfix expressions with variables.");
    System.out.println("Model Solution\n");

    processAllAssignmentStatements();

    System.out.println("\nProgram ends.");
    System.out.println( );
  } // end method main

  /***********************************************************************
  * processAllAssignmentStatements
  *
  * Get the input file's name (prompt user for keyboard input).
  * Then read in and process all the assignment statements
  * (which contain postfix expressions using variables and constants).
  ************************************************************************/
  public static void processAllAssignmentStatements()
  {
    // For reading in each line in the file
    Scanner file;
    int numStatements;
    String inputLine;
    // For processing an assignment statement
    String variableID; 
    Expression postfix = null;
    int value;
    int equalsPosn;
    Table variables = new Table();
    Variable variable;

    try
    {      
      file = new Scanner( new File( getInputFileName() ) );
      System.out.println( "\n\n**********************************************\n");

      // Print out a title for the assignment statements processing

      System.out.println( "The assignment statements and the values of their right sides"
                       + "\n============================================================" );

      // Read in the number of assignment statements to read in

      numStatements = file.nextInt();
      file.nextLine(); // skip to the next line

      // Process each assignment statement

      for ( int i = 0; i < numStatements; i++ )
      {
        inputLine = file.nextLine().trim();
        equalsPosn = inputLine.indexOf( "=" );
        if ( equalsPosn > 0 )
        {
          variableID = (inputLine.substring( 0, equalsPosn )).trim();
   if ( Variable.isVariableID( variableID ) )
   {
            System.out.println( "\nThe variable to be assigned to: " + variableID );
            postfix = new Expression( inputLine.substring( equalsPosn+1 ) );
            System.out.println( "The postfix expression on the right side: " + postfix );
            value = postfix.evaluatePostfix( variables );
            if ( value != Integer.MIN_VALUE )
            {
              System.out.println( "The value of the expression: " + value );
              variable = variables.findKey( variableID );
              if ( variable != null )
                variable.setValue( value );
              else
                variables.insert( new Variable( variableID, value ) );
            }
            else
              System.out.println( "Expression erroneous, value cannot be computed;"
                                   + " invalid assignment." );
          }
          else // invalid variable ID
          {
            System.out.println( "\nInvalid variable ID (" + variableID + ")"
                                + " on the left side of the assignment statement;"
                                + " invalid assignment." );
            System.out.println( "Input line: " + inputLine );
          }
        } // end if ( equalsPosn > 0 )
        else
        {
          System.out.println( "\nNo \"=\" in assignment statement;"
                                + " invalid assignment." );
          System.out.println( "Input line: " + inputLine );
        }
      } // end for

      System.out.println( "\n\nVariables and Their Values"
                          + "\n--------------------------" );

      variables.printAll();

    }
    catch (IOException e) 
    {
      System.out.println("IO Error: " + e.getMessage());
    }
  
  } // end processAllAssignmentStatements

  /***********************************************************************
  * getInputFileName
  *
  * Return the input file's name (retrieved using keyboard input).
  ************************************************************************/
  private static String getInputFileName()
  {
    // For reading in the file name (using keyboard input)
    Scanner keyboard; 
    String fileName;

    // Allow user to choose file with keyboard input.
    keyboard = new Scanner( System.in );
    System.out.println( "\nEnter the postfix expressions file name (.txt files only): " );
    fileName = keyboard.nextLine();

    return fileName;
  } // end getInputFileName


} // end class A4SolutionFall2016



/***********************************************************************
************************************************************************
*  The Expression class.
*
*  An expression is simply an array of tokens (which are stored as Strings).
***********************************************************************
************************************************************************/
class Expression
{
  private static final String DIGITS = "0123456789";
  private static final String OPERATORS = "+-*/";

  private String[] tokens;

  public Expression( )
  {
    tokens = null;
  }

  public Expression( String inputLine )
  {
    tokens = inputLine.trim().split( "\\s+" );
  }

  
  /***********************************************************************
  * evaluatePostfix
  *
  * Return the value of the calling postfix expression (or Integer.MIN_VALUE,
  * if the postfix expression contains one or more errors)
  ************************************************************************/
  public int evaluatePostfix( Table variables )
  {
    int result = Integer.MIN_VALUE;
    boolean errorFree = true;
    Stack operandStack = new Stack();
    int operand1, operand2;
    Variable var;

    // Process tokens from left to right
    for ( int i = 0; i < tokens.length && errorFree; i++ )
    {
      tokens[i] = tokens[i].trim();
      if ( !tokens[i].equals( "" ) ) // ignore the empty-string token.
      {
        if ( isInteger( tokens[i] ) ) // if constant operand, push onto the stack.
        {
          operandStack.push( Integer.parseInt( tokens[i] ) );
        }
        else if ( Variable.isVariableID( tokens[i] ) )
        {
          var = variables.findKey( tokens[i] );
          if ( var != null )
            operandStack.push( var.getValue() );
          else
          {
            errorFree = false;
            System.out.println( "ERROR: undeclared variable " + tokens[i] );
          }
        }
        else if ( isOperator( tokens[i] ) ) // if operator, pop its operands and 
        {                                   // push the result of performing the 
          if ( !operandStack.isEmpty() )    // operator on its operands.
          {
            operand2 = operandStack.pop();
            if ( !operandStack.isEmpty() )
            {
              operand1 = operandStack.pop();
              operandStack.push( operationResult( operand1, tokens[i], operand2 ) );
            }
            else
            {
              // missing only one operand (the first one)
              errorFree = false;
       System.out.println( "ERROR: Operator " + tokens[i] 
                  + " is missing its first operand (second operand is "
                  + operand2 + ")" );
            }
          }
          else
          {
            // missing both operands
            errorFree = false;
     System.out.println( "ERROR: Operator " + tokens[i] 
                + " is missing both of its operands." );
          }
        }
        else
        {
          // invalid token: not an operator or an operand (constant or variable)
          errorFree = false;
          System.out.println( "ERROR: Token " + tokens[i] 
                  + " is neither an operator nor an operand." );
        }
      }  // end if ( !tokens[i].equals( "" ) )
    } // end for

    if ( errorFree && !operandStack.isEmpty() )
    {
      result = operandStack.pop();
      if ( !operandStack.isEmpty() )
      {
        result = Integer.MIN_VALUE;
        System.out.println( "ERROR: missing operator(s) "
                  + "(stack contains more than one value at the end)" );
      }
    }
    // else something went terribly wrong (return Integer.MIN_VALUE)
    
    return result;
  } // end evaluatePostfix

  
  /***********************************************************************
  * isOperand
  *
  * Return true if the String contains an integer;
  * Return false otherwise.
  ************************************************************************/
  private static boolean isInteger( String token )
  {
    return token.matches( "\\d+|[-+]\\d+" );
  }

  
  /***********************************************************************
  * isOperator
  *
  * Return true if the String consists of a single char that is one of
  * + or - or * or /.
  * Return false otherwise.
  ************************************************************************/
  private static boolean isOperator( String token )
  {
    return (token.length() == 1) && (OPERATORS.indexOf( token.charAt(0) ) >= 0);
  }

  
  /***********************************************************************
  * operationResult
  *
  * Perform the operation on its two operands and return the result.
  ************************************************************************/
  private static int operationResult( int operand1, String operation, int operand2 )
  {
    int result = Integer.MIN_VALUE;
    char op = operation.charAt(0); // switch statments work with char, not String

    switch ( op )
    {
      case '+': result = operand1 + operand2; break;
      case '-': result = operand1 - operand2; break;
      case '*': result = operand1 * operand2; break;
      case '/': result = operand1 / operand2; break;
    }

    return result;
  }

  
  /***********************************************************************
  * toString
  *
  * Return a String representation of the Expression for printing.
  ************************************************************************/
  public String toString()
  {
    String result = "";

    for ( int i = 0; i < tokens.length; i++ )
    {
      result += tokens[i];
      if ( (i+1) != tokens.length ) // a blank between consecutive tokens
        result += " ";
    }

    return result;
  }

} // end class Expression



/***********************************************************************
************************************************************************
*  The Stack class 
*
*  Implemented as an ordinary linked list with a top pointer (pointing
*  to the first node) and no dummy nodes.
*
*  The stack stores ints (as Integers).
*
*  Push, pop, and top all happen at the top (of course), with NO loops.
*
***********************************************************************
************************************************************************/
class Stack
{
  private Node top; // a pointer to the top (first node) of the stack.

  
  /***********************************************************************
  * constructor creates an empty stack.
  ************************************************************************/
  public Stack()
  {
    top = null;
  }

  
  /***********************************************************************
  * isEmpty
  *
  * Returns true if the stack is empty, false if it is not empty.
  ************************************************************************/
  public boolean isEmpty()
  {
    return top == null;
  }

  
  /***********************************************************************
  * push
  *
  * Add the new item to the top of the stack: add a new first node,
  * (and point top at the new first node).
  ************************************************************************/
  public void push( int newItem )
  {
    top = new Node( new Integer(newItem), top );
  }

  
  /***********************************************************************
  * top
  *
  * Return the item contained in the node that top points at
  * (or Integer.MAX_VALUE, if the stack is empty).
  ************************************************************************/
  public int top()
  {
    int result = Integer.MAX_VALUE;

    if ( top != null )
      result = ((Integer)top.getItem()).intValue();

    return result;
  }

  
  /***********************************************************************
  * pop 
  *
  * Remove the node pointed at by top (and return the item it contains).
  * Returns Integer.MAX_VALUE if the stack is empty.
  ************************************************************************/
  public int pop()
  {
    int result = Integer.MAX_VALUE;

    if ( top != null )
    {
      result = ((Integer)top.getItem()).intValue();
      top = top.getNext();
    }

    return result;
  }

} // end class Stack



/***********************************************************************
************************************************************************
*  The Node class.
*
*  Ordinary linked-list nodes, storing an Object item (so it can
*  be used by both the Stack class (which stores ints as Integers)
*  and the Table class (which stores Variables).
***********************************************************************
************************************************************************/
class Node
{
  private Object item;
  private Node next;

  public Node( Object i, Node n )
  {
    item = i;
    next = n;
  }

  public Object getItem()
  {
    return item;
  }

  public Node getNext()
  {
    return next;
  }

  public void setNext( Node n )
  {
    next = n;
  }
} // end class Node


/***********************************************************************
************************************************************************
*  The Table class.
*
*  A table class that uses binary search tree's (BST's) to store the 
*  variables used in evaluting the postfix expressions.
***********************************************************************
************************************************************************/
class Table
{
  private BSTNode bRoot;
    
  /***********************************************************************
  * contructor creates an empty BST
  ************************************************************************/
  public Table()
  {
    bRoot = null;   
  }//constructor
  
  /***********************************************************************
  * findKey
  * 
  * Use the identifier's unicode value as indices to traverse and search 
  * the BST for a given identifier. If the nodes are inserted in BST order, 
  * then binary logic can be used to traverse the tree until the key is 
  * located or the search "falls off" the tree.
  ************************************************************************/
  
  public Variable findKey( String key )
  {
    boolean found = false;
    BSTNode curr = bRoot;
    
    while (curr != null && !found){
      if (key.equals(curr.data.getIdentifier()))
        found = true;
      else if (key.compareTo(curr.data.getIdentifier()) < 0)
        curr = curr.left;
      else // key > currID
        curr = curr.right;
    }//while
    
    if (curr != null)
      return curr.data;
    else
      return null;
    
  } // end findKey

  /***********************************************************************
  * insert
  * 
  * Uses the variables identifier unicode value to compare and insert the 
  * variable in BST order.
  ************************************************************************/
  
  public void insert( Variable var )
  {
    if (bRoot == null)
      bRoot = new BSTNode(var);
    else{
      BSTNode curr = bRoot;
      BSTNode prev = null;
      
      while (curr != null && curr.data != var){
        prev = curr;
        if ((var.getIdentifier()).compareTo(curr.data.getIdentifier()) < 0)
          curr = curr.left;
        else // newItem > currID
          curr = curr.right;
      }//while
      
      if (curr == null){
        if ((var.getIdentifier()).compareTo(prev.data.getIdentifier()) < 0)
          prev.left = new BSTNode(var);
        else // newItem > prevID
          prev.right = new BSTNode(var);
      }//if
      else
        System.out.println("Attempted duplicate insertion - cannot insert duplicates");     
    }//else
      
  } // end insert

  /***********************************************************************
  * printAll
  * 
  * A recursive driver method that passes the work to the printAll method
  * located in the BSTNode class.
  ************************************************************************/
  
  public void printAll()
  {
    if (bRoot != null){
      bRoot.printAll(); // recursive driver
    }//if
    
  } // end printAll
  
    /***********************************************************************
    ************************************************************************
    *  The BSTNode class.
    *
    *  This instantiates the nodes that are used to store the variables 
    *  in the BST created in the Table class.
    ***********************************************************************
    ************************************************************************/
  
  private class BSTNode{
    
    //Instance variables
    public Variable data;
    public BSTNode left;
    public BSTNode right;
    
     /***********************************************************************
      * constructor that creates a node containing a variable and with it's
      * children set to null (ie creates a leaf - assigning of children is
      * dedicated to the insert method of the Table class.
      ************************************************************************/
    
    public BSTNode(Variable d){
      data = d;
      left = right = null;
    }//constructor
    
      /***********************************************************************
      * printAll
      * 
      * A recursive method that performs an inorder traversal of the BST such 
      * that it prints all the values in sorted order. This is accomplished by
      * first recursively traversing the left child, then print the variable,
      * and finally recursively traversing the right child.
      ************************************************************************/
    
    private void printAll(){
      if (left != null)
        left.printAll();
      
      System.out.println("Variable " + data.getIdentifier() + " = " + data.getValue());
      
      if (right != null)
        right.printAll();
    }//printAll
    
  }//BSTNode
  
} // end class Table

/***********************************************************************
************************************************************************
*  The Variable class.
*
*  It contains the variable's name (identifier) and its value (an int).
***********************************************************************
************************************************************************/
class Variable
{
  private String identifier;
  private int value;

  public Variable( String id, int val )
  {
    identifier = id;
    value = val;
  }

  public String getIdentifier() { return identifier; }
  public int getValue() { return value; }
  public void setValue( int v ) { value = v; }

  public static boolean isVariableID( String name )
  {
    return name.length() != 0 && name.toLowerCase().matches("[a-z][a-z[0-9]]*" );
  }

  public boolean equals( String id )
  {
    return id.equals( this.identifier );
  }
  public boolean equals( Variable var )
  {
    return (var.identifier).equals( this.identifier );
  }

  public String toString()
  {
    return "Variable " + identifier + " = " + value;
  }

} // end class Variable
