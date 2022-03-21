package ru.lanolin.agent;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		Configuration p = Configuration.loadProperties();
		Client client = new Client();
		client.connect(p);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
	}

}
