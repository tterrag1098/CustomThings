package tterrag.customthings.common.handlers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatisticsFile;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import tterrag.customthings.common.config.json.AchievementType;
import tterrag.customthings.common.config.json.AchievementType.AchievementData;
import tterrag.customthings.common.config.json.AchievementType.AchievementSource;

import com.enderio.core.EnderCore;
import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.util.ItemUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

@Handler
public class AchievementHandler
{
    @SubscribeEvent
    public void onBlockBreak(BreakEvent event)
    {
        triggerAchievement(AchievementSource.BLOCK_BREAK, event.getPlayer(), event.block, event.blockMetadata);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        if (event.source.getSourceOfDamage() instanceof EntityPlayer)
        {
            triggerAchievement(AchievementSource.ENTITY_KILL, (EntityPlayer) event.source.getSourceOfDamage(), event.entityLiving);
        }
    }

    @SubscribeEvent
    public void onItemPickup(ItemPickupEvent event)
    {
        triggerAchievement(AchievementSource.ITEM_PICKUP, event.player, event.pickedUp.getEntityItem());
    }

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event)
    {
        triggerAchievement(AchievementSource.CRAFTING, event.player, event.crafting);
    }
    
    private static boolean ignoreAchievement = false;
    
    @SubscribeEvent
    public void onAchievement(final AchievementEvent event)
    {
        if (ignoreAchievement || event.entityPlayer.worldObj.isRemote || ((EntityPlayerMP)event.entityPlayer).func_147099_x().hasAchievementUnlocked(event.achievement))
        {
            return;
        }
        
        AchievementPage page = null;
        for (AchievementPage p : AchievementPage.getAchievementPages())
        {
            if (p.getAchievements().contains(event.achievement))
            {
                page = p;
            }
        }
       
        final AchievementPage pf = page;
        EnderCore.proxy.getScheduler().schedule(50, new Runnable()
        {
            @Override
            public void run()
            {
                ignoreAchievement = true;
                triggerAchievement(AchievementSource.ACHIEVEMENT_PAGE, event.entityPlayer, pf, event.entityPlayer, event.achievement);
                ignoreAchievement = false;
            }
        });
    }

    private static void triggerAchievement(AchievementSource source, EntityPlayer player, Object... in)
    {
        if (!player.worldObj.isRemote && player instanceof EntityPlayerMP) // LP made me do it
        {
            for (AchievementType type : AchievementType.lookup.get(source))
            {
                if (source.matchesObject(type.sourceObj, in))
                {
                    if (type.rewardStack != null)
                    {
                        StatisticsFile file = ((EntityPlayerMP) player).func_147099_x();
                        if (!file.hasAchievementUnlocked(type.achievement) && file.canUnlockAchievement(type.achievement))
                        {
                            ItemStack stack = type.rewardStack.copy();
                            if (type.rewardNBT != null) {
                                stack = ItemUtil.parseStringIntoItemStack(ItemUtil.getStringForItemStack(stack, true, true) + "$" + AchievementType.Template.format(type.rewardNBT, new AchievementData(type.achievement, player)));
                            }
                            if (!player.inventory.addItemStackToInventory(stack))
                            {
                                player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ,
                                        type.rewardStack.copy()));
                            }
                        }
                    }
                    player.addStat(type.achievement, 1);
                }
            }
        }
    }
}
