#String
-- url = "jdbc:mysql://127.0.0.1:3306/24_08_Spring?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
#String
-- user = "root";
#String
-- pass = "1234";
#Class.forName
# ("org.mariadb.jdbc.Driver");


###(INIT 시작)
# DB 세팅
DROP DATABASE IF EXISTS `24_08_Spring`;
CREATE DATABASE `24_08_Spring`;
USE `24_08_Spring`;

# 게시글 테이블 생성
CREATE TABLE article(
                        id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                        regDate DATETIME NOT NULL,
                        updateDate DATETIME NOT NULL,
                        title CHAR(100) NOT NULL,
                        `body` TEXT NOT NULL
);

# 회원 테이블 생성
CREATE TABLE `member`(
                         id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         regDate DATETIME NOT NULL,
                         updateDate DATETIME NOT NULL,
                         loginId CHAR(30) NOT NULL,
                         loginPw CHAR(100) NOT NULL,
                         `authLevel` smallint(2) unsigned default 3 comment '권한 레벨 (3=일반,7=관리자)',
                         `name` char(20) not null,
                         nickname char(20) not null,
                         cellphoneNum char(20) not null,
                         email char(50) not null,
                         delStatus tinyint(1) unsigned not null default 0 comment '탈퇴 여부 (0=탈퇴 전, 1=탈퇴 후)',
                         delDate datetime comment '탈퇴 날짜'
);



## 게시글 테스트 데이터 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목1',
`body` = '내용1';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목2',
`body` = '내용2';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목3',
`body` = '내용3';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목4',
`body` = '내용4';


## 회원 테스트 데이터 생성
## (관리자)
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'admin',
loginPw = 'admin',
`authLevel` = 7,
`name` = '관리자',
nickname = '관리자',
cellphoneNum = '01012341234',
email = 'abc@gmail.com';

## (일반)
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'test1',
loginPw = 'test1',
`name` = '회원1_이름',
nickname = '회원1_닉네임',
cellphoneNum = '01043214321',
email = 'axdswww12@gmail.com';

## (일반)
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'test2',
loginPw = 'test2',
`name` = '회원2_이름',
nickname = '회원2_닉네임',
cellphoneNum = '01056785678',
email = 'abcde@gmail.com';

alter table article add column memberId int(10) unsigned not null after updateDate;

update article
set memberId = 2
where id in (1,2);

update article
set memberId = 3
where id in (3,4);


# 게시판(board) 테이블 생성
CREATE TABLE board (
                       id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       regDate DATETIME NOT NULL,
                       updateDate DATETIME NOT NULL,
                       `code` CHAR(50) NOT NULL UNIQUE COMMENT 'notice(공지사항) free(자유) QnA(질의응답) ...',
                       `name` CHAR(20) NOT NULL UNIQUE COMMENT '게시판 이름',
                       delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '삭제 여부 (0=삭제 전, 1=삭제 후)',
                       delDate DATETIME COMMENT '삭제 날짜'
);

## 게시판(board) 테스트 데이터 생성
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'NOTICE',
`name` = '공지사항';

INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'FREE',
`name` = '자유';

INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'QnA',
`name` = '질의응답';

alter table article add column boardId int(10) unsigned not null after `memberId`;

UPDATE article
SET boardId = 1
WHERE id IN (1,2);

UPDATE article
SET boardId = 2
WHERE id = 3;

UPDATE article
SET boardId = 3
WHERE id = 4;

alter table article add column hitCount int(10) unsigned not null default 0 after `body`;



# reactionPoint 테이블 생성
CREATE TABLE reactionPoint(
                              id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                              regDate DATETIME NOT NULL,
                              updateDate DATETIME NOT NULL,
                              memberId INT(10) UNSIGNED NOT NULL,
                              relTypeCode CHAR(50) NOT NULL COMMENT '관련 데이터 타입 코드',
                              relId INT(10) NOT NULL COMMENT '관련 데이터 번호',
                              `point` INT(10) NOT NULL
);

# reactionPoint 테스트 데이터 생성
# 1번 회원이 1번 글에 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'article',
relId = 1,
`point` = -1;

