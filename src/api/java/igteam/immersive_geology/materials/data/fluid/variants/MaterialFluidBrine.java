package igteam.immersive_geology.materials.data.fluid.variants;

import igteam.immersive_geology.materials.data.fluid.MaterialBaseFluid;
import igteam.immersive_geology.materials.helper.PeriodicTableElement;
import igteam.immersive_geology.materials.pattern.MaterialPattern;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class MaterialFluidBrine extends MaterialBaseFluid {
    public MaterialFluidBrine() {
        super("brine");
    }

    @Override
    public int getColor(MaterialPattern p) {
        return 0xBCA271;
    }

    @Override
    public LinkedHashSet<PeriodicTableElement.ElementProportion> getElements() {
        return new LinkedHashSet<>(Arrays.asList(
                new PeriodicTableElement.ElementProportion(PeriodicTableElement.SODIUM),
                new PeriodicTableElement.ElementProportion(PeriodicTableElement.CHLORINE),
                new PeriodicTableElement.ElementProportion(PeriodicTableElement.POTASSIUM)));
    }
}
