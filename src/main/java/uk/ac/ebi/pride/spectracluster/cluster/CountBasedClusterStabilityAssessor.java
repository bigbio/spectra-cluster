package uk.ac.ebi.pride.spectracluster.cluster;

import uk.ac.ebi.pride.spectracluster.util.StableClusterUtilities;

/**
 * Cluster stability assessor based on the spectra count
 *
 * @author Steve Lewis
 * @author Rui Wang
 * @version $Id$
 */
public class CountBasedClusterStabilityAssessor implements IClusterStabilityAssessor {


    private final int stableClusterSize;
    private final int semiStableClusterSize;

    @SuppressWarnings("UnusedDeclaration")
    public CountBasedClusterStabilityAssessor() {
        this.stableClusterSize = StableClusterUtilities.getStableClusterSize();
        this.semiStableClusterSize = StableClusterUtilities.getSemiStableClusterSize();
    }

    public CountBasedClusterStabilityAssessor(int stableClusterSize, int semiStableClusterSize) {
        this.stableClusterSize = stableClusterSize;
        this.semiStableClusterSize = semiStableClusterSize;
    }

    @Override
    public boolean isStable(ICluster cluster) {
        int count = cluster.getClusteredSpectraCount();
        return count >= stableClusterSize;
    }

    @Override
    public boolean isSemiStable(ICluster cluster) {
        int count = cluster.getClusteredSpectraCount();
        return count >= semiStableClusterSize;
    }
}
