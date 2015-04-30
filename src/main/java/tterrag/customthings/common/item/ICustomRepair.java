package tterrag.customthings.common.item;

import tterrag.customthings.common.config.json.IHasMaterial;
import tterrag.customthings.common.config.json.items.ItemType;

public interface ICustomRepair<T extends ItemType & IHasMaterial> extends ICustomItem<T>
{
}
