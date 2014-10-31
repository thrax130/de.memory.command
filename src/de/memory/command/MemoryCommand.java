package de.memory.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;

import de.memory.api.IMemoryModel;
import de.memory.api.IMemoryModelFactory;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=memory",
		CommandProcessor.COMMAND_FUNCTION + ":String=memory"
	},
	provide = Object.class
)
public class MemoryCommand {
	private volatile IMemoryModelFactory modelFactory;
    private static Map<String, IMemoryModel> memoryModels = new HashMap<String, IMemoryModel>();

    @Reference(multiple = false)
    public void setMemoryModelFactory(IMemoryModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    /**
     * Creates a new memory model.
     * 
     * @param name
     *            the name of the new memory
     */
    public void memory(String name) {
        if (memoryModels.containsKey(name))
            System.err.println("Name " + name + " already in use for a memory model.");
        else {
            IMemoryModel model = modelFactory.createModel();
            memoryModels.put(name, model);
            System.out.println("Memory model " + name + " created.");
        }
    }
    
    /**
     * Prints the list of all available memories.
     */
    public void memory() {
        List<String> memories = new ArrayList<String>(memoryModels.keySet());
        Collections.sort(memories);
        System.out.println("Available memories: " + memories);
    }

    static IMemoryModel findModel(String name) {
        return memoryModels.get(name);
    }
}