# 1번 회원이 2번 글에 좋아요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'article',
relId = 2,
`point` = 1;

# 2번 회원이 1번 글에 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 1,
`point` = -1;

# 2번 회원이 2번 글에 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 2,
`point` = -1;

# 3번 회원이 1번 글에 좋아요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 3,
relTypeCode = 'article',
relId = 1,
`point` = 1;

# article 테이블에 reactionPoint(좋아요) 관련 컬럼 추가
alter table article add column goodReactionPoint int(10) unsigned not null default 0;
ALTER TABLE article ADD COLUMN badReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

# update join -> 기존 게시글의 good bad RP 값을 RP 테이블에서 추출해서 article table에 채운다
# update article as A
#     inner join (
#     select RP.relTypeCode, Rp.relId,
#     SUM(IF(RP.point > 0,RP.point,0)) AS goodReactionPoint,
#     SUM(IF(RP.point < 0,RP.point * -1,0)) AS badReactionPoint
#     from reactionPoint As RP
#     group by RP.relTypeCode,Rp.relId
#     ) as RP_SUM
# on A.id = RP_SUM.relId
#     set A.goodReactionPoint = RP_SUM.goodReactionPoint,
#         A.badReactionPoint = RP_SUM.badReactionPoint;

# reply 테이블 생성
CREATE TABLE reply (
                       id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       regDate DATETIME NOT NULL,
                       updateDate DATETIME NOT NULL,
                       memberId INT(10) UNSIGNED NOT NULL,
                       relTypeCode CHAR(50) NOT NULL COMMENT '관련 데이터 타입 코드',
                       relId INT(10) NOT NULL COMMENT '관련 데이터 번호',
                       `body`TEXT NOT NULL
);

# 2번 회원이 1번 글에 댓글 작성
INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 1,
`body` = '댓글 1';

# 2번 회원이 1번 글에 댓글 작성
INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 1,
`body` = '댓글 2';

# 3번 회원이 1번 글에 댓글 작성
INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 3,
relTypeCode = 'article',
relId = 1,
`body` = '댓글 3';

# 3번 회원이 1번 글에 댓글 작성
INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'article',
relId = 2,
`body` = '댓글 4';

# reply 테이블에 좋아요 관련 컬럼 추가
ALTER TABLE reply ADD COLUMN goodReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE reply ADD COLUMN badReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0;

# reactionPoint 테스트 데이터 생성
# 1번 회원이 1번 댓글에 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'reply',
relId = 1,
`point` = -1;

# 1번 회원이 2번 댓글에 좋아요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
relTypeCode = 'reply',
relId = 2,
`point` = 1;

# 2번 회원이 1번 댓글에 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'reply',
relId = 1,
`point` = -1;

# 2번 회원이 2번 댓글에 싫어요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 2,
relTypeCode = 'reply',
relId = 2,
`point` = -1;

# 3번 회원이 1번 댓글에 좋아요
INSERT INTO reactionPoint
SET regDate = NOW(),
updateDate = NOW(),
memberId = 3,
relTypeCode = 'reply',
relId = 1,
`point` = 1;

# update join -> 기존 게시물의 good,bad RP 값을 RP 테이블에서 가져온 데이터로 채운다
UPDATE reply AS R
    INNER JOIN (
    SELECT RP.relTypeCode,RP.relId,
    SUM(IF(RP.point > 0, RP.point, 0)) AS goodReactionPoint,
    SUM(IF(RP.point < 0, RP.point * -1, 0)) AS badReactionPoint
    FROM reactionPoint AS RP
    GROUP BY RP.relTypeCode, RP.relId
    ) AS RP_SUM
ON R.id = RP_SUM.relId
    SET R.goodReactionPoint = RP_SUM.goodReactionPoint,
        R.badReactionPoint = RP_SUM.badReactionPoint;

