# DBSCAN
Lightweight Java implementation of density-based clustering algorithm DBSCAN

## Building

To build the library, you can either compile the sources manually or use maven (`mvn package`).

## Usage

* Implement a distance metric for the data type to be clustered (using the interface `DistanceMetric`).
* Instantiate `DBSCANClusterer`.
* Invoke `performClustering()`.