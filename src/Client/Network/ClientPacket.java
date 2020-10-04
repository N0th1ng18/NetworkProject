package Client.Network;

public class ClientPacket {
	
	private byte msgType;
	private int clientID;
	private int tick;
	private int hasChanged;
	//name length
	private String name;
	private int color;
	private int w;
	private int a;
	private int s;
	private int d;
	
	//password length
	private String password;
	
	public ClientPacket() {
		msgType = (byte) -1;
		clientID = -1;
		tick = -1;
		name = "";
		color = -1;
		w = -1;
		a = -1;
		s = -1;
		d = -1;
	}

	
	
	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	
	public void setTick(int tick) {//Tick
		this.tick = tick;
	}

	public void resetHasChanged() {
		this.hasChanged = 0;
	}

	//hasChanged
	public void setName(String name) {
		if(this.name.compareTo(name) != 0) {
			hasChanged = setBit(hasChanged, 0);//nameLength
			hasChanged = setBit(hasChanged, 1);//name
			this.name = name;
		}
	}

	public void setColor(int color) {
		if(this.color != color) {
			hasChanged = setBit(hasChanged, 2);//Color
			this.color = color;
		}
	
	}

	public void setW(int w) {
		if(this.w != w) {
			hasChanged = setBit(hasChanged, 3);//W
			this.w = w;
		}
	}

	public void setA(int a) {
		if(this.a != a) {
			hasChanged = setBit(hasChanged, 4);//A
			this.a = a;
		}
	}

	public void setS(int s) {
		if(this.s != s) {
			hasChanged = setBit(hasChanged, 5);//S
			this.s = s;
		}
	}

	public void setD(int d) {
		if(this.d != d) {
			hasChanged = setBit(hasChanged, 6);//D
			this.d = d;
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public byte getMsgType() {
		return msgType;
	}


	public int getClientID() {
		return clientID;
	}


	public int getHasChanged() {
		return hasChanged;
	}


	public String getName() {
		return name;
	}


	public int getColor() {
		return color;
	}


	public int getTick() {
		return tick;
	}


	public int getW() {
		return w;
	}


	public int getA() {
		return a;
	}


	public int getS() {
		return s;
	}


	public int getD() {
		return d;
	}


	public String getPassword() {
		return password;
	}


	private int setBit(int hasChanged, int bitPos) {
		return (hasChanged |= 1 << bitPos);
	}
	
}
