/*
 * Postfix Expressions
 * 
 * COMP 1020
 * Instructor: Helen Cameron
 * Assignment: 4
 * @author     Richard Constantine
 * @version    2016/11/21
 * 
 * Purpose: To extend assingment 3 (evaluating postfix expressions like a calculator) implenting functional variables
 * using a Variable class and store the using a hash table in the Table class.
 * 
 * 
 */

import java.io.*;
import java.util.*;

public class A4Constantine7686561
{
  
  public static void main( String[] args)
  { 
    
    // Print output headers.
    System.out.println( );
    System.out.println("Comp 2140  Assignment 4   Fall 2016");
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
    * 
    * ADDED: Now reads the first token as the indentifier and the second
    * token as the equal sign "=" (both as strings) - proper error messages
    * are printed if the line is not a correct assignment statement. The 
    * rest of the tokens are processed as the postfix expression.
    * 
    * The method also utilizes the Variable and Table classes to store any 
    * declared identifiers as variable and into the hash table (which can be 
    * accessed later when performing the operations).
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
    Table table = new Table(37,13); // create new table (for variables)

    try
    {      
      file = new Scanner( new File( getInputFileName() ) );
      System.out.println( "\n\n**********************************************\n");
      
      // Print out a title for the expression processing
      
      System.out.println( "The assignment statements and the values of their right sides"
                           + "\n=============================================================" );
      
      // Read in the number of postfix expressions to read in
      
      numExpressions = file.nextInt();
      file.nextLine(); // skip to the next line
      
      // Process each expression
      
      for ( int i = 0; i < numExpressions; i++ )
      {
        inputLine = file.nextLine().trim();
        postfix = new Expression( inputLine ); 
        
        Variable newVar = new Variable(postfix.getID(), 0); // temporary variable
        
        if (newVar.id != null && postfix.equalCheck.equals("=")){ // check valid assignment
          // variable ID print
          System.out.println("\nThe variable to be assigned to: " + postfix.getID());
          // post fix expression print    
          System.out.println( "The postfix expression on the right side: " + postfix);
          
          // value
          value = postfix.evaluatePostfix(table);
          
          if ( value != Integer.MIN_VALUE ){
            System.out.println( "Value of expression: " + value );
            newVar.value = value; // store result of the expression to the variable
            table.insert(newVar); // store variable in table
          }//if
          
          else
            System.out.println( "Expression erroneous, value cannot be computed; invalid assignment." );
        }//if
        
        else if(newVar.id == null) // invalid assignment print statements
          System.out.println("\nInvalid variable ID (" + postfix.getID() + ") on the left side of the assignment " + 
                               "statement; invalid assignment." +
                               "\nInput line: " + inputLine);
        else if(!postfix.equalCheck.equals("="))
          System.out.println("\nNo \"=\" in assignment statement; invalid assignment." +
                             "\nInput line: " + inputLine);
        
      } // end for
    }
    catch (IOException e) 
    {
      System.out.println("IO Error: " + e.getMessage());
    }
    
    System.out.println("\nVariables and Their Values");
    System.out.println("--------------------------");
    table.printAll();
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
  
  
} // end class A4Constantine7686561

/***********************************************************************
  ************************************************************************
  *  ADDED: The Variable class.
  *
  *  The Variable class acts to store postfix expression result along
  *  with an identifier. It also checks for correct identifer notation
  *  (ie must start w/ lower case, proceeded by a mix of lower case,
  *  upper case, and digits). If the check is not met, then the variable
  *  will contain a null identifier and value.
  ***********************************************************************
  ************************************************************************/

class Variable{
  
//Instance Variables
  String id;
  int value;
  
  public Variable (String i, int v){
    boolean check = true;
    if(i.charAt(0) < 'a' || i.charAt(0) > 'z'){ // first character check 
      
      check = false;
    }
    else{
      for(int j = 1; j < i.length(); j++){ // iterate through all characters except the 1st
        if(i.charAt(j) < 'a' || i.charAt(j) > 'z') // lower case check 
          if(i.charAt(j) < 'A' || i.charAt(j) > 'Z') // upper case check 
          if (i.charAt(j) < '0' || i.charAt(j) > '9') // digit check
          check = false;
      }//for
    }//else
    
    if (check == true){
      id = i;
      value = v;
    }//if
    else{
      id = null;
      value = 0;
    }//else
  }//constructor 
    
   /***********************************************************************
    * ADDED: equals
    *
    * The equals method compares a string value to the current variables identifer
    * to determine if the identifier has already been used previously.
    ************************************************************************/
  
  public boolean equals (String idCompare){
    boolean result = false;
    
    if (id.equals(idCompare))
      result = true;
    
    return result;
  }//equals
  
    /***********************************************************************
    * ADDED: toString
    *
    * The toString method turns the variable into a printable string.
    ************************************************************************/
  
  public String toString(){
    String result = "Variable " + id + " = " + value;
    return result;
  }//toString
  
}//Variable

 /***********************************************************************
  ************************************************************************
  *  ADDED: The Table class.
  *
  *  The Table class uses a hash table to store variables (from the Variable class)
  *  by computing a polynomial hashcode using a variables identifier (via Horner's
  *  method), and then index within the tabl by modding the polyHashCode by
  *  the table size.
  *  
  *  Note: This class uses separate chaining to store the variables in
  *  the table.
  ***********************************************************************
  ************************************************************************/

class Table{
  
//Instance Variables
  int tableSize; 
  int a; 
  Node[] hashTable; 
  
