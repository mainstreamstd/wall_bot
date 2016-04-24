package wallbot.vkapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VKApi {

	public static final String BASE_URL = "https://api.vk.com/method/";

	String token;
	String groupId;
	
	public VKApi(String token, String groupId)
	{
		this.token = token;
		this.groupId = groupId;
	}
	
	
	public ArrayList<WallPost> getPosts(long fromDate) throws IOException
	{
		String jsonString = get(BASE_URL, "wall.get?owner_id=-" + groupId + "&access_token=" + token);
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = (JSONObject) obj;
		JSONArray items = (JSONArray) jsonObj.get("response");
		//JSONArray items = (JSONArray) response.get("items");
		
		ArrayList<WallPost> result = new ArrayList<WallPost>();
		
		JSONObject jItem;
		for (int i=1; i < items.size(); i++)
		{
			
			jItem = (JSONObject) items.get(i);
			
			if((long)jItem.get("date") >= fromDate)
			{
				WallPost post = new WallPost();
				post.text = (String) jItem.get("text");
				post.link =  "vk.com/wall-" + groupId + "_" + (long) jItem.get("id");
				if(jItem.containsKey("attachments"))
				{
					JSONArray attachments = (JSONArray) jItem.get("attachments");
					
					JSONObject jAttachment;
					for(Object attachment : attachments)
					{
						jAttachment = (JSONObject) attachment;
						if(((String) jAttachment.get("type")).equals("photo"))
						{
							post.photos.add((String)jAttachment.get("photo_604"));
						}
					}
				}		
				result.add(post);
			}
		}
		return result;
	}
	
	private String get(String url, String params) throws IOException
	{
		URL obj = new URL(url + params);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream(), "UTF-8"));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		
		return response.toString();		
	}
	
}
