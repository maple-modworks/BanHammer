package com.herrkatze.banhammer.lists;

import com.herrkatze.banhammer.BanHammer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;

public class damageTypeList {
    public static final ResourceKey<DamageType>
        BAN = key("ban");
    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BanHammer.MODID,name));
    }
}
