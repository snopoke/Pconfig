package com.nomsic.pconfig.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.nomsic.pconfig.model.BaseParameter;
import com.nomsic.pconfig.model.BooleanParameter;
import com.nomsic.pconfig.model.DateParameter;
import com.nomsic.pconfig.model.EntityParameter;
import com.nomsic.pconfig.model.FilledPconfig;
import com.nomsic.pconfig.model.IntegerParameter;
import com.nomsic.pconfig.model.LabelParameter;
import com.nomsic.pconfig.model.Parameter;
import com.nomsic.pconfig.model.Pconfig;
import com.nomsic.pconfig.model.RepeatInterval;
import com.nomsic.pconfig.model.ScheduledPconfig;
import com.nomsic.pconfig.model.StringParameter;

public class MarshallingTest {

	private static final String UTF_8 = "UTF-8";
	protected static final File TEST_FILE = new File("target/test.xml");

	@Before
	public void setUp() throws Exception {
		if (TEST_FILE.exists()) {
			if (!TEST_FILE.delete()) {
				Assert.fail("impossible to delete the test file, please release it and run the test again");
			}
		}
	}
	
	@Test
	public void testReadWrite() throws JAXBException, IOException, SAXException{
		Pconfig report = new Pconfig();
		report.setLabel("report name");
		report.setResource("/com/nomsik/pconfig/model/testreport.jasper");
		report.setId("id");
		
		writeXml(report, TEST_FILE);
		Pconfig read = (Pconfig) read(report, TEST_FILE);
		
		Assert.assertEquals(report.getLabel(), read.getLabel());
		Assert.assertEquals(report.getResource(), read.getResource());
		Assert.assertEquals(report.getId(), read.getId());
	}
	
	@Test
	public void testReadWrite_dateParam() throws JAXBException, IOException, SAXException{
		DateParameter dateParameter = new DateParameter();
		dateParameter.setValue(new Date());
		
		DateParameter param = readWriteReportWithParameter(dateParameter);
		
		Assert.assertEquals(dateParameter.getValue(), param.getValue());
	}
	
	@Test
	public void testReadWrite_integerParam() throws JAXBException, IOException, SAXException{
		IntegerParameter p2 = new IntegerParameter();
		p2.setValue(5);

		IntegerParameter param = readWriteReportWithParameter(p2);
		
		Assert.assertEquals(p2.getValue(), param.getValue());
	}
	
	@Test
	public void testReadWrite_stringParam() throws JAXBException, IOException, SAXException{
		StringParameter p3 = new StringParameter();
		p3.setValue("hello");
		
		StringParameter param = readWriteReportWithParameter(p3);
		
		Assert.assertEquals(p3.getValue(), param.getValue());
	}
		
	@Test
	public void testReadWrite_entityParam() throws JAXBException, IOException, SAXException{
		EntityParameter p4 = new EntityParameter();
		p4.setValue("123345");
		p4.setValueLabel("value label");
		
		EntityParameter param = readWriteReportWithParameter(p4);
		
		Assert.assertEquals(p4.getValue(), param.getValue());
		Assert.assertEquals(p4.getValueLabel(), param.getValueLabel());
	}
	
	@Test
	public void testReadWrite_boolParam() throws JAXBException, IOException, SAXException{
		BooleanParameter p5 = new BooleanParameter();
		p5.setValue(true);
		
		BooleanParameter param = readWriteReportWithParameter(p5);
		
		Assert.assertEquals(p5.getValue(), param.getValue());
	}

	private <T extends BaseParameter<?>> T readWriteReportWithParameter(T param) throws JAXBException, IOException,
			SAXException {
		Pconfig report = new Pconfig();
		ArrayList<Parameter<?>> params = new ArrayList<Parameter<?>>();
		params.add(param);
		report.setParameters(params);
		writeXml(report, TEST_FILE);
		Pconfig read = (Pconfig) read(report, TEST_FILE);
		@SuppressWarnings("unchecked")
		T readParam = (T) read.getParameters().get(0);
		return readParam;
	}
	
	@Test
	public void testReadWrite_properties() throws JAXBException, IOException, SAXException{
		Pconfig report = new Pconfig();
		
		report.addProperty("test-key1", "test value1");
		report.addProperty("test-key2", "test value2");
		
		writeXml(report, TEST_FILE);
		Pconfig read = (Pconfig) read(report, TEST_FILE);
		
		Assert.assertEquals("test value1", read.getProperty("test-key1"));
		Assert.assertEquals("test value2", read.getProperty("test-key2"));
	}
		
	
	@Test
	public void testReading() throws JAXBException, SAXException, IOException{
		String file = this.getClass().getClassLoader().getResource("testReport.xml").getFile();
		Pconfig report = (Pconfig) read(new Pconfig(), new File(file));
		validateTestPconfig(report);
	}

