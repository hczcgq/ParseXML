package com.chen.parsexml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.os.Bundle;
import android.app.Activity;
import android.util.Xml;
import android.view.Menu;

public class MainActivity extends Activity {

	List<News> news;
	
	private String ns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String xml = glob.str;
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(xml.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		news = ParseXML(in);
		for (News entry : news) {
			System.out.println(entry.getTitle());
			System.out.println(entry.getAuthor());
			System.out.println(entry.getCommentCount());
			System.out.println(entry.getPubDate());
		}

	}

	private List<News> ParseXML(InputStream in) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readOschina(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	private List<News> readOschina(XmlPullParser parser) {
		

		try {
			parser.require(XmlPullParser.START_TAG, ns, "oschina");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();
//				System.out.println(name);
				if (name.equals("newslist")) {
					return readNewslist(parser);
				} else {
					 skip(parser);
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<News> readNewslist(XmlPullParser parser) {
		 List<News> news = new ArrayList<News>();
		try {
			parser.require(XmlPullParser.START_TAG, ns, "newslist");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();
				if (name.equals("news")) {
					news.add(readNews(parser));
				} else {
					 skip(parser);
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return news;

	}

	
	

	private News readNews(XmlPullParser parser) {
		 String title = null;
		 int commentCount = 0;
		 String author = null;
		 String pubDate = null;
		 News newss=new News();
		try {
			parser.require(XmlPullParser.START_TAG, ns, "news");
			
	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
//	            System.out.println(name);
	            if (name.equals("title")) {
	                title = readTitle(parser);
	                newss.setTitle(title);
	            } else if (name.equals("commentCount")) {
	            	commentCount = readCommentCount(parser);
	            	newss.setCommentCount(commentCount);
	            } else if (name.equals("author")) {
	            	author = readAuthor(parser);
	            	newss.setAuthor(author);
	            } else if(name.equals("pubDate")) {
					pubDate=readPubDate(parser);
					newss.setPubDate(pubDate);
				}else {
	                skip(parser);
	            }
	            
	            
	        }
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return newss;
	}

	private String readPubDate(XmlPullParser parser) {
		try {
			parser.require(XmlPullParser.START_TAG, ns, "pubDate");
			String pubDate = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
	        return pubDate;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
        
        
	}

	private String readAuthor(XmlPullParser parser) {
		try {
			parser.require(XmlPullParser.START_TAG, ns, "author");
			String author = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, "author");
	        return author;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
        
       
	}

	private int readCommentCount(XmlPullParser parser) {
		try {
			parser.require(XmlPullParser.START_TAG, ns, "commentCount");
			int commentCount = Integer.parseInt(readText(parser));
	        parser.require(XmlPullParser.END_TAG, ns, "commentCount");
	        return commentCount;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
        
        
	}

	private String readTitle(XmlPullParser parser) {
		try {
			parser.require(XmlPullParser.START_TAG, ns, "title");
			String title = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, "title");
	        return title;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
       
	}

	
	 private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	        String result = "";
	        if (parser.next() == XmlPullParser.TEXT) {
	            result = parser.getText();
	            parser.nextTag();
	        }
	        return result;
	    }
	 
	 
	 private void skip(XmlPullParser parser) {
		 try {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
			        throw new IllegalStateException();
			    }
			int depth = 1;
	        while (depth != 0) {
	            switch (parser.next()) {
	            case XmlPullParser.END_TAG:
	                    depth--;
	                    break;
	            case XmlPullParser.START_TAG:
	                    depth++;
	                    break;
	            }
	        }
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	        
		}
}
