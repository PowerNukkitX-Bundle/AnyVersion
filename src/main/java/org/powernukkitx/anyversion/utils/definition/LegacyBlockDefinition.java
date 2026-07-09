package org.powernukkitx.anyversion.utils.definition;

import org.powernukkitx.block.BlockState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class LegacyBlockDefinition extends Definition {

    private final String name;
    private final int id;
    private final int runtimeId;
    private final Map<String, Object> states;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BlockState state) {
            if(!name.equals(state.getIdentifier())) return false;
            for(var val : state.getBlockPropertyValues()) {
                String name = val.getPropertyType().getName();
                var value = val.getSerializedValue();
                if(value instanceof Byte b) value = Integer.valueOf(b);
                if(states.containsKey(name)) {
                    if(!value.equals(states.get(name))) return false;
                } else return false;
            }
            return true;
        }
        return super.equals(obj);
    }
}
