package tterrag.customthings.common.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import tterrag.customthings.common.config.json.AchievementType;
import tterrag.customthings.common.config.json.AchievementType.AchievementSource;

import com.enderio.core.common.Handlers.Handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

@Handler
public class AchievementHandler
{
    @SubscribeEvent
    public void onBlockBreak(BreakEvent event)
    {
        if (event.world.isRemote)
        {
            return;
        }

        triggerAchievement(AchievementSource.BLOCK_BREAK, event.getPlayer(), event.block, event.blockMetadata);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        if (event.entity.worldObj.isRemote || !(event.source.getSourceOfDamage() instanceof EntityPlayer))
        {
            return;
        }

        triggerAchievement(AchievementSource.ENTITY_KILL, (EntityPlayer) event.source.getSourceOfDamage(), event.entityLiving);
    }

    @SubscribeEvent
    public void onItemPickup(ItemPickupEvent event)
    {
        if (event.player.worldObj.isRemote)
        {
            return;
        }

        triggerAchievement(AchievementSource.ITEM_PICKUP, event.player, event.pickedUp.getEntityItem());
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event)
    {
        if (event.player.worldObj.isRemote)
        {
            return;
        }

        triggerAchievement(AchievementSource.CRAFTING, event.player, event.crafting);
    }

    private static void triggerAchievement(AchievementSource source, EntityPlayer player, Object... in)
    {
        for (AchievementType type : AchievementType.lookup.get(source))
        {
            if (source.matchesObject(type.sourceObj, in))
            {
                player.addStat(type.achievement, 1);
            }
        }
    }
}
