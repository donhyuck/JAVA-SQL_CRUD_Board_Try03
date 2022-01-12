package board;

import java.util.Scanner;

public class BoardMain {

	public static void main(String[] args) {
		System.out.println("== 프로그램 시작 ==");

		Scanner input = new Scanner(System.in);

		while (true) {

			System.out.print("명령어 : ");
			String command = input.nextLine();

			if (command.length() == 0) {
				continue;
			}

			if (command.equals("system exit")) {
				System.out.println("== 프로그램 종료 ==");
				break;
			} else if (command.equals("article write")) {
				System.out.println("== 게시글 작성 ==");

				// 구현중

			}

			else if (command.equals("article list")) {
				System.out.println("== 게시글 목록 ==");

			} else {
				System.out.println("잘못된 명령어입니다.");
			}
		}

	}

}
