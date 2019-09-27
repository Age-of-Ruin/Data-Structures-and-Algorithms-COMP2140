/*
 * Compare Sorting Algorithms
 * 
 * COMP 2140
 * Instructor: Helen Cameron
 * Assignment: 1
 * @author     Richard Constantine
 * @version    2016/10/10
 * 
 * Purpose: Compares the run times of different sorting algorithms.
 * 
 */

import java.util.*;
import java.io.*; 
import java.util.Scanner;

public class A1Constantine7686561 {
  
  
  public static void main(String[] args) { 
    final int NUM_TRIALS = 50; // number of trials
    final long BIG_NUMBER = 10^1000000000; // used to compare the quickest sort time
    final int BREAKPOINT = 307; // given breakpoint in the assignment (for use with hybrid quick sort)
    
    long startTime, endTime, elapsedTime, sumTime, avgTime; // variables used in time calculations
    long quickestTime = BIG_NUMBER; // used to track the quickest average sort time
    String quickestSort = ""; // used to store the name of the quickest sort when identified
    int numInts = 0; // used to track the number of integers contained in the file
    int[] arrayOrignal = new int[0]; // initalize array that will store the list of integers to be sorted
    int[] arrayCopy = new int[0]; // initialize array that will store a copy the list of integers to be sorted
    
    System.out.println("Comp 2140 - Assignment 1 - Fall 2016");
    System.out.println("Sorting with insertion sort, merge sort, and quick sort."); 
    System.out.println("Model Solution"); 
    System.out.println("Enter the input file name (.txt files only):"); 
    
    Scanner lineReader = null;
    Scanner keyboard = new Scanner(System.in); // declare scanner to read input from keyboard
    String fileName = keyboard.next(); // read file name from keyboard and save it to a string
    
    System.out.println("\n**************************************************************************\n");  
    
    try 
    {
      lineReader = new Scanner(new File(fileName)); // declare scanner to read lines from file   
      String line = lineReader.nextLine(); // store the line in a string
      numInts = Integer.parseInt(line); // read and store the number of integers contained in the file 
      arrayOrignal = new int[numInts]; // give the array size numInts
      arrayCopy = new int[numInts]; // give the copy array size numInts
      int i = 0; // used to index the array in the while loop
      
      while(lineReader.hasNextLine() && i < arrayOrignal.length){ /* loop over each line of the file and 
                                                                   * save it into an element of the array
                                                                   */
        line = lineReader.next(); // read next line and save to string
        arrayOrignal[i] = Integer.parseInt(line); // save each integer to an element in the array
        i++; // increment index
      }//while
      
      keyboard.close(); // close keyboard scanner
      lineReader.close(); // close input/read scanner
    }//try
    
    catch(IOException e)
    {
      System.out.println(e.getMessage());
    }//catch 
    
    // Perform Sorts **************************************************************************************
    
    // insertion sort
    
    sumTime = 0; // initialize sumTime
    for (int i = 0; i <= NUM_TRIALS; i++){; // loop over and sort 50 times
      System.arraycopy(arrayOrignal, 0, arrayCopy, 0, numInts); // copy orignal array into the duplicate array (ie reset)
      startTime = System.nanoTime(); // record start time
      insertionSort(arrayCopy);// call to insertion sort
      endTime = System.nanoTime(); // record stop time
      if (!isSorted(arrayCopy)) // check if the array is sorted
        System.out.println("ERROR - NOT SORTED"); // print statement if not sorted
      elapsedTime = endTime - startTime; // calculate elapsed time
      sumTime += elapsedTime; // add the elapsed time to running sum
    }//for
    avgTime = sumTime/NUM_TRIALS; // average the sum run time over the number of trials
    
    if (avgTime < quickestTime){ // checks if this is the quickest average sorting time so far
      quickestTime = avgTime; // if quickest, change the quickest time
      quickestSort = "insertion sort"; // store the name of the quickest sort
    }//if
    
    System.out.println("Average time for insertion sort: " + avgTime + " nanoseconds"); 
    
    // recursive merge sort
    
    sumTime = 0; // initialize sumTime
    for (int i = 0; i <= NUM_TRIALS; i++){; // loop over and sort 50 times
      System.arraycopy(arrayOrignal, 0, arrayCopy, 0, numInts); // copy orignal array into the duplicate array (ie reset)
      startTime = System.nanoTime(); // record start time
      recursiveMergeSort(arrayCopy);// call to recursive merge sort
      endTime = System.nanoTime(); // record stop time
      
      if (!isSorted(arrayCopy)) // check if the array is sorted
        System.out.println("ERROR - NOT SORTED"); // print statement if not sorted
      
      elapsedTime = endTime - startTime; // calculate elapsed time
      sumTime += elapsedTime; // add the elapsed time to running sum
    }//for
    avgTime = sumTime/NUM_TRIALS; // average the sum run time over the number of trials
    
    if (avgTime < quickestTime){ // checks if this is the quickest average sorting time so far
      quickestTime = avgTime; // if quickest, change the quickest time
      quickestSort = "insertion sort"; // store the name of the quickest sort
    }//if
    
    System.out.println("Average time for recursive merge sort: " + avgTime + " nanoseconds"); 
    
    // iterative merge sort
    
    sumTime = 0; // initialize sumTime
    for (int i = 0; i <= NUM_TRIALS; i++){; // loop over and sort 50 times
      System.arraycopy(arrayOrignal, 0, arrayCopy, 0, numInts); // copy orignal array into the duplicate array (ie reset)
      startTime = System.nanoTime(); // record start time
      iterativeMergeSort(arrayCopy);// call to iterative merge sort
      endTime = System.nanoTime(); // record stop time
      
      if (!isSorted(arrayCopy)) // check if the array is sorted
        System.out.println("ERROR - NOT SORTED"); // print statement if not sorted
      
      elapsedTime = endTime - startTime; // calculate elapsed time
      sumTime += elapsedTime; // add the elapsed time to running sum
    }//for
    avgTime = sumTime/NUM_TRIALS; // average the sum run time over the number of trials
    
    if (avgTime < quickestTime){ // checks if this is the quickest average sorting time so far
      quickestTime = avgTime; // if quickest, change the quickest time
      quickestSort = "iterative quick sort"; // store the name of the quickest sort
    }//if
    
    System.out.println("Average time for iterative merge sort: " + avgTime + " nanoseconds"); 
    
    // quick sort
    
    sumTime = 0; // initialize sumTime
    for (int i = 0; i <= NUM_TRIALS; i++){; // loop over and sort 50 times
      System.arraycopy(arrayOrignal, 0, arrayCopy, 0, numInts); // copy orignal array into the duplicate array (ie reset)
      startTime = System.nanoTime(); // record start time
      quickSort(arrayCopy);// call to quick sort
      endTime = System.nanoTime(); // record stop time
      
      if (!isSorted(arrayCopy)) // check if the array is sorted
        System.out.println("ERROR - NOT SORTED"); // print statement if not sorted
      
      elapsedTime = endTime - startTime; // calculate elapsed time
      sumTime += elapsedTime; // add the elapsed time to running sum
    }//for
    avgTime = sumTime/NUM_TRIALS; // average the sum run time over the number of trials
    
    if (avgTime < quickestTime){ // checks if this is the quickest average sorting time so far
      quickestTime = avgTime; // if quickest, change the quickest time
      quickestSort = "quick sort"; // store the name of the quickest sort
    }//if
    
    System.out.println("Average time for quick sort: " + avgTime + " nanoseconds");    
    
    // hybrid quick sort
    
    sumTime = 0; // initialize sumTime
    for (int i = 0; i <= NUM_TRIALS; i++){; // loop over and sort 50 times
      System.arraycopy(arrayOrignal, 0, arrayCopy, 0, numInts); // copy orignal array into the duplicate array (ie reset)
      startTime = System.nanoTime(); // record start time
      hybridQuickSort(arrayCopy, BREAKPOINT);// call to quick sort
      endTime = System.nanoTime(); // record stop time
      
      if (!isSorted(arrayCopy)) // check if the array is sorted
        System.out.println("ERROR - NOT SORTED"); // print statement if not sorted
      
      elapsedTime = endTime - startTime; // calculate elapsed time
      sumTime += elapsedTime; // add the elapsed time to running sum
    }//for
    avgTime = sumTime/NUM_TRIALS; // average the sum run time over the number of trials
    
    if (avgTime < quickestTime){ // checks if this is the quickest average sorting time so far
      quickestTime = avgTime; // if quickest, change the quickest time
      quickestSort = "hybrid quick sort"; // store the name of the quickest sort
    }//if
    
    System.out.println("Average time for hybrid quick sort: " + avgTime + " nanoseconds");   
    
    //mutally recursive merge sort and quick sort
    
    sumTime = 0; // initialize sumTime
    for (int i = 0; i <= NUM_TRIALS; i++){; // loop over and sort 50 times
      System.arraycopy(arrayOrignal, 0, arrayCopy, 0, numInts); // copy orignal array into the duplicate array (ie reset)
      startTime = System.nanoTime(); // record start time
      mutuallyRecursiveSort(arrayCopy);// call to quick sort
      endTime = System.nanoTime(); // record stop time
      
      if (!isSorted(arrayCopy)) // check if the array is sorted
        System.out.println("ERROR - NOT SORTED"); // print statement if not sorted
      
      elapsedTime = endTime - startTime; // calculate elapsed time
      sumTime += elapsedTime; // add the elapsed time to running sum
    }//for
    avgTime = sumTime/NUM_TRIALS; // average the sum run time over the number of trials
    
    if (avgTime < quickestTime){ // checks if this is the quickest average sorting time so far
      quickestTime = avgTime; // if quickest, change the quickest time
      quickestSort = "mutally recursive merge sort and quick sort"; // store the name of the quickest sort
    }//if
    
    System.out.println("Average time for mutually recursive merge sort and quick sort: " + avgTime + " nanoseconds");    
    
    System.out.println("\n**************************************************************************\n");  
    
    System.out.println("The best sorting method is " + quickestSort + ".");
    
    System.out.println("It sorted all " + numInts + " numbers in an average time of " + quickestTime + 
                       " over " + NUM_TRIALS + " trials.");
    
    System.out.println("\n**************************************************************************\n");
    
    System.out.println("Program Ends");
    
  }//main
  
