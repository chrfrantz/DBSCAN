package org.christopherfrantz.dbscan;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.christopherfrantz.dbscan.metrics.DistanceMetricNumbers;
import org.junit.Test;

/**
 * Unit tests for DBSCANClusterer
 * 
 * @author Christopher Frantz <cf@christopherfrantz.org>
 *
 */
public class TestDBSCAN {

	@Test
	public void testNullInputValues() {
		DBSCANClusterer<Number> clusterer = null;
		
		try {
			clusterer = new DBSCANClusterer<Number>(null, 1, 1, null);
			clusterer.performClustering();
		} catch (DBSCANClusteringException e1) {
			assertEquals("Null input values error", "DBSCAN: List of input values is null.", e1.getMessage());
		}
	}
	
	@Test
	public void testEmptyInputValues() {
		DBSCANClusterer<Number> clusterer = null;
		ArrayList<Number> input = new ArrayList<>();
		
		try {
			clusterer = new DBSCANClusterer<Number>(input, 1, 1, new DistanceMetricNumbers());
			clusterer.performClustering();
		} catch (DBSCANClusteringException e1) {
			assertEquals("Empty input values error", "DBSCAN: List of input values is empty.", e1.getMessage());
		}
	}
	
	@Test
	public void testDistanceMetricIsNull() {
		DBSCANClusterer<Number> clusterer = null;
		ArrayList<Number> input = new ArrayList<>();
		input.add(1);
		
		try {
			clusterer = new DBSCANClusterer<Number>(input, 1, 1, null);
			clusterer.performClustering();
		} catch (DBSCANClusteringException e1) {
			assertEquals("Null distance metric", "DBSCAN: Distance metric has not been specified (null).", e1.getMessage());
		}
	}
	
	@Test
	public void testTooFewInputValuesForClustering() {
		DBSCANClusterer<Number> clusterer = null;
		ArrayList<Number> input = new ArrayList<>();
		input.add(1);
		try {
			clusterer = new DBSCANClusterer<Number>(input, 1, 1, new DistanceMetricNumbers());
			clusterer.performClustering();
		} catch (DBSCANClusteringException e1) {
			assertEquals("Too few input values error", "DBSCAN: Less than two input values cannot be clustered. Number of input values: 1", e1.getMessage());
		}
	}
	
	@Test
	public void testInputZeroMinimumMembers() {
		DBSCANClusterer<Number> clusterer = null;
		ArrayList<Number> input = new ArrayList<>();
		input.add(1);
		input.add(2);
		
		try {
			clusterer = new DBSCANClusterer<Number>(input, 0, 1, new DistanceMetricNumbers());
		} catch (DBSCANClusteringException e1) {
			fail("This exception should not have been thrown: " + e1);
		}
		
		ArrayList<ArrayList<Number>> result = null;
		
		try {
			result = clusterer.performClustering();
		} catch (DBSCANClusteringException e) {
			assertEquals("Cluster size of 0 values", "DBSCAN: Clusters with less than 2 members don't make sense. Current value: 0", e.getMessage());
		}	
	}
	
	@Test
	public void testInputOneMinimumMembers() {
		DBSCANClusterer<Number> clusterer = null;
		ArrayList<Number> input = new ArrayList<>();
		input.add(1);
		input.add(2);
		
		try {
			clusterer = new DBSCANClusterer<Number>(input, 1, 1, new DistanceMetricNumbers());
		} catch (DBSCANClusteringException e1) {
			fail("This exception should not have been thrown: " + e1);
		}
		
		ArrayList<ArrayList<Number>> result = null;
		
		try {
			result = clusterer.performClustering();
		} catch (DBSCANClusteringException e) {
			assertEquals("Cluster size of 1 value", "DBSCAN: Clusters with less than 2 members don't make sense. Current value: 1", e.getMessage());
		}	
	}
	
	@Test
	public void testInputNegativeDistance() {
		DBSCANClusterer<Number> clusterer = null;
		ArrayList<Number> input = new ArrayList<>();
		input.add(1);
		input.add(2);
		
		try {
			clusterer = new DBSCANClusterer<Number>(input, 1, -1, new DistanceMetricNumbers());
		} catch (DBSCANClusteringException e1) {
			fail("This exception should not have been thrown: " + e1);
		}
		
		ArrayList<ArrayList<Number>> result = null;
		
		try {
			result = clusterer.performClustering();
		} catch (DBSCANClusteringException e) {
			assertEquals("Cluster size of 1 value", "DBSCAN: Maximum distance of input values cannot be negative. Current value: -1.0", e.getMessage());
		}
		
	}
	
	@Test
	public void testClustering() {
		
		Random random = new Random(4522);
		
		ArrayList<Number> numbers = new ArrayList<Number>();
		
		int i = 0;
		while (i < 1000) {
			numbers.add(random.nextInt(1000));
			i++;
		}
		
		int minCluster = 5;
		double maxDistance = 2;
		
		DBSCANClusterer<Number> clusterer = null;
		try {
			clusterer = new DBSCANClusterer<Number>(numbers, minCluster, maxDistance, new DistanceMetricNumbers());
		} catch (DBSCANClusteringException e1) {
			fail("Should not have failed on instantiation: " + e1);
		}
		
		ArrayList<ArrayList<Number>> result = null;
		
		try {
			result = clusterer.performClustering();
		} catch (DBSCANClusteringException e) {
			fail("Should not have failed while performing clustering: " + e);
		}
		
		assertEquals("Number of clusters", 77, result.size());
	}
	
	

}
