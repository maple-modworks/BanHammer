package com.herrkatze.banhammer.lists;

import com.herrkatze.banhammer.BanHammer;
import com.herrkatze.banhammer.BanHammerItem;
import com.herrkatze.banhammer.KickStick;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class itemList {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BanHammer.MODID);
    public static final RegistryObject<Item> BAN_HAMMER = ITEMS.register("ban_hammer", () -> new BanHammerItem(new Item.Properties()));
    public static final RegistryObject<Item> KICK_STICK = ITEMS.register("kick_stick", () -> new KickStick(new Item.Properties()));
}
