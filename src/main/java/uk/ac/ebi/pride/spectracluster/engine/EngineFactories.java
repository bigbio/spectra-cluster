package uk.ac.ebi.pride.spectracluster.engine;

import uk.ac.ebi.pride.spectracluster.cdf.INumberOfComparisonAssessor;
import uk.ac.ebi.pride.spectracluster.cluster.ICluster;
import uk.ac.ebi.pride.spectracluster.similarity.ISimilarityChecker;
import uk.ac.ebi.pride.spectracluster.spectrum.IPeak;
import uk.ac.ebi.pride.spectracluster.util.Defaults;
import uk.ac.ebi.pride.spectracluster.util.IDefaultingFactory;
import uk.ac.ebi.pride.spectracluster.util.comparator.ClusterComparator;
import uk.ac.ebi.pride.spectracluster.util.function.IFunction;
import uk.ac.ebi.pride.spectracluster.util.predicate.IComparisonPredicate;

import java.util.List;

/**
 * uk.ac.ebi.pride.spectracluster.engine.EngineFactories
 *
 * @author Steve Lewis
 */
public class EngineFactories {

    /**
     * make a clustering engine assuming the defaults are used
     */
    public static IDefaultingFactory<IClusteringEngine> DEFAULT_CLUSTERING_ENGINE_FACTORY =
            new ClusteringEngineFactory(Defaults.getDefaultSimilarityChecker(),
                    Defaults.getDefaultSpectrumComparator(),
                    Defaults.getSimilarityThreshold(),
                    Defaults.getRetainThreshold());


    public static IDefaultingFactory<IClusteringEngine> buildClusteringEngineFactory(
            final ISimilarityChecker pSimilarityChecker,
            ClusterComparator pSpectrumComparator,
            double threshold,
            double retainThreshold) {
        return new ClusteringEngineFactory(pSimilarityChecker,
                pSpectrumComparator,
                threshold,
                retainThreshold);
    }


    protected static class ClusteringEngineFactory implements IDefaultingFactory<IClusteringEngine> {
        private final ISimilarityChecker similarityChecker;
        private final ClusterComparator spectrumComparator;
        private final double similarityThreshold;
        private final double retainThreshold;

        private ClusteringEngineFactory(final ISimilarityChecker pSimilarityChecker,
                                        ClusterComparator pSpectrumComparator,
                                        double threshold,
                                        double retainThreshold
        ) {
            similarityChecker = pSimilarityChecker;
            spectrumComparator = pSpectrumComparator;
            similarityThreshold = threshold;
            this.retainThreshold = retainThreshold;
        }

        /**
         * make a copy of the clustering engine
         *
         * @return
         */
        @Override
        public IClusteringEngine buildInstance(Object... otherdata) {
            return new ClusteringEngine(similarityChecker, spectrumComparator, similarityThreshold, retainThreshold);
        }
    }

    public static IDefaultingFactory<IIncrementalClusteringEngine> DEFAULT_INCREMENTAL_CLUSTERING_ENGINE_FACTORY =
            buildDefaultGreedyIncrementalClusteringEngineFactory();

    /**
     * make a clustering engine assuming the defaults are used  for all but window size
     */
    public static IDefaultingFactory<IIncrementalClusteringEngine> buildIncrementalClusteringEngineFactory(
            float windowSize) {
        return new IncrementalClusteringEngineFactory(Defaults.getDefaultSimilarityChecker(),
                Defaults.getDefaultSpectrumComparator(),
                Defaults.getSimilarityThreshold(),
                windowSize);
    }


    public static IDefaultingFactory<IIncrementalClusteringEngine> buildIncrementalClusteringEngineFactory(
            final ISimilarityChecker pSimilarityChecker,
            ClusterComparator pSpectrumComparator,
            double threshold,
            float windowSize) {
        return new IncrementalClusteringEngineFactory(pSimilarityChecker,
                pSpectrumComparator,
                threshold,
                windowSize);
    }


    public static class IncrementalClusteringEngineFactory implements IDefaultingFactory<IIncrementalClusteringEngine> {
        private final ISimilarityChecker similarityChecker;
        private final ClusterComparator spectrumComparator;
        private final double similarityThreshold;
        private final float windowSize;

        private IncrementalClusteringEngineFactory(final ISimilarityChecker pSimilarityChecker,
                                                   ClusterComparator pSpectrumComparator,
                                                   double threshold,
                                                   float windowSize
        ) {
            similarityChecker = pSimilarityChecker;
            spectrumComparator = pSpectrumComparator;
            similarityThreshold = threshold;
            this.windowSize = windowSize;
        }

