package com.bawnorton.trimica.trim;

import com.bawnorton.configurable.Configurable;
import com.bawnorton.trimica.Trimica;
import com.bawnorton.trimica.item.component.MaterialAdditions;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public final class TrimMaterialRuntimeRegistry {

    public static final Holder<TrimMaterial> UNKNOWN_HOLDER = Holder.direct(
        new TrimMaterial(
            MaterialAssetGroup.create("unknown"),
            Component.literal("unknown")
        )
    );
    private final Map<TrimMaterial, MaterialAdditions> intrinsicAdditions =
        new HashMap<>();
    private final Map<TrimMaterial, Item> materialProviders = new HashMap<>();
    private final Map<Identifier, Holder<TrimMaterial>> materials =
        new HashMap<>();

    /**
     * Whether you can trim anything with anything.
     * Disabling this will prevent non-builtin materials from being used for trimming
     * and prevent any armour that doesn't support trimming from being trimmed
     */
    @Configurable(onSet = "com.bawnorton.trimica.Trimica#refreshEverything")
    public static boolean enableTrimEverything = true;

    public Item guessMaterialProvider(TrimMaterial material) {
        return materialProviders.computeIfAbsent(material, k -> {
            String suffix = Trimica.getMaterialRegistry().getSuffix(material);
            int trimicaStartIndex = suffix.indexOf("trimica/");
            if (trimicaStartIndex == -1) {
                return null;
            }
            String materialId = suffix
                .substring(trimicaStartIndex + "trimica/".length())
                .replaceFirst("/", ":");
            Identifier id = Identifier.tryParse(materialId);
            if (id == null) {
                return null;
            }
            return BuiltInRegistries.ITEM.getOptional(id).orElse(null);
        });
    }

    public Holder<TrimMaterial> getOrCreate(DataComponentGetter getter) {
        Identifier itemKey = getter.get(DataComponents.ITEM_MODEL);
        if (itemKey == null) return UNKNOWN_HOLDER;

        Identifier materialKey = Trimica.rl(
            "generated/%s/%s".formatted(
                itemKey.getNamespace(),
                itemKey.getPath()
            )
        );
        return materials.computeIfAbsent(materialKey, k -> {
            String suffix = "trimica/%s/%s".formatted(
                itemKey.getNamespace(),
                itemKey.getPath()
            );
            MaterialAssetGroup id = MaterialAssetGroup.create(suffix);
            return Holder.direct(
                new TrimMaterial(
                    id,
                    Component.translatable(
                        "trimica.material",
                        getter.getOrDefault(
                            DataComponents.ITEM_NAME,
                            Component.literal("Unknown")
                        )
                    )
                )
            );
        });
    }

    public String getSuffix(
        TrimMaterial material,
        ResourceKey<EquipmentAsset> assetKey
    ) {
        return assetKey == null
            ? material.assets().base().suffix()
            : material.assets().assetId(assetKey).suffix();
    }

    public String getSuffix(TrimMaterial material) {
        return material.assets().base().suffix();
    }

    public void registerMaterialReference(
        Holder.Reference<TrimMaterial> reference
    ) {
        materials.put(reference.key().identifier(), reference);
    }

    public void setIntrinsicAdditions(
        TrimMaterial material,
        MaterialAdditions addition
    ) {
        if (addition == null) return;

        intrinsicAdditions.put(material, addition);
    }

    public MaterialAdditions getIntrinsicAdditions(TrimMaterial trimMaterial) {
        return intrinsicAdditions.getOrDefault(
            trimMaterial,
            MaterialAdditions.NONE
        );
    }

    public void clear() {
        materialProviders.clear();
        materials.clear();
    }
}
