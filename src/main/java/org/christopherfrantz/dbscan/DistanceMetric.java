package org.christopherfrantz.dbscan;

/**
 * Interface for the implementation of distance metrics.
 * 
 * @author Christopher Frantz <cf@christopherfrantz.org>
 *
 * @param <V> Value type to which distance metric is applied.
 */
public interface DistanceMetric<V> {

	public double calculateDistance(V val1, V val2) throws DBSCANClusteringException;
	
}
