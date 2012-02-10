package com.nomsic.pconfig.jasperreport.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.nomsic.pconfig.jasperreport.ReportLoader;
import com.nomsic.pconfig.model.Pconfig;
import com.nomsic.pconfig.util.JaxbUtil;
import com.nomsic.pconfig.util.SerializationException;

public class SpringResourceLoader implements ReportLoader, ResourceLoaderAware {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	private ResourceLoader resourceLoader;
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	@Override
	public File getReportFile(Pconfig report, String location){
		String path = report.getResource();
		Resource resource = resourceLoader.getResource(location + File.separator + path);
		try {
			File file = resource.getFile();
			return file;
		} catch (IOException e) {
			return null;
		}
		
	}
	
	@Override
	public <T> Collection<T> loadReports(final JaxbUtil jaxbUtil, String location, String suffix) {
		if (location.startsWith("/") || location.charAt(1) == ':'){
			location = "file:" + location;
		}
		
		Resource resource = resourceLoader.getResource(location);
		List<T> reports = new ArrayList<T>();
		try {
			if (!resource.exists()){
				log.warn("Source directory does not exist: {}", location);
				return reports;
			}
			File sourceFolder = resource.getFile();
			if (!sourceFolder.isDirectory()) {
				log.warn("Source directory is not a directory: {}", sourceFolder.getAbsolutePath());
				return reports;
			}

			if (suffix == null){
				suffix = ".xml";
			} else {
				suffix += ".xml";
			}
			
			File[] files = sourceFolder.listFiles((FileFilter) new AndFileFilter(
					new SuffixFileFilter(suffix), CanReadFileFilter.CAN_READ));
			if (files == null) {
				log.warn("No report files found in source directory: {}", sourceFolder);
			} else {
				for (File file : files) {
					try {
						@SuppressWarnings("unchecked")
						T report = (T) jaxbUtil.read(new FileInputStream(file));
						reports.add(report);
					} catch (SerializationException e) {
						log.warn("Error reading report file: {}"
								+ file.getAbsolutePath());
					}
				}
			}
		} catch (IOException e) {
			log.error("Error loading reports.",e);
		}
		return reports;
	}
	
}
