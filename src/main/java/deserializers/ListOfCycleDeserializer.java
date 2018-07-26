package deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import zephyrapiclasses.ListOfCycle;

public class ListOfCycleDeserializer implements JsonDeserializer<ListOfCycle> {

    @Override
    public ListOfCycle deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = element.getAsJsonObject();
        List<ListOfCycle.Cycle> cycles = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

            JsonElement entryValue = entry.getValue();
            if(!entryValue.isJsonPrimitive()) {
                ListOfCycle.Cycle cycle = context.deserialize(entryValue, ListOfCycle.Cycle.class);
                cycle.setCycleId(Integer.parseInt(entry.getKey()));
                cycles.add(cycle);
            }
        }
        return new ListOfCycle(cycles);
    }

}