package com.netease.hzqa.olive.newAppInterface.utils;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** @author raoxiaojun Date: 13-12-17 Time: 上午10:04 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final String TEST_RETRY_COUNT = "testRetryCount";
    private int count = 1;
    private int maxCount = 1;
    private Logger logger = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public RetryAnalyzer() {
        String retryMaxCount = System.getProperty(TEST_RETRY_COUNT);
        if (retryMaxCount != null) {
            maxCount = Integer.parseInt(retryMaxCount);
        }
    }

    public int getCount() {
        return this.count;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public synchronized boolean retry(ITestResult result) {
        String testClassName = String.format("%s.%s", result.getMethod().getRealClass().toString(),
                result.getMethod().getMethodName());
        if (count <= maxCount) {
            result.setAttribute("RETRY", new Integer(count));
            System.out.println("[RETRYING] " + testClassName + "FAILED, " + "Retrying " + count + "time");
            count += 1;
            return true;
        }
        return false;
    }

}
