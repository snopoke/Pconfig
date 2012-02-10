package com.nomsic.pconfig.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nomsic.pconfig.model.AdaptedMap;

public class MapAdapter extends XmlAdapter<AdaptedMap, Map<String, String>> {

    @Override
    public AdaptedMap marshal(Map<String, String> map) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element rootElement = document.createElement("map");
        document.appendChild(rootElement);

        if (map != null){
	        for(Entry<String,String> entry : map.entrySet()) {
	            Element mapElement = document.createElement(entry.getKey());
	            mapElement.setTextContent(entry.getValue());
	            rootElement.appendChild(mapElement);
	        }
        }

        AdaptedMap adaptedMap = new AdaptedMap();
        adaptedMap.setValue(rootElement);
        return adaptedMap;
    }

    @Override
    public Map<String, String> unmarshal(AdaptedMap adaptedMap) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Element rootElement = (Element) adaptedMap.getValue();
        NodeList childNodes = rootElement.getChildNodes();
        for(int x=0,size=childNodes.getLength(); x<size; x++) {
            Node childNode = childNodes.item(x);
            if(childNode.getNodeType() == Node.ELEMENT_NODE) {
                map.put(childNode.getLocalName(), childNode.getTextContent());
            }
        }
        return map;
    }

}