# 파일 테이블 추가
CREATE TABLE genFile (
                         id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, # 번호
        regDate DATETIME DEFAULT NULL, # 작성날짜
                             updateDate DATETIME DEFAULT NULL, # 갱신날짜
                             delDate DATETIME DEFAULT NULL, # 삭제날짜
                             delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
  relTypeCode CHAR(50) NOT NULL, # 관련 데이터 타입(article, member)
  relId INT(10) UNSIGNED NOT NULL, # 관련 데이터 번호
  originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일이름
  fileExt CHAR(10) NOT NULL, # 확장자
                             typeCode CHAR(20) NOT NULL, # 종류코드 (common)
  type2Code CHAR(20) NOT NULL, # 종류2코드 (attatchment)
  fileSize INT(10) UNSIGNED NOT NULL, # 파일의 사이즈
  fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
  fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
  fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1)
  fileDir CHAR(20) NOT NULL, # 파일이 저장되는 폴더명
  PRIMARY KEY (id),
                         KEY relId (relTypeCode,relId,typeCode,type2Code,fileNo)
);

# 기존의 회원 비번을 암호화
UPDATE `member`
SET loginPw = SHA2(loginPw,256);

# FAQ 테이블 생성
CREATE TABLE faq (
                     id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                     question TEXT NOT NULL COMMENT '질문',
                     answer TEXT NOT NULL COMMENT '답변',
                     delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '삭제 여부 (0=삭제 전, 1=삭제 후)',
                     delDate DATETIME COMMENT '삭제 날짜'
);

# FAQ 테스트 데이터 생성 (예시)
INSERT INTO faq
SET question = '회원 가입은 어떻게 하나요?',
    answer = '회원 가입은 홈페이지 상단의 "회원가입" 버튼을 클릭하여 진행할 수 있습니다.';

INSERT INTO faq
SET question = '비밀번호를 잊어버렸는데 어떻게 해야 하나요?',
    answer = '비밀번호 찾기 페이지에서 이메일을 입력하면 비밀번호 재설정 링크가 전송됩니다.';

INSERT INTO faq
SET question = '상품 환불은 어떻게 하나요?',
    answer = '환불은 구매일로부터 7일 이내에 가능하며, 고객센터로 문의해주시면 처리해드립니다.';

INSERT INTO faq
SET question = '배송 기간은 얼마나 걸리나요?',
    answer = '배송 기간은 평균 2~3일이 소요되며, 지역에 따라 차이가 있을 수 있습니다.';

INSERT INTO faq
SET question = '결제 수단은 어떤 것이 있나요?',
    answer = '신용카드, 체크카드, Toss, Paypal을 이용하여 결제할 수 있습니다.';

