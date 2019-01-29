package core.message.define;

public class CodeMessageBase extends MessageBase
{
	public int code;
	public String e="";

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}
}
