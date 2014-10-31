package de.memory.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.felix.service.command.CommandProcessor;

import de.memory.api.IMemoryModel;
import de.memory.api.IMemoryViewFactory;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(properties = { CommandProcessor.COMMAND_SCOPE + ":String=memory",
        CommandProcessor.COMMAND_FUNCTION + ":String=view" },
        provide = Object.class)
public class ViewCommand {
	
	private volatile List<IMemoryViewFactory> viewFactories = 
			Collections.synchronizedList(new ArrayList<IMemoryViewFactory>());
	
	@Reference(multiple = true, optional = true)
	public void addMemoryViewFactory(IMemoryViewFactory viewFactory) {
		viewFactories.add(viewFactory);
	}
	
	public void view(String name, String type) {
		IMemoryModel model = MemoryCommand.findModel(name);
		if(model == null) {
			System.err.println("Unknown memory "+ name);
			return;
		}
		boolean found = false;
		for(IMemoryViewFactory factory : factories()) {
			if(type.equals(factory.getProperties().getProperty("type"))) {
				factory.createView(model);
				found = true;
			}
		}
		if(found)
			System.out.println("Added "+type+" view to memory "+name);
		else
			System.err.println("Cannot create view of type "+type );
	}
	
	/**
	 * Print all view types
	 */
	public void view() {
		List<String> types = new ArrayList<String>();
		for(IMemoryViewFactory factory: factories()) {
			types.add(factory.getProperties().getProperty("type"));
		}
		Collections.sort(types);
		System.out.println("Available view types: " + types);
	}
	
	private List<IMemoryViewFactory> factories() {
		synchronized (viewFactories) {
			return new ArrayList<IMemoryViewFactory>(viewFactories);
		}
	}
	
}
