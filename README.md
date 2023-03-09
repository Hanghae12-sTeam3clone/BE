# 프로젝트 소개
Pinterrest 클론 코딩


## 팀장 : 황원준
**팀원 : 이건호, 김현호, 표창영**
## 기술스택
<div align="center">
	<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=Spring Boot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=Spring Security&logoColor=white" />
  <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat&logo=Amazon S3&logoColor=white" />
  <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat&logo=Amazon RDS&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" />
  <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat&logo=Amazon EC2&logoColor=white" />
</div>

## 구현기능
**1. 로그인/회원가입 기능**
* 유효성 검사가 통과되면 회원가입이 가능하다.
* 회원가입된 회원은 로그인이 가능하다.
* JWT 토큰으로 Spring Security 인증/인가 구현한다.

**2. 메인 페이지 검색 기능**
* 찾고자하는 **키워드**를 검색하면 관련된 게시글을 볼 수 있다.

**3. 메인 페이지 전체 조회 기능**
* 가장 최근 게시물로 조회가 된다.
* 모든 사용자는 전체 **메인페이지 조회**가 가능하다.

**4. 게시물/댓글 기능**
* 로그인된 회원은 게시글 작성이 가능하다.
* 작성된 게시글에 댓글 작성이 가능하다.
* 내가 작성한 게시물과 댓글 수정/삭제가 가능하다.

**5. 게시물 좋아요 기능**
* 로그인된 회원은 원하는 게시물에 좋아요 버튼을 누를 수 있다.
* 버튼을 한번 더 누르면 좋아요 취소가 된다.

**6. 게시물 마이페이지 저장 기능**
* 원하는 게시물은 저장 버튼을 누르면 마이페이지에 저장된다.
* 저장 취소를 하고 싶으면 버튼을 한번 더 누르면 된다.

**7. 저장된 게시물 마이페이지 불러오기 기능**
* 저장된 게시물은 마이페이지에서 모아서 볼 수 있다.
* 게시물 저장 취소하면 모아보기에서 제외된다.



## API명세서

![FireShot Capture 002 - API 보기 - buttered-wave-dd3 notion site](https://user-images.githubusercontent.com/107843779/223948275-b19bb8ff-2186-4a45-9260-203d275a5d9c.png)




## ERD

![pinterest3](https://user-images.githubusercontent.com/107843779/223933731-60dc2fd6-bc18-4ae6-9260-73ef9c71afc7.png)


## 와이어 프레임
**회원가입**

![1](https://user-images.githubusercontent.com/107843779/223942415-4763f63f-7bf7-4777-914c-f32b51cc9fed.png)

**로그인(토근)+도전(리프레쉬)**

![2](https://user-images.githubusercontent.com/107843779/223942447-e6cf47c8-6be6-46a9-8d0a-2ea47803cd3c.png)

**메인페이지 (전체 핀 불러오기)**

![3](https://user-images.githubusercontent.com/107843779/223942709-ddadd364-9d49-4bc7-97fc-76259bfed915.png)

**상세페이지 (제목, 내용, 댓글, 좋아요)**

![4](https://user-images.githubusercontent.com/107843779/223942777-7b9afc2e-5ae2-488c-9ced-c55e985acbf2.png)

**핀 만든기 (제목, 내용, 사진)**

![5](https://user-images.githubusercontent.com/107843779/223942976-9ed57f72-f1d5-42a6-afb9-8c7336468995.png)

**마이페이지 (내 게시물)**

![6](https://user-images.githubusercontent.com/107843779/223943044-e284b0db-0241-4635-b6f1-56608feb16bb.png)

## Trouble Shooting
1.github 소통 오류로 인한 프로젝트 초기화 현상 발생. 
  - 깃허브 공부를 새롭게 진행해가면서 하나식 다시 구축해서 해결
  - 수정 및 삭제 @Transactional이 없이 했더니 되지 않아서 코드만 열심히 보다가 나중에 @Transactional을 넣으니 되었습니다. 꼭 확인하자!!
  - PinService에서 처음에 코드를 짰는데 오류가 많이나서 확인해보니 “**팀 내  Git 암살자**”가 계셔서 코드가 변신해 있어서 당황했습니다.
  - “**팀 내 Git 암살자**”가 있어서 yml과 build.grade 파일을 깃에 commit, push에 넣어주셔서 pull 받을때마다 yml과 build.grade 파일이 다 날라갔습니다. 앞으로 있을 팀 실전 프로젝트에서 파일들을 잘 확인하여 commit, push를 하도록 노력하겠습니다.

2. S3연결시 계속된 에러 발생
  - 공식 문서와 팀원들간의 소통을 통해 연결 성공.

3. 리사이징 과정에서 발생한 비율 및 파일 변환 문제
 - 처음 api구현 완료 후 작동시 파일이 저장이 되는것은 문제가 없으나 다량의 에러 메시지들이 발생.
 - SpringBoot 버전을 낮춰보라는 의견을 받아서 버전 다운그래이드시 에러 메시지 발생 없어짐.
 - AWS의 람다를 통해 리사이징 시도하였으나 시간상의 문제로 보류

4.QueryDsl 을 사용하여 폐이징 구현시 에러발생
 - 처음 QueryDsl 작성 후 실행하였으나 빈이 생성되지 않는 문제 발생.
 - 환경설정값을 변경시도  -> 자바 버전, build.gradle 의 DI 버전 변경 등  -> 에러 계속해서 발생.
 - 메서드 네임을 JPA Data 규칙에 맞춰서 작성 -> 에러 해결.. 
 - 메서드 네임 비규칙적 사용시 에러메시지 발생. 
