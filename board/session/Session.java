package board.session;

import board.dto.Member;

public class Session {

	private int loginedMemberId;
	private Member loginedMember;

	public Session() {
		loginedMemberId = -1;
		loginedMember = null;
	}

	public int getLoginedMemberId() {
		return loginedMemberId;
	}

	public void setLoginedMemberId(int loginedMemberId) {
		this.loginedMemberId = loginedMemberId;
	}

	public Member getLoginedMember() {
		return loginedMember;
	}

	public void setLoginedMember(Member loginedMember) {
		this.loginedMember = loginedMember;
	}
}
