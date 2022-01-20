package board.session;

import board.dto.Member;

public class Session {

	private int loginedMemberId;
	private Member loginMember;

	public Session() {
		loginedMemberId = -1;
	}

	public int getLoginedMemberId() {
		return loginedMemberId;
	}

	public void setLoginedMemberId(int loginedMemberId) {
		this.loginedMemberId = loginedMemberId;
	}

	public Member getLoginMember() {
		return loginMember;
	}

	public void setLoginMember(Member loginMember) {
		this.loginMember = loginMember;
	}
}
