package board.controller;

import java.sql.Connection;
import java.util.Map;
import java.util.Scanner;

import board.Member;
import board.session.Session;
import board.util.DBUtil;
import board.util.SecSql;

public class MemberController {
	Connection conn;
	Scanner input;
	String command;
	Session session;

	public MemberController(Connection conn, Scanner input, String command, Session session) {
		this.conn = conn;
		this.input = input;
		this.command = command;
		this.session = session;
	}

	public void doLogin() {

		System.out.println("== 회원 로그인 ==");

		String loginId;
		String loginPw;
		SecSql sql;

		// 로그인 입력횟수 제한
		int blockCnt = 0;

		while (true) {

			sql = new SecSql();

			if (blockCnt >= 3) {
				System.out.println("입력횟수 초과! 다시 시도해주세요.");
				return;
			}

			System.out.print("로그인 아이디 : ");
			loginId = input.nextLine();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				blockCnt++;
				continue;
			}

			sql.append("SELECT COUNT(*) FROM `member`");
			sql.append("WHERE loginId = ?", loginId);

			int memberCnt = DBUtil.selectRowIntValue(conn, sql);

			if (memberCnt == 0) {
				System.out.println("아이디가 존재하지 않습니다.");
				blockCnt++;
				continue;
			}
			break;

		}

		blockCnt = 0;
		Member member;

		while (true) {

			if (blockCnt >= 3) {
				System.out.println("입력횟수 초과! 다시 시도해주세요.");
				return;
			}

			System.out.print("로그인 비밀번호 : ");
			loginPw = input.nextLine();

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				blockCnt++;
				continue;
			}

			// 등록된 회원의 비밀번호와 입력한 비밀번호가 일치하는지 확인해야함
			sql = new SecSql();
			sql.append("SELECT * FROM `member`");
			sql.append("WHERE loginId = ?", loginId);

			Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);
			member = new Member(memberMap);

			if (!member.loginPw.equals(loginPw)) {
				System.out.println("비밀번호가 일치하지 않습니다.");
				blockCnt++;
				continue;
			}
			break;

		}

		System.out.printf("%s님 환영합니다.\n", member.name);

		session.loginedMemberId = member.id;
		session.loginMember = member;

	}

	public void doJoin() {

		System.out.println("== 회원가입 ==");

		String loginId;
		String loginPw;
		String loginPwConfirm;
		String name;

		SecSql sql;

		// 입력횟수 제한, 3회 틀릴시 다시 명령어 입력받기
		int blockCnt = 0;

		while (true) {

			sql = new SecSql();

			if (blockCnt >= 3) {
				System.out.println("입력횟수 초과! 다시 시도해주세요.");
				return;
			}

			System.out.print("로그인 아이디 : ");
			loginId = input.nextLine();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				blockCnt++;
				continue;
			}

			// DB에 등록된 아이디를 조회, 아이디 중복방지
			sql.append("SELECT COUNT(*) FROM `member`");
			sql.append("WHERE loginId = ?", loginId);

			int memberCnt = DBUtil.selectRowIntValue(conn, sql);

			if (memberCnt > 0) {
				System.out.println("이미 존재하는 아이디입니다.");
				blockCnt++;
				continue;
			}

			break;
		}

		blockCnt = 0;
		// 비밀번호 입력
		while (true) {

			if (blockCnt >= 3) {
				System.out.println("입력횟수 초과! 다시 시도해주세요.");
				return;
			}

			System.out.print("로그인 비밀번호 : ");
			loginPw = input.nextLine();

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				blockCnt++;
				continue;
			}

			while (true) {
				System.out.print("로그인 비밀번호 확인 : ");
				loginPwConfirm = input.nextLine();

				if (loginPwConfirm.length() == 0) {
					System.out.println("비밀번호를 확인해주세요.");
					continue;
				}

				break;
			}

			if (!loginPw.equals(loginPwConfirm)) {
				System.out.println("입력하신 비밀번호와 일치하지 않습니다.");
				blockCnt++;
				continue;
			}

			break;
		}

		while (true) {
			System.out.print("이름 : ");
			name = input.nextLine();

			if (name.length() == 0) {
				System.out.println("이름을 입력해주세요.");
				continue;
			}

			break;

		}

		sql = new SecSql();

		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", name = ?", name);

		DBUtil.insert(conn, sql);

		System.out.printf("%s님 환영합니다.\n", name);

	}

	public void doLogout() {

		if (session.loginMember == null) {
			System.out.println("현재 로그아웃 상태입니다.");
			return;
		}

		System.out.printf("%s님 로그아웃되었습니다.\n", session.loginMember.name);
		session.loginMember = null;
		session.loginedMemberId = -1;

	}

	public void showWhoami() {

		if (session.loginMember == null) {
			System.out.println("현재 로그아웃 상태입니다.");
			return;
		}

		System.out.println("== 회원로그인 정보 ==");
		System.out.printf(" 아이디: %s\n", session.loginMember.loginId);
		System.out.printf(" 이 름 : %s\n", session.loginMember.name);

	}

}
