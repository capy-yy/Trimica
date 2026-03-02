package com.bawnorton.trimica.mixin;

import com.bawnorton.trimica.Trimica;
import com.bawnorton.trimica.TrimicaToggles;
import com.bawnorton.trimica.item.component.MaterialAdditions;
import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SimpleJsonResourceReloadListener.class)
public abstract class SimpleJsonResourceReloadListenerMixin {

    @ModifyExpressionValue(
        method = "scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
        )
    )
    private static Iterator<
        Map.Entry<Identifier, Resource>
    > preventLoadingInvalidResources(
        Iterator<Map.Entry<Identifier, Resource>> original
    ) {
        return Iterators.filter(original, entry -> {
            List<Pair<Identifier, Boolean>> disableList = new ArrayList<>();
            disableList.add(
                Pair.of(
                    Trimica.rl(
                        "advancement/adventure/add_material_addition.json"
                    ),
                    !TrimicaToggles.enableItems ||
                        !TrimicaToggles.enableAnimator ||
                        !MaterialAdditions.enableMaterialAdditions
                )
            );
            disableList.add(
                Pair.of(
                    Trimica.rl("recipe/rainbowifier.json"),
                    !TrimicaToggles.enableItems ||
                        !TrimicaToggles.enableRainbowifier
                )
            );
            disableList.add(
                Pair.of(
                    Trimica.rl(
                        "advancement/adventure/add_rainbowifier_material.json"
                    ),
                    !TrimicaToggles.enableItems ||
                        !TrimicaToggles.enableRainbowifier
                )
            );
            Identifier id = entry.getKey();
            for (Pair<Identifier, Boolean> pair : disableList) {
                if (pair.getFirst().equals(id) && pair.getSecond()) {
                    return false;
                }
            }
            return true;
        });
    }
}