-- faq 테스트 데이터 더 추가하기.
INSERT INTO faq (question, answer)
VALUES
    ('사용자 계정이 잠겼는데 어떻게 해결하나요?', '사용자 계정이 잠겼을 경우, 고객센터에 문의하시면 잠금 해제가 가능합니다.'),
    ('비밀번호 변경은 어떻게 하나요?', '계정 설정에서 비밀번호 변경 옵션을 통해 변경할 수 있습니다.'),
    ('사이트 접속이 불안정한데 어떻게 해야 하나요?', '인터넷 연결을 확인하고, 지속적인 문제가 발생 시 고객센터에 문의하세요.'),
    ('무료 체험 기간은 얼마인가요?', '무료 체험 기간은 가입 후 30일입니다.'),
    ('구매한 상품을 취소할 수 있나요?', '구매한 상품은 배송 전에만 취소 가능합니다.'),
    ('프로모션 코드는 어떻게 적용하나요?', '결제 페이지에서 프로모션 코드를 입력하여 할인을 받을 수 있습니다.'),
    ('새로운 기능이 업데이트되면 알림을 받을 수 있나요?', '알림 설정에서 업데이트 알림을 활성화하면 새 기능에 대한 알림을 받을 수 있습니다.'),
    ('환불 요청은 어떻게 하나요?', '고객센터에 환불 요청을 제출하시면 처리됩니다.'),
    ('회원 탈퇴 후 재가입은 가능한가요?', '회원 탈퇴 후 동일한 이메일로 재가입이 가능합니다.'),
    ('휴대폰 번호를 변경하고 싶은데 어떻게 해야 하나요?', '계정 설정에서 휴대폰 번호 변경이 가능합니다.'),
    ('포인트 적립은 어떻게 하나요?', '상품 구매 시 자동으로 포인트가 적립됩니다.'),
    ('로그인에 실패했을 때 어떻게 해야 하나요?', '비밀번호를 재확인하시고 문제가 계속되면 비밀번호 찾기를 이용하세요.'),
    ('제품 보증 기간은 얼마나 되나요?', '구매일로부터 1년간 제품 보증이 적용됩니다.'),
    ('배송 추적은 어디서 확인할 수 있나요?', '주문 내역 페이지에서 배송 추적 링크를 확인할 수 있습니다.'),
    ('다양한 결제 수단을 지원하나요?', '신용카드, 체크카드, PayPal 등 다양한 결제 수단을 지원합니다.'),
    ('배송비는 얼마인가요?', '구매 금액에 따라 배송비는 달라질 수 있으며, 50,000원 이상 구매 시 무료 배송이 제공됩니다.'),
    ('할인 쿠폰은 어떻게 받을 수 있나요?', '이벤트 참여 또는 회원 가입 시 할인 쿠폰이 발급됩니다.'),
    ('동일한 제품을 여러 개 구매할 수 있나요?', '장바구니에서 원하는 수량만큼 선택하여 구매할 수 있습니다.'),
    ('결제 후 영수증은 어디서 확인할 수 있나요?', '결제 완료 후 이메일로 영수증이 전송되며, 계정 내 주문 내역에서도 확인 가능합니다.'),
    ('해외 배송이 가능한가요?', '해외 배송은 지원되지 않으며, 국내 배송만 가능합니다.'),
    ('휴면 계정이 되었을 때는 어떻게 하나요?', '로그인 시 자동으로 휴면 상태가 해제됩니다.'),
    ('포인트 유효기간은 얼마인가요?', '적립된 포인트는 1년간 유효하며, 만료 시 자동 소멸됩니다.'),
    ('기프트 카드 사용 방법은 무엇인가요?', '결제 페이지에서 기프트 카드 번호를 입력하여 사용 가능합니다.'),
    ('계정 이메일 주소를 변경할 수 있나요?', '계정 설정에서 이메일 주소 변경이 가능합니다.'),
    ('고객센터 운영 시간은 언제인가요?', '고객센터 운영 시간은 평일 오전 9시부터 오후 6시까지입니다.'),
    ('교환/반품 요청은 어떻게 하나요?', '주문 내역 페이지에서 교환 또는 반품 요청을 할 수 있습니다.'),
    ('회원 등급은 어떻게 나뉘어 있나요?', '회원 등급은 일반, 실버, 골드, 플래티넘으로 나뉘며, 구매 금액에 따라 결정됩니다.'),
    ('상품 리뷰는 어떻게 작성하나요?', '구매 완료 후 상품 상세 페이지에서 리뷰를 작성할 수 있습니다.'),
    ('리뷰 작성 시 포인트는 어떻게 받나요?', '리뷰 작성 후 검토가 완료되면 포인트가 적립됩니다.'),
    ('주문 취소 시 환불은 얼마나 걸리나요?', '환불 처리는 취소 요청 후 3~5일 이내에 완료됩니다.');

###(INIT 끝)
##########################################
SELECT *
FROM article
ORDER BY id DESC;

SELECT * FROM board;

SELECT * FROM `member`;

SELECT * FROM `reactionPoint`;

SELECT * FROM `reply`;

SELECT * FROM `genFile`;

SELECT * FROM `faq`;

###############################################################################

SELECT R.*, M.nickname AS extra__writer
FROM reply AS R
         INNER JOIN `member` AS M
                    ON R.memberId = M.id
WHERE relTypeCode = 'article'
  AND relId = 2
ORDER BY R.id ASC;

SELECT IFNULL(SUM(RP.point),0)
FROM reactionPoint as RP
WHERE RP.relTypeCode = 'article'
  AND RP.relId = 3
  AND RP.memberId = 2;


    ## 게시글 테스트 데이터 대량 생성
