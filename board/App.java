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

				int actionResult = doAction(conn, input, command);

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

	private int doAction(Connection conn, Scanner input, String command) {

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

			System.out.println("== 게시글 목록 ==");
			System.out.println("번호 / 제목");
			for (Article article : articles) {
				System.out.printf(" %d / %s \n", article.id, article.title);
			}

		} else if (command.startsWith("article modify ")) {

			boolean isInt = command.split(" ")[2].matches("-?\\d+");

			if (!isInt) {
				System.out.println("게시글의 ID는 숫자로 입력해주세요.");
				return 0;
			}

			int id = Integer.parseInt(command.split(" ")[2].trim());

			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			int foundArticleId = DBUtil.selectRowIntValue(conn, sql);

			if (foundArticleId == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}

			System.out.println("== 게시글 수정 ==");
			System.out.print("새 제목 : ");
			String title = input.nextLine();
			System.out.print("새 내용 : ");
			String body = input.nextLine();

			sql = new SecSql();
			sql.append("UPDATE article");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", body = ?", body);
			sql.append("WHERE id = ?", id);

			DBUtil.update(conn, sql);
			System.out.printf("%d번 게시글이 수정되었습니다.\n", id);

		} else if (command.startsWith("article delete ")) {

			boolean isInt = command.split(" ")[2].matches("-?\\d+");

			if (!isInt) {
				System.out.println("게시글의 ID는 숫자로 입력해주세요.");
				return 0;
			}

			int id = Integer.parseInt(command.split(" ")[2].trim());

			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			int foundArticleId = DBUtil.selectRowIntValue(conn, sql);

			if (foundArticleId == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}

			sql = new SecSql();
			sql.append("DELETE FROM article");
			sql.append("WHERE id = ?", id);

			DBUtil.delete(conn, sql);
			System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);

		} else if (command.startsWith("article detail ")) {

			boolean isInt = command.split(" ")[2].matches("-?\\d+");

			if (!isInt) {
				System.out.println("게시글의 ID는 숫자로 입력해주세요.");
				return 0;
			}

			int id = Integer.parseInt(command.split(" ")[2].trim());

			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			int foundArticleId = DBUtil.selectRowIntValue(conn, sql);

			if (foundArticleId == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}

			sql = new SecSql();
			sql.append("SELECT * FROM article");
			sql.append("WHERE id = ?", id);

			Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
			Article article = new Article(articleMap);

			System.out.printf("== %d번 게시글 조회 ==\n", id);
			System.out.printf(" 번 호  : %d\n", article.id);
			System.out.printf("등록날짜 : %s\n", article.regDate);
			System.out.printf("수정날짜 : %s\n", article.updateDate);
			System.out.printf(" 제 목  : %s\n", article.title);
			System.out.printf(" 날 짜  : %s\n", article.body);

		} else if (command.equals("member join")) {
			System.out.println("== 회원가입 ==");

			String loginId; // 1. 아이디 생성 2. 중복아이디
			String loginPw; // 비밀번호 입력
			String loginPwConfirm; // 비밀번호와 1. 일치 2. 불일치
			String name;

			SecSql sql;

			// 입력횟수 제한, 3회 틀릴시 다시 명령어 입력받기
			int blockCnt = 0;

			// 아이디 입력
			while (true) {

				sql = new SecSql();

				if (blockCnt >= 3) {
					System.out.println("입력횟수 초과! 다시 시도해주세요.");
					return 0;
				}

				System.out.print("로그인 아이디 : ");
				loginId = input.nextLine();

				// 미입력(공백) 방지
				if (loginId.length() == 0) {
					System.out.println("아이디를 입력해주세요.");
					blockCnt++;
					continue;
				}

				// DB에 등록된 아이디를 조회, 아이디 중복방지
				sql.append("SELECT COUNT(*) FROM `member`");
				sql.append("WHERE loginId = ?", loginId);

				// 일치하는 아이디가 없으면 0 리턴
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
					return 0;
				}

				System.out.print("로그인 비밀번호 : ");
				loginPw = input.nextLine();

				// 미입력(공백) 방지
				if (loginPw.length() == 0) {
					System.out.println("비밀번호를 입력해주세요.");
					blockCnt++;
					continue;
				}

				// 비밀번호 확인
				while (true) {
					System.out.print("로그인 비밀번호 확인 : ");
					loginPwConfirm = input.nextLine();

					// 미입력(공백) 방지
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

		} else if (command.equals("member login")) {

			System.out.println("== 회원 로그인 ==");

			String loginId;
			String loginPw;
			SecSql sql = new SecSql();

			while (true) {

				System.out.print("로그인 아이디 : ");
				loginId = input.nextLine();

				if (loginId.length() == 0) {
					System.out.println("아이디를 입력해주세요.");
					continue;
				}

				sql.append("SELECT COUNT(*) FROM `member`");
				sql.append("WHERE loginId = ?", loginId);

				int memberCnt = DBUtil.selectRowIntValue(conn, sql);

				// 해당하는 아이디가 없으면 0이 리턴되며, 이는 로그인할 아이디가 없는 것이다.
				if (memberCnt == 0) {
					System.out.println("아이디가 존재하지 않습니다.");
					continue;
				}
				break;

			}

			while (true) {
				System.out.print("로그인 비밀번호 : ");
				loginPw = input.nextLine();

				if (loginPw.length() == 0) {
					System.out.println("비밀번호를 입력해주세요.");
					continue;
				}
				break;
			}

			// 등록된 회원의 비밀번호와 입력한 비밀번호가 일치하는지 확인해야함
			// Member 인스턴스를 만들어서 비밀번호값을 조회합니다.
			sql = new SecSql();
			sql.append("SELECT * FROM article");
			sql.append("WHERE loginId = ?", loginId);

			Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);
			Member member = new Member(memberMap);

			if (!member.loginPw.equals(loginPw)) {
				System.out.println("비밀번호가 일치하지 않습니다.");
				return 0;
			}

			System.out.printf("%s님 환영합니다.\n", member.name);

		} else {
			System.out.println("잘못된 명령어입니다.");
		}
		return 0;
	}

}
