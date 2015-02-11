package tterrag.customthings.common.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import tterrag.core.common.Handlers.Handler;
import tterrag.customthings.common.config.json.AchievementType;
import tterrag.customthings.common.config.json.AchievementType.AchievementSource;

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

        for (AchievementType type : AchievementType.achievements)
        {
            if (type.achievementSource == AchievementSource.BLOCK_BREAK)
            {
                if (type.achievementSource.matchesObject(type.sourceObj, event.block, event.blockMetadata))
                {
                    event.getPlayer().addStat(type.achievement, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        if (event.entity.worldObj.isRemote || !(event.source.getSourceOfDamage() instanceof EntityPlayer))
        {
            return;
        }

        for (AchievementType type : AchievementType.achievements)
        {
            if (type.achievementSource == AchievementSource.ENTITY_KILL)
            {
                if (type.achievementSource.matchesObject(type.sourceObj, event.entityLiving))
                {
                    ((EntityPlayer) event.source.getSourceOfDamage()).addStat(type.achievement, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemPickup(ItemPickupEvent event)
    {
        if (event.player.worldObj.isRemote)
        {
            return;
        }

        for (AchievementType type : AchievementType.achievements)
        {
            if (type.achievementSource == AchievementSource.ITEM_PICKUP)
            {
                if (type.achievementSource.matchesObject(type.sourceObj, event.pickedUp.getEntityItem()))
                {
                    event.player.addStat(type.achievement, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event)
    {
        if (event.player.worldObj.isRemote)
        {
            return;
        }

        for (AchievementType type : AchievementType.achievements)
        {
            if (type.achievementSource == AchievementSource.CRAFTING)
            {
                if (type.achievementSource.matchesObject(type.sourceObj, event.crafting))
                {
                    event.player.addStat(type.achievement, 1);
                }
            }
        }
    }
}
