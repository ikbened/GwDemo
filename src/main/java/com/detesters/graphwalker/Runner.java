package com.detesters.graphwalker;

import org.graphwalker.java.test.Result;
import org.graphwalker.java.test.TestExecutor;
import org.tinylog.Logger;

import java.io.IOException;

public class Runner {

  public static void main(String[] args) throws IOException {
    TestExecutor executor = new TestExecutor(
      SomeSmallTest.class
    );

    Result result = executor.execute(true);
    result.getErrors().forEach(Logger::trace);
    System.out.println("Done: [" + result.getResults().toString(2) + "]");
  }
}
