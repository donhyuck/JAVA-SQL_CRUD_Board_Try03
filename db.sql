DROP DATABASE IF EXISTS text_board;

CREATE DATABASE text_board;

# DB - 게시글 작성쿼리
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
WHERE loginId = 'test3'; 

SELECT * FROM `member`;

ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER updateDate;

DESC article;
SELECT * FROM article;

SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
WHERE a.id = 1;

## 검색어와 일치하는 게시글 출력
SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
WHERE a.title LIKE '%t%';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
title = "제목6",
`body` = "내용6",
hit = 25;

SELECT * FROM article;

## 게시글 페이징 구현
SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
ORDER BY a.id DESC
LIMIT 64,5;

DROP TABLE `like`;
## like 테이블 생성
## liketype = 1 추천 / liketype = 2 비추천
CREATE TABLE `like` (
	id INT(10)  UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	articleId INT(10) UNSIGNED NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL,
	likeType TINYINT(1) NOT NULL
);

SELECT * FROM `like`;

# like했는지 체크 -> case when 사용
# 추천/비추천 했다면 해당하는 likeType 값 리턴
# 추천/비추천 안했다면 0리턴 
SELECT
CASE WHEN COUNT(*) != 0
THEN likeType ELSE 0 END
FROM `like`
WHERE articleId = 12 AND memberId = 2;

SELECT * FROM article;

# 추천/비추천 조회
SELECT COUNT(*)
FROM `like`
WHERE articleId = 12 AND likeType = 1;

# comment 테이블 삭제
DROP TABLE `comment`;
# comment 테이블 생성
CREATE TABLE `comment` (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	articleId INT(10) UNSIGNED NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL,
	commentTitle CHAR(100) NOT NULL,
	commentBody CHAR(100) NOT NULL
);

DESC `comment`;
SELECT * FROM `comment`;

SELECT COUNT(*)
FROM `comment`
WHERE id = 2 AND articleId = 13;

# 댓글 페이징
SELECT c.*,m.name AS extra_writer
FROM `comment` c
INNER JOIN `member` m
ON c.memberId = m.id
WHERE articleId = 10
LIMIT 5,5;

# 전체 댓글 수
SELECT COUNT(*)
FROM `comment`
WHERE articleId = 10;