  public Table(int input1, int input2) {
    tableSize = input1; 
    a = input2;
    
    hashTable = new Node[tableSize];
    for (int i = 0; i < tableSize; i++)
      hashTable[i] = null;
  }//constructor
  
    /***********************************************************************
    * ADDED: hash
    * 
    * The hash method computes the polynomial hash code via Horner's method
    * (discussed in class). This works by adding a summing all the characters
    * and mulitplying each step of the sum by a constant. Taking the polynomial
    * hash code mod table size yields the index within the table to store
    * the variable.
    ************************************************************************/
  
  private int hash(String id){
    int polyHashCode = id.charAt(0);
    
    for (int i = 1; i < id.length(); i++){
      polyHashCode *= a;
      polyHashCode += id.charAt(i);
      polyHashCode = Math.abs(polyHashCode) % tableSize;
      
    }//for
    
    return polyHashCode; // return index = |polyHashCode| % tableSize
    
  }//hash
  
   /***********************************************************************
    * ADDED: search
    *
    * The search method performs a linear search for a particular variably by
    * looping through each index of the table, then by looping through each node 
    * (containing a variable), and compares each of the variable's identfier's 
    * to the String key (passed as a parameter).
    * 
    * If the variable is located, the method returns the variable. If it cannot
    * be located, the method returns null.
    ************************************************************************/
  
  public Variable search(String key){
    
    Variable result = null;
    boolean found = false;
    
    for (int i = 0; i < tableSize; i++){ // iterate through each index of the table
      Node curr = hashTable[i];
      Node prev = null;
      
      while (curr != null && !found){ // iterate through each table entry in a given index
        Variable currVar = (Variable) curr.getItem();
        
        if(currVar.equals(key)) // use the equals method from the Variable class
          found = true;
        
        prev = curr;
        curr = curr.getNext();
      }//while
      
      if (prev != null && found)
        result = (Variable) prev.getItem();
    }//for
    
    return result;
    
  }//search
  
    /***********************************************************************
    * ADDED: insert
    *
    * The insert method calls the hash method to compute the index location
    * for a particular variable identifier. Using separate chaining (ie a linked
    * list at each index) the variable is stored in front of any other variables
    * located at that index by rearranging the node pointers.
    ************************************************************************/
  
  public void insert(Variable in){
    if(in.id != null){ // insert only if valid identifier
      int index = hash(in.id);
      Variable duplicate = search(in.id);
      
      if(duplicate == null){ // if no duplicate
        if (hashTable[index] == null){ // empty case
          Node newNode = new Node(in, null);
          hashTable[index] = newNode;
        }//if
        else { // insert at front of list
          Node newNode = new Node(in, hashTable[index].getNext());
          hashTable[index].setNext(newNode); 
        }//else
      }//if
      else{ // if duplicate exists
        duplicate.value = in.value;
      }//else
    }//if
  }//insert
  
    /***********************************************************************
    * ADDED: printAll()
    *
    * The printAll method loops through each index of the table, as well as
    * each node/variable located at a particular index and prints every variable
    * stored in the table.
    ************************************************************************/
  
  public void printAll(){
    
    for (int i = 0; i < tableSize; i++){ // iterate through each index of the table
      Node curr = hashTable[i];
      
      while (curr != null){ // iterate through each table entry in a given index
        System.out.println(curr.getItem().toString()); // print each entry
        curr = curr.getNext();
      }//while
    }//for
  }//printAll  
  
  
}//Table

/***********************************************************************
  ************************************************************************
  *  The Expression class.
  *
  *  An expression is simply an array of tokens (which are stored as Strings).
  * 
  *  ADDED: The expression class now reads the first 2 tokens as the variable
  *  indentifier and the equals sign (used in the variable assignment) respectively.
  *  These can be used to confirm a valid assignment statement.
  * 
  *  Note: The class performs the postfix operations in the same fashion
  *  (using tokens[] to the store the operators and operands).
  ***********************************************************************
  ************************************************************************/
class Expression
{
  private static final String DIGITS = "0123456789";
  private static final String OPERATORS = "+-*/";
  
  private String[] tokens;
  
  public String id;
  public String equalCheck;
  
  public Expression( )
  {
    id = null; // store the variable name
    tokens = null; // store postfix tokens
    equalCheck = null; // check equals sign
  }
  
  public Expression( String inputLine )
  {
    String[] temp = inputLine.trim().split( "\\s+" );
    id = temp[0]; // store the variable name
    equalCheck = temp[1];
    tokens = new String[temp.length -2];
    
    for(int i = 2; i < temp.length; i++)
      tokens[i-2] = temp[i]; // store postfix tokens
  }
  
  public String getID(){
    return id;
  }//getID
  
  /***********************************************************************
    * evaluatePostfix
    *
    * Return the value of the calling postfix expression (or Integer.MIN_VALUE,
    * if the postfix expression contains one or more errors)
    * 
    * ADDED: This method now checks for variables by searching for each token
    * in the hash table. If located, it will push the value of this variable
    * (as an operand) to the stack.
    ************************************************************************/
  public int evaluatePostfix(Table table)
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
      var = table.search(tokens[i]);
      if ( !tokens[i].equals( "" ) ) // ignore the empty-string token.
      {
        if ( isInteger( tokens[i] ) ) // if constant operand, push onto the stack.
        {
          operandStack.push( Integer.parseInt( tokens[i] ) );
        }
        
        else if (var != null)
        {
          operandStack.push(var.value);
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
                               + " is neither an operator nor an operand, or is an undeclared variable." );
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
