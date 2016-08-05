package com.pce.testprepare;

public interface DoIt {

   void doSomething(int i, double x);
   int doSomethingElse(String s);
   default boolean didItWork(int i, double x, String s) {
      System.out.println("dit it work");
      return true;
   }
   
}
