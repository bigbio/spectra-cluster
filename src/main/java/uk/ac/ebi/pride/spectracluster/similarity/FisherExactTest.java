package uk.ac.ebi.pride.spectracluster.similarity;

import cern.jet.random.HyperGeometric;

/**
 * This SimilarityChecker is based on the hypergeometric
 * probability that the observed similar m/z values are a
 * random match. It only calculates a point probability.
 *
 * Created by jg on 15.01.15.
 */
public class FisherExactTest extends HypergeometricScore {
    public static final String algorithmName = "Fisher Exact Test";
    public static final String algorithmVersion = "0.1";

    public FisherExactTest() {
        super();
    }

    public FisherExactTest(float peakMzTolerance) {
        super(peakMzTolerance);
    }

    public FisherExactTest(float peakMzTolerance, boolean peakFiltering) {
        super(peakMzTolerance, peakFiltering);
    }

    @Override
    protected double calculateSimilarityProbablity(int numberOfSharedPeaks, int numberOfPeaksFromSpec1, int numberOfPeaksFromSpec2, int numberOfBins) {
        if (numberOfBins < 1) {
            return 1;
        }

        double hgtScore = new HyperGeometric(numberOfBins, numberOfPeaksFromSpec1, numberOfPeaksFromSpec2, randomEngine).pdf(numberOfSharedPeaks);

        if (hgtScore == 0) {
            return 1;
        }

        return hgtScore;
    }

    @Override
    public String getName() {
        return algorithmName;
    }

    @Override
    public String getCurrentVersion() {
        return algorithmVersion;
    }
}
