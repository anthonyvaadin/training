package com.example.bugrap;

import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class BugRapReportDetails extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6131652696513246209L;
	private AbstractSelect prioritySelect = new NativeSelect("Priority");
	private AbstractSelect typeSelect = new NativeSelect("Type");
	private AbstractSelect statusSelect = new NativeSelect("Status");
	private AbstractSelect assignSelect = new NativeSelect("Assigned To");
	private AbstractSelect versionSelect = new NativeSelect("Version");
	
	private TextArea reportDescription = new TextArea();
	
	private Button updateButton;
	private ClickListener updateClickListener;
	private Button revertButton;
	private ClickListener revertClickListener;

	public BugRapReportDetails(final BugrapRepository repo, final Report report, final Table reportsTable) {
		initDefaultValues(repo, report);
		addComponent(new HorizontalSplitPanel()); //TODO calc height
		
		HorizontalLayout reportData = new HorizontalLayout();
		reportData.addComponent(prioritySelect);
		reportData.addComponent(typeSelect);
		reportData.addComponent(statusSelect);
		reportData.addComponent(assignSelect);
		reportData.addComponent(versionSelect);		

		updateButton = new Button("Update");
		reportData.addComponent(updateButton);
		
		revertButton = new Button("Revert");
		reportData.addComponent(revertButton);
		
		addComponent(reportData);
		addComponent(reportDescription);
		
		fillReportValues(repo, report, reportsTable);
	}
	
	public void fillReportValues(final BugrapRepository repo, final Report report, final Table reportsTable) {
		updateButton.removeClickListener(updateClickListener);
		revertButton.removeClickListener(revertClickListener);
		updateClickListener = new UpdateClickListener(repo, report, reportsTable);
		revertClickListener = new RevertClickListener(repo, report, reportsTable);
		updateButton.addClickListener(updateClickListener);
		revertButton.addClickListener(revertClickListener);
		
		prioritySelect.setValue(report.getPriority());
		typeSelect.setValue(report.getType());
		statusSelect.setValue(report.getStatus());
		versionSelect.setValue(report.getVersion());
		assignSelect.setValue(report.getAssigned());
		
		reportDescription.setValue(report.getDescription());
	}
	
	private void initDefaultValues(final BugrapRepository repo, Report report) {
		for (Priority p: Report.Priority.values())
			prioritySelect.addItem(p);
		
		for (Type t: Report.Type.values())
			typeSelect.addItem(t);
		
		for (Status s: Report.Status.values())
			statusSelect.addItem(s);
		
		for (Reporter r: repo.findReporters())
			assignSelect.addItem(r);
			
		for (ProjectVersion version: repo.findProjectVersions(report.getProject()))
			versionSelect.addItem(version);
		
		prioritySelect.setNullSelectionAllowed(false);
		typeSelect.setNullSelectionAllowed(false);
		statusSelect.setNullSelectionAllowed(false);
		versionSelect.setNullSelectionAllowed(false);
		
		//TODO read: imediate vs non-imediate.
		prioritySelect.setImmediate(false);
		typeSelect.setImmediate(false);
		statusSelect.setImmediate(false);
		versionSelect.setImmediate(false);
	}
	
	private class UpdateClickListener implements Button.ClickListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5172896210062311818L;
		private final Report report;
		private final BugrapRepository repo;
		private final Table reportsTable;
		private UpdateClickListener(final BugrapRepository repo, final Report report, final Table reportsTable) {
			this.report = report;
			this.repo = repo;
			this.reportsTable = reportsTable;
		}
		@Override
		public void buttonClick(ClickEvent event) {
			report.setPriority((Priority)prioritySelect.getValue());
			report.setType((Type)typeSelect.getValue());
			report.setStatus((Status)statusSelect.getValue());
			report.setAssigned((Reporter)assignSelect.getValue());
			report.setVersion((ProjectVersion)versionSelect.getValue());
			repo.save(report);
			
			report.setConsistencyVersion(report.getConsistencyVersion() + 1);
			reportsTable.refreshRowCache();
		}
	}
	
	private class RevertClickListener implements Button.ClickListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4639104237529403188L;
		private final Report report;
		private final BugrapRepository repo;
		private final Table reportsTable;
		private RevertClickListener(final BugrapRepository repo, final Report report, final Table reportsTable) {
			this.repo = repo;
			this.report = report;
			this.reportsTable = reportsTable;
		}
		@Override
		public void buttonClick(ClickEvent event) {
			fillReportValues(repo, report, reportsTable);
		}
	}
}
