package board;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardMain {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		// 명령어를 통해 Array에 정보가 저장되어도 main함수가 끝나면 소멸된다.
		// 다시 시작하면 이전 데이터가 사라진다. => DB가 필요한 이유
		List<Article> articles = new ArrayList<>();

		int lastArticleId = 1;

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

				int id = lastArticleId;
				String title;
				String body;

				System.out.print("제목 : ");
				title = input.nextLine();

				System.out.print("내용 : ");
				body = input.nextLine();

				Article article = new Article(id, title, body);
				articles.add(article);
				System.out.printf("%d번 글이 등록되었습니다.\n", id);

				lastArticleId++;

			}

			else if (command.equals("article list")) {

				if (articles.size() == 0) {
					System.out.println("게시글이 존재하지 않습니다.");
				}

				System.out.println("== 게시글 목록 ==");
				System.out.println("번호 / 제목 ");

				for (Article article : articles) {
					System.out.printf(" %d / %s \n", article.id, article.title);
				}

			} else {
				System.out.println("잘못된 명령어입니다.");
			}
		}

	}

}
