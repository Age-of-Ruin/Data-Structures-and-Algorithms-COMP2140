import java.io.*;
import java.util.*;

/*************************************************************************
* COMP 2140   Fall 2016   Assignment 3 Solution
*
* Instructor: Helen Cameron
*
* Evaluating postfix expressions with constant integer operands.
**************************************************************************/

public class A3Solution
{

  /***********************************************************************
  * main - Read in the input and process it, printing out any output
  ************************************************************************/
  public static void main( String[] args)
  { 

    // Print output headers.
    System.out.println( );
    System.out.println("Comp 2140  Assignment 3   Fall 2016");
    System.out.println("Evaluating postfix expressions.");
    System.out.println("Model Solution\n");

    processAllExpressions();

    System.out.println("\nProgram ends.");
    System.out.println( );
  } // end method main

  /***********************************************************************
  * processAllExpressions
  *
  * Get the input file's name (prompt user for keyboard input).
  * Then read in and process all the postfix expressions.
  ************************************************************************/
  public static void processAllExpressions()
  {
    // For reading in each line in the file
    Scanner file;
    int numExpressions;
    String inputLine;
    // For processing an expression
    Expression postfix = null;
    int value;

    try
    {      
      file = new Scanner( new File( getInputFileName() ) );
      System.out.println( "\n\n**********************************************\n");

      // Print out a title for the expression processing

      System.out.println( "The postfix expressions and their values"
                       + "\n========================================" );

      // Read in the number of postfix expressions to read in

      numExpressions = file.nextInt();
      file.nextLine(); // skip to the next line

      // Process each expression

      for ( int i = 0; i < numExpressions; i++ )
      {
        inputLine = file.nextLine().trim();
        postfix = new Expression( inputLine );
        System.out.println( "\nPostfix expression: " + postfix );
        value = postfix.evaluatePostfix();
        if ( value != Integer.MIN_VALUE )
          System.out.println( "Value of expression: " + value );
        else
          System.out.println( "Expression erroneous, value cannot be computed." );
      } // end for

    }
    catch (IOException e) 
    {
      System.out.println("IO Error: " + e.getMessage());
    }
  
  } // end processAllExpressions

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


} // end class A3Solution



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
  public int evaluatePostfix()
  {
    int result = Integer.MIN_VALUE;
    boolean errorFree = true;
    Stack operandStack = new Stack();
    int operand1, operand2;

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
          // invalid token: not an operator or an operand (int constant)
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
