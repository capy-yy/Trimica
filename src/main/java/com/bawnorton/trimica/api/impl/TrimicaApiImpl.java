package com.bawnorton.trimica.api.impl;

import com.bawnorton.trimica.api.*;
import com.bawnorton.trimica.client.palette.TrimPalette;
import com.bawnorton.trimica.item.component.MaterialAdditions;
import com.bawnorton.trimica.util.SortableEndpointHolder;
import java.util.PriorityQueue;
import java.util.Queue;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
@ApiStatus.Internal
public final class TrimicaApiImpl implements TrimicaApi {

    public static final TrimicaApiImpl INSTANCE = new TrimicaApiImpl();

    private final Queue<
        SortableEndpointHolder<BaseTextureInterceptor>
    > baseTextureInterceptors = new PriorityQueue<>();
    private final Queue<
        SortableEndpointHolder<CraftingRecipeInterceptor>
    > craftingRecipeInterceptors = new PriorityQueue<>();
    private final Queue<
        SortableEndpointHolder<PaletteInterceptor>
    > paletteInterceptors = new PriorityQueue<>();

    public void registerBaseTextureInterceptor(
        int priority,
        BaseTextureInterceptor baseTextureInterceptor
    ) {
        baseTextureInterceptors.add(
            new SortableEndpointHolder<>(baseTextureInterceptor, priority)
        );
    }

    public void registerCraftingRecipeInterceptor(
        int priority,
        CraftingRecipeInterceptor craftingRecipeInterceptor
    ) {
        craftingRecipeInterceptors.add(
            new SortableEndpointHolder<>(craftingRecipeInterceptor, priority)
        );
    }

    public void registerPaletteInterceptor(
        int priority,
        PaletteInterceptor paletteInterceptor
    ) {
        paletteInterceptors.add(
            new SortableEndpointHolder<>(paletteInterceptor, priority)
        );
    }

    public Identifier applyBaseTextureInterceptorsForItem(
        Identifier expectedBaseTexture,
        ItemStack itemWithTrim,
        ArmorTrim armourTrim
    ) {
        for (SortableEndpointHolder<
            BaseTextureInterceptor
        > endpointHolder : baseTextureInterceptors) {
            expectedBaseTexture = endpointHolder
                .endpoint()
                .interceptItemTexture(
                    expectedBaseTexture,
                    itemWithTrim,
                    armourTrim
                );
        }
        return expectedBaseTexture;
    }

    public Identifier applyBaseTextureInterceptorsForArmour(
        Identifier expectedBaseTexture,
        ItemStack itemWithTrim,
        ArmorTrim armourTrim
    ) {
        for (SortableEndpointHolder<
            BaseTextureInterceptor
        > endpointHolder : baseTextureInterceptors) {
            expectedBaseTexture = endpointHolder
                .endpoint()
                .interceptArmourTexture(
                    expectedBaseTexture,
                    itemWithTrim,
                    armourTrim
                );
        }
        return expectedBaseTexture;
    }

    public Identifier applyBaseTextureInterceptorsForShield(
        Identifier expectedBaseTexture,
        DataComponentGetter componentGetter,
        ArmorTrim armourTrim
    ) {
        for (SortableEndpointHolder<
            BaseTextureInterceptor
        > endpointHolder : baseTextureInterceptors) {
            expectedBaseTexture = endpointHolder
                .endpoint()
                .interceptShieldTexture(
                    expectedBaseTexture,
                    componentGetter,
                    armourTrim
                );
        }
        return expectedBaseTexture;
    }

    public Ingredient applyCraftingRecipeInterceptorsForAddition(
        Ingredient current
    ) {
        for (SortableEndpointHolder<
            CraftingRecipeInterceptor
        > endpointHolder : craftingRecipeInterceptors) {
            current = endpointHolder.endpoint().getAdditionIngredient(current);
        }
        return current;
    }

    public Ingredient applyCraftingRecipeInterceptorsForBase(
        Ingredient current
    ) {
        for (SortableEndpointHolder<
            CraftingRecipeInterceptor
        > endpointHolder : craftingRecipeInterceptors) {
            current = endpointHolder.endpoint().getBaseIngredient(current);
        }
        return current;
    }

    @Deprecated(since = "1.4.0")
    public TrimPalette applyPaletteInterceptorsForGeneration(
        TrimPalette generated,
        TrimMaterial material
    ) {
        for (SortableEndpointHolder<
            PaletteInterceptor
        > endpointHolder : paletteInterceptors) {
            generated = endpointHolder
                .endpoint()
                .interceptGenerated(generated, material);
        }
        return generated;
    }

    @Deprecated(since = "1.4.0")
    public TrimPalette applyPaletteInterceptorsForMaterialAdditions(
        TrimPalette palette,
        MaterialAdditions additions
    ) {
        for (SortableEndpointHolder<
            PaletteInterceptor
        > endpointHolder : paletteInterceptors) {
            palette = endpointHolder
                .endpoint()
                .interceptMaterialAdditions(palette, additions);
        }
        return palette;
    }
}
