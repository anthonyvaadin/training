package com.example.bugrap;

import org.vaadin.peter.buttongroup.ButtonGroup;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

public class BugRapTableFilters extends HorizontalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4955231460572320145L;

	public BugRapTableFilters() {
    	ButtonGroup bg = new ButtonGroup();
    	bg.setCaption("Assignees");
    	bg.addButton(new Button("Only Me"));
    	bg.addButton(new Button("Everyone"));
    	bg.addStyleName("inline");
    	addComponent(bg);
    	
    	ButtonGroup bg2 = new ButtonGroup();
    	bg2.setCaption("Status");
    	bg2.addButton(new Button("Open"));
    	bg2.addButton(new Button("All Kinds"));
    	bg2.addButton(new Button("Custom")); //TODO dropdown list thing
    	bg2.addStyleName("inline");
    	addComponent(bg2);
	}
}
