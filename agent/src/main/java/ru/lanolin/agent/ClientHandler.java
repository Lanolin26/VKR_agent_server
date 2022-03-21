package ru.lanolin.agent;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.lanolin.common.Command;

import java.io.BufferedReader;
import java.io.PrintWriter;

@AllArgsConstructor
public class ClientHandler implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	private Configuration ident;

	@SneakyThrows
	@Override
	public void run() {
		String commandLine;
		while ((commandLine = in.readLine()) != null) {
			if (commandLine.startsWith(Command.Ping.toString())) {
				out.printf("%s %s\n", Command.Pong, ident);
			}
		}
	}

}
