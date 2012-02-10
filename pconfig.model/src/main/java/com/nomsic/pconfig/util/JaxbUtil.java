/*
 * This file is part of Randb.
 *
 * Randb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Randb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Randb.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.nomsic.pconfig.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

public class JaxbUtil {

	private JAXBContext context;
	
	public JaxbUtil(Class<?> example) throws JAXBException {
		String name = example.getPackage().getName();
		context = JAXBContext.newInstance(name, Thread
				.currentThread().getContextClassLoader());
	}
	
	public JAXBContext getContext() {
		return context;
	}
	
	public Object read(InputStream inputStream) throws SerializationException{
		InputStreamReader reader = null;
		try{
			reader = new InputStreamReader(inputStream);
			Unmarshaller unmarshaller = getContext().createUnmarshaller();
	
			Object element = unmarshaller.unmarshal(reader);
			return element;
		} catch (JAXBException e) {
			throw new SerializationException("Error parsing file",e);
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException ignore) {}
			}
		}
	}
	
	public void write(Object object, OutputStream out) throws SerializationException{
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(out);
			Marshaller m = getContext().createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(object, writer);
		} catch (PropertyException e) {
			throw new SerializationException("Error parsing file",e);
		} catch (JAXBException e) {
			throw new SerializationException("Error parsing file",e);
		} finally {
			if (writer != null){
				try {
					writer.close();
				} catch (IOException ignore) {}
			}
		}
	}
}
