package com.lynx.lib.location.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lynx.lib.location.entity.Coord;

public class AMapLocateResponse {
	
	public String ParseSapsXML(String input) {
		InputStream instream = new ByteArrayInputStream(input.getBytes());
		SAXParserFactory sax_factory = SAXParserFactory.newInstance();
		LTContentHandler handler = new LTContentHandler(this);
		try {
			SAXParser parser = sax_factory.newSAXParser();
			parser.parse(instream, handler);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return handler.getContent();
	}
	
	public Coord parseCoordFromXML(String input) {
		InputStream instream = new ByteArrayInputStream(input.getBytes());
		SAXParserFactory sax_factory = SAXParserFactory.newInstance();
		LTCoordHandler handler = new LTCoordHandler(this);
		try {
			SAXParser parser = sax_factory.newSAXParser();
			parser.parse(instream, handler);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return handler.getCoord();
	}
	
	private class LTContentHandler extends DefaultHandler {
		public String content = "";
		private boolean is_end = false;
		
		public LTContentHandler(AMapLocateResponse resp) {
			
		}
		
		@Override
		public final void characters(char[] chars, int start, int length) 
				throws SAXException{
			if (is_end) {
				content = new String(chars, start, length);
			}
			super.characters(chars, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("sres".equals(localName)) {
				this.is_end = false;
			}
			super.endElement(uri, localName, qName);
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("sres".equals(localName)) {
				this.is_end = true;
			}
			super.startElement(uri, localName, qName, attributes);
		}
		
		public String getContent() {
			return this.content;
		}
	}

	private class LTCoordHandler extends DefaultHandler {
		private Coord coord;
		private StringBuffer str_buf;
		
		public LTCoordHandler(AMapLocateResponse resp) {
			coord = new Coord();
			str_buf = new StringBuffer();
		}
		
		@Override
		public final void characters(char[] chars, int start, int length) 
				throws SAXException{
			str_buf.append(new String(chars, start, length));
			super.characters(chars, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("result".equals(localName)) {
				// do nothing now
			}
			else if ("rdesc".equals(localName)) {
				
			}
			else if ("cenx".equals(localName)) {
				if (coord != null) {
					try {
						double lng = Double.parseDouble(str_buf.toString());
						coord.setLng(LocationUtil.format(lng, 5));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else if ("ceny".equals(localName)) {
				if (coord != null) {
					try {
						double lat = Double.parseDouble(str_buf.toString());
						coord.setLng(LocationUtil.format(lat, 5));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else if ("radius".equals(localName)) {
				if (coord != null) {
					try {
						int acc = (int)Double.parseDouble(str_buf.toString());
						coord.setAcc(acc);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else if ("citycode".equals(localName)) {
				// do nothing now
			}
			else if ("desc".equals(localName)) {
				// do nothing now
			}
			else if ("adcode".equals(localName)) {
				// do nothing now
			}
			super.endElement(uri, localName, qName);
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			str_buf.delete(0, str_buf.toString().length());
			super.startElement(uri, localName, qName, attributes);
		}
		
		public Coord getCoord() {
			if (coord.getLat() != 0.0 && coord.getLng() != 0.0) {
				return this.coord;
			}
			else {
				return null;
			}
		}
	}
}
