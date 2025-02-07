package uk.ac.ebi.pride.tools.pride_spectra_clustering.normalizer.impl;

import uk.ac.ebi.pride.tools.pride_spectra_clustering.normalizer.IntensityNormalizer;
import uk.ac.ebi.pride.tools.pride_spectra_clustering.util.Peak;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Normalizes a given spectrum by setting the
 * highest peak to a predefined number. The default
 * is 10,000.
 * This is the method used by Lam et al. (2008), Nat. Methods 5(10):873..
 *
 * @author jg
 */
public class HighestPeakNormalizer implements IntensityNormalizer {
    private double highestPeakIntensity = 10000;

    public List<Peak> normalizeSpectrum(List<Peak> spectrum) {
        if (spectrum.size() < 1)
            return spectrum;

        // get the max intensity
        Double maxIntensity = 0.0;
        for (Peak p : spectrum) {
            if (p.getIntensity() > maxIntensity)
                maxIntensity = p.getIntensity();
        }

        // if there's no suitable max intensity, return the unchanged spectrum
        if (maxIntensity <= 0)
            return spectrum;

        // calculate the ratio
        double ratio = highestPeakIntensity / maxIntensity;

        // create the new spectrum
        List<Peak> normalizedSpectrum = spectrum.stream()
                .map(p -> new Peak(p.getMz(), p.getIntensity() * ratio))
                .collect(Collectors.toCollection(() -> new ArrayList<>(spectrum.size())));

        return normalizedSpectrum;
    }

    /**
     * Sets the intensity of the highest peak
     * to the given number.
     *
     * @param intensity
     */
    public void setHighestPeakIntensity(double intensity) {
        highestPeakIntensity = intensity;
    }
}
