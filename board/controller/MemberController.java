package board.controller;

import java.sql.Connection;
import java.util.Scanner;

import board.dto.Member;
import board.service.MemberService;
import board.session.Session;

public class MemberController {

	Scanner input;
	String command;
	Session session;

	MemberService memberService;

	public MemberController(Connection conn, Scanner input, String command, Session session) {
		this.input = input;
		this.command = command;
		this.session = session;

		// scanner, command,session은 컨트롤러 영역에서 처리하는 것이 적절하다.
		memberService = new MemberService(conn);
	}

	public void doLogin() {

		System.out.println("== 회원 로그인 ==");

		String loginId;
		String loginPw;

		// 로그인 입력횟수 제한
		int blockCnt = 0;

		while (true) {

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

			int memberCnt = memberService.getMemberCntByLoginId(loginId);

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
			member = memberService.getMemberByLoginId(loginId);

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

		// 입력횟수 제한, 3회 틀릴시 다시 명령어 입력받기
		int blockCnt = 0;

		while (true) {

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

			int memberCnt = memberService.getMemberCntByLoginId(loginId);

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

		memberService.doJoin(loginId, loginPw, name);

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
