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

		String[] cmdBits = command.split(" ");
		String actionMethodName = cmdBits[1];

		switch (actionMethodName) {
		case "write":
			doWrite();
			break;
		case "list":
			showList();
			break;
		case "modify":
			doModify();
			break;
		case "delete":
			doDelete();
			break;
		case "detail":
			showDetail();
			break;
		default:
			System.out.println("존재하지 않는 명령어입니다.");
			break;
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

		String[] cmdBits = command.split(" ");
		String SearchKeyword = "";
		List<Article> articles;

		// 게시글 목록을 페이징으로 작성
		int page = 1;
		int itemsInAPage = 5; // 한번에 보이는 게시글 수

		while (true) {

			// 검색어가 있는 경우 (article list ???)
			if (cmdBits.length > 2) {
				SearchKeyword = command.substring("article list ".length());
				articles = articleService.getArticlesByKeyword(page, itemsInAPage, SearchKeyword);
			}

			// 검색어가 없는 경우 (article list) 모든 글을 출력
			else {
				if (command.length() != 12) {
					System.out.println("잘못된 명령어입니다.");
					return;
				}
				articles = articleService.getArticles(page, itemsInAPage);
			}

			if (articles.size() == 0) {
				System.out.println("게시글이 존재하지 않습니다.");
				return;
			}

			System.out.println("== 게시글 목록 ==");

			System.out.println("번호 / 제목 / 작성자");
			for (Article article : articles) {
				System.out.printf(" %d / %s / %s\n", article.getId(), article.getTitle(), article.getExtra_writer());
			}

			// 현재 페이지, 마지막 페이지, 전체 글 수
			int articleCnt = articleService.getArticlesCnt(SearchKeyword);
			int lastPage = (int) Math.ceil(articleCnt / (double) itemsInAPage);

			System.out.printf("페이지 %d / %d, 게시글 %d건\n", page, lastPage, articleCnt);
			System.out.println("\n== [나가기] 0이하입력 [게시글 목록 이동] 페이지입력 ==");
			System.out.print("[article list] 명령어 : ");
			page = input.nextInt();

			input.nextLine();

			if (page <= 0) {
				System.out.println("게시글 페이지를 나갑니다.");
				break;
			}
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

		// 게시글의 작성자를 파악해서 그 사람만 수정, 삭제하도록
		Article article = articleService.getArticle(id);

		if (article.getMemberId() != session.getLoginedMemberId()) {
			System.out.printf("%s님은 %d번 게시글에 대한 권한이 없습니다.\n", session.getLoginedMember().getName(), id);
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

		// 게시글의 작성자를 파악해서 그 사람만 수정, 삭제하도록
		Article article = articleService.getArticle(id);

		if (article.getMemberId() != session.getLoginedMemberId()) {
			System.out.printf("%s님은 %d번 게시글에 대한 권한이 없습니다.\n", session.getLoginedMember().getName(), id);
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

		articleService.increaseHit(id);

		Article article = articleService.getArticle(id);

		System.out.printf("== %d번 게시글 조회 ==\n", id);
		System.out.printf(" 번 호  : %d\n", article.getId());
		System.out.printf("등록날짜 : %s\n", article.getRegDate());
		System.out.printf("수정날짜 : %s\n", article.getUpdateDate());
		System.out.printf(" 작성자  : %s\n", article.getExtra_writer());
		System.out.printf(" 제 목  : %s\n", article.getTitle());
		System.out.printf(" 내 용  : %s\n", article.getBody());
		System.out.printf(" 조회수  : %d\n", article.getHit());

	}
}
