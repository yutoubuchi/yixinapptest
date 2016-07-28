package com.netease.hzqa.olive.newAppInterface.utils;

import org.apache.log4j.Logger;
import org.testng.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/** @author raoxiaojun Date: 13-12-17 Time: 上午10:03 */
public class Listener extends TestListenerAdapter {
    private static Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private boolean isRetryHandleNeeded = false;
    private IResultMap skippedCases;
    private IResultMap failedCases;

    @Override
    public void onTestStart(ITestResult result) {
        int length = result.getParameters().length;
        StringBuilder stringBuilder = new StringBuilder("| ");
        for (int i = 0; i < length; i++) {
            stringBuilder.append(result.getParameters()[i]);
            stringBuilder.append(" | ");
        }

        logger.info("\n-------------------------------------------------------------------------" +
                "\n   T E S T    " + result.getName() +
                "\n  P A R A S   " + stringBuilder.toString() +
                "\n  C L A S S   " + result.getTestClass().getName() +
                "\n-------------------------------------------------------------------------");
    }

    @Override
    public synchronized void onTestFailure(ITestResult arg0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("TEST FAILED: " + arg0.getName() + " >>> " + arg0.getTestClass().getName() +
                "\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +
                "\n%%    T E S T    " + String.format("%1$-82s%%%%", arg0.getName()) +
                "\n%%   C L A S S   " + String.format("%1$-82s%%%%", arg0.getTestClass().getName()) +
                "\n%%  R E S U L T  " + String.format("%1$-82s%%%%", "failed") +
                "\n%%  Start Time:  " + String.format("%1$-82s%%%%", sdf.format(arg0.getStartMillis())) +
                "\n%%    End Time:  " + String.format("%1$-82s%%%%", sdf.format(arg0.getEndMillis())) +
                "\n%%    Duration:  " + String.format("%1$-82s%%%%", String.valueOf(arg0.getEndMillis() - arg0.getStartMillis()) + " ms") +
                "\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        if (arg0.getMethod().getRetryAnalyzer() != null) {
            RetryAnalyzer retryAnalyzer = (RetryAnalyzer) arg0.getMethod().getRetryAnalyzer();

            if (retryAnalyzer.getCount() <= retryAnalyzer.getMaxCount()) {
                arg0.setStatus(ITestResult.SKIP);
                Reporter.setCurrentTestResult(null);
            } else {
                failedCases.addResult(arg0, arg0.getMethod());
                isRetryHandleNeeded = true;
            }
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult arg0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("TEST SKIPPED: " + arg0.getName() + " >>> " + arg0.getTestClass().getName() +
                "\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +
                "\n%%   T E S T    " + String.format("%1$-82s%%%%", arg0.getName()) +
                "\n%%  C L A S S   " + String.format("%1$-82s%%%%", arg0.getTestClass().getName()) +
                "\n%% R E S U L T  " + String.format("%1$-82s%%%%", "skipped") +
                "\n%% Start Time:  " + String.format("%1$-82s%%%%", sdf.format(arg0.getStartMillis())) +
                "\n%%   End Time:  " + String.format("%1$-82s%%%%", sdf.format(arg0.getEndMillis())) +
                "\n%%   Duration:  " + String.format("%1$-82s%%%%", String.valueOf(arg0.getEndMillis() - arg0.getStartMillis()) + " ms") +
                "\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
        );
    }

    public void onFinish(final ITestContext arg0) {
        if (isRetryHandleNeeded) {
            removeIncorrectlySkippedTests(arg0, failedCases);
            removeFailedTestInTestNG(arg0);
        } else {
            skippedCases = arg0.getSkippedTests();
            failedCases = arg0.getFailedTests();
        }
    }

    protected IResultMap removeIncorrectlySkippedTests(ITestContext test, IResultMap map) {
        List<ITestNGMethod> failsToRemove = new ArrayList<ITestNGMethod>();
        IResultMap returnValue = test.getSkippedTests();
        for (ITestResult result : returnValue.getAllResults()) {
            for (ITestResult resultToCheck : map.getAllResults()) {
                if (resultToCheck.getMethod().equals(result.getMethod())) {
                    failsToRemove.add(resultToCheck.getMethod());
                    break;
                }
            }
            for (ITestResult resultToCheck : test.getPassedTests().getAllResults()) {
                if (resultToCheck.getMethod().equals(result.getMethod())) {
                    failsToRemove.add(resultToCheck.getMethod());
                    break;
                }
            }
        }
        for (ITestNGMethod method : failsToRemove) {
            returnValue.removeResult(method);
        }
        skippedCases = returnValue;
        return returnValue;
    }

    private void removeFailedTestInTestNG(ITestContext test) {
        IResultMap returnValue = test.getFailedTests();
        for (ITestResult result : returnValue.getAllResults()) {
            boolean isFailed = false;
            for (ITestResult resultToCheck : failedCases.getAllResults()) {
                if (result.getMethod().equals(resultToCheck.getMethod())) {
                    System.out.println("NotPassed: " + result.getMethod().getMethodName());
                    isFailed = true;
                    break;
                }
            }
            if (!isFailed) {
                returnValue.removeResult(result.getMethod());
                test.getFailedConfigurations().removeResult(result.getMethod());
            }
        }
    }


}
