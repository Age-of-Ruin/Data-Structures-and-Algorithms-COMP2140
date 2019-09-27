/*
 * LinkedList
 * 
 * COMP 1020
 * Instructor: Helen Cameron
 * Assignment: 3
 * @author     Richard Constantine
 * @version    2016/11/04
 * 
 * Purpose: To evaluate postfix expressions like a calculator.
 */

 /* Assignment Questions:
  * 
  * 1. Translate infix expression 16 - 5 + 3 x 2 to postfix.
  * 
  * First take operands: 
  * 16 5 3 2
  * 
  * The first operation to perform is 3 x 2:
  * 16 5 3 2 x
  *
  * The next operation is 16 - 5:
  * 16 5 - 3 2 x
  * 
  * Finally the last step is (16 - 5) + (3 x 2):
  * 16 5 - 3 2 x +
  * 
  * 2. Translate infix expression (2 x 5 - 6) x ((2 + 18) / 4) to postfix.
  * 
  * First take operands: 
  * 2 5 6 2 18 4
  * 
  * The first operation to perform is 2 x 5:
  * 2 5 x 6 2 18 4
  *
  * The next operation is (2 x 5) - 6:
  * 2 5 x 6 - 2 18 4
  * 
  * The next operation is 2 + 18:
  * 2 5 x 6 - 2 18 + 4
  * 
  * The next operation is (2 + 18) / 4:
  * 2 5 x 6 - 2 18 + 4 /
  * 
  * Finally the last step is (2 x 5 - 6) x ((2 + 18) / 4):
  * 2 5 x 6 - 2 18 + 4 / x 
  * 
  * 3. Translate posfix expression 2 7 2 3 x - x to infix.
  * 
  * The first operation (left to right) is x preceded by operands 2 then 3:
  * 2 7 (2 x 3) - x
  * 
  * The next operation is - preceded by 7 then (2 x 3):
  * 2 (7 - (2 x 3)) x
  * 
  * The final operation is x preceded by 2 then (7 - 2 x 3):
  * 2 x (7 - 2 x 3)
  * 
  * 4. Translate postfix expression 2 3 4 + 2 / 5 1 - - to infix.
  *
  * The first operation (left to right) is + preceded by 3 then 4:
  * 2 (3 + 4) 2 / 5 1 - -
  * 
  * The next operation is / preceded by (3 + 4) then 2:
  * 2 ((3 + 4) / 2) / 5 1 - - 
  * 
  * The next operation is / preceded by 2 then ((3 + 4) / 2):
  * (2 / ((3 + 4) / 2)) 5 1 - - 
  * 
  * The next operation is - preceded by (2 / ((3 + 4) / 2)) then 5:
  * ((2 / ((3 + 4) / 2)) - 5) 1  - 
  * 
  * The final operation is - preceded by ((2 / ((3 + 4) / 2)) - 5) then 1:
  * (2 / ((3 + 4) / 2)) - 5 - 1
  */

import java.util.*;
import java.io.*; 

public class A3Constantine7686561 {
  
  public static void main(String[] args) { 
    
    System.out.println("Comp 2140 - Assignment 3 - Fall 2016");
    System.out.println("Evaluating postfix expressions.");
    System.out.println("Model Solution");
    System.out.println("\nEnter the postfix expressions file name (.txt files only)");
    
    Stack operandStack = new Stack(); // declare new stack
    Scanner lineReader = null; // declare scanner to read lines
    int numExp = 0; // integer to store the number of postfix expressions
    Expression exp = null; // variable to hold each expression
    int operand1, operand2 = 0; // integers for the operands
    Integer result = null; // integer to store the result
    
    // Keyboard
      Scanner keyboard = new Scanner(System.in); // declare scanner to read input from keyboard
      String fileName = keyboard.next(); // read file name from keyboard and save it to a string
      keyboard.close();
      
      try{
        lineReader = new Scanner(new File (fileName)); // instantiate scanner to read lines
        
        System.out.println("\n**********************************************\n");
        System.out.println("The postfix expressions and their values");
        System.out.println("========================================\n");
        
        if(lineReader.hasNextLine()){
          numExp = lineReader.nextInt(); // store the integer number of postfix expressions
          lineReader.nextLine();
        }//if
        
        for (int i = 0; i < numExp; i++){ // iterate through the lines of the file
          if(lineReader.hasNextLine()){
            exp = new Expression(lineReader.nextLine()); // store expression as tokens
            System.out.println("Postfix expression: " + exp.toString()); // print postfix expression
            
            // Analyze Result
            for(int j = 0; j < exp.tokenArray.length; j++){ // iterate through the tokens in the expression
              String token = exp.tokenArray[j];
              
              if(exp.isOperand(token))
                operandStack.push(Integer.parseInt(token)); // push operands to the stack
              
              else if(!exp.isOperand(token)){ // if not operand (ie if token is operator)
                
                if (!operandStack.isEmpty()){
                  operand2 = operandStack.pop(); // pop operand 2
                  
                  if(!operandStack.isEmpty()){
                    operand1 = operandStack.pop(); // pop operand 1
                    result = performOperation(operand1, token, operand2); // perform operation
                    operandStack.push(result); // push result to the stack
                  }//if
                  else
                    System.out.println("ERROR: Operator " + token + " is missing its first operand (second operand is " 
                                         + operand2 + "\nExpression erroneous, value cannot be computed.\n");
                }//if
                else
                  System.out.println("ERROR: Operator " + token + " is missing both operands" + 
                                       "\nExpression erroneous, value cannot be computed.\n");                
              }//else if
              else{
                System.out.println("ERROR: token is neither an operand nor an operator"); 
              }//else
            }//for
            if (!operandStack.isEmpty()){
              result = operandStack.pop(); // pop result of the expression
              
              if(!operandStack.isEmpty()){
                System.out.println("ERROR: too many operands on stack\nExpression erroneous, value cannot be " +
                                   "computed.\n"); 
                result = null;
              }//if
            }//if
            else{
              System.out.println("ERROR: stack is empty at the end\nExpression erroneous, value cannot be computed.\n"); 
              result = null;
            }//else
          }//if
          if(result !=null)
            System.out.println("Value of expression: " + result + "\n"); // print value of expression
        }//for
      }//try
      catch (IOException e) {
        System.out.println(e.getMessage());
      }//catch
      
      if(result != null)
        System.out.println("Final Result (with no erroneous intermediary expression): " + result); // print final result
      
      System.out.println("Program Ends");
      
  }//main    
  