  /* 
   * [The main method reads a file containing integers (each on separate lines) and stores them into an array via
   *  a scanner, a try/catch statement, and a for loop. This array is then sorted by a variety of methods, in
   *  particular: insertion sort, recursive & iterative merge sort, quick sort, insertion/quick hybrid sort and 
   *  the mutually recursive merge/quick sort. These sorts are then timed using the system clock (in 
   *  nanoseconds) over 50 trials. The average time of each sort as well as the quickest sort are displayed to the
   *  user.]
   * [Input: Recieves arguments (as an aray of strings) when the program is executed]
   * [Output: Prints the average time taken by each sort as well which sort finished the quickest]
   * 
   * @param [firstParam: string value containing arguments passed by the user]
   * @return [Void - n/a]
   */
  
  private static void insertionSort (int[] a, int start, int end){ // **Part 1**
    int siftVal; // item to be sifted
    int j; // a[j] is to be compared to siftVal
    
    for (int i = start; i < end; i++){ // this condition sorts a[start] to a[end-1]
      siftVal = a[i];
      j = i-1;
      
      while (j >= start && a[j]  > siftVal){ /* only shift item right if siftVal is less then the unmoved item or  
                                              * if it is the last value to be moved (ie first value in the list)
                                              */ 
        a[j+1] = a[j]; // move item right
        j--;
      }//while
      a[j+1] = siftVal; // place the siftVal in the open position provided by the while loop
    }//for
    
  }//insertionSort
  
