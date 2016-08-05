package com.pce.testprepare;

/**
 * Created by Leonardo Tarjadi on 7/04/2016.
 */
public class InterfaceImplementation  {

  public void doSomething(){
      String someVar = SomeInterface.SOME_VAR;
      /*int x1 = _52;
      System.out.println(x);
      int x2 = 5_2;
*/
      int x2 = 5_2;
      System.out.println(x2);
  }

  public static void main (String[] args){
    InterfaceImplementation interfaceImplementation = new InterfaceImplementation();
      interfaceImplementation.doSomething();
  }
}
