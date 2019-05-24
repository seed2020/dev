
[D: 가 아닌  드라이브에 설치 할 경우]
----------------------------------------------------
  1. log4j.xml 추가
    - [소스] resources/settings/[고객코드]/log4j.xml 추가 
      - ../../deploy/log4j.xml 참조로 내용중 "D:" 변경
  2. context.properties
    - [소스] resources/settings/[고객코드]/context-plain.properties 파일의  내용중 "D:" 변경
    - 변경 후 PropertyEncUtil 실행 하여 암호화 함
  3. [서버] 톰켓 설정 변경
    - ./bin/  catalina.bat, startup.bat - 파일의  내용중 "D:" 변경
    - ./conf/  logging.properties, server.xml - 파일의  내용중 "D:" 변경
    - ./isapi/ isapi_redirect.properties - 파일의  내용중 "D:" 변경

[메시저 설치 안함]
----------------------------------------------------
  - [소스] resources/settings/[고객코드]/servlet-context.xml 추가
    - ../../deploy/servlet-context.xml 참조로 내용중 "D:" 변경
    - 메신저 관련 설정 제거 : 참조 (고객코드: 70404F)
