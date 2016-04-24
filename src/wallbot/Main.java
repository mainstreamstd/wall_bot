package wallbot;

import java.io.IOException;
import java.util.ArrayList;

import wallbot.telegramapi.BotApi;
import wallbot.vkapi.VKApi;
import wallbot.vkapi.WallPost;

public class Main {


	
	public static void main(String[] args) {	
			Main main = new Main();
			main.start(loadConfig());
	}
	
	static Config loadConfig()
	{
		 Config config = ConfigManager.loadConfig();
			if(config == null) {
				System.out.println("Файл конфигурации отсутствует. Создание нового файла по умолчанию. Отредактируйте config.json и перезапустите.");
				config = new  Config();
				config.enabled = true;
				config.vkApiKey = "1767432a355097cc2d77aa1c33af5d3c5b61c2f249ecc3b2f5685908ff8e2b152d282ef094ac08ebd5f49";
				config.vkGroupId = "112984583";
				config.telegramApiKey = "205801143:AAEFD9GkMTVOO38YFQjJX1a6RxCEyMGNYpw";
				config.channelId = "@test_channel_3";
				config.addFullPost = true;
				config.addLink = true;
				config.refreshInterval = 10;
				ConfigManager.createConfig(config);
				System.exit(0);
			} else return config;
			return null;
	}

	
	void start(Config config)
	{
		
		if(!config.enabled)
		{
			System.out.println("Бот отключен в файле конфигурации. Выход.");
			System.exit(0);
		}
		
		VKApi vk_api = new VKApi(config.vkApiKey, config.vkGroupId);
		BotApi bot_api = new BotApi(config.telegramApiKey);
		
		try {
			System.out.println(loadConfig().lastTime);
			ArrayList<WallPost> posts = vk_api.getPosts(loadConfig().lastTime);
			ConfigManager.writeTime();
			
			
			for(WallPost post : posts)
			{
				bot_api.sendMessage(config.channelId, post.link + "\n" + post.text);
			}
			
			System.out.println("done");
		} catch (IOException e1) {
			e1.printStackTrace();
		}			
			try {
				Thread.sleep(config.refreshInterval*1000);
				vk_api = null;
				bot_api = null;
				start(ConfigManager.loadConfig());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
}

