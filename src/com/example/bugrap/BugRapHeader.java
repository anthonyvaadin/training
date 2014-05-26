package com.example.bugrap;

import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;

public class BugRapHeader extends HorizontalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1713451596897901446L;

	private AbstractSelect projectSelect = new NativeSelect();

	public BugRapHeader(final BugrapRepository repo) {
    	
    	for (Project p: repo.findProjects())
    		projectSelect.addItem(p);
        
        projectSelect.setNullSelectionAllowed(false);
        addComponent(projectSelect);
        
        projectSelect.setImmediate(true);
        projectSelect.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 444933683679324930L;

			@Override
			public void valueChange(ValueChangeEvent event) {
//				versionSelect.removeAllItems();
//				for (ProjectVersion version: repo.findProjectVersions((Project)event.getProperty().getValue()))
//					versionSelect.addItem(version);
			}
        	
        });
        
        //username, logout
	}
	
	public AbstractSelect getProjectSelect() {
		return projectSelect;
	}
}
