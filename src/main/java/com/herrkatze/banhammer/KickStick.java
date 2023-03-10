package com.herrkatze.banhammer;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KickStick extends Item {


    public KickStick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND){
            BanHammerScreenLoader.loadBanReasonGui(player,true);
        }
        return super.use(world, player, hand);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean  onLeftClickEntity(ItemStack stack, Player attackerPlayer, Entity targetEntity) {
        if (targetEntity instanceof Player) {
            Player targetPlayer = (Player) targetEntity;
            Level lvl = targetPlayer.getLevel();
            if (lvl instanceof ServerLevel && attackerPlayer.hasPermissions(3)){
                ServerLevel serverlvl = (ServerLevel) lvl;
                MinecraftServer server = serverlvl.getServer();
                GameProfile profile = targetPlayer.getGameProfile();
                CompoundTag Tag = attackerPlayer.getInventory().getSelected().getTag();
                String reason;
                if( Tag == null || Tag.getString("reason").equals("")){
                    reason = "Kicked by an operator";
                }
                else{
                    reason = Tag.getString("reason");
                }
                server.getPlayerList().getPlayer(profile.getId()).connection.disconnect(Component.literal(reason));
            }
        }
        return super.onLeftClickEntity(stack,attackerPlayer,targetEntity);
    }
}