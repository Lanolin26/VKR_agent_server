package ru.lanolin.agent;

import ru.lanolin.common.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnexpectedException;
import java.util.Objects;

public class Client implements AutoCloseable {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private Thread handler;

	public void connect(Configuration conf) throws IOException {
		int portNumber = Integer.parseInt(conf.getServerPort());
		this.clientSocket = new Socket(conf.getServerAddress(), portNumber);
		this.out = new PrintWriter(clientSocket.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

//		if (Objects.isNull(conf.getIdentificator()) || conf.getIdentificator().isEmpty()) {
//			String msg = Command.CreateIdent.toString();
//			out.println(msg);
//			String answ = in.readLine();
//			if(answ.startsWith(Command.AnsCreateIdent.toString())) {
//				String ident = answ.split("\\s+", 2)[1];
//				if(Objects.nonNull(ident) && !ident.isBlank() && !ident.isEmpty()){
//					conf.setIdentificator(ident);
//				}
//			}else{
//				throw new UnexpectedException("Not created Identification");
//			}
//		}

		ClientHandler clientHandler = new ClientHandler(in, out, conf);
		handler = new Thread(clientHandler);
		handler.start();
	}

	@Override
	public void close() throws Exception {
		if(handler != null && handler.isAlive()) handler.interrupt();
		if(in != null) in.close();
		if(out != null) out.close();
		if (clientSocket.isConnected()) { clientSocket.close(); }
	}
}
