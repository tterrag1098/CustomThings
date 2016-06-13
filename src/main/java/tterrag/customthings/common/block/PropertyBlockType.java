package tterrag.customthings.common.block;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import net.minecraft.block.properties.PropertyHelper;
import tterrag.customthings.common.config.json.BlockType;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;


public class PropertyBlockType extends PropertyHelper<BlockType> {

    private final List<BlockType> types;
    
    protected PropertyBlockType(String name, BlockType... types) {
        super(name, BlockType.class);
        this.types = ImmutableList.copyOf(types);
    }

    @Override
    public Collection<BlockType> getAllowedValues() {
        return types;
    }

    @Override
    public Optional<BlockType> parseValue(String value) {
        return BlockType.getType(value);
    }

    @Override
    public String getName(BlockType value) {
        return value.name.toLowerCase(Locale.US);
    }
}
