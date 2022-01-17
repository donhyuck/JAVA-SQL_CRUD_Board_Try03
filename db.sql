DROP DATABASE IF EXISTS text_board;

CREATE DATABASE text_board;
USE text_board;

CREATE TABLE article (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	title CHAR(100) NOT NULL,
	`body` TEXT NOT NULL
);

DESC article;
SELECT * FROM article;

# id에 해당하는 article이 있는지 확인
SELECT COUNT(*)
FROM article
WHERE id = 7;

# 게시글 조회
SELECT * FROM article
WHERE id = 5;

SELECT * FROM article;

# 회원 테이블생성
CREATE TABLE `member` (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	loginId CHAR(100) NOT NULL,
	loginPw CHAR(100) NOT NULL,
	`name` CHAR(100) NOT NULL
);

DESC `member`;

# 해당하는 아이디가 있으면 1, 없으면 0 (아이디 중복방지)
SELECT COUNT(*) FROM `member`
WHERE loginId = 'test1'; 