  /* 
   * [Insertion sort uses 2  pointers (ie int i, j) as well as a temp value (siftVal) to sort the list. It operates by
   *  saving the an array element (starting with 2nd element) into siftVal. It then compares the previous elements to 
   *  locate its postion in the sorted portion of the array (a[start] to a[i-1] and shifts right any values greater 
   *  then siftVal. Finally it places siftVal into the correct position in the sorted part of the array.]
   * [Input: Receives the array to be sorted, as well as the starting and ending position of the sort from the driver 
   *  method (or calling method) and acts to sort the given list]
   * [Output: The portion of the array passed to the method, and defined by start and end, will be sorted in ascending
   *  order]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within the array to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @return [Void - n/a]
   */
  
  public static void insertionSort (int[] a){ 
    
    insertionSort(a, 0, a.length); // call to insertionSort, providing correct parameters (entire array)
    
  }//insertionSort driver
  
  /* 
   * [The insertion sort driver allows for easy and convienent calling of the insertionSort method from the main method]
   * [Input: Receives the array to be sorted from the main method (or calling method) and acts to sort the given list]
   * [Output: The array is sorted via insertion sort]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @return [Void - n/a]
   */
  
  private static void recursiveMergeSort (int[] a, int start, int end, int[] temp){ // **Part 2**
    int mid; // tracks the middle position of the list
    
    if (1 < end - start){ // this condition handles the base case (ie < 2 items then do nothing)
      mid = start + (end-start)/2; // calculate mid point
      
      recursiveMergeSort(a, start, mid, temp); // sort lower half of array
      recursiveMergeSort(a, mid, end, temp); // sort upper half of array
      recursiveMerge(a, start, mid, end, temp); // merges/sorts the the current section of the array
    }//if
    
  }//recursiveMergeSort
  
