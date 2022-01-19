package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import board.controller.ArticleController;
import board.controller.MemberController;
import board.session.Session;

public class App {

	public void start() {

		Scanner input = new Scanner(System.in);

		Connection conn = null;
		Session session = new Session();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Mysql JDBC 드라이버 로딩
			String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
			conn = DriverManager.getConnection(url, "root", "");

			System.out.println("== 프로그램 시작 ==");

			while (true) {

				System.out.print("명령어 : ");
				String command = input.nextLine();

				command = command.trim();

				if (command.length() == 0) {
					continue;
				}

				int actionResult = doAction(conn, input, command, session);

				// 프로그램 종료 제어
				if (actionResult == -1) {
					break;
				}
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close(); // 연결 종료
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private int doAction(Connection conn, Scanner input, String command, Session session) {

		ArticleController articleController = new ArticleController(conn, input, command, session);
		MemberController memberController = new MemberController(conn, input, command, session);

		if (command.equals("system exit")) {
			System.out.println("== 프로그램 종료 ==");
			return -1;

		} else if (command.equals("article write")) {

			articleController.doWrite();

		} else if (command.equals("article list")) {

			articleController.showList();

		} else if (command.startsWith("article modify ")) {

			articleController.doModify();

		} else if (command.startsWith("article delete ")) {

			articleController.doDelete();

		} else if (command.startsWith("article detail ")) {

			articleController.showDetail();

		} else if (command.equals("member join")) {

			memberController.doJoin();

		} else if (command.equals("member login")) {

			memberController.doLogin();

		} else if (command.equals("member logout")) {

			memberController.doLogout();

		} else if (command.equals("member whoami")) {

			memberController.showWhoami();

		} else {
			System.out.println("잘못된 명령어입니다.");
		}
		return 0;
	}

}
