package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class BoardMain {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		// 명령어를 통해 Array에 정보가 저장되어도 main함수가 끝나면 소멸된다.
		// 다시 시작하면 이전 데이터가 사라진다. => DB가 필요한 이유

		System.out.println("== 프로그램 시작 ==");

		while (true) {

			System.out.print("명령어 : ");
			String command = input.nextLine();

			command = command.trim();

			if (command.length() == 0) {
				continue;
			}

			if (command.equals("system exit")) {
				System.out.println("== 프로그램 종료 ==");
				break;

			} else if (command.equals("article write")) {
				System.out.println("== 게시글 작성 ==");

				String title;
				String body;

				System.out.print("제목 : ");
				title = input.nextLine();

				System.out.print("내용 : ");
				body = input.nextLine();

				// JDBC적용
				Connection conn = null; // DB 접속 객체
				PreparedStatement pstat = null; // SQL 구문을 실행하는 역할

				try {
					Class.forName("com.mysql.cj.jdbc.Driver"); // Mysql JDBC 드라이버 로딩
					String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");

					String sql = "INSERT INTO article";
					sql += " SET regDate = NOW()";
					sql += ", updateDate = NOW()";
					sql += ", title = \"" + title + "\"";
					sql += ", body = \"" + body + "\"";

					pstat = conn.prepareStatement(sql);
					int affectedRows = pstat.executeUpdate();

					System.out.println("affectedRows: " + affectedRows);

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러: " + e);
				} finally { // 예외 상황이든 아니든 무조건 마지막에 실행하는 finally
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

			} else if (command.equals("article list")) {

				// 재구성 예정

			} else {
				System.out.println("잘못된 명령어입니다.");
			}
		}

	}

}
