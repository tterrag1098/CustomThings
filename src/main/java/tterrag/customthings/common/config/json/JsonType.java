package tterrag.customthings.common.config.json;

import javax.annotation.Nonnull;

public abstract class JsonType implements IJsonType
{
    /* JSON Fields @formatter:off */
    public @Nonnull String name = "null";
    /* End JSON Fields @formatter:on */
    
    @Override
    public void postInit()
    {
        ;
    }
}
