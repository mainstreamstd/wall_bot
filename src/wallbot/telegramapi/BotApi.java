package wallbot.telegramapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BotApi {
	
	
	private static final String BASE_URL = "https://api.telegram.org/bot";
	
	private String token;
	
	public BotApi(String token)
	{
		this.token = token;
	}
	
	public  void testRequest()
	{
		String request = BASE_URL + token + "/getMe";
		try {
			System.out.println(get(request));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getUpdates()
	{
		String request = BASE_URL + token + "/getUpdates";
		try {
			System.out.println(get(request));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void sendMessage(String chatId, String text)
	{
		String request = BASE_URL + token + "/sendMessage?chat_id="+URLEncoder.encode(chatId)+"&text="+URLEncoder.encode(text);
		try {
			System.out.println(get(request));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String get(String url) throws IOException
	{
		URL obj = new URL(url);
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
