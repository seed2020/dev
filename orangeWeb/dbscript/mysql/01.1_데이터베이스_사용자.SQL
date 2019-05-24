
-- 루트 접속 작업함
-- mysql -u root -pro123$%^

-- [주의]
-- ABC123 >> [고객코드] 로 변경해서 실행 할 것


create user 'GW_ABC123_USER'@'%' identified by 'asdqwe123$%^';
create user 'GW_ABC123_USER'@'localhost' identified by 'asdqwe123$%^';
flush privileges;

create database GW_ORANGE DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
grant all privileges on GW_ORANGE.* to 'GW_ABC123_USER'@'%';
grant all privileges on GW_ORANGE.* to 'GW_ABC123_USER'@'localhost';

grant SELECT, INSERT, UPDATE, DELETE ON `mysql`.`proc` TO 'GW_ABC123_USER'@'%';
grant SELECT, INSERT, UPDATE, DELETE ON `mysql`.`proc` TO 'GW_ABC123_USER'@'localhost';
flush privileges;

-- use mysql
-- UPDATE user SET authentication_string=PASSWORD('asdqwe123$%^') WHERE User = 'GW_ABC123_USER';
-- flush privileges;

-- show databases;
-- drop database GW_ABC123;

-- select host, user from mysql.user;
-- drop user 'GW_ABC123_USER'@'%';
-- drop user 'GW_ABC123_USER'@'localhost';

-- select user, host from mysql.user where user like 'GW%' order by user, host;
-- select 
-- concat('drop user ''', user, '''@''', host, ''';') cmd
-- from mysql.user where user like 'GW%' order by user, host;


