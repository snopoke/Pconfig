package com.nomsic.pconfig.jasperreport;

import java.io.File;
import java.util.Collection;


import com.nomsic.pconfig.model.Pconfig;
import com.nomsic.pconfig.util.JaxbUtil;

public interface ReportLoader {

	File getReportFile(Pconfig report, String sourceFolder);

	<T> Collection<T> loadReports(JaxbUtil jaxbUtil, String location, String suffix);

}
