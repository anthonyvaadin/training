package com.example.bugrap;

import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main UI class
 */
@Widgetset("com.example.bugrap.widgetset.BugrapWidgetset")
@Theme("bugrap")
public class BugrapUI extends UI {

    private static final long serialVersionUID = 1261106759300058322L;
    

    /**
     * Backend access point
     */
    private final BugrapRepository repo = new BugrapRepository("bugrap");

    // ^^^ You probably would like to use "/var/tmp/bugrap" on OSX/Linux ;)
    
    private BugRapHeader bugRapHeader;
    private BugRapReportTable tableSection;
    private BugRapProjectMenu bugRapProjectMenu;
    private BugRapReportHeader bugRapReportHeader;
    private BugRapTableFilters bugRapTableFilters;
    
    public AbstractSelect getProjectSelect() {
    	if (bugRapHeader == null)
        	return null;
    	return bugRapHeader.getProjectSelect();
    }
    
    public AbstractSelect getVersionSelect() {
    	if (bugRapReportHeader == null)
    		return null;
    	return bugRapReportHeader.getVersionSelect();
    }
    
	@Override
    protected void init(VaadinRequest request) {

        // initialize backend
        repo.populateWithTestData();

        // build layout
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        // TODO implement BugRap. Good luck :)
        
        bugRapHeader = new BugRapHeader(repo);
        bugRapReportHeader = new BugRapReportHeader(repo, tableSection, project);        
        tableSection = new BugRapReportTable(layout, repo, project, projectVersion);
        bugRapProjectMenu = new BugRapProjectMenu();        
        bugRapTableFilters = new BugRapTableFilters();
        
        layout.addComponent(bugRapHeader);
        layout.addComponent(bugRapProjectMenu);
        layout.addComponent(bugRapReportHeader);
        layout.addComponent(bugRapTableFilters);
        layout.addComponent(tableSection);
	}
}