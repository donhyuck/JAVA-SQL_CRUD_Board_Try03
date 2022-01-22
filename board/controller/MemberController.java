package board.controller;

import java.sql.Connection;
import java.util.Scanner;

import board.dto.Member;
import board.service.MemberService;
import board.session.Session;

public class MemberController extends Controller {

	private Scanner input;
	private String command;
	private Session session;

	private MemberService memberService;

	public MemberController(Connection conn, Scanner input, String command, Session session) {
		this.input = input;
		this.command = command;
		this.session = session;

		memberService = new MemberService(conn);
	}

	@Override
	public void doAction() {

		String[] cmdBits = command.split(" ");
		String actionMethodName = cmdBits[1];

		switch (actionMethodName) {
		case "join":
			doJoin();
			break;
		case "login":
			doLogin();
			break;
		case "logout":
			doLogout();
			break;
		case "whoami":
			showWhoami();
			break;
		default:
			System.out.println("존재하지 않는 명령어입니다.");
		}
	}

	private void doLogin() {

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

			if (!member.getLoginPw().equals(loginPw)) {
				System.out.println("비밀번호가 일치하지 않습니다.");
				blockCnt++;
				continue;
			}
			break;

		}

		System.out.printf("%s님 환영합니다.\n", member.getName());

		session.setLoginedMemberId(member.getId());
		session.setLoginedMember(member);

	}

	private void doJoin() {

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

	private void doLogout() {

		if (session.getLoginedMember() == null) {
			System.out.println("현재 로그아웃 상태입니다.");
			return;
		}

		System.out.printf("%s님 로그아웃되었습니다.\n", session.getLoginedMember().getName());
		session.setLoginedMember(null);
		session.setLoginedMemberId(-1);

	}

	private void showWhoami() {

		if (session.getLoginedMember() == null) {
			System.out.println("현재 로그아웃 상태입니다.");
			return;
		}

		System.out.println("== 회원로그인 정보 ==");
		System.out.printf(" 아이디: %s\n", session.getLoginedMember().getLoginId());
		System.out.printf(" 이 름 : %s\n", session.getLoginedMember().getName());

	}

}