  /* 
   * [Recursive merge sort works by dividing the list into individual elements by recursively dividing the list in half
   *  multiple times. It then calls the recursiveMerge method (below) to compare/sort each element in pairs. These 
   *  sorted pairs are then merged/sorted into a temporary array. It now begins comparing these newly formed pairs and 
   *  merges these into the temp array (which is now slightly more sorted). These merged pairs are then compared and so 
   *  on until the array finally broken into 2 sub groups. These are then sorted/merged and contents of the now sorted 
   *  temp array are copied back to provide the final sorted result.]
   * [Input: Receives the array to be sorted, the starting and ending position, as well as the temp array (of same size
   *  as the orignal array) from the driver method (or calling method) and acts to sort the given list]
   * [Output: The portion of the array passed to the method, and defined by start and end, will be sorted in ascending
   *  order]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @param [fourthParam; the temporary integer array that will be used to perform the intermediary sorts]
   * @return [Void - n/a]
   */
  
  private static void recursiveMerge (int[] a, int start, int mid, int end, int[] temp){ 
    int currL = start; // pointer to track position in lower/left section of array
    int currR = mid; // pointer to track position in upper/right section of array
    int currT;  // pointer to track position in temp array
    
    for (currT = start; currT < end; currT++){
      if (currL < mid && (currR >= end || a[currL] < a[currR])){ /* copy values into temp array from lower section   
                                                                  * of the orignal array if that value is smaller 
                                                                  * or no values remain in the upper section
                                                                  */
        temp[currT] = a[currL];
        currL++;
      }//if
      else{
        temp[currT] = a[currR]; // else copy the the right value
        currR++;
      }//else
    }//for    
    for (currT = start; currT < end; currT++)
      a[currT] = temp[currT]; // hard copy the temp array back into original array
    
  }//recursiveMerge
  
  /* 
   * [Recursive merge works by taken the sub-lists/pairs passed by recursiveMergeSort and sorts/merges them into
   *  the temp array.]
   * [Input: Receives the integer array to be sorted, the starting and ending position, as well as the temp array 
   *  (of same size as the orignal array) from the driver method (or calling method) and acts to sort the given list]
   * [Output: The portion of the array passed to the method, and defined by start and end, will be sorted in ascending
   *  order]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the middle postion within the array that defines the mid point between the sublists/pairs
   *         to be sorted/merged]
   * @param [fourthParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @param [fifthParam; the temporary integer array that will be used to perform the intermediary sorts]
   * @return [Void - n/a]
   */
  
  public static void recursiveMergeSort (int[] a){ 
    
    int[] temp = new int[a.length]; // temp array used during the recursiveMerge method
    recursiveMergeSort(a, 0, a.length, temp); // call to mergeSort, providing correct parameters (entire array)
    
  }//public recursiveMergeSort driver
  
  /* 
   * [The recursive merge sort driver allows for easy and convienent calling of the recursiveMergeSort method from
   *  the main method]
   * [Input: Receives the array to be sorted from the main method (or calling method) and acts to sort the given list]
   * [Output: The integer array is sorted via recursive merge sort]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @return [Void - n/a]
   */
  
