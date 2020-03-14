package test_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CoVid19API {
	private JSONObject httpRequest(String req) throws IOException, ParseException{
		//URL url = new URL("https://covid2019-api.herokuapp.com/country/italy");
		URL url = new URL(req);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();
		//System.out.println(content);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(content.toString());
		JSONObject jo = (JSONObject) obj; 
		return jo;
		//long confirmed = (long) jo.get("confirmed");
		//System.out.println(confirmed);
	}
	
	public String getCurrentDate() throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/total");
		return (String) jo.get("dt");
	}
	
	public long getTotalNumberOfConfirmed() throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/confirmed");
		return (long) jo.get("confirmed");
	}
	
	public long getTotalNumberOfDeaths() throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/deaths");
		return (long) jo.get("deaths");
	}
	
	public long getTotalNumberOfRecoveries() throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/recovered");
		return (long) jo.get("recovered");
	}
	
	public long getNumberOfAffectedCountries() throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/countries");
		JSONArray ja = (JSONArray) jo.get("countries"); 
        Iterator itr = ja.iterator();    
        long counter = 0;
        
        while (itr.hasNext())  
        { 
            counter++;
            itr.next();
        } 
        return counter;
	}
	
	public List<String> getCountries() throws IOException, ParseException{
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/countries");
		JSONArray ja = (JSONArray) jo.get("countries"); 
        Iterator itr = ja.iterator();    
        List<String> countries = new ArrayList<String>();
        
        while (itr.hasNext())  
        { 
        	countries.add((String)itr.next());
        } 
        return countries;
	}
	
	public long getConfirmedForCountry(String country) throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/country/" + country);
		Map cData = ((Map)jo.get(country)); 
        return (long) cData.get("confirmed");
	}
	
	public long getDeathsForCountry(String country) throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/country/" + country);
		Map cData = ((Map)jo.get(country)); 
        return (long) cData.get("deaths");
	}
	
	public long getRecoveriesForCountry(String country) throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/country/" + country);
		Map cData = ((Map)jo.get(country)); 
        return (long) cData.get("recovered");
	}
	
	public double getLatitudeForCountry(String country, String state) throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/timeseries/confirmed");
		JSONArray ja = (JSONArray) jo.get("confirmed"); 
        Iterator itr = ja.iterator(); 
        while (itr.hasNext())  
        { 
        	Map timeseries = (Map) itr.next();
        	if(country.equals((String) timeseries.get("Country/Region")) && state.equals((String) timeseries.get("Province/State"))){
        		return Double.parseDouble((String) timeseries.get("Lat"));
        	}
        } 
        return 0.0;
	}
	
	public double getLongitudeForCountry(String country, String state) throws IOException, ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/timeseries/confirmed");
		JSONArray ja = (JSONArray) jo.get("confirmed"); 
        Iterator itr = ja.iterator(); 
        while (itr.hasNext())  
        { 
        	Map timeseries = (Map) itr.next();
        	if(country.equals((String) timeseries.get("Country/Region")) && state.equals((String) timeseries.get("Province/State"))){
        		return Double.parseDouble((String) timeseries.get("Long"));
        	}
        } 
        return 0.0;
	}
	
	public Map getTimeseriesConfirmedForCountry(String country, String state) throws IOException, ParseException, java.text.ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/timeseries/confirmed");
		JSONArray ja = (JSONArray) jo.get("confirmed"); 
        Iterator itr = ja.iterator(); 
        while (itr.hasNext())  
        { 
        	Map timeseries = (Map) itr.next();
        	if(country.equals((String) timeseries.get("Country/Region")) && state.equals((String) timeseries.get("Province/State"))){
        		timeseries.remove("Lat");
        		timeseries.remove("Long");
        		timeseries.remove("Province/State");
        		timeseries.remove("Country/Region");
        		Map result = new HashMap();
        		Iterator<Map.Entry> itr1 = timeseries.entrySet().iterator(); 
                while (itr1.hasNext()) { 
                    Map.Entry pair = itr1.next(); 
                    result.put(new SimpleDateFormat("MM/dd/yyyy").parse((String) pair.getKey()), pair.getValue());
                } 
        		Map treeMap = new TreeMap(result);
        		return treeMap;
        		//return timeseries;
        	}
        } 
        return null;
	}
	
	public Map getTimeseriesDeathsForCountry(String country, String state) throws IOException, ParseException, java.text.ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/timeseries/deaths");
		JSONArray ja = (JSONArray) jo.get("deaths"); 
        Iterator itr = ja.iterator(); 
        while (itr.hasNext())  
        { 
        	Map timeseries = (Map) itr.next();
        	if(country.equals((String) timeseries.get("Country/Region")) && state.equals((String) timeseries.get("Province/State"))){
        		timeseries.remove("Lat");
        		timeseries.remove("Long");
        		timeseries.remove("Province/State");
        		timeseries.remove("Country/Region");
        		Map result = new HashMap();
        		Iterator<Map.Entry> itr1 = timeseries.entrySet().iterator(); 
                while (itr1.hasNext()) { 
                    Map.Entry pair = itr1.next(); 
                    result.put(new SimpleDateFormat("MM/dd/yyyy").parse((String) pair.getKey()), pair.getValue());
                } 
        		Map treeMap = new TreeMap(result);
        		return treeMap;
        		//return timeseries;
        	}
        } 
        return null;
	}
	
	public Map getTimeseriesRecoveredForCountry(String country, String state) throws IOException, ParseException, java.text.ParseException {
		JSONObject jo = httpRequest("https://covid2019-api.herokuapp.com/timeseries/recovered");
		JSONArray ja = (JSONArray) jo.get("recovered");  
        Iterator itr = ja.iterator(); 
        while (itr.hasNext())  
        { 
        	Map timeseries = (Map) itr.next();
        	if(country.equals((String) timeseries.get("Country/Region")) && state.equals((String) timeseries.get("Province/State"))){
        		timeseries.remove("Lat");
        		timeseries.remove("Long");
        		timeseries.remove("Province/State");
        		timeseries.remove("Country/Region");
        		Map result = new HashMap();
        		Iterator<Map.Entry> itr1 = timeseries.entrySet().iterator(); 
                while (itr1.hasNext()) { 
                    Map.Entry pair = itr1.next(); 
                    result.put(new SimpleDateFormat("MM/dd/yyyy").parse((String) pair.getKey()), pair.getValue());
                } 
        		Map treeMap = new TreeMap(result);
        		return treeMap;
        	}
        } 
        return null;
	}
	
	//TODO: write function that gives a list with all countries and all provinces
}
