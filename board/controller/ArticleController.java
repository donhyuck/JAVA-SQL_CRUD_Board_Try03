package board.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import board.dto.Article;
import board.service.ArticleService;
import board.session.Session;

public class ArticleController extends Controller {
	private Scanner input;
	private String command;
	private Session session;

	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner input, String command, Session session) {
		this.input = input;
		this.command = command;
		this.session = session;

		articleService = new ArticleService(conn);
	}

	@Override
	public void doAction() {

		if (command.equals("article write")) {
			doWrite();
		} else if (command.equals("article list")) {
			showList();
		} else if (command.startsWith("article modify ")) {
			doModify();
		} else if (command.startsWith("article delete ")) {
			doDelete();
		} else if (command.startsWith("article detail ")) {
			showDetail();
		} else {
			System.out.println("존재하지 않는 명령어입니다.");
		}

	}

	private void doWrite() {

		if (session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		System.out.println("== 게시글 작성 ==");

		System.out.print("제목 : ");
		String title = input.nextLine();

		System.out.print("내용 : ");
		String body = input.nextLine();

		int id = articleService.doWrite(title, body, session.getLoginedMemberId());

		System.out.printf("%d번 게시글이 등록되었습니다.\n", id);

	}

	private void showList() {

		List<Article> articles = articleService.getArticles();

		if (articles.size() == 0) {
			System.out.println("게시글이 존재하지 않습니다.");
			return;
		}

		System.out.println("== 게시글 목록 ==");
		System.out.println("번호 / 제목 / 작성자");
		for (Article article : articles) {
			System.out.printf(" %d / %s / %s\n", article.getId(), article.getTitle(), article.getExtra_writer());
		}

	}

	private void doModify() {

		if (session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		boolean isInt = command.split(" ")[2].matches("-?\\d+");

		if (!isInt) {
			System.out.println("게시글의 ID는 숫자로 입력해주세요.");
			return;
		}

		int id = Integer.parseInt(command.split(" ")[2].trim());

		int foundArticleId = articleService.getArticlesCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		System.out.println("== 게시글 수정 ==");
		System.out.print("새 제목 : ");
		String title = input.nextLine();
		System.out.print("새 내용 : ");
		String body = input.nextLine();

		articleService.doModify(title, body, id);
		System.out.printf("%d번 게시글이 수정되었습니다.\n", id);

	}

	private void doDelete() {

		if (session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		boolean isInt = command.split(" ")[2].matches("-?\\d+");

		if (!isInt) {
			System.out.println("게시글의 ID는 숫자로 입력해주세요.");
			return;
		}

		int id = Integer.parseInt(command.split(" ")[2].trim());

		int foundArticleId = articleService.getArticlesCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		articleService.doDelete(id);
		System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);

	}

	private void showDetail() {

		boolean isInt = command.split(" ")[2].matches("-?\\d+");

		if (!isInt) {
			System.out.println("게시글의 ID는 숫자로 입력해주세요.");
			return;
		}

		int id = Integer.parseInt(command.split(" ")[2].trim());

		int foundArticleId = articleService.getArticlesCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		Article article = articleService.getArticle(id);

		System.out.printf("== %d번 게시글 조회 ==\n", id);
		System.out.printf(" 번 호  : %d\n", article.getId());
		System.out.printf("등록날짜 : %s\n", article.getRegDate());
		System.out.printf("수정날짜 : %s\n", article.getUpdateDate());
		System.out.printf(" 작성자  : %s\n", article.getExtra_writer());
		System.out.printf(" 제 목  : %s\n", article.getTitle());
		System.out.printf(" 내 용  : %s\n", article.getBody());

	}
}