  public static void iterativeMergeSort (int[] a){ // **Part 3**
    int start, mid, end;
    int[] temp = new int[a.length]; // temp array used during the merge step
    
    for(int subListSize = 1; subListSize < a.length; subListSize *= 2){ // counts the sublist size
      for(int i = 0; i < a.length; i += (subListSize * 2)){ // moves through list and tracks start of each pair
        start = i;
        mid = i + subListSize;
        end = i + subListSize * 2;
        
        if(end > a.length) /* checks if the 2nd sublist is not multiple of subListSize and also accounts for an odd 
                            * number of sublists because recursiveMerge takes into account when mid (ie currR) >= end
                            */
          end = a.length; // adjust the endpoint

          recursiveMerge(a, start, mid, end, temp);

      }//for
    }//for
    
  }//iterativeMergeSort
  
  /* 
   * [Iterative merge sort works in almost the same fashion as the recursive merge sort, however it uses 2 for loops (ie
   *  2 pointers) to define the sublists/pairs. The first for loop tracks the size of the sublist and the second
   *  tracks the starting position of each pair/sublist. These values can be used to identify the start, mid, and
   *  end values used by the recursiveMerge method which works to sort each sublist/pair passed to it.]
   * [Input: Receives the array to be sorted from the main method]
   * [Output: The array passed to it will be sorted]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @return [Void - n/a]
   */
  
  private static void quickSort (int[] a, int start, int end ){ // **Part 4**
    int pivotPosn;   
    
    if ((end - start) == 2 && a[start] > a[end-1]) // this condition handles list of only 2 items
      swap(a, start, end-1);
        
    else if (1 < (end - start)){ // this condition handles the base case (ie < 2 items then do nothing)
      choosePivot(a, start, end); // choose pivot position (using median of 3's)
      pivotPosn = partition(a, start, end); // partitions the array according the pivot
      quickSort(a, start, pivotPosn); // sort the smalls
      quickSort(a, pivotPosn + 1, end); // sort the bigs
    }//if  
  }//quickSort
  
  /* 
   * [Quick sort works by recursively choosing a pivot point, sorting all the values around this pivot point (by 
   *  comparing to it), then placing the pivot into sorted postion.]
   * [Input: Receives the array to be sorted and the starting/ending position from the driver method (below) and acts 
   *  to sort the given list]
   * [Output: The portion of the array passed to the method, and defined by start and end, will be sorted in ascending
   *  order]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @return [Void - n/a]
   */
  
  private static int partition(int[] a, int start, int end){
    int bigStart = start + 1; //the bigs will start one after the pivot until smalls are swapped in between
    int pivot = a[start]; //pivot begins at start of the array
    
    for (int curr = start + 1; curr < end; curr++){
      if (a [curr] < pivot) { // belongs to smalls
        swap(a, curr, bigStart); // swap into correct position
        bigStart++;
      }//if
    }//for
    
    swap (a, start, bigStart - 1); // swap pivot into correct location
    return bigStart - 1; // returns the position of the pivot
    
  }//partition (for quickSort)
  
  /* 
   * [The partition method works by sorting the array around the pivot chosen by the choosePivot method.]
   * [Input: Receives input from quick sort and acts to sort the arrray according to the pivot]
   * [Output: Returns the integer value of the position occupied by the pivot array element]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @return [Integer value of the postion of the pivot]
   */
  
  private static void swap(int[] a, int  posn1, int posn2){ 
    int temp = a[posn1];
    a[posn1] = a[posn2];
    a[posn2] = temp;      
    
  }//swap
  
  /* 
   * [The swap method is a simple method that swaps 2 values in an array without data loss.]
   * [Input: Receives the array to be sorted, and the 2 postions within the array to be swapped]
   * [Output: The array passed to swap will have the element at posn1 switched with the element at posn2]
   * 
   * @param [firstParam: the array to be sorted]
   * @param [secondParam; the first position of the array to swapped]
   * @param [thirdParam; the second position of the array to swapped
   * @return [Void - n/a]
   */
  