        /**
         * make a copy of the clustering engine
         *
         * @return
         */
        public IIncrementalClusteringEngine getIncrementalClusteringEngine(float ws) {
            return new IncrementalClusteringEngine(similarityChecker, spectrumComparator, ws, similarityThreshold);
        }

        /**
         * make a copy of the clustering engine
         *
         * @return
         */
        @Override
        public IIncrementalClusteringEngine buildInstance(Object... otherdata) {
            return new IncrementalClusteringEngine(similarityChecker, spectrumComparator, windowSize, similarityThreshold);
        }
    }

    public static IDefaultingFactory<IIncrementalClusteringEngine> buildGreedyIncrementalClusteringEngineFactory(final ISimilarityChecker pSimilarityChecker,
                                                                                                                 ClusterComparator pSpectrumComparator,
                                                                                                                 double threshold,
                                                                                                                 float windowSize,
                                                                                                                 IFunction<List<IPeak>, List<IPeak>> peakFilterFunction,
                                                                                                                 IComparisonPredicate<ICluster> comparisonPredicate)
    {
        return new GreedyIncrementalClusteringEngineFactory(pSimilarityChecker, pSpectrumComparator, threshold, windowSize, peakFilterFunction, comparisonPredicate);
    }

    public static IDefaultingFactory<IIncrementalClusteringEngine> buildDefaultGreedyIncrementalClusteringEngineFactory() {
        return buildGreedyIncrementalClusteringEngineFactory(Defaults.getDefaultSimilarityChecker(), Defaults.getDefaultSpectrumComparator(), Defaults.getSimilarityThreshold(), Defaults.getDefaultPrecursorIonTolerance(), Defaults.getDefaultComparisonPeakFilter(), null);
    }

    public static class GreedyIncrementalClusteringEngineFactory implements IDefaultingFactory<IIncrementalClusteringEngine> {
        private final ISimilarityChecker similarityChecker;
        private final ClusterComparator spectrumComparator;
        private final double similarityThreshold;
        private final float windowSize;
        private final IFunction<List<IPeak>, List<IPeak>> peakFilterFunction;
        private final IComparisonPredicate<ICluster> comparisonPredicate;

        public GreedyIncrementalClusteringEngineFactory(ISimilarityChecker similarityChecker, ClusterComparator spectrumComparator, double similarityThreshold, float windowSize, IFunction<List<IPeak>, List<IPeak>> peakFilterFunction, IComparisonPredicate<ICluster> comparisonPredicate) {
            this.similarityChecker = similarityChecker;
            this.spectrumComparator = spectrumComparator;
            this.similarityThreshold = similarityThreshold;
            this.windowSize = windowSize;
            this.peakFilterFunction = peakFilterFunction;
            this.comparisonPredicate = comparisonPredicate;
        }

        public IIncrementalClusteringEngine getGreedyIncrementalClusteringEngine(float windowSize) {
            return new GreedyIncrementalClusteringEngine(similarityChecker, spectrumComparator, windowSize, similarityThreshold, peakFilterFunction, comparisonPredicate);
        }

        public IIncrementalClusteringEngine getGreedyIncrementalClusteringEngine(float windowSize, IComparisonPredicate<ICluster> comparisonPredicate) {
            return new GreedyIncrementalClusteringEngine(similarityChecker, spectrumComparator, windowSize, similarityThreshold, peakFilterFunction, comparisonPredicate);
        }

        public IIncrementalClusteringEngine getGreedyIncrementalClusteringEngine(float windowSize, IComparisonPredicate<ICluster> comparisonPredicate, INumberOfComparisonAssessor numberOfComparisonAssessor) {
            return new GreedyIncrementalClusteringEngine(similarityChecker, spectrumComparator, windowSize, similarityThreshold, peakFilterFunction, comparisonPredicate, numberOfComparisonAssessor);
        }

        @Override
        public IIncrementalClusteringEngine buildInstance(Object... input) {
            float theWindowSize = windowSize;
            if (input.length > 0 && Float.class.isInstance(input[0])) {
                theWindowSize = (Float) input[0];
            }

            return new GreedyIncrementalClusteringEngine(similarityChecker, spectrumComparator, theWindowSize, similarityThreshold, peakFilterFunction, comparisonPredicate);
        }
    }

}