INSERT INTO article
(
    regDate, updateDate, memberId, boardId, title, `body`
)
select now(), now(), floor(RAND() * 2) + 2, FLOOR(RAND() * 3) + 1, CONCAT('제목__', RAND()), CONCAT('내용__', RAND())
from article;


select floor(RAND() * 2) + 2;

SELECT FLOOR(RAND() * 3) + 1;


    INSERT INTO article
SET regDate = NOW(),
    updateDate = NOW(),
    title = CONCAT('제목__', RAND()),
    `body` = CONCAT('내용__', RAND());

show full columns from `member`;
desc `member`;

SELECT *
FROM article
where boardId = 1
ORDER BY id DESC;

SELECT *
FROM article
WHERE boardId = 2
ORDER BY id DESC;

SELECT *
FROM article
WHERE boardId = 3
ORDER BY id DESC;



SELECT COUNT(*) AS cnt
FROM article
WHERE boardId = 1
ORDER BY id DESC;

SELECT *
FROM article
WHERE boardId = 1 and title like '%123%'
ORDER BY id DESC;

SELECT *
FROM article
WHERE boardId = 1 and `body` like '%123%'
ORDER BY id DESC;

SELECT *
FROM article
WHERE boardId = 1 and title like '%123%' or `body` like '%123%'
ORDER BY id DESC;

SELECT count(*)
FROM article AS A
WHERE A.boardId = 1
ORDER BY A.id DESC;

-- boardId=1&searchKeywordTypeCode=nickname&searchKeyword=1

SELECT COUNT(*)
FROM article AS A
WHERE A.boardId = 1 and A.memberId = 3
ORDER BY A.id DESC;

select hitCount
from article where id = 3;

SELECT * FROM `reactionPoint`;

SELECT A.* , M.nickname AS extra__writer
FROM article AS A
         INNER JOIN `member` AS M
                    ON A.memberId = M.id
WHERE A.id = 1;

    # LEFT JOIN
SELECT A.*, M.nickname AS extra__writer, RP.point
FROM article AS A
         INNER JOIN `member` AS M
                    ON A.memberId = M.id
         left join reactionPoint as RP
                   on A.id = RP.relId and RP.relTypeCode = 'article'
group by A.id
order by A.id desc;

# 서브쿼리
SELECT A.*,
       ifnull(sum(RP.point),0) as extra__sumReactionPoint,
       IFNULL(SUM(if(RP.point > 0,RP.point,0)),0) AS extra__goodReactionPoint,
       IFNULL(SUM(IF(RP.point < 0,RP.point,0)),0) AS extra__badReactionPoint
FROM (
         select A.*, M.nickname as extra__writer
         from article as A
                  inner join `member` as M
                             on A.memberId = M.id) AS A
         LEFT JOIN reactionPoint AS RP
                   ON A.id = RP.relId AND RP.relTypeCode = 'article'
GROUP BY A.id
ORDER BY A.id DESC;

# JOIN
    SELECT A.*, M.nickname AS extra__writer,
IFNULL(SUM(RP.point),0) AS extra__sumReactionPoint,
IFNULL(SUM(IF(RP.point > 0,RP.point,0)),0) AS extra__goodReactionPoint,
IFNULL(SUM(IF(RP.point < 0,RP.point,0)),0) AS extra__badReactionPoint
from article as A
INNER JOIN `member` AS M
ON A.memberId = M.id
LEFT JOIN reactionPoint AS RP
ON A.id = RP.relId AND RP.relTypeCode = 'article'
GROUP BY A.id
ORDER BY A.id DESC;

select ifnull(sum(RP.point),0)
from reactionPoint as RP
where RP.relTypeCode = 'article'
  and RP.relId = 3
  and RP.memberId = 1;

select A.*, M.nickname as extra__writer, ifnull(COUNT(R.id),0) as extra__repliesCount
from article as A
         inner join `member` as M
                    on A.memberId = M.id
         left join `reply` as R
                   on A.id = R.relId
group by A.id;