	private void validateTestPconfig(Pconfig report) {
		Assert.assertEquals("Test report", report.getLabel());
		Assert.assertEquals("path/to/reportfile.jasper", report.getResource());
		
		Assert.assertEquals(6, report.getParameters().size());
		
		Parameter<?> p0 = report.getParameters().get(0);
		Assert.assertTrue(p0 instanceof DateParameter);
		Assert.assertEquals("startDate", p0.getName());
		Assert.assertEquals("Start date", p0.getLabel());
		Assert.assertEquals("date tooltip", p0.getTooltip());
		Assert.assertEquals(false, ((DateParameter)p0).isAllowFuture());
		Assert.assertEquals(true, ((DateParameter)p0).isAllowPast());
		
		Parameter<?> p1 = report.getParameters().get(1);
		Assert.assertTrue(p1 instanceof IntegerParameter);
		Assert.assertEquals("integer-param", p1.getName());
		Assert.assertEquals("Ingeger param", p1.getLabel());
		Assert.assertEquals(10, ((IntegerParameter)p1).getMax().intValue());
		Assert.assertEquals(17, ((IntegerParameter)p1).getDefaultValue().intValue());
		Assert.assertNull(((IntegerParameter)p1).getMin());
		
		Parameter<?> p2 = report.getParameters().get(2);
		Assert.assertTrue(p2 instanceof StringParameter);
		Assert.assertEquals("msisdn", p2.getName());
		Assert.assertEquals("Mobilie number", p2.getLabel());
		Assert.assertEquals("^27[1-9][0-9]{8}$", ((StringParameter)p2).getRegex());
		Assert.assertEquals("Regex error message", ((StringParameter)p2).getErrorMessage());
		Assert.assertEquals("nullValue", ((StringParameter)p2).getDefaultValue());

		Parameter<?> p3 = report.getParameters().get(3);
		Assert.assertTrue(p3 instanceof EntityParameter);
		Assert.assertEquals("entity-param", p3.getName());
		Assert.assertEquals("Entity param", p3.getLabel());
		Assert.assertEquals(Pconfig.class.getName(), ((EntityParameter)p3).getEntityClass());
		Assert.assertEquals("name", ((EntityParameter)p3).getDisplayProperty());
		Assert.assertEquals("name,id", ((EntityParameter)p3).getSearchFields());
		Assert.assertEquals("id", ((EntityParameter)p3).getValueProperty());
		Assert.assertEquals(Integer.class.getSimpleName(), ((EntityParameter)p3).getValueType());
		Assert.assertEquals("%", ((EntityParameter)p3).getDefaultValue());

		Parameter<?> p4 = report.getParameters().get(4);
		Assert.assertTrue(p4 instanceof BooleanParameter);
		Assert.assertEquals("include-test", p4.getName());
		Assert.assertEquals("Include test data", p4.getLabel());

		Parameter<?> p5 = report.getParameters().get(5);
		Assert.assertTrue(p5 instanceof LabelParameter);
		Assert.assertEquals("Note", p5.getLabel());
		Assert.assertEquals("this a a note", p5.getValue());
		
		Assert.assertEquals("test value1", report.getProperty("test-key1"));
		Assert.assertEquals("test value2", report.getProperty("test-key2"));
	}
	
	@Test
	public void testFilledReport() throws Exception {
		String file = this.getClass().getClassLoader().getResource("testReport.xml").getFile();
		Pconfig pconfig = (Pconfig) read(new Pconfig(), new File(file));
		
		FilledPconfig filledPconfig = new FilledPconfig("id-123", "PDF", new Date(), pconfig);
		writeXml(filledPconfig, TEST_FILE);
		FilledPconfig read = (FilledPconfig) read(new FilledPconfig(), TEST_FILE);
		
		validateTestPconfig(read.getPconfig());
		Assert.assertEquals(filledPconfig.getId(), read.getId());
		Assert.assertEquals(filledPconfig.getResourceType(), read.getResourceType());
		Assert.assertEquals(filledPconfig.getResourcePath(), read.getResourcePath());
		Assert.assertEquals(filledPconfig.getDateFilled(), read.getDateFilled());
	}
	
	@Test
	public void testScheduledPconfig() throws Exception {
		String file = this.getClass().getClassLoader().getResource("testReport.xml").getFile();
		Pconfig pconfig = (Pconfig) read(new Pconfig(), new File(file));
		
		ScheduledPconfig sPconfig = new ScheduledPconfig(pconfig);
		sPconfig.setId("id-123");
		sPconfig.setIntervalCount(9);
		sPconfig.setRepeatInterval(RepeatInterval.Monthly);
		sPconfig.setScheduledFor("scheduledForMe");
		sPconfig.setStartDate(new Date());
		sPconfig.setEndDate(new Date());
		
		writeXml(sPconfig, TEST_FILE);
		ScheduledPconfig read = (ScheduledPconfig) read(new ScheduledPconfig(), TEST_FILE);
		
		validateTestPconfig(read.getPconfig());
		Assert.assertEquals(sPconfig.getId(), read.getId());
		Assert.assertEquals(sPconfig.getIntervalCount(), read.getIntervalCount());
		Assert.assertEquals(sPconfig.getRepeatInterval(), read.getRepeatInterval());
		Assert.assertEquals(sPconfig.getEndDate(), read.getEndDate());
		Assert.assertEquals(sPconfig.getStartDate(), read.getStartDate());
	}
	
	protected void writeXml(Object sample, File file) throws JAXBException,
			IOException {
		FileWriter writer = new FileWriter(file);
		try {
			String name = sample.getClass().getPackage().getName();
			JAXBContext jc = JAXBContext.newInstance(name, Thread
					.currentThread().getContextClassLoader());
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, UTF_8);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(sample, writer);
		} finally {
			writer.close();
		}
	}

	public Object read(Object sample, File file) throws JAXBException,
			SAXException, IOException {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				file));
		try {
			String name = sample.getClass().getPackage().getName();
			JAXBContext jc = JAXBContext.newInstance(name, Thread
					.currentThread().getContextClassLoader());

			Unmarshaller unmarshaller = jc.createUnmarshaller();

			Object element = unmarshaller.unmarshal(reader);
			return element;
		} finally {
			reader.close();
		}
	}

}

