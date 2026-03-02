package com.bawnorton.trimica.mixin.accessor;

import java.util.List;
import java.util.Optional;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootTable.class)
public interface LootTableAccessor {
    @Accessor("pools")
    List<LootPool> trimica$pools();

    @Accessor("functions")
    List<LootItemFunction> trimica$functions();

    @Accessor("randomSequence")
    Optional<Identifier> trimica$randomSequence();
}