  private static void choosePivot(int[] a, int  start, int end){ 
    int mid = start + (end - start)/2; // calculate middle position
    if ((a[start] <= a[mid] && a[mid] <= a[end-1]) || (a[start] >= a[mid] && a[mid] >= a[end-1])) // check if a[mid] is median
      swap(a, start, mid); // swap a[mid] (pivot) to start position
    else if ((a[mid] <= a[end-1] && a[end-1] <= a [start]) || (a[mid] >= a[end-1] || a[end-1] >= a[start])) // check if a[end-1] is median
      swap(a, start, end-1); // swap a[end] (pivot) to start position
//  else if a[start] is median then do nothing
  
  }//choosePivot (for quickSort)
  
  /* 
   * [The choosePivot method uses the median of three method to find the median value of first, middle and last element
   *  of the array and use this element as the partition/pivot for the quick sort. Choosing a practical partition/pivot
   *  point speeds up the sort. It also swaps the chosen partition/pivot element into the first position of the 
   *  array for use by the partition method.]
   * [Input: Receives input from quick sort acts to select a partition value using the median of three]
   * [Output: A partition/pivot is chosen and moved to the first element of the array]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within array that defines where the method chooses the pivot]
   * @param [thirdParam; the ending position that defines where the method chooses the pivot]
   * @return [Void - n/a]
   */
  
  public static void quickSort (int[] a){
    
    quickSort(a, 0, a.length); // call to quickSort providing, correct parameters (entire array)
    
  }//quickSort driver
  
  /* 
   * [The quick sort driver allows for easy and convienent calling of the quick sort method from the main method 
   *  (or calling method).]
   * [Input: Receives the array to be sorted from the main method (or calling method) and acts to sort the given list]
   * [Output: The array is sorted via quick sort]
   * 
   * @param [firstParam: the array to be sorted]
   * @return [Void - n/a]
   */
  
  private static void hybridQuickSort( int[] a, int start, int end, int breakpoint ){ // **Part 5**
    int pivotPosn;
    
    if ((end-start) == 2 && a[start] > a[end-1]) // this condition handles list of only 2 unsorted items
      swap(a, start, end-1);
    
    else if (2 < (end-start) && (end-start) <= breakpoint) /* this condtion handles where end-start <= breakpoint however
                                                            * contains more than 2 array elements
                                                            */ 
      insertionSort(a, start, end);
    
    else if ((end-start) > breakpoint){ /* this condition handles the case where number of items > breakpoint 
                                         * and will do nothing when the list has < 2 total items or the 2 items are 
                                         * already sorted
                                         */
      choosePivot(a, start, end); // choose pivot position (using median of 3's)
      pivotPosn = partition(a, start, end); // partitions the array according the pivot
      hybridQuickSort(a, start, pivotPosn, breakpoint); // sort the smalls
      hybridQuickSort(a, pivotPosn + 1, end, breakpoint); // sort the bigs
    }//if
    
  }//hybridQuickSort
  
  /* 
   * [The hybrid quick sort method uses the quick sort for the large parts of the array, but begins to use insertion
   *  sort once the number of elements in each partition are equal to or below the user specified breakpoint.]
   * [Input: Receives the array to be sorted from the driver method (below) and acts to sort the given list]
   * [Output: A partition is chosen and moved to the first element of the array]
   * 
   * @param [firstParam: the array to be sorted]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @return [Void - n/a]
   */
  
  public static void hybridQuickSort ( int[] a, int breakpoint ){
    
    hybridQuickSort(a, 0, a.length, breakpoint); // call to hybridQuickSort, providing correct parameters (entire array)
    
  }//public hybridQuickSort driver
  
  /* 
   * [The hybrid quick sort driver allows for easy and convienent calling of the hybrid quick sort method from
   *  the main method (or calling method).]
   * [Input: Receives the array to be sorted and breakpoint from the main method and acts to sort the given list]
   * [Output: The array is sorted via the hybridQuickSort]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @return [Void - n/a]
   */
  
  private static void mutualQuickSort( int[] a, int start, int end, int[] temp ){ // **Part 6**
    int pivotPosn;
    
    if (1 < end - start){ // this condition handles the base case (ie < 2 items then do nothing)
      choosePivot(a, start, end); // choose pivot position (using median of 3's)
      pivotPosn = partition(a, start, end); // partitions the array according the pivot
      mutualMergeSort(a, start, pivotPosn, temp); // sort the smalls
      mutualMergeSort(a, pivotPosn + 1, end, temp); // sort the bigs
    }//if    
    
  }//mutualQuickSort
  
