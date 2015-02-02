package com.mlsdev.serhiy.weathercloud;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by android on 02.02.15.
 */
public class FullTestSuite extends TestSuite {
    
    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }
    
    public FullTestSuite() {
        suite();
    }
    
}
