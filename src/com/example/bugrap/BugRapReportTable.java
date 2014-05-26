package com.example.bugrap;

import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.BugrapRepository.ReportsQuery;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class BugRapReportTable extends HorizontalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8998557091964789738L;
	private Table reportsTable = new Table();
	private BugRapReportDetails reportDetails;
	
	private final VerticalLayout mainLayout;
	
	public BugRapReportTable(final VerticalLayout layout, final BugrapRepository repo, Project project, ProjectVersion projectVersion) {
		this.mainLayout = layout;
		
		addComponent(reportsTable);
		reportsTable.setWidth(1f, Unit.PERCENTAGE);
    	reportsTable.setSelectable(true);
    	updateBugsListData(repo, project, projectVersion);
	}
	
    public void updateBugsListData(final BugrapRepository repo, Project project, ProjectVersion projectVersion) {
    	ReportsQuery query = new ReportsQuery();
    	query.project = project;
    	query.projectVersion = projectVersion;
    	query.reportAssignee = null;
    	query.reportStatuses = null;
    	
    	BeanItemContainer<Report> container = new BeanItemContainer<Report>(Report.class);
		for (Report report: repo.findReports(query)) {
			container.addItem(report);
		}
		reportsTable.setContainerDataSource(container);

		reportsTable.setVisibleColumns(new Object[]{"priority", "type", "summary", "assigned", "timestamp", "reportedTimestamp"});
		reportsTable.setColumnHeaders(new String[]{"Priority", "Type", "Summary", "Assigned To", "Last Modified", "Reported"});
		
		reportsTable.addValueChangeListener(new ValueChangeListener(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 7956092326271575913L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Report report = (Report)event.getProperty().getValue();
				
				if (report == null) {
					if (reportDetails != null)
						mainLayout.removeComponent(reportDetails);
					reportDetails = null;
				} else {
					
					if (reportDetails == null) {
						reportDetails = new BugRapReportDetails(repo, report, reportsTable);
						mainLayout.addComponent(reportDetails);
					}
					reportDetails.fillReportValues(repo, report, reportsTable);
				}
			}
			
		});
	}
}
