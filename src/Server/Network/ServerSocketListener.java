package Server.Network;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import Tools.IpChecker;

public class ServerSocketListener implements Runnable{
	
	//Exit
	public volatile CountDownLatch returnLatch;
	
	//Loop
	private boolean running;
	
	//Socket
	private DatagramSocket socket;
	private byte[] receiveData;
	
	//Blocking Queue
	private BlockingQueue<SocketData> blockingQueue;

	
	public ServerSocketListener(DatagramSocket socket, BlockingQueue<SocketData> blockingQueue, int packetSize) {
		this.socket = socket;
		this.blockingQueue = blockingQueue;
		returnLatch = new CountDownLatch(1);
		receiveData = new byte[packetSize];
		running = true;
	}
	
	@Override
	public void run() {
		System.out.println("Server's SocketListener Started");
		listen();
		terminate();
	}
	
	private void listen() {
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		while(running) {
			
				//Get Packet
				try {
					socket.receive(receivePacket);
					
					if(!blockingQueue.offer(new SocketData(receivePacket.getAddress(), receivePacket.getPort(), receivePacket.getData()))) {
						System.out.println("ServerSocketListener/Listen(): BlockingQueue Full. Packet Dropped");
					}
				
				} catch (SocketTimeoutException e) {
					//Do Nothing
				} catch (SocketException e) {
					running = false;
				} catch (IOException e) {
					running = false;
				}
			
		}
		
	}
	
	public void stop() {
		running = false;
		if(socket != null) {
			socket.close();
		}
	}
	
	private void terminate() {
		System.out.println("Server's SocketListener Terminated");
		returnLatch.countDown();
	}
}
