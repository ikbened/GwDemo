package com.detesters.graphwalker;

import org.graphwalker.core.statistics.Execution;
import org.graphwalker.java.test.Result;
import org.graphwalker.java.test.TestExecutor;
import org.tinylog.Logger;

import java.io.IOException;

public class Runner {

  public static void main(String[] args) throws IOException {
    TestExecutor executor = new TestExecutor(
      SearchTest.class,
            ShoppingCartTest.class
    );

    Result result = executor.execute(true);
    result.getErrors().forEach(Logger::trace);

    Logger.info("Done: [" + result.getResults().toString(2) + "]");

    int step = 1;
    for (Execution execution : executor.getMachine().getProfiler().getExecutionPath()) {
      Logger.info(
              "Step {}: Visited count {}: {}",
              step++,
              executor.getMachine().getProfiler().getVisitCount(execution.getContext(),execution.getElement()),
              execution.getContext().getModel().getName() + "." + execution.getElement().getName());
    }
  }
}
