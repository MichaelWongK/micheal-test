create table URP_User(
	UserId int auto_increment primary key,
	UserName varchar(10) not null,
	UserTell int not null,
	Password varchar(32) not null,
	Department varchar(20) not null default "",
	CreateTime timestamp,
	Type tinyint not null default 0 comment "0学生，1教师，3管理员.默认学生"
);