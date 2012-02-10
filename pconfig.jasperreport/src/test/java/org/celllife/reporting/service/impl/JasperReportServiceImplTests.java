package org.celllife.reporting.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.springframework.core.io.FileSystemResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nomsic.pconfig.jasperreport.ReportingException;
import com.nomsic.pconfig.jasperreport.impl.JasperReportServiceImpl;
import com.nomsic.pconfig.jasperreport.impl.SpringResourceLoader;
import com.nomsic.pconfig.model.BooleanParameter;
import com.nomsic.pconfig.model.DateParameter;
import com.nomsic.pconfig.model.EntityParameter;
import com.nomsic.pconfig.model.FileType;
import com.nomsic.pconfig.model.FilledPconfig;
import com.nomsic.pconfig.model.IntegerParameter;
import com.nomsic.pconfig.model.Parameter;
import com.nomsic.pconfig.model.Pconfig;
import com.nomsic.pconfig.model.RepeatInterval;
import com.nomsic.pconfig.model.ScheduledPconfig;
import com.nomsic.pconfig.model.StringParameter;

/**
 * @author Simon
 *
 */
public class JasperReportServiceImplTests {
	
	private JasperReportServiceImpl service;
	
	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Before
	public void setup() throws ReportingException{
		service = new JasperReportServiceImpl();
		service.setGeneratedReportFolder("target/generatedReports");
		service.setScheduledReportFolder("target/schedulesReports");
		service.setSourceReportFolder("target/reports/org/celllife/reporting/testreports");
		service.setScheduledReportFolder("target/scheduledReports");
		SpringResourceLoader loader = new SpringResourceLoader();
		loader.setResourceLoader(new FileSystemResourceLoader());
		service.setReportLoader(loader);
		service.buildService();
	}
	
	@Test
	public void testGetReports(){
		Collection<Pconfig> reports = service.getReports();
		Assert.assertEquals(6, reports.size());
	}
	
	@Test
	public void testGetReportByName(){
		String name = "report2";
		Pconfig report = service.getReportByName(name);
		Assert.assertEquals(name, report.getId());
	}
	
	@Test
	public void testGetReportsByProperty_nullValue(){
		String prop = "test-key1";
		Collection<Pconfig> reports = service.getReportsByProperty(prop, null);
		Assert.assertEquals(2, reports.size());
		for (Pconfig report : reports) {
			String name = report.getId();
			Assert.assertTrue(name.equals("report1") || name.equals("report2"));
		}
	}
	
	@Test
	public void testGetReportsByProperty_notNullValue(){
		String prop = "test-key1";
		String propVal = "value1";
		Collection<Pconfig> reports = service.getReportsByProperty(prop, propVal);
		Assert.assertEquals(1, reports.size());
		reports.iterator().next().getId().equals("report1");
	}
	
	@Test
	public void testGenerateReport() throws Exception{
		Pconfig demo = service.getReportByName("demo");
		fillParameters(demo);
		String id = service.generateReport(demo, FileType.XML);
		
		FilledPconfig report = service.getGeneratedReport(id);
		Assert.assertEquals(id, report.getId());
		File reportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(reportFile.exists());
		
		validateReportFile(report.getPconfig(), reportFile);
	}
	
	@Test
	public void testGenerateReport_entity() throws Exception{
		Pconfig demo = service.getReportByName("entity_demo");
		fillParameters(demo);
		String id = service.generateReport(demo, FileType.XML);
		File generatedReportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(generatedReportFile.exists());
		
		FilledPconfig report = service.getGeneratedReport(id);
		Assert.assertEquals(id, report.getId());
		File reportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(reportFile.exists());
		
		validateReportFile(report.getPconfig(), reportFile);
	}
	
	@Test
	public void testGenerateReport_entity_nullParams() throws Exception{
		Pconfig demo = service.getReportByName("entity_demo");
		String id = service.generateReport(demo, FileType.XML);
		File generatedReportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(generatedReportFile.exists());
		
		FilledPconfig report = service.getGeneratedReport(id);
		Assert.assertEquals(id, report.getId());
		File reportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(reportFile.exists());
		
		validateReportFile(report.getPconfig(), reportFile);
	}
	
	@Test
	public void testGenerateReport_subreport() throws ReportingException, FileNotFoundException, IOException{
		Pconfig demo = service.getReportByName("demo_subreport");
		fillParameters(demo);
		String id = service.generateReport(demo, FileType.XML);
		File generatedReportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(generatedReportFile.exists());
		
		FilledPconfig report = service.getGeneratedReport(id);
		Assert.assertEquals(id, report.getId());
		File reportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(reportFile.exists());
		
		String reportContents = IOUtils.toString(new FileInputStream(reportFile));
		Assert.assertTrue(reportContents.contains("unique report text only in subreport"));
	}
	
	@Test
	public void testDeleteOldReports() throws ReportingException{
		Pconfig demo = service.getReportByName("demo");
		fillParameters(demo);
		String id1 = service.generateReport(demo, FileType.XML);
		File generatedReportFile1 = service.getGeneratedReportFile(id1);
		Assert.assertTrue(generatedReportFile1.exists());
		
		service.setMaxAge(-1);
		service.deleteOldReports();
		
		Assert.assertFalse(generatedReportFile1.exists());
	}
	
