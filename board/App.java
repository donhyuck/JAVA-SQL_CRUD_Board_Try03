package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import board.util.DBUtil;
import board.util.SecSql;

public class App {

	public void start() {

		Scanner input = new Scanner(System.in);

		Connection conn = null;
		PreparedStatement pstat = null; // SQL 구문을 실행하는 역할

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

				int actionResult = doAction(conn, input, command, pstat);

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

			try {
				if (pstat != null && !pstat.isClosed()) {
					pstat.close(); // 연결 종료
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private int doAction(Connection conn, Scanner input, String command, PreparedStatement pstat) {

		if (command.equals("system exit")) {
			System.out.println("== 프로그램 종료 ==");
			return -1;

		} else if (command.equals("article write")) {
			System.out.println("== 게시글 작성 ==");

			String title;
			String body;

			System.out.print("제목 : ");
			title = input.nextLine();

			System.out.print("내용 : ");
			body = input.nextLine();

			// DBUtil 적용
			SecSql sql = new SecSql();
			sql.append("INSERT INTO article");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", body = ?", body);

			int id = DBUtil.insert(conn, sql);

			System.out.printf("%d번 게시글이 등록되었습니다.\n", id);

		} else if (command.equals("article list")) {

			List<Article> articles = new ArrayList<>();

			System.out.println("== 게시글 목록 ==");

			// DBUtil 적용
			SecSql sql = new SecSql();
			sql.append("SELECT * FROM article");
			sql.append("ORDER BY id DESC");

			List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
			for (Map<String, Object> articleMap : articleListMap) {
				articles.add(new Article(articleMap));
			}

			if (articles.size() == 0) {
				System.out.println("게시글이 존재하지 않습니다.");
				return 0;
			}

			System.out.println("번호 / 제목");
			for (Article article : articles) {
				System.out.printf(" %d / %s \n", article.id, article.title);
			}

		} else if (command.startsWith("article modify")) {

			int id = Integer.parseInt(command.split(" ")[2].trim());

			String title;
			String body;

			System.out.println("== 게시글 수정 ==");
			System.out.print("새 제목 : ");
			title = input.nextLine();
			System.out.print("새 내용 : ");
			body = input.nextLine();

			// DBUtil 적용
			SecSql sql = new SecSql();
			sql.append("UPDATE article");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", body = ?", body);
			sql.append("WHERE id = ?", id);

			DBUtil.update(conn, sql);
			System.out.printf("%d번 게시글이 수정되었습니다.\n", id);

		} else {
			System.out.println("잘못된 명령어입니다.");
		}
		return 0;
	}

}
