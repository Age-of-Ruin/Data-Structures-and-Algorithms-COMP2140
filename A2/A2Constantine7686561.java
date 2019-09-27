/*
 * LinkedList
 * 
 * COMP 1020
 * Instructor: Helen Cameron
 * Assignment: 2
 * @author     Richard Constantine
 * @version    2016/10/25
 * 
 * Purpose: Creates a doubly linked list (using the classes LinkedList and Node) to store/edit text from a file. In 
 * particular, read the author and title information of books from a text file, and maintain a ranking
 * system for the books that is also laid out in the commands of the text file.
 */

import java.util.*;
import java.io.*; 

public class A2Constantine7686561 {
  
  public static void main(String[] args) { 
    
    // Constants
    final int STARTING_RANK = 1;
    
    // Instance Variables
    int numBooks = 0; // count number of books
    int posn1, posn2, posn3, posn4; // used to track positions of quotations that define author and title within a line
    Book book; // create instance of class Book to be used to store titles and authors
    String author, title;
    LinkedList list = new LinkedList(); // create instance of LinkedList
    Scanner lineReader = null; // declare scanner to read lines
    
    System.out.println("Comp 2140 - Assignment 2 - Fall 2016");
    System.out.println("Keeping track of popular books in a doubly-linked list.");
    System.out.println("Model Solution");
    System.out.println("\nEnter the book lists file name (.txt files only)");
    
    // Keyboard
    Scanner keyboard = new Scanner(System.in); // declare scanner to read input from keyboard
    String fileName = keyboard.next(); // read file name from keyboard and save it to a string
    keyboard.close();
    
    System.out.println("\n************************************************************************************\n");
    
    // Read the book info from a file and store it in the list
    try 
    {
      lineReader = new Scanner(new File (fileName)); // instantiate scanner to read lines
      String line = ""; // declare string to hold contents of the current line being processed
      
      if(lineReader.hasNextLine()){
        numBooks = lineReader.nextInt(); // store the integer number of books aka 'n' (first line of file)
        lineReader.nextLine();
      }//if
      
      for (int i = 0; i < numBooks; i++){
        if(lineReader.hasNextLine()){
          line = lineReader.nextLine(); // first book of the initial list
          
          posn1 = line.indexOf('\"'); // positions of the quotation marks that define the author and title
          posn2 = line.indexOf('\"', posn1 + 1);
          posn3 = line.indexOf('\"', posn2 + 1);
          posn4 = line.indexOf('\"', posn3 + 1);
          
          author = line.substring(posn1+1, posn2); // read author from line
          title = line.substring(posn3+1, posn4); // read title from the line
          book = new Book(title, author); // store this in an instance of book
          
          list.insert(STARTING_RANK, book); // add this bookInfo and starting rank to the list
        }//if
      }//for
      
      // Print initial book list (ie previous days top 'n' popular books)
      System.out.println("Initial top-" + numBooks + " book list:");
      System.out.println("------------------------");
      list.printAll();
      
      // Start processing todays commands
      System.out.println("\nThe Day's Commands");
      System.out.println("------------------");
      
      int count; 
      while (lineReader.hasNextLine()) { // check for next line
        count = 0; // count groups of 'n' commands
        System.out.println(); 
          
        while (lineReader.hasNextLine() && count < numBooks) { // process group of 'n' commands and check for next line
          count++; // used to process 'n' commands
          line = lineReader.nextLine(); // read next line
            System.out.println("Command: " + line); // print the command
            
            posn1 = line.indexOf('\"'); // positions of the quotation marks that define the author and title
            posn2 = line.indexOf('\"', posn1 + 1);
            posn3 = line.indexOf('\"', posn2 + 1);
            posn4 = line.indexOf('\"', posn3 + 1);
            
            char cmd = line.charAt(0); // read the command
            author = line.substring(posn1+1, posn2); // read author from line
            title = line.substring(posn3+1, posn4); // read title from the line
            
            book = new Book(title, author); // store this in an instance of book
            
            LinkedList.Node query = list.search(book); // either the node containing the book being searched or null
            
            boolean cmdCheck = (cmd == 'A' || cmd == 'Q' || cmd == 'H' || cmd == 'B'); // everything except returns
            
            if (query == null && cmdCheck){ // if the current book being commanded is not in list and not a return cmd
              list.insert(STARTING_RANK, book); // add the new book to the list with starting rank of 1
            }//if
            else if (cmdCheck){ // if the list contains the book and not a return cmd
              query.rank++; // increase the rank of that search item
              list.rearrange(query); // rearrange its place in the list (without creating new node)
          }//else if
        }//while
        
        // Print the top 5 books
        System.out.println("\n\tTop " + numBooks + " Books");
        System.out.println("\t------------");
        list.printN(numBooks);
      }//while
      
      // Print complete & sorted book list
      System.out.println("\nComplete Book List (End of Day)");
      System.out.println("-------------------------------");
      list.printAll();
      lineReader.close();
    }//try
    
    catch(IOException e){    
      System.out.println(e.getMessage());
    }//catch 
    
  }//main
  
