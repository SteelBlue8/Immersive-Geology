package generators;

import blusunrize.immersiveengineering.data.ItemModels;
import blusunrize.immersiveengineering.data.models.TRSRModelBuilder;
import com.google.common.base.Preconditions;
import com.igteam.immersive_geology.ImmersiveGeology;
import com.igteam.immersive_geology.api.materials.Material;
import com.igteam.immersive_geology.api.materials.MaterialEnum;
import com.igteam.immersive_geology.api.materials.MaterialUseType;
import com.igteam.immersive_geology.api.materials.material_bases.MaterialMineralBase;
import com.igteam.immersive_geology.common.block.*;
import com.igteam.immersive_geology.common.block.helpers.BlockMaterialType;
import com.igteam.immersive_geology.common.item.*;
import com.igteam.immersive_geology.core.lib.IGLib;
import com.igteam.immersive_geology.core.registration.IGMultiblockRegistrationHolder;
import com.igteam.immersive_geology.core.registration.IGRegistrationHolder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.client.model.generators.loaders.OBJLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class IGItemModelProvider extends ItemModelProvider {

    private Logger log = ImmersiveGeology.getNewLogger();

    public IGItemModelProvider(DataGenerator gen, ExistingFileHelper exHelper){
        super(gen, IGLib.MODID, exHelper);
    }

    @Override
    public String getName(){
        return "Item Models";
    }

    @Override
    protected void registerModels() {
        generateMultiblockItems();
        for(Item item : IGRegistrationHolder.registeredIGItems.values()){
            try{
                if(item instanceof IGBucketItem){
                    IGBucketItem itemBase = ((IGBucketItem) item);
                    genericFlask(item);
                } else
                if(item instanceof IGBlockItem){
                    IGBlockItem blockItem = (IGBlockItem) item;
                    MaterialUseType useType = blockItem.getUseType();
                    switch (useType) {
                        case MACHINE:
                            break;
                        default:
                            generateBlockItem(blockItem);
                            break;
                    }
                } else if(item instanceof IGOreItem) {
                    genericOreItem(item);
                } else if (item instanceof ItemBase) {
                    ItemBase itemBase = ((ItemBase) item);
                    MaterialUseType useType = itemBase.getUseType();

                    switch(useType){
                        case CUT_CRYSTAL:
                        case RAW_CRYSTAL:
                            generateCrystalItem(itemBase);
                            break;
                        case FLUIDS:
                            break;
                        default:
                            genericItem(item);
                    }
                } else if(item instanceof IGStaticItem){
                    staticItem(item);
                }
            } catch (Exception e){
                log.error("Failed to create Item Model: \n" + e);
            }
        }

        //generateManualPageSkeleton();

    }

    private void generateManualPageSkeleton() {
        for(MaterialEnum material : MaterialEnum.worldMaterials()) {
            Material mineral = material.getMaterial();
            Block block = IGRegistrationHolder.getBlockByMaterial(MaterialEnum.Vanilla.getMaterial(), mineral, MaterialUseType.ORE_STONE);
            if (block instanceof IGOreBlock) {
                IGOreBlock oreBlock = (IGOreBlock) block;

                try {
                    /*
                    PrintWriter out = new PrintWriter("mineral_" + mineral.getName() + ".json");
                    out.println("{");
                    out.println("\"mineral_" + mineral.getName() + "\": {");
                    out.println("  \"type\":" + "\"item_display\",");
                    out.println("  \"item\": {");
                    out.println("     \"item\":" + "\"immersive_geology:" + oreBlock.getHolderName() + "\"");
                    out.println("    }");
                    out.println("  }");
                    out.println("}");
                    out.close();
                    */
                    PrintWriter out = new PrintWriter("mineral_" + mineral.getName() + ".txt");
                    out.println(mineral.getName().substring(0,1).toUpperCase() + mineral.getName().substring(1));
                    out.println("Minerals");
                    out.println("<&"+mineral.getName()+">");
                    out.println("Mineral Test for " + mineral.getName());
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ItemModelBuilder obj(IItemProvider item, String model){
        return getBuilder(item.asItem().getRegistryName().toString())
                .customLoader(OBJLoaderBuilder::begin)
                .modelLocation(modLoc("models/" + model)).flipV(true).end();
    }

    private final Vector3f ZERO = new Vector3f(0, 0, 0);
    private void doTransform(ModelBuilder<?>.TransformsBuilder transform, Perspective type, Vector3f translation, @Nullable Vector3f rotationAngle, float scale){
        if(rotationAngle == null){
            rotationAngle = ZERO;
        }

        transform.transform(type)
                .translation(translation.getX(), translation.getY(), translation.getZ())
                .rotation(rotationAngle.getX(), rotationAngle.getY(), rotationAngle.getZ())
                .scale(scale)
                .end();
    }

    private void generateCrystalItem(ItemBase item){
        if(item == null){
            StackTraceElement where = new NullPointerException().getStackTrace()[1];
            log.warn("Skipping null item. ( {} -> {} )", where.getFileName(), where.getLineNumber());
            return;
        }
        withExistingParent(new ResourceLocation(IGLib.MODID, "item/" + item.getHoldingName()).getPath(), new ResourceLocation(IGLib.MODID, "item/base/" + item.getUseType().getName() + "_" + item.getMaterial(BlockMaterialType.BASE_MATERIAL).getCrystalFamily().getCrystalSystem()));
    }

    private void generateBlockItem(IGBlockItem blockItem){
        if(blockItem.getBlock() instanceof IGOreBlock) {
            IGOreBlock oreBlock = (IGOreBlock) blockItem.getBlock();
            generateBlockOreItem(oreBlock);
        } else if(blockItem.getBlock() instanceof IGStairsBlock) {
            IGStairsBlock stairsBlock = (IGStairsBlock) blockItem.getBlock();
            String builder_name = new ResourceLocation(IGLib.MODID,"item/"+stairsBlock.getHolderName()).getPath();
            withExistingParent(builder_name, new ResourceLocation(IGLib.MODID, "block/base/" + stairsBlock.getBlockUseType().getName())).
                    texture("all", new ResourceLocation(IGLib.MODID, stairsBlock.getParentTexture())).
                    texture("particle", new ResourceLocation(IGLib.MODID, stairsBlock.getParentTexture()));
        } else if(blockItem.getBlock() instanceof IGStaticBlock) {
            IGStaticBlock staticBlock = (IGStaticBlock) blockItem.getBlock();
            String builder_name = new ResourceLocation(IGLib.MODID, "item/"+staticBlock.getHolderName()).getPath();
            withExistingParent(builder_name, new ResourceLocation(IGLib.MODID, "block/static_block/" +staticBlock.getHolderName()));
        }  else if(blockItem.getBlock() instanceof IGSlabBlock) {
            IGSlabBlock slabBlock = (IGSlabBlock) blockItem.getBlock();
            String builder_name = new ResourceLocation(IGLib.MODID, "item/"+slabBlock.getHolderName()).getPath();
            withExistingParent(builder_name, new ResourceLocation(IGLib.MODID, "block/base/slab/" +slabBlock.getBlockUseType().getName())).
                    texture("side", new ResourceLocation(IGLib.MODID, slabBlock.getSideTexturePath())).
                    texture("all", new ResourceLocation(IGLib.MODID, slabBlock.getSideTexturePath())).
                    texture("cover", new ResourceLocation(IGLib.MODID, slabBlock.getCoverTexturePath()));;
        }else if (blockItem.getUseType() != MaterialUseType.MACHINE)
        {
            IGBaseBlock baseBlock = (IGBaseBlock) blockItem.getBlock();
            String builder_name = new ResourceLocation(IGLib.MODID, "item/"+baseBlock.getHolderName()).getPath();
            withExistingParent(builder_name, new ResourceLocation(IGLib.MODID, "block/base/" + baseBlock.getBlockUseType().getName()));
        }
    }

    private void generateBlockOreItem(IGOreBlock oreBlock){
        String builder_name = new ResourceLocation(IGLib.MODID, "item/"+oreBlock.getHolderName()).getPath();
        String stone_name = oreBlock.getMaterial(BlockMaterialType.BASE_MATERIAL).getStoneType().getName().toLowerCase();
        withExistingParent(builder_name, new ResourceLocation(IGLib.MODID, "block/base/rock_" + stone_name));
        if(oreBlock.getMineralType() == MaterialMineralBase.EnumMineralType.CLAY) {
            getBuilder(builder_name).texture("ore", IGLib.MODID + ":block/greyscale/rock/ore_bearing/clay");
        } else {
            getBuilder(builder_name).texture("ore", IGLib.MODID + ":block/greyscale/rock/ore_bearing/" + stone_name + "/" + stone_name + "_normal");
        }
        getBuilder(builder_name).texture("base", IGLib.MODID + ":block/greyscale/rock/rock_" + stone_name);
        getBuilder(builder_name).element().allFaces(((direction, faceBuilder) -> faceBuilder.texture("#base").tintindex(1).uvs(0, 0, 16, 16)));
        getBuilder(builder_name).element().allFaces(((direction, faceBuilder) -> faceBuilder.texture("#ore").tintindex(0).uvs(0, 0, 16, 16)));
    }

    private void staticItem(Item item){
        if(item == null){
            StackTraceElement where = new NullPointerException().getStackTrace()[1];
            log.warn("Skipping null item. ( {} -> {} )", where.getFileName(), where.getLineNumber());
            return;
        }
        IGStaticItem i = (IGStaticItem) item;
        withExistingParent(new ResourceLocation(IGLib.MODID, "item/" + i.getResourceName()).getPath(), new ResourceLocation(IGLib.MODID, "item/base/" + i.getResourceName()));
    }

    private void genericItem(Item item){
        if(item == null){
            StackTraceElement where = new NullPointerException().getStackTrace()[1];
            log.warn("Skipping null item. ( {} -> {} )", where.getFileName(), where.getLineNumber());
            return;
        }
        ItemBase i = (ItemBase) item;
        withExistingParent(new ResourceLocation(IGLib.MODID, "item/" + i.getHoldingName()).getPath(), new ResourceLocation(IGLib.MODID, "item/base/" + i.getUseType().getName()));
    }

    private void generateMultiblockItems(){
        rotarykilnItem();
        chemcialVatItem();
        gravitySeparatorItem();
        revfurnaceItem();
        crystalizerItem();
    }

    private void genericOreItem(Item item){
        if(item == null){
            StackTraceElement where = new NullPointerException().getStackTrace()[1];
            log.warn("Skipping null item. ( {} -> {} )", where.getFileName(), where.getLineNumber());
            return;
        }
        IGOreItem i = (IGOreItem) item;
        withExistingParent(new ResourceLocation(IGLib.MODID, "item/" + i.getHoldingName()).getPath(), new ResourceLocation(IGLib.MODID, "item/base/" + i.getUseType().getName()));
    }

    private void genericFlask(Item item){
        if(item == null){
            StackTraceElement where = new NullPointerException().getStackTrace()[1];
            log.warn("Skipping null item. ( {} -> {} )", where.getFileName(), where.getLineNumber());
            return;
        }
        IGBucketItem i = (IGBucketItem) item;
        if(i.getFluid() == Fluids.EMPTY){
            withExistingParent(new ResourceLocation(IGLib.MODID, "item/" + i.getHoldingName()).getPath(), new ResourceLocation(IGLib.MODID, "item/base/empty_" + i.getUseType().getName()));
        } else {
            withExistingParent(new ResourceLocation(IGLib.MODID, "item/" + i.getHoldingName()).getPath(), new ResourceLocation(IGLib.MODID, "item/base/" + i.getUseType().getName()));
        }
    }

    private void createBucket(Fluid f){
        withExistingParent(f.getFilledBucket().asItem().getRegistryName().getPath(), forgeLoc("item/bucket"))
                .customLoader(DynamicBucketModelBuilder::begin)
                .fluid(f);
    }

    protected ResourceLocation forgeLoc(String str){
        return new ResourceLocation("forge", str);
    }

    private String name(IItemProvider item){
        return item.asItem().getRegistryName().getPath();
    }

    public static ResourceLocation rl(String path)
    {
        return new ResourceLocation(IGLib.MODID, path);
    }

    private void rotarykilnItem(){
        ItemModelBuilder model = obj(IGMultiblockRegistrationHolder.Multiblock.rotarykiln, "multiblock/obj/rotarykiln/rotarykiln.obj")
                .texture("texture", modLoc("multiblock/rotarykiln"));

        ModelBuilder<?>.TransformsBuilder trans = model.transforms();
        doTransform(trans, Perspective.FIRSTPERSON_LEFT, new Vector3f(-1.75F, 2.5F, 1.25F), new Vector3f(0, 0, 0), 0.03125F);
        doTransform(trans, Perspective.FIRSTPERSON_RIGHT, new Vector3f(-1.75F, 2.5F, 1.75F), new Vector3f(0, 0, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_LEFT, new Vector3f(-0.75F, 0, -1.25F), new Vector3f(0, 0, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_RIGHT, new Vector3f(1.0F, 0, -1.75F), new Vector3f(0, 0, 0), 0.03125F);
        doTransform(trans, Perspective.HEAD, new Vector3f(0, 8, -8), null, 0.2F);
        doTransform(trans, Perspective.GUI, new Vector3f(3.5F, 0, 0), new Vector3f(30, 225, 0), 0.125F);
        doTransform(trans, Perspective.GROUND, new Vector3f(-1.5F, 3, -1.5F), null, 0.0625F);
        doTransform(trans, Perspective.FIXED, new Vector3f(-1, -8, -2), null, 0.0625F);
    }


    private void chemcialVatItem(){
        ItemModelBuilder model = obj(IGMultiblockRegistrationHolder.Multiblock.chemicalvat, "multiblock/obj/chemicalvat/chemicalvat.obj")
                .texture("texture", modLoc("multiblock/chemicalvat_base"));
        ModelBuilder<?>.TransformsBuilder trans = model.transforms();
        doTransform(trans, Perspective.FIRSTPERSON_LEFT, new Vector3f(-1.75F, 2.5F, 1.25F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.FIRSTPERSON_RIGHT, new Vector3f(-1.75F, 2.5F, 1.75F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_LEFT, new Vector3f(-0.75F, 0, -1.25F), new Vector3f(0, 90, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_RIGHT, new Vector3f(1.0F, 0, -1.75F), new Vector3f(0, 270, 0), 0.03125F);
        doTransform(trans, Perspective.HEAD, new Vector3f(0, 8, -8), null, 0.2F);
        doTransform(trans, Perspective.GUI, new Vector3f(0, -6, 0), new Vector3f(30, 225, 0), 0.1875F);
        doTransform(trans, Perspective.GROUND, new Vector3f(-1.5F, 3, -1.5F), null, 0.0625F);
        doTransform(trans, Perspective.FIXED, new Vector3f(-1, -8, -2), null, 0.0625F);
    }

    private void gravitySeparatorItem(){
        ItemModelBuilder model = obj(IGMultiblockRegistrationHolder.Multiblock.gravityseparator, "multiblock/obj/gravityseparator/gravityseparator.obj")
                .texture("texture", modLoc("multiblock/gravityseparator_base"));
        ModelBuilder<?>.TransformsBuilder trans = model.transforms();
        doTransform(trans, Perspective.FIRSTPERSON_LEFT, new Vector3f(-1.75F, 2.5F, 1.25F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.FIRSTPERSON_RIGHT, new Vector3f(-1.75F, 2.5F, 1.75F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_LEFT, new Vector3f(-0.75F, 0, -1.25F), new Vector3f(0, 90, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_RIGHT, new Vector3f(1.0F, 0, -1.75F), new Vector3f(0, 270, 0), 0.03125F);
        doTransform(trans, Perspective.HEAD, new Vector3f(0, 8, -8), null, 0.2F);
        doTransform(trans, Perspective.GUI, new Vector3f(0, -6, 0), new Vector3f(30, 225, 0), 0.15F);
        doTransform(trans, Perspective.GROUND, new Vector3f(-1.5F, 3, -1.5F), null, 0.0625F);
        doTransform(trans, Perspective.FIXED, new Vector3f(-1, -8, -2), null, 0.0625F);
    }

    private void revfurnaceItem(){
        ItemModelBuilder model = obj(IGMultiblockRegistrationHolder.Multiblock.reverberation_furnace, "multiblock/obj/revfurnace/reverberation_furnace.obj")
                .texture("texture", modLoc("multiblock/reverberation_furnace"));
        ModelBuilder<?>.TransformsBuilder trans = model.transforms();
        doTransform(trans, Perspective.FIRSTPERSON_LEFT, new Vector3f(-1.75F, 2.5F, 1.25F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.FIRSTPERSON_RIGHT, new Vector3f(-1.75F, 2.5F, 1.75F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_LEFT, new Vector3f(-0.75F, 0, -1.25F), new Vector3f(0, 90, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_RIGHT, new Vector3f(1.0F, 0, -1.75F), new Vector3f(0, 270, 0), 0.03125F);
        doTransform(trans, Perspective.HEAD, new Vector3f(0, 8, -8), null, 0.2F);
        doTransform(trans, Perspective.GUI, new Vector3f(0, -7, 0), new Vector3f(30, 120, 0), 0.08F);
        doTransform(trans, Perspective.GROUND, new Vector3f(-1.5F, 3, -1.5F), null, 0.1F);
        doTransform(trans, Perspective.FIXED, new Vector3f(-1, -8, -2), null, 0.0625F);
    }

    private void crystalizerItem() {
        ItemModelBuilder model = obj(IGMultiblockRegistrationHolder.Multiblock.crystallizer, "multiblock/obj/crystallizer/crystallizer.obj")
                .texture("texture", modLoc("multiblock/crystallizer"));
        ModelBuilder<?>.TransformsBuilder trans = model.transforms();
        doTransform(trans, Perspective.FIRSTPERSON_LEFT, new Vector3f(-1.75F, 2.5F, 1.25F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.FIRSTPERSON_RIGHT, new Vector3f(-1.75F, 2.5F, 1.75F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_LEFT, new Vector3f(-0.75F, 0, -1.25F), new Vector3f(0, 90, 0), 0.03125F);
        doTransform(trans, Perspective.THIRDPERSON_RIGHT, new Vector3f(1.0F, 0, -1.75F), new Vector3f(0, 270, 0), 0.03125F);
        doTransform(trans, Perspective.HEAD, new Vector3f(0, 8, -8), null, 0.2F);
        doTransform(trans, Perspective.GUI, new Vector3f(0F, -3F, 0), new Vector3f(30, 225, 0), 0.16F);
        doTransform(trans, Perspective.GROUND, new Vector3f(-1.5F, 3, -1.5F), null, 0.0625F);
        doTransform(trans, Perspective.FIXED, new Vector3f(-1, -8, -2), null, 0.0625F);
    }
}
