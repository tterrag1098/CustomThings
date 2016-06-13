package tterrag.customthings.common.handlers;

import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tterrag.customthings.common.item.ICustomItem;

import com.enderio.core.common.Handlers.Handler;

@Handler
@SuppressWarnings("deprecation")
public class TooltipHandler
{
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event)
    {
        if (event.getItemStack() != null && event.getItemStack().getItem() instanceof ICustomItem<?>)
        {
            String unloc = event.getItemStack().getUnlocalizedName() + ".tooltip.line";
            int lineNum = 1;
            while (I18n.canTranslate(unloc + lineNum))
            {
                event.getToolTip().add(I18n.translateToLocal(unloc + lineNum));
                lineNum++;
            }
        }
    }
}