  /* 
   * [Main method initializes the linked list and performs the operations set out by this assignment (ie read input 
   *  from a text file and maintain a doubly linked list of popular books).]
   * [Input: Recieves arguments (as an aray of strings) when the program is executed - the method will also read from a
   *  .txt file]
   * [Output: Maintains the list of books via commands read from the text file and prints the results in 'n' intervals 
   *  ('n' specified in the text file) and also prints the final sorted result]
   *
   * @param [firstParam: string value containing arguments passed by the user]
   * 
   * @return [Void - N/A]
   */
  
}//A2Constantine7686561

class LinkedList {
  
//Instance Variables
  public Node top;
  
  public LinkedList(){
    top = null;
  }//constructor
  
  class Node {
    
    public int rank;
    public Node fwd;
    public Node back;
    public Book bookInfo;
    
    public Node(int r, Node f, Node bk, Book b) { 
      rank = r;
      fwd = f;
      back = bk;
      bookInfo = b;
    }//constructor
    
  }//Node
  
  public void insert (int newRank, Book newBookInfo){
    Node prev = null;
    Node curr = top;
    
    // Ordered Insert
    while ((curr != null) && (newRank <= curr.rank)){ // while newRank <= currRank and not the end of the list
      prev = curr; // move pointers along
      curr = curr.fwd;
    }//while
    
    if (prev != null){ // if this is not the first item (insert new node)
      prev.fwd = new Node(newRank, curr, prev, newBookInfo);
      
      if (curr != null) // if there exists a node after this one then edit its back pointer
        curr.back = prev.fwd;
    }//if
    
    else{ // operating on first item (insert at front)
      top = new Node(newRank, curr, null, newBookInfo);
      
      if(curr != null) // if 2nd node exists
        curr.back = top;
    }//else
  }//insert 
  
  /* 
   * [The insert method performs an ordered insert of a BOOK into the linked list with respect to its rank by CREATING
   *  A NEW NODE and inserting in the correct position.]
   * [Input: Receives input from the a method and acts to insert a value into an ordered doubly linked list]
   * [Output: The list will have the newly added element in the correct position]
   *
   * @param [firstParam: An integer value containing the rank of the book]
   * @param [secondParam: The book info (ie author and title) stored in a book class]
   * 
   * @return [Void - N/A]
   */
    
  public void insertNode (Node key){
    Node prev = null;
    Node curr = top;

    // Ordered Insert
    while ((curr != null) && (key.rank <= curr.rank)){ // while keyRank <= currRank and not the end of the list
      prev = curr; // move pointers along
      curr = curr.fwd;
    }//while
    
    if (prev != null){ // if this is not the first item (insert new node)
      prev.fwd = key;
      key.back = prev;
      key.fwd = curr;
      
      if (curr != null) // if there exists a node after this one then edit its back pointer
        curr.back = key;
    }//if
    
    else{ // operating on first item (insert at front)
      top = key;
      key.back = prev;
      
      if(curr != null) // if 2nd node exists
        key.fwd = curr;
        curr.back = key;
    }//else
  }//insert 
  
  /* 
   * [The insertNode method performs an ordered insert of a NODE into the linked list with respect to its rank WITHOUT
   *  CREATING A NEW NODE, but by rearranging the forward and pointers of the list.]
   * [Input: Receives input from the a method and acts to insert a node into an ordered doubly linked list]
   * [Output: The list will have the newly added element in the correct position]
   *
   * @param [firstParam: The node of the book being inserted]
   * 
   * @return [Void - N/A]
   */
  
