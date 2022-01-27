package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import board.controller.ArticleController;
import board.controller.Controller;
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

				} else if (command.equals("system exit")) {
					System.out.println("== 프로그램 종료 ==");
					break;
				}

				// 프론트 컨트롤러 작성
				String[] commandBits = command.split(" ");

				if (commandBits.length < 2) {
					System.out.println("존재하지 않는 명령어입니다.");
					continue;
				}

				// 명령어 앞 글자를 통해 해당하는 컨트롤러로 넘겨준다.
				String controllerName = commandBits[0];
				String actionMethodName = commandBits[1];

				Controller controller = null;
				ArticleController articleController = new ArticleController(conn, input, command, session);
				MemberController memberController = new MemberController(conn, input, command, session);

				if (controllerName.equals("article")) {
					controller = articleController;
				} else if (controllerName.equals("member")) {
					controller = memberController;
				} else {
					System.out.printf("%s는 존재하지 않는 명령어입니다.\n", command);
					continue;
				}

				String actionName = controllerName + "/" + actionMethodName;

				switch (actionName) {
				case "article/write":
				case "article/modify":
				case "article/delete":
					if (session.getLoginedMember() == null) {
						System.out.println("로그인 후 이용해주세요");
						continue;
					}
					break;
				}

				switch (actionName) {
				case "member/join":
				case "member/login":
					if (session.getLoginedMember() != null) {
						System.out.println("로그아웃 후 이용해주세요");
						continue;
					}
					break;
				}

				controller.doAction();
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
}
