package igteam.immersive_geology.materials.data.metal.variants;

import igteam.immersive_geology.materials.data.metal.MaterialBaseMetal;
import igteam.immersive_geology.materials.pattern.MaterialPattern;

public class MaterialMetalPlatinum extends MaterialBaseMetal {

    public MaterialMetalPlatinum() {
        super("platinum");
    }

    @Override
    public int getColor(MaterialPattern p) {
        return 0xe7e7f7;
    }

    @Override
    public boolean isNative() {
        return true;
    }
}