  public Node search(Book key){
    Node curr = top;
    Node result = null; // node to hold the result (if found)
    boolean flag = false; // flag to detect when search is complete
    
    while (curr != null && !flag){ // keep searching while key has not been found, while not end of list and not empty list
      if (key.title.equals(curr.bookInfo.title) && key.author.equals(curr.bookInfo.author)){ // check books title and author     
        result = curr; // change result to point at current node
        flag = true; // stop searching
      }//if
      else
      curr = curr.fwd; //move curr pointer forward
    }//while  
    return result; // return pointer to key or null
  }//search 
  
  /* 
   * [The search method searchs the list for a particular book and returns either a pointer to the node of the search
   *  query, or returns null.]
   * [Input: Receives input from the a method and acts to search for a particular book within the doubly linked list]
   * [Output: Returns either null if not found or a pointer to the node that contains the search result]
   *
   * @param [secondParam: The book info (ie author and title) that will be searched for within the list]
   * 
   * @return [either null (if not book is not found) or a pointer to the node containing the book]
   */
  
  public void rearrange(Node key){

    // "Delete" the item
    if(key.back == null){ // if this is the first item in the list
      top = key.fwd; // adjust the top point
      key.fwd.back = null; // set the 2nd node to point back at null (aka start of list)
    }//if
    if (key.fwd == null) // if this is the last item in the last
      key.back.fwd = null; // set the previous node to point at null (aka end of list)
    else if (key.fwd != null && key.back != null){      
    key.back.fwd = key.fwd; // change previous node to point at next node
    key.fwd.back = key.back; // change the next  node to point back at the previous node
    }//else if
    
    // Reinsert item
    insertNode(key); // performs insert by rearranging pointers
     
  }//rearrange  
  
  /* 
   * [The rearrange method rearranges the pointers of the list (without creating new node) to accomodate a change in 
   *  rank (ie performs an ordered insert of a node).]
   * [Input: Receives input as node from the a method and acts reorder the node to the correct position within the
   *  ordered doubly linked list]
   * [Output: The node passed to this method will be placed in the correct, ordered position within the list]
   *
   * @param [firstParam: The Node that will be re positioned in the list]
   * 
   * @return [Void - N/A]
   */
  
  public void printAll (){
    
    Node prev = null;
    Node curr = top;
    
    while (curr != null){
      prev = curr; 
      curr = curr.fwd;
      System.out.println("Rank: " + prev.rank + ": \"" + prev.bookInfo.title + "\""  + " by " + prev.bookInfo.author);
    }//while
  }//printAll
  
  /* 
   * [The printAll method uses a while loop to print the entire contents of the linked list.]
   * [Input: Receives input/call from a method and acts to print all elements of the linked list]
   * [Output: The list will be printed for the user]
   *
   * @param [N/A]
   * 
   * @return [Void - N/A]
   */
  
    public void printN (int n){
    
    Node prev = null;
    Node curr = top;
    int count = 0;
    
    while (curr != null && count < n){
      prev = curr; 
      curr = curr.fwd;
      count++;
      System.out.println("\tRank: " + prev.rank + ": \"" + prev.bookInfo.title + "\""  + " by " + prev.bookInfo.author);
    }//while
  }//printN
  
  /* 
   * [The printN method uses a while loop to print only the highest 'n' rank books.]
   * [Input: Receives input/call from a method and acts to print the requested elements of the linked list]
   * [Output: The list will be printed for the user]
   *
   * @param [firstParam: Integer n denotes how many of the top ranked books will be printed]
   * 
   * @return [Void - N/A]
   */
    
}//LinkedList

  /* 
   * [The LinkedList class combines both the LinkedList class and Node class to implement a doubly linked list.]
   * [Input: Receives input from the a method and acts to initialize the instance variables and maintain the list]
   * [Output: Sets initial values for these variables, maintains popular book ranks and prints them appropriatly]
   *
   * @param [firstParam: Integer containing the rank of the book]
   * @param [secondParam: Node pointing to the next (forward) node in the list]
   * @param [thirdParam: Node pointing to the previous node (back) in the list]
   * @param [fourthParam: Book containing the author and title info]
   * 
   * @return [Void - N/A]
   */

class Book {
  
  public String title;
  public String author;
  
  public Book(String t, String a) { 
    title = t;
    author = a;
  }
  
  /* 
   * [The book class acts to store the books author and title info as strings.]
   * [Input: Receives input in the form of 2 strings containing the title and author of the book respectively]
   * [Output: Stores the 2 input strings in the class Book]
   *
   * @param [firstParam: String containing the title of the book]
   * @param [secondParam: String containing the author of the book]
   * 
   * @return [Void - N/A]
   */
  
}//Book