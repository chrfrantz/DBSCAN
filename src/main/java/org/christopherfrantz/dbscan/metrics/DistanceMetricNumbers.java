package org.christopherfrantz.dbscan.metrics;

import org.christopherfrantz.dbscan.DistanceMetric;

/**
 * Distance metric implementation for numeric values. Calculates absolute
 * distance between two numbers
 * 
 * @author <a href="mailto:cf@christopherfrantz.org">Christopher Frantz</a>
 * @version 0.1
 *
 */
public class DistanceMetricNumbers implements DistanceMetric<Number>{

    /**
     * Calculates absolute distance between val1 and val2.
     * @param val1 Value 1
     * @param val2 Value 2
     * @return Absolute distance between val1 and val2 as double value
     */
    @Override
    public double calculateDistance(Number val1, Number val2) {
        return Math.abs(val1.doubleValue() - val2.doubleValue());
    }

}
