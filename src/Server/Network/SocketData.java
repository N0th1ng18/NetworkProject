package Server.Network;
import java.net.InetAddress;
import java.util.Arrays;

public class SocketData {
	
	public InetAddress address;
	public int port;
	public byte[] data;
	
	public SocketData(InetAddress address, int port, byte[] data) {
		this.address = address;
		this.port = port;
		this.data = Arrays.copyOf(data, data.length);
	}
}
