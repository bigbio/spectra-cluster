package uk.ac.ebi.pride.spectracluster.util.function.spectrum;

import uk.ac.ebi.pride.spectracluster.spectrum.IPeak;
import uk.ac.ebi.pride.spectracluster.spectrum.ISpectrum;
import uk.ac.ebi.pride.spectracluster.spectrum.Masses;
import uk.ac.ebi.pride.spectracluster.spectrum.Spectrum;
import uk.ac.ebi.pride.spectracluster.util.function.IFunction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This filter removes all peaks that are
 * above the precursor mass + a tolerance.
 * This filter requires the precursor charge
 * state to be known. For spectra where this
 * is unknown, the original spectrum is returned
 * unchanged.
 *
 * Created by jg on 13.05.15.
 */
public class RemoveImpossiblyHighPeaksFunction implements IFunction<ISpectrum, ISpectrum> {

    public final static float DEFAULT_TOLERANCE = 3.0F;
    public final float tolerance;

    public RemoveImpossiblyHighPeaksFunction(float tolerance) {
        this.tolerance = tolerance;
    }

    public RemoveImpossiblyHighPeaksFunction() {
        this(DEFAULT_TOLERANCE);
    }

    @Override
    public ISpectrum apply(ISpectrum o) {
        // this filter only works on spectra where the charge is known
        if (o.getPrecursorCharge() < 1) {
            return(o);
        }

        final float monoisotopicMass = Masses.getMonoisotopicMass(o.getPrecursorMz(), o.getPrecursorCharge());
        final float maxMass = monoisotopicMass + Masses.PROTON + tolerance;

        List<IPeak> filteredPeaks = o.getPeaks().stream()
                .filter(peak -> !(peak.getMz() > maxMass))
                .collect(Collectors.toList());

        return new Spectrum(o, filteredPeaks, true);
    }
}
