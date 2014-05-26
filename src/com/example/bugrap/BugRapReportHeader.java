package com.example.bugrap;

import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;

public class BugRapReportHeader extends HorizontalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private AbstractSelect versionSelect = new NativeSelect("Reports for");
	private Table reportCountBar = new Table();
	
	public BugRapReportHeader(final BugrapRepository repo, final BugRapReportTable tableSection, final Project project) {
		
//		Project project = bugRapUI.get
		versionSelect.setNullSelectionItemId("All versions");
        for (ProjectVersion version: repo.findProjectVersions(project))
        	versionSelect.addItem(version);
        
        addComponent(versionSelect);
        versionSelect.addStyleName("inline");
        
        versionSelect.addValueChangeListener(new ValueChangeListener(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -2872652654125058619L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				updateReportCountBar(repo, tableSection, project, (ProjectVersion)versionSelect.getValue());
				tableSection.updateBugsListData(repo, project, (ProjectVersion)versionSelect.getValue());
			}
        });
        
        addComponent(reportCountBar);
        updateReportCountBar(repo, tableSection, project, (ProjectVersion)versionSelect.getValue());
        
        reportCountBar.addContainerProperty("closedReports", Long.class, null);
        reportCountBar.addContainerProperty("nonResolvedReports", Long.class, null);
        reportCountBar.addContainerProperty("unassignedReports", Long.class, null);
        reportCountBar.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        
        reportCountBar.setColumnReorderingAllowed(false);
        reportCountBar.setColumnCollapsingAllowed(false);
	}
	
    private void updateReportCountBar(final BugrapRepository repo, final BugRapReportTable tableSection, final Project project, final ProjectVersion version) {
    	  long closedReports;
          long nonResolvedReports;
          long unassignedReports;
          if (version != null) {
          	closedReports = repo.countClosedReports(version);
          	nonResolvedReports = repo.countOpenedReports(version);
          	unassignedReports = repo.countUnassignedReports(version);
          } else { 
          	closedReports = repo.countClosedReports(project);
          	nonResolvedReports = repo.countOpenedReports(project);
          	unassignedReports = repo.countUnassignedReports(project);
          }
          
          reportCountBar.removeAllItems();
          reportCountBar.addItem(new Object[]{closedReports, nonResolvedReports, unassignedReports});
//          reportCountBar.setHeight("");
          
          long totalReports = closedReports + nonResolvedReports + unassignedReports;
          reportCountBar.setColumnWidth("closedReports", calcColumnWidth(closedReports, totalReports));
          reportCountBar.setColumnWidth("nonResolvedReports", calcColumnWidth(nonResolvedReports, totalReports));
          reportCountBar.setColumnWidth("unassignedReports", calcColumnWidth(unassignedReports, totalReports));
          reportCountBar.setId("reportBar");
    }
    
    private int calcColumnWidth(long columnValue, long total) {
    	if (total == 0l)
    		return 30;
    	
        float maxWidth = 300f;
    	long percent = columnValue / total;
    	
    	int columnWidth = (int)(percent * maxWidth); 
    	return columnWidth < 30 ? 30 : columnWidth;
    }
    
    public AbstractSelect getVersionSelect() {
    	return versionSelect;
    }
}
