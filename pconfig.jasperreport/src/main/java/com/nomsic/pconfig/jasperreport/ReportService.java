package com.nomsic.pconfig.jasperreport;

import java.io.File;
import java.util.List;

import com.nomsic.pconfig.model.FileType;
import com.nomsic.pconfig.model.FilledPconfig;
import com.nomsic.pconfig.model.Pconfig;
import com.nomsic.pconfig.model.ScheduledPconfig;



public interface ReportService {
	
	/**
	 * @return a list of available reports
	 */
	public List<Pconfig> getReports();
	
	/**
	 * @param name
	 * @return
	 */
	public Pconfig getReportByName(String name);
	
	/**
	 * Get all reports with the given property.
	 * 
	 * @param propertyName the name of the property.
	 * @param value optional parameter to also match the property value. Leave null to ignore.
	 * @return
	 */
	public List<Pconfig> getReportsByProperty(String propertyName, String value);
	
	/**
	 * Generate a report. The report parameters must all contain values.
	 * @param report
	 * @return the generated report id
	 * @throws ReportingException 
	 */
	public String generateReport(Pconfig report) throws ReportingException;
	
	/**
	 * @return a list of generated reports
	 */
	public List<FilledPconfig> getGeneratedReports(String name);
	
	/**
	 * @param reportId
	 * @return
	 */
	public FilledPconfig getGeneratedReport(String reportId);
	
	/**
	 * @param reportId
	 * @return the report file or null
	 */
	public File getGeneratedReportFile(String reportId);
	
	/**
	 * @param reportId
	 */
	public void deleteGeneratedReport(String reportId);

	String generateReport(Pconfig report, FileType type) throws ReportingException;

	/**
	 * Reload all reports and generated reports.
	 */
	public void refreshCache();

	/**
	 * Delete reports older than the maxAge. Default maxAge is 7 days.
	 */
	void deleteOldReports();
	
	public String saveScheduledReportConfig(ScheduledPconfig report) throws ReportingException ;

	public ScheduledPconfig getScheduledReport(String reportId);
	
	public String getPath(String id, FileType type);

	/**
	 * Gets a list of {@link ScheduledPconfig}'s that contain a {@link Pconfig} with
	 * the given pconfigId.
	 * 
	 * If the pconfigId is null all {@link ScheduledPconfig}'s will be returned;
	 * 
	 * @param pconfigId
	 * @return
	 */
	List<ScheduledPconfig> getScheduledReports(String pconfigId);

	void deleteScheduledReport(String reportId);

}
