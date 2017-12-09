package com.durafficpark;

import com.durafficpark.osm.OSMNode;
import com.durafficpark.osm.OSMObject;
import com.durafficpark.osm.OSMWay;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OSMFileParser extends DefaultHandler {

    private OSMObject currentObject;

    private File file;

    private BufferedWriter bufferedWriter;

    public OSMFileParser(File file){
        System.out.printf("[!] Parsing OSM file; %s ", file.toString());
        this.file = file;

        System.out.println("Starting parsing document");

        try {
            bufferedWriter = new BufferedWriter(new FileWriter("osm_json.txt", true));

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.parse(file.getAbsolutePath());

            bufferedWriter.close();
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
        catch (ParserConfigurationException e){
            System.err.println(e.getMessage());
        }
        catch (SAXException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(qName.equals("tag") && currentObject != null){
            currentObject.getTags().put(attributes.getValue("k"), attributes.getValue("v"));
        }
        else if(qName.equalsIgnoreCase("nd") && (currentObject instanceof OSMWay)) {
            ((OSMWay) currentObject).getNodes().add(attributes.getValue("ref"));
        }
        else {
            if (currentObject != null) {
                saveObject();
            }
            if(qName.equalsIgnoreCase("node")) {
                currentObject = new OSMNode(attributes.getValue("id"),
                        Double.parseDouble(attributes.getValue("lat")),
                        Double.parseDouble(attributes.getValue("lon")));
            }
            else if(qName.equalsIgnoreCase("way")){
                currentObject = new OSMWay(attributes.getValue("id"));
            }
        }
    }

    public void saveObject(){
        try {
            bufferedWriter.write(currentObject.getJSON().toJSONString()+"\n");
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
        finally {
            currentObject = null;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {

        try {
            bufferedWriter.close();
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public static void main(String args[]){
        OSMFileParser parser = new OSMFileParser(new File("/Users/georgeprice/Documents/GitHub/durafficpark/src/main/java/parsing/north-yorkshire-latest.osm"));

        try {
            parser.startDocument();
        }
        catch (Exception e){

        }

    }
}
