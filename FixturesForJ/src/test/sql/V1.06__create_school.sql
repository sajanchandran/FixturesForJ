CREATE TABLE T_SCHOOL
   (	
	NAME VARCHAR(20) NOT NULL, 
	COUNTRY VARCHAR(20)
   );

ALTER TABLE T_SCHOOL
ADD CONSTRAINT PK_SCHOL PRIMARY KEY (NAME);