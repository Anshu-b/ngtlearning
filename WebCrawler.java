//package ngtlearning;

import java.io.*; 
import org.jsoup.Jsoup; 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements; 

public class WebCrawler {
	
	final String dataFile = "data.txt"; 
	File file;
	String baseURL = "www.dvhigh.net"; 
	//String baseURL = "www.ngtlearning.com";
	final String site = "http://" + baseURL + "/"; 
	
	public static void main (String[] args) throws IOException {
		WebCrawler myCrawler = new WebCrawler(); 
		try { 
			myCrawler.file = new File (myCrawler.dataFile);
			myCrawler.file.delete(); 
			myCrawler.file.createNewFile(); 
			
		}
		
		catch (FileNotFoundException fnfe) {
			myCrawler.file.createNewFile();
		}
		myCrawler.processPage(myCrawler.site);
	}
		
		public void processPage (String URL) throws IOException { 
			//Filler out invalid links...
			if (URL.contains(".pdf") || URL.contains("@") 
			|| URL.contains(":80") || URL.contains(".jpg")
			|| URL.contains("&return_url") || URL.contains("?")
			|| URL.contains("doc") || URL.contains("xls") 
			|| URL.contains("ppt") || URL.contains("gif"))
			return; 
			
			//Only process if it a internal link
			if (URL.contains(baseURL) && URL.endsWith("/")) { 
				URL = URL.substring(0, URL.length()-1); }
			else if (!URL.contains(baseURL)) { 
				return; }
			
			boolean urlExists = writeToFile(URL.trim());
			
			if (!urlExists) { 
				Document doc = null; 
				try { 
					doc = Jsoup.connect(URL).userAgent("Mozilla").get(); 
				}
				catch (IOException e1) { 
					e1.printStackTrace(); 
					return;
				}
				if (doc.text().contains("PE")) { 
					System.out.println("Found keyword PE in " + URL); 
				}
				Elements questions = doc.select("a[href]"); 
				for (Element link: questions) { 
					String followURL = link.attr("abs:href"); 
					processPage(followURL);
					
				}	
				
			}
					
		}
		
		public boolean writeToFile(String URL) throws IOException { 
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			String line; 
			
			while ((line = reader.readLine()) != null) { 
				if (line.trim().contains(URL)) { 
					reader.close(); 
					return true;
				}
			}
			reader.close(); 
			
			//If URL not found then add it to the file
			FileWriter fileWriter = new FileWriter(file, true); 
			BufferedWriter writer = new BufferedWriter(fileWriter); 
			System.out.println("---->> Writing the URL to the file " + URL);
			writer.append(URL); 
			writer.newLine(); 
			writer.close();
			return false; 
			
		}
		
	}
	
	