	@Test
	public void testGetGeneratedReports() throws ReportingException{
		Pconfig demo = service.getReportByName("demo");
		fillParameters(demo);
		String id = service.generateReport(demo, FileType.XML);
		
		FilledPconfig report = service.getGeneratedReport(id);
		Assert.assertEquals(id, report.getId());
		File reportFile = service.getGeneratedReportFile(id);
		Assert.assertTrue(reportFile.exists());
		
		List<FilledPconfig> generatedReports = service.getGeneratedReports(demo.getId());
		Assert.assertEquals(1, generatedReports.size());
	}
	
	@Test
	public void testSaveScheduledReport() throws ReportingException{
		Pconfig demo = service.getReportByName("demo");
		fillParameters(demo);
		ScheduledPconfig scheduledPconfig = new ScheduledPconfig(demo);
		scheduledPconfig.setEndDate(new Date());
		scheduledPconfig.setStartDate(new Date());
		scheduledPconfig.setIntervalCount(7);
		scheduledPconfig.setRepeatInterval(RepeatInterval.Daily);
		scheduledPconfig.setScheduledFor("test@test.com");
		
		String id = service.saveScheduledReportConfig(scheduledPconfig);
		
		List<ScheduledPconfig> scheduledReports = service.getScheduledReports(demo.getId());
		Assert.assertEquals(1, scheduledReports.size());
		
		ScheduledPconfig scheduledReport = service.getScheduledReport(id);
		Assert.assertEquals(scheduledPconfig.getId(), scheduledReport.getId());
		Assert.assertEquals(scheduledPconfig.getEndDate(), scheduledReport.getEndDate());
		Assert.assertEquals(scheduledPconfig.getStartDate(), scheduledReport.getStartDate());
		Assert.assertEquals(scheduledPconfig.getIntervalCount(), scheduledReport.getIntervalCount());
		Assert.assertEquals(scheduledPconfig.getRepeatInterval(), scheduledReport.getRepeatInterval());
		Assert.assertEquals(scheduledPconfig.getScheduledFor(), scheduledReport.getScheduledFor());
	}
	
	@Test
	public void testDeleteScheduledReport() throws ReportingException{
		Pconfig demo = service.getReportByName("demo");
		ScheduledPconfig scheduledPconfig = new ScheduledPconfig(demo);
		
		String id = service.saveScheduledReportConfig(scheduledPconfig);
		ScheduledPconfig scheduledReport = service.getScheduledReport(id);
		Assert.assertNotNull(scheduledReport);
		
		service.deleteScheduledReport(id);
		ScheduledPconfig deleted = service.getScheduledReport(id);
		Assert.assertNull(deleted);
		
	}
	/**
	 * Sets test values for the report paramters
	 */
	private void fillParameters(Pconfig report) {
		List<? extends Parameter<?>> parameters = report.getParameters();
		for (Parameter<?> param : parameters) {
			if (param instanceof StringParameter){
				((StringParameter) param).setValue("test string");
			} else if (param instanceof IntegerParameter){
				((IntegerParameter) param).setValue(13);
			} else if (param instanceof DateParameter){
				((DateParameter) param).setValue(new Date());
			} else if (param instanceof BooleanParameter){
				((BooleanParameter) param).setValue(true);
			} else if (param instanceof EntityParameter){
				EntityParameter eparam = (EntityParameter) param;
				String type = eparam.getValueType();
				if (Integer.class.getSimpleName().equals(type)){
					eparam.setValue("21");
				} else if (Long.class.getSimpleName().equals(type)){
					eparam.setValue("42");
				}if (Double.class.getSimpleName().equals(type)){
					eparam.setValue("9.4");
				}if (Boolean.class.getSimpleName().equals(type)){
					eparam.setValue("false");
				}
			}
		}
	}
	
	/**
	 * Checks that the XML report file contains the parameter values. It does this by
	 * looking up Xpath expressions. The report file contains element keys which locate
	 * the elements. These can be used to find the element which should contain the value
	 * of a particular parameter.
	 */
	private void validateReportFile(Pconfig report, File reportFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(reportFile);
		
		XPathFactory xfactory = XPathFactory.newInstance();
		XPath xpath = xfactory.newXPath();

		for (Parameter<?> param : report.getParameters()) {
			String paramkey = param.getName() + "key";
			XPathExpression expr = xpath.compile("//reportElement[@key='"+paramkey+"']/following-sibling::*");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			
			NodeList nodes = (NodeList) result;
			collector.checkThat(nodes.getLength(), IsEqual.equalTo(1));
			
			Node item = nodes.item(0);
			String textContent = item.getTextContent();
			Object value = param.getValue();
			if (value == null){
				value = param.getDefaultValue();
			}
			
			if (param instanceof DateParameter){
				Date date = ((DateParameter) param).getValue();
				value = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
			}
			collector.checkThat(value, IsNull.notNullValue());
			if (value != null){
				collector.checkThat(textContent, IsEqual.equalTo(value.toString()));
			}
		}
	}

}
