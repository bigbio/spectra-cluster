package uk.ac.ebi.pride.spectracluster.consensus;

import junit.framework.*;
import org.junit.*;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.pride.spectracluster.cluster.ICluster;
import uk.ac.ebi.pride.spectracluster.filter.*;
import uk.ac.ebi.pride.spectracluster.similarity.AllPeaksDotProduct;
import uk.ac.ebi.pride.spectracluster.similarity.FrankEtAlDotProduct;
import uk.ac.ebi.pride.spectracluster.similarity.ISimilarityChecker;
import uk.ac.ebi.pride.spectracluster.spectrum.*;
import uk.ac.ebi.pride.spectracluster.util.*;

import java.util.List;

/**
 * uk.ac.ebi.pride.spectracluster.consensus.ConsensusSpectrumBuilderTests
 *
 * @author Steve Lewis
 *         This tests the difference between  IConsensusSpectrumBuilder implementations
 */
public class ConsensusSpectrumBuilderTests {


    @Test
    public void testConsensusSpectrum() throws Exception {
        // do nothing to filter
        Defaults.setDefaultPeakFilter(IPeakFilter.NULL_FILTER);
        int nLargeDifference = 0;

        List<ICluster> clusters = ClusteringTestUtilities.readSpectraClustersFromResource();
        ISimilarityChecker similarityChecker = new AllPeaksDotProduct(0.1);

        for (int i = 0; i < clusters.size(); i++) {
            ICluster clusterToTest = clusters.get(i);

            IConsensusSpectrumBuilder currentConsensusSpectrumBuilder = ConsensusSpectrum.buildFactory(IPeakFilter.NULL_FILTER).getConsensusSpectrumBuilder();
            IConsensusSpectrumBuilder originalConsensusSpectrumBuilder = new JohannesConsensusSpectrum();

            for (ISpectrum s : clusterToTest.getClusteredSpectra()) {
                currentConsensusSpectrumBuilder.addSpectra(s);
                originalConsensusSpectrumBuilder.addSpectra(s);
            }

            ISpectrum currentSpec = currentConsensusSpectrumBuilder.getConsensusSpectrum();
            ISpectrum originalSpec = originalConsensusSpectrumBuilder.getConsensusSpectrum();

            double dotProduct = similarityChecker.assessSimilarity(currentSpec, originalSpec);
            if (dotProduct < 0.95) {
                nLargeDifference++;
                System.out.println("dotProduct: " + dotProduct);
            }
        }

        Assert.assertTrue(nLargeDifference + " largely different consensus spectra found.", nLargeDifference == 0);

    }

    @Test
    public void testSpecificCluster() {
        int clusterIndex = 148;

        ICluster clusterToTest = ClusteringTestUtilities.readSpectraClustersFromResource().get(clusterIndex);
        Assert.assertNotNull(clusterToTest);

        IConsensusSpectrumBuilder currentConsensusSpectrumBuilder = ConsensusSpectrum.buildFactory(IPeakFilter.NULL_FILTER).getConsensusSpectrumBuilder();
        IConsensusSpectrumBuilder originalConsensusSpectrumBuilder = new JohannesConsensusSpectrum();

        for (ISpectrum s : clusterToTest.getClusteredSpectra()) {
            currentConsensusSpectrumBuilder.addSpectra(s);
            originalConsensusSpectrumBuilder.addSpectra(s);
        }

        ISpectrum originalSpec = originalConsensusSpectrumBuilder.getConsensusSpectrum();
        ISpectrum currentSpec = currentConsensusSpectrumBuilder.getConsensusSpectrum();

        ISimilarityChecker similarityChecker = new FrankEtAlDotProduct(0.1, 15);

        double dotProduct = similarityChecker.assessSimilarity(originalSpec, currentSpec);

        Assert.assertTrue(dotProduct >= 0.8);
    }
}