  /* 
   * [The mutual quick sort utilizes the skeleton of quick sort (including choosePivot and partition methods) however
   *  calls mutualMergeSort to perform the sort via merging.]
   * [Input: Receives the array to be sorted and the starting/ending position from the driver method (below) and acts 
   *  to sort the given list]
   * [Output: The portion of the array passed to the method, and defined by start and end, will be sorted in ascending
   *  order]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @param [fourthParam; the temporary integer array that will be used to perform the intermediary sorts]
   * @return [Void - n/a]
   */
  
  private static void mutualMergeSort( int[] a, int start, int end, int[] temp ){
    int mid; // tracks the middle position of the list
    
    if (1 < end - start){ // this condition handles the base case (ie < 2 items then do nothing)
      mid = start + (end-start)/2; // calculate mid point
      
      mutualQuickSort(a, start, mid, temp); // sort lower half of array
      mutualQuickSort(a, mid, end, temp); // sort upper half of array
      recursiveMerge(a, start, mid, end, temp); // merges/sorts the the current section of the array
    }//if
    
  }//mutualMergeSort
  
  /* 
   * [The mutual merge sort utilizes the skeleton of recursive merge sort (including the recuriveMerge method) however
   *  calls mutualQuickSort to perform the partition and recursiveMerge to perform the sort.]
   * [Input: Receives the array to be sorted and the starting/ending position from the driver method (below) and acts 
   *  to sort the given list]
   * [Output: The portion of the array passed to the method, and defined by start and end, will be sorted in ascending
   *  order]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @param [fourthParam; the temporary integer array that will be used to perform the intermediary sorts]
   * @return [Void - n/a]
   */
  
  public static void mutuallyRecursiveSort(int[] a){
    
    int[] temp = new int[a.length]; // temp array used during the mutuallyRecursiveSort method
    mutualQuickSort(a, 0, a.length, temp); // call to mutalQuickSort, providing correct parameters (entire array)
    
  }//mutallyRecursiveSort driver
  
  /* 
   * [The mutallyRecursiveSort driver allows for easy and convienent calling of the mutallyRecursiveSort method from
   *  the main method (or calling method).]
   * [Input: Receives the array to be sorted and acts to sort the given list]
   * [Output: The array is sorted via the mutallyRecursiveSort]
   * 
   * @param [firstParam: the integer array to be sorted]
   * @return [Void - n/a]
   */
  
  private static boolean isSorted( int[] a, int start, int end ){ // **Part 7**
    boolean result = true;
    
    for (int i = start+1; i < end && result; i++){ /* loop through the array while comparing each subsequent value
                                                    * to the previous one - exit only if not sorted or the end is reached
                                                    */
      boolean lessThan = a[i-1] <= a[i]; // this method ensures the array index is not out of bounds
      if (!lessThan)                              
        result = false; // return false if any part of the array is not sorted
    }//for
    
    return result;
    
  }//isSorted
  
  /* 
   * [isSorted performs a linear search through the array passed to it (between start and end), comparing each value to
   *  ensure the array is sorted. It returns true if it is sorted and false otherwise.]
   * [Input: Receives the array to test as well as the start and end positions to perform the check from the driver
   *  method (or calling method)]
   * [Output: Outputs a boolean that states whether the array is sorted]
   * 
   * @param [firstParam: the integer array to be checked]
   * @param [secondParam; the starting postion within array the to begin sorting (usually start of the array)]
   * @param [thirdParam; the ending position that defines where the method will stop sorting (usually the end of the 
   *         array]
   * @return [the boolean result stating true if sorted and false if not sorted]
   */
  
  public static boolean isSorted( int[] a ){
    
    return isSorted(a, 0, a.length); /* call to isSorted, providing correct parameters (entire array)
                                      * and returning its result as a boolean
     */
  }//isSorted driver
  
  /* 
   * [The isSorted driver allows for easy and convienent calling of the isSorted method from the main method (or 
   *  calling method).]
   * [Input: Receives the integer array to be checked]
   * [Output: Outputs a boolean stating whether the array is sorted]
   * 
   * @param [firstParam: the integer array to be checked]
   * @return [the boolean result stating true if sorted and false if not sorted]
   */
  
  
}//A1Constantine7686561
