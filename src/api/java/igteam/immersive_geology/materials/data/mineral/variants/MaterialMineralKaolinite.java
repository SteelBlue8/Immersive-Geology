package igteam.immersive_geology.materials.data.mineral.variants;

import igteam.immersive_geology.materials.data.mineral.MaterialBaseMineral;
import igteam.immersive_geology.materials.helper.CrystalFamily;
import igteam.immersive_geology.materials.helper.PeriodicTableElement;
import igteam.immersive_geology.materials.pattern.MaterialPattern;
import igteam.immersive_geology.processing.IGProcessingStage;
import net.minecraft.item.Rarity;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class MaterialMineralKaolinite extends MaterialBaseMineral {

    public MaterialMineralKaolinite() {
        super("kaolinite");
    }

    @Override
    public int getColor(MaterialPattern p) {
        return 0xE5DFD1;
    }

    @Override
    public CrystalFamily getCrystalFamily() {
        return CrystalFamily.HEXAGONAL;
    }

    @Override
    protected void setupProcessingStages() {
        super.setupProcessingStages();

        new IGProcessingStage(this,"Extraction Stage") {
            @Override
            protected void describe() {

            }
        };
    }

    @Override
    public LinkedHashSet<PeriodicTableElement.ElementProportion> getElements() {
        // TODO Auto-generated method stub
        return new LinkedHashSet<>(Arrays.asList(
                new PeriodicTableElement.ElementProportion(PeriodicTableElement.ALUMINIUM, 2),
                new PeriodicTableElement.ElementProportion(PeriodicTableElement.SILICON, 2),
                new PeriodicTableElement.ElementProportion(PeriodicTableElement.OXYGEN, 5)
        ));
    }

    @Override
    public Rarity getRarity() {
        // TODO Auto-generated method stub
        return Rarity.COMMON;
    }

}
