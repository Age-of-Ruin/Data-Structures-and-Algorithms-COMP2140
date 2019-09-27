/**
 * Lab # 1
 */
public class Lab_1 {
  
  public static final int CADO_INPUT = 100; 
  
  public static void main(String[] args) { 
    long startTime, endTime, elapsedTime;
    int resultR;
    int[] resultI = new int[CADO_INPUT+1];
    
    //Recursive
    startTime = System.nanoTime(); //start clock
    
    resultR = cadoR(CADO_INPUT); //Call Recursive Cado Method
    
    endTime = System.nanoTime(); //stop clock
    
    elapsedTime = endTime-startTime; //measure time
    
    System.out.println("For n =  " + CADO_INPUT + ", the Cado Number is " + resultR + 
                       " and the time it took using recursive is " + elapsedTime + ".");  
    
    //Iterative
    startTime = System.nanoTime(); //start clock
    
    resultI = cadoI(CADO_INPUT); //Call Iterative Cado Method
    
    endTime = System.nanoTime(); //stop clock
    
    elapsedTime = endTime-startTime; //measure time
    
    System.out.println("For n =  " + CADO_INPUT + ", the Cado Number is " + resultI[CADO_INPUT] + 
                       " and the time it took using iterative is " + elapsedTime + "."); 
  }//main
  
  public static int cadoR(int n){
    int result, temp;
    
    if(n <= 2) //base cases
      result = 1;
    else{
      temp = cadoR(n-1); //needed for speed
      result = cadoR(temp) + cadoR(n-temp); //recursive cases
    }
    return result;
  }//cadoR
  
  public static int[] cadoI(int n){
    int result;
    int[] cadoNumbers = new int[CADO_INPUT+1];
    
    for(int i = 0; i <= CADO_INPUT; i++){
      if(i <= 2){
        result = 1; 
        cadoNumbers[i] = result; //base cases
      }//if
      else{
        result = cadoNumbers[cadoNumbers[i-1]] + cadoNumbers[i-cadoNumbers[i-1]]; //iterative cases
        cadoNumbers[i] = result;
      }//else     
    }//for
    return cadoNumbers;
  }//cadoI
}//Lab_1_Main
