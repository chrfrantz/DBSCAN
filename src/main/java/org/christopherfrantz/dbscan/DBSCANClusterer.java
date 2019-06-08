package org.christopherfrantz.dbscan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Implementation of density-based clustering algorithm DBSCAN.
 * 
 * Original Publication: 
 * Ester, Martin; Kriegel, Hans-Peter; Sander, Jörg; Xu, Xiaowei (1996). 
 * Simoudis, Evangelos; Han, Jiawei; Fayyad, Usama M., eds. 
 * A density-based algorithm for discovering clusters in large spatial 
 * databases with noise. Proceedings of the Second International Conference 
 * on Knowledge Discovery and Data Mining (KDD-96). AAAI Press. pp. 226-231
 * 
 * Usage:
 * - Identify type of input values.
 * - Implement metric for input value type using DistanceMetric interface.
 * - Instantiate using {@link #DBSCANClusterer(Collection, int, double, DistanceMetric)}.
 * - Invoke {@link #performClustering()}.
 * 
 * See tests and metrics for example implementation and use.
 * 
 * @author <a href="mailto:cf@christopherfrantz.org">Christopher Frantz</a>
 * @version 0.1
 *
 * @param <V> Input value element type
 */
public class DBSCANClusterer<V> {

    /** maximum distance of values to be considered as cluster */
    private double epsilon = 1f;

    /** minimum number of members to consider cluster */
    private int minimumNumberOfClusterMembers = 2;

    /** distance metric applied for clustering **/
    private DistanceMetric<V> metric = null;

    /** internal list of input values to be clustered */
    private ArrayList<V> inputValues = null;

    /** index maintaining visited points */
    private HashSet<V> visitedPoints = new HashSet<V>();

    /**
     * Creates a DBSCAN clusterer instance. 
     * Upon instantiation, call {@link #performClustering()} 
     * to perform the actual clustering.
     * 
     * @param inputValues Input values to be clustered
     * @param minNumElements Minimum number of elements to constitute cluster
     * @param maxDistance Maximum distance of elements to consider clustered
     * @param metric Metric implementation to determine distance
     * @throws DBSCANClusteringException 
     */
    public DBSCANClusterer(final Collection<V> inputValues, int minNumElements, double maxDistance, DistanceMetric<V> metric) throws DBSCANClusteringException {
        setInputValues(inputValues);
        setMinimalNumberOfMembersForCluster(minNumElements);
        setMaximalDistanceOfClusterMembers(maxDistance);
        setDistanceMetric(metric);
    }

    /**
     * Sets the distance metric
     * 
     * @param metric
     * @throws DBSCANClusteringException 
     */
    public void setDistanceMetric(final DistanceMetric<V> metric) throws DBSCANClusteringException {
        if (metric == null) {
            throw new DBSCANClusteringException("DBSCAN: Distance metric has not been specified (null).");
        }
        this.metric = metric;
    }

    /**
     * Sets a collection of input values to be clustered.
     * Repeated call overwrite the original input values.
     * 
     * @param collection
     * @throws DBSCANClusteringException 
     */
    public void setInputValues(final Collection<V> collection) throws DBSCANClusteringException {
        if (collection == null) {
            throw new DBSCANClusteringException("DBSCAN: List of input values is null.");
        }
        this.inputValues = new ArrayList<V>(collection);
    }

    /**
     * Sets the minimal number of members to consider points of close proximity
     * clustered.
     * 
     * @param minimalNumberOfMembers
     */
    public void setMinimalNumberOfMembersForCluster(final int minimalNumberOfMembers) {
        this.minimumNumberOfClusterMembers = minimalNumberOfMembers;
    }

    /**
     * Sets the maximal distance members of the same cluster can have while
     * still be considered in the same cluster.
     * 
     * @param maximalDistance
     */
    public void setMaximalDistanceOfClusterMembers(final double maximalDistance) {
        this.epsilon = maximalDistance;
    }

    /**
     * Determines the neighbours of a given input value.
     * 
     * @param inputValue Input value for which neighbours are to be determined
     * @return list of neighbours
     * @throws DBSCANClusteringException 
     */
    private ArrayList<V> getNeighbours(final V inputValue) throws DBSCANClusteringException {
        ArrayList<V> neighbours = new ArrayList<V>();
        for(int i=0; i<inputValues.size(); i++) {
            V candidate = inputValues.get(i);
            if (metric.calculateDistance(inputValue, candidate) <= epsilon) {
                neighbours.add(candidate);
            }
        }
        return neighbours;
    }

    /**
     * Merges the elements of the right collection to the left one and returns
     * the combination.
     * 
     * @param neighbours1 left collection
     * @param neighbours2 right collection
     * @return Modified left collection
     */
    private ArrayList<V> mergeRightToLeftCollection(final ArrayList<V> neighbours1,
            final ArrayList<V> neighbours2) {
        for (int i = 0; i < neighbours2.size(); i++) {
            V tempPt = neighbours2.get(i);
            if (!neighbours1.contains(tempPt)) {
                neighbours1.add(tempPt);
            }
        }
        return neighbours1;
    }

    /**
     * Applies the clustering and returns a collection of clusters (i.e. a list
     * of lists of the respective cluster members).
     * 
     * @return
     * @throws DBSCANClusteringException 
     */
    public ArrayList<ArrayList<V>> performClustering() throws DBSCANClusteringException {

        if (inputValues == null) {
            throw new DBSCANClusteringException("DBSCAN: List of input values is null.");
        }

        if (inputValues.isEmpty()) {
            throw new DBSCANClusteringException("DBSCAN: List of input values is empty.");
        }

        if (inputValues.size() < 2) {
            throw new DBSCANClusteringException("DBSCAN: Less than two input values cannot be clustered. Number of input values: " + inputValues.size());
        }

        if (epsilon < 0) {
            throw new DBSCANClusteringException("DBSCAN: Maximum distance of input values cannot be negative. Current value: " + epsilon);
        }

        if (minimumNumberOfClusterMembers < 2) {
            throw new DBSCANClusteringException("DBSCAN: Clusters with less than 2 members don't make sense. Current value: " + minimumNumberOfClusterMembers);
        }

        ArrayList<ArrayList<V>> resultList = new ArrayList<ArrayList<V>>();
        visitedPoints.clear();

        ArrayList<V> neighbours;
        int index = 0;

        while (inputValues.size() > index) {
            V p = inputValues.get(index);
            if (!visitedPoints.contains(p)) {
                visitedPoints.add(p);
                neighbours = getNeighbours(p);

                if (neighbours.size() >= minimumNumberOfClusterMembers) {
                    int ind = 0;
                    while (neighbours.size() > ind) {
                        V r = neighbours.get(ind);
                        if (!visitedPoints.contains(r)) {
                            visitedPoints.add(r);
                            ArrayList<V> individualNeighbours = getNeighbours(r);
                            if (individualNeighbours.size() >= minimumNumberOfClusterMembers) {
                                neighbours = mergeRightToLeftCollection(
                                        neighbours,
                                        individualNeighbours);
                            }
                        }
                        ind++;
                    }
                    resultList.add(neighbours);
                }
            }
            index++;
        }
        return resultList;
    }

}
