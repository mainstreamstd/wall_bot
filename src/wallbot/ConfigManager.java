package wallbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigManager {

	
	public static String CONFIG_PATH = "./config.json";
	public static String TIME_PATH = "./.last_time";
	
	public static Config loadConfig()
	{
		File configFile = new File(CONFIG_PATH);
			if(!configFile.exists()) return null;
			
		File timeFile = new File(TIME_PATH);
			if(!timeFile.exists())
			{
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(
			              new FileOutputStream(TIME_PATH), "utf-8"))) {
			   writer.write(String.valueOf(System.currentTimeMillis()/1000));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			StringBuilder sb = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_PATH)))
			{
				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					sb.append(sCurrentLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			
			

			
		Config config = new Config();		
		JSONParser parser = new JSONParser();

		String json = sb.toString();
		
		Object obj = null;
		try {
			obj = parser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = (JSONObject) obj;
		
		config.enabled = (boolean) jsonObj.get("bot_enabled");
		config.vkApiKey = (String) jsonObj.get("vk_access_token");
		config.telegramApiKey = (String) jsonObj.get("telegram_key");
		config.vkGroupId = (String) jsonObj.get("vk_group_id");
		config.addLink =  (boolean) jsonObj.get("bot_add_link");
		config.addFullPost = (boolean) jsonObj.get("bot_add_full_post");
		config.refreshInterval = (long) jsonObj.get("refresh_interval");
		config.channelId = (String) jsonObj.get("telegram_channel_id");
		
		sb = new StringBuilder();
		
		try (BufferedReader br = new BufferedReader(new FileReader(TIME_PATH)))
		{
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		config.lastTime = Long.parseLong(sb.toString());
		
		return config;
		
	}
	
	public static void writeTime()
	{

			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(TIME_PATH), "utf-8"))) {
		   writer.write(String.valueOf(System.currentTimeMillis()/1000));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void createConfig(Config config)
	{
		JSONObject resultJson = new JSONObject();

		resultJson.put("vk_access_token",config.vkApiKey);
		resultJson.put("telegram_key",config.telegramApiKey);
		resultJson.put("vk_group_id",config.vkGroupId);
		resultJson.put("bot_add_link", config.addLink);
		resultJson.put("bot_add_full_post", config.addFullPost);
		resultJson.put("bot_enabled", config.enabled);
		resultJson.put("refresh_interval", config.refreshInterval);
		resultJson.put("telegram_channel_id", config.channelId);
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(CONFIG_PATH), "utf-8"))) {
	   writer.write(resultJson.toJSONString());
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
}