  /* 
   * [Main method initializes the operand stack and performs the operations set out by this assignment (ie perform
   *  postfix operations).]
   * [Input: Recieves arguments (as an aray of strings) when the program is executed - the method will also read from a
   *  .txt file]
   * [Output: Outputs the result of each postfix expression]
   *
   * @param [firstParam: string value containing arguments passed by the user]
   * 
   * @return [Void - N/A]
   */
  
  public static int performOperation(int operand1, String operator, int operand2){
    int result = 0;

    if(operator.equals("/")){ // check division operation
      result = operand1 / operand2;
    }//if
    else if(operator.equals("*")){ // check multiplication operation
      result = operand1 * operand2;
    }//else if   
    
    else if(operator.equals("+")){ // check addition operation
      result = operand1 + operand2;
    }//else if  
    
    else if(operator.equals("-")){ // check subtraction operation
      result = operand1 - operand2;
    }//else if  
    
    return result;
    
  }//performOperation
  
  /* 
   * [performOperation method detects which of the 4 operations (+, -, /, *) will be executed on the current operands.]
   * [Input: Receives input in the form of 2 integer operands and string representing the operation to perform]
   * [Output: Outputs the result of each postfix expression]
   *
   * @param [firstParam: integer value of the first operand to be worked on]
   * @param [secondParam: string value containing the operator to execute on the operands]
   * @param [thirdParam: integer value of the second operand to be worked on]
   * 
   * @return [the result of the given operation]
   */
  
}//A3Constantine7686561

class Stack {
 
//Instance Variables 
  public Node top;
  
  public Stack(){
    top = null;
  }//constructor
  
  class Node {
    
//Instance Variables 
    public int item;
    public Node next;
    
    public Node(int i, Node n) { 
      item = i;
      next = n;
    }//constructor
    
  }//Node

  public void push(int item){
    
    if(top == null)
      top = new Node(item, null);
    else
      top = new Node(item,top);
    
  }//push
  
  /* 
   * [push creates a new node and places at the top of the stack.]
   * [Input: Receives input in the form of an integer to add to the stack]
   * [Output: A new node on top of stack ]
   *
   * @param [firstParam: integer value to be placed onto the stack]
   * 
   * @return [Void - N/A]
   */
  
  public int pop(){
    
    int result = top.item;
    top = top.next;
    
    return result;
   
  }//pop
  
  /* 
   * [pop removes the top element of the stack and returns it.]
   * [Input: Receives a call to remove/capture the top element]
   * [Output: Returns this top element]
   *
   * @param [N/A]
   * 
   * @return [the integer value of the top element of the stack]
   */
  
  
  public int top(){

    return top.item;
    
  }//top
  
  /* 
   * [top looks at the top the value of the stack without removing it.]
   * [Input: Receives a call to capture the top element of the stack]
   * [Output: Returns the value of this top element]
   *
   * @param [N/A]
   * 
   * @return [the integer of the top value on the stack]
   */
  
  
  public boolean isEmpty(){
    
    return top == null;
    
  }//top
  
  /* 
   * [isEmpty checks if the stack is empty (ie contains no elements).]
   * [Input: Receives a call to to check the status of the stack]
   * [Output: Returns a boolean stating whether the stack is currently empty]
   *
   * @param [N/A]
   * 
   * @return [a boolean that states true if the stack is empty (and false if it is not)]
   */
  
}//Stack

class Expression {
  
//Instance Variables 
  public String[] tokenArray;
  
  public Expression(String line){
    tokenArray = line.trim().split("\\s+"); // split line into tokens
  }//constructor

  public boolean isOperand(String input){
    
    try  
    {  
      Integer.parseInt(input);  
      return true;  
    }  
    catch( Exception e )  
    {  
      return false;  
    }  
    
  }//isOperand
  
  /* 
   * [isOperand checks if the current token is an integer (and therefore an operand, not an operator).]
   * [Input: Receives the token as a string to verify if it is an operand/operator]
   * [Output: Returns a boolean determining whether the token is an operand or not]
   *
   * @param [string value of the token to be checked]
   * 
   * @return [a boolean value stating true if the token is an integer (ie an operand) or false otherwise]
   */
  
  public String toString(){
    String result = "";
    
    for(int i = 0; i < tokenArray.length; i++)
      result += (tokenArray[i] + " ");
    
    return result;
    
  }//toString
  
    /* 
   * [toString converts the entire postfix expression to a string.]
   * [Input: Receives a call to convert the current expression (stored as an array of tokens) into a printable string]
   * [Output: A string variable containg the elements of the current expression]
   *
   * @param [N/A]
   * 
   * @return [a string variable of the expression]
   */

}//Expression