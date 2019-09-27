/*******************************************************************
*
* Lab 3   COMP 2140   Fall 2016
* Instructors: Helen Cameron
* 
* Simulating two bank tellers serving customers as they arrive.
*
* Uses a queue (implemented as a circular linked list) to
* simulate the line-up of customers waiting in line for a free teller
* when there are more than two customers in the bank.
*
* The Queue class and its Node class are at the end of the file.
* Fill in the bodies of the enter, leave, and isEmpty methods.
*
********************************************************************/

import java.util.Scanner;
import java.io.*;


public class Lab03
{

  // Event types
  private static final int TELLER1DONE = 0; // teller 1 completed current customer's task
  private static final int TELLER2DONE = 1; // teller 2 completed current customer's task
  private static final int CUSTOMER_ARRIVING = 2; // new customer arriving at the bank
    
  public static void main( String[] args ) throws Exception
  {

    EventList events; // Future events

    events = readEventList( "bankSimulationData.txt" );
    
    System.out.println( "\nCOMP 2140 Lab 3" );
    System.out.println( "Simulating two bank tellers serving customers" );
    System.out.println( "\nTime    Event" );
    System.out.println( "----    -----" );
 
    simulateSmallBank( events );
 
    System.out.println( "\n\nLab 3 bank-teller simulation completed\n" );
  } // end main

  private static EventList readEventList( String fileName )
  {
    Scanner inFile;
    EventList events = new EventList(); // Future events
    int arrivalTime; // customer arrival time
    int taskTime; // how much time will the customer's task take?
    int customerNumber = 1; // Each customer is assigned a unique # on arrival    

    try
    {
      // Get a scanner for the file containing the customer arrivals
      inFile = new Scanner( new File( fileName ) );
    
      // read in all the customers and add their arrival events to the events list
      while ( inFile.hasNext() )
      {
        arrivalTime = inFile.nextInt(); // when is the customer arriving?
        taskTime = inFile.nextInt(); // how long will his/her task take to comple?

        // add this (future) event to the list of future events
        events.add( new Event( 
                        new Customer( customerNumber, taskTime ),
                        arrivalTime, 
                        CUSTOMER_ARRIVING 
                             )
                  );

        customerNumber++;
      } // end while
    }
    catch (IOException e) 
    {
      System.out.println("IO Error: " + e.getMessage());
    }

    return events;
  } // end readEventList

  private static void simulateSmallBank( EventList events )
  {
    int bankTime = 0; // Current simulation time
    Queue<Customer> customerLineUp = new Queue<Customer>();
    Customer teller1Serving = null; // Who is teller 1 currently serving?
    Customer teller2Serving = null; // Who is teller 2 currently serving?
    Customer nextToArrive = null; // Who is arriving at the bank?
    Event now;  // The event that is happening right now (at bankTime)
    Customer nowCustomer; // The customer whose event is happening right now
    Customer next1; // The next customer to be served (from the line up)

    // Loop until simulation ends (all events have happened)
    while ( !events.isEmpty() )
    {
      // Get the next event and "fast-forward" time to that event's time
      now = events.removeEarliest();
      nowCustomer = now.getCustomer();
      bankTime = now.getEventTime();

      // Figure out which type of event we have, and handle it.
      if ( now.getEventType() == CUSTOMER_ARRIVING )
      {
        System.out.println( bankTime + "  Customer " 
          + nowCustomer.getCustomerNumber()
          + " arrives at the bank." );

        // Can the customer be immediately served by one of the two
        // tellers, or must the customer wait in the line-up?
        if ( teller1Serving == null ) // teller 1 is free
        {
          teller1Serving = nowCustomer;
          events.add( new Event( teller1Serving, 
                                 bankTime+teller1Serving.getTaskTime(), 
                                 TELLER1DONE )                   );
          System.out.println( bankTime + "  Teller 1 is "
            + "now serving customer " + teller1Serving.getCustomerNumber() 
            + " with task time " + teller1Serving.getTaskTime() );
        }
        else if ( teller2Serving == null ) // teller 2 is free
        {
          teller2Serving = nowCustomer;
          events.add( new Event( teller2Serving, 
                                 bankTime+teller2Serving.getTaskTime(), 
                                 TELLER2DONE )                   );
          System.out.println( bankTime + "  Teller 2 is "
            + "now serving customer " + teller2Serving.getCustomerNumber() 
            + " with task time " + teller2Serving.getTaskTime() );
        }
        else // No teller is free, wait in the line-up
        {
          customerLineUp.enter( nowCustomer );
          System.out.println( bankTime + "  Customer "
            + nowCustomer.getCustomerNumber()
            + " has joined the line-up." );
        }
      } // end if customer arriving event
      else if ( now.getEventType() == TELLER1DONE ) 
      {
        System.out.println( bankTime + "  Teller 1 completes task"
          + " for customer " + nowCustomer.getCustomerNumber() 
          + ", who leaves."  );

        // Serve the next waiting customer, if any
        if ( !customerLineUp.isEmpty() )
        {
          teller1Serving = customerLineUp.leave();
          events.add( new Event( teller1Serving, 
                                 bankTime+teller1Serving.getTaskTime(), 
                                 TELLER1DONE )                   );
          System.out.println( bankTime + "  Teller 1 is "
            + "now serving customer " + teller1Serving.getCustomerNumber() 
            + " with task time " + teller1Serving.getTaskTime() );
        }
        else // no waiting customers: sit idle until one comes
        {
          teller1Serving = null;
          System.out.println( bankTime + "  Teller 1 is idle." );
        }
      }
      else if ( now.getEventType() == TELLER2DONE )
      {
        System.out.println( bankTime + "  Teller 2 completes task"
          + " for customer " + nowCustomer.getCustomerNumber() 
          + ", who leaves."  );

        // Serve the next waiting customer, if any
        if ( !customerLineUp.isEmpty() )
        {
          teller2Serving = customerLineUp.leave();
          events.add( new Event( teller2Serving, 
                                 bankTime+teller2Serving.getTaskTime(), 
                                 TELLER2DONE )                   );
          System.out.println( bankTime + "  Teller 2 is "
            + "now serving customer " + teller2Serving.getCustomerNumber() 
            + " with task time " + teller2Serving.getTaskTime() );
        }
        else // no waiting customers: sit idle until one comes
        {
          teller2Serving = null;
          System.out.println( bankTime + "  Teller 2 is idle." );
        }
      }  // end else if teller 2 is done event
      else
      {
        System.out.println( "ERROR: incomprehensible event code "
          + now.getEventType() ); 
      }
    } // end while more events to process

  } // end simulateSmallBank
 
} // end class Lab3


/*******************************************************************
*
* Customer
*
********************************************************************/
class Customer
{
  private int customerNumber;  // unique num assigned when customer arrives
  private int taskTime; // how long will this customer's task take to accomplish?

  public Customer( int cNumber, int tTime )
  {
    customerNumber = cNumber;
    taskTime = tTime;
  }

  public int getCustomerNumber()
  {
    return customerNumber;
  }

  public int getTaskTime()
  {
    return taskTime;
  }

} // end class Customer



/*******************************************************************
*
* Event
*
********************************************************************/
class Event
{
  private Customer client;  // the customer the event is happening to
  private int eventTime; // when the event happens
  private int eventType; // what type of event is it?

  public Event( Customer c, int etime, int etype  )
  {
    client = c;
    eventTime = etime;
    eventType = etype;
  }

  public Customer getCustomer()
  {
    return client;
  }

  public int getEventTime()
  {
    return eventTime;
  }

  public int getEventType()
  {
    return eventType;
  }

  public String toString()
  {
    return "At time " + eventTime + ", customer " 
            + client.getCustomerNumber() + " has an event of type " 
            + eventType;
  }

} // end class Event




/*******************************************************************
*
*  EventList 
*  - a list of all events we know about so far,
*    sorted so we can easily retrieve the earliest
*    one.
*
********************************************************************/
class EventList
{
  private Node<Event> head;
  
  public EventList()
  {
    head = null;
  }

  // Add a new event to the list.
  // Keep the nodes sorted by event time.
  public void add( Event e )
  {
    Node<Event> prev = null;
    Node<Event> curr = head;

    while ( curr != null
            && curr.getItem().getEventTime() < e.getEventTime() )
    {
      prev = curr;
      curr = curr.getNext();
    }

    if ( prev != null ) // insert after prev
    {
      prev.setNext( new Node<Event>( e, curr ) );
    }
    else // insert at front (e has earliest event time)
    {
      head = new Node<Event>( e, head );
    }
  } // end add

  // Remove the earliest event in the list and return it.
  // It is stored at the head of the list.
  // Special case: if the list is empty, return null.
  public Event removeEarliest()
  {
    Event earliest = null;

    if ( head != null )
    {
      earliest = head.getItem();
      head = head.getNext();
    }

    return earliest;
  } // end removeEarliest

  public boolean isEmpty()
  {
    return head == null;
  }

  public String toString()
  {
    Node<Event> curr = head;
    String result = "";

    while ( curr != null )
    {
      result = result + curr.getItem().toString() + "\n";
      curr = curr.getNext();
    }

    return result;
  }
  
}  // end class EventList




/*******************************************************************
*
* Standard generic-type Node class
*
********************************************************************/
class Node<E> 
{
  private E item;
  private Node<E> next;

  // constructor: you provide next
  public Node(E newItem, Node<E> newNext) 
  {
    item = newItem;
    next = newNext;
  }

  // constructor: next is set to null
  // (simply calls the other constructor)
  public Node(E newItem) 
  { 
    this(newItem, null); 
  }

  // The usual accessors and mutators

  public E getItem() 
  { 
    return item; 
  }
  public void setItem(E newItem) 
  { 
    item = newItem;
  }

  public Node<E> getNext() 
  { 
    return next; 
  }
  public void setNext(Node<E> newNext) 
  { 
    next = newNext; 
  }

} // end class Node


/*******************************************************************
*
* Queue
*
* - implemented as a circular linked-list with just a pointer 
*   (called end) to the last node (and the last node points
*   at the first node).  Note that there are no dummy nodes.
*
********************************************************************/
class Queue<E>
{
  private Node<E> end; // a pointer to the last node

  public Queue()
  {
      end = null; // an empty queue
  }

  public boolean isEmpty()
  {
    return end == null; 
  }//isEmpty

  public void enter( E item )
  {
    if(isEmpty()){
      end = new Node<E>(item, null);
      end.setNext(end);
    }//if
    
    else{
      Node<E> newNode = new Node<E>(item, end.getNext());
      end.setNext(newNode);
      end = newNode;    
    }//else
  }//enter
  
  public E leave()
  {
    E frontItem = null;
    
    if(isEmpty())
      frontItem = null;
     
    else if (!isEmpty()){
      if(end.getNext() != end && end.getNext() != null){
        frontItem =  end.getNext().getItem();
        end.setNext(end.getNext().getNext());
      }
      else{
        frontItem = end.getItem();
        end = null;
      }//else
    }//else
    return frontItem;
  }//leave
  
} // end class Queue
