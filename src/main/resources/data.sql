insert into DESIGNATION (LVL_ID, ROLE) values(1,'Director');
insert into DESIGNATION (LVL_ID, ROLE) values(2,'Manager');
insert into DESIGNATION (LVL_ID, ROLE) values(3,'Lead');
insert into DESIGNATION (LVL_ID, ROLE) values(4,'DevOps');
insert into DESIGNATION (LVL_ID, ROLE) values(4,'Developer');
insert into DESIGNATION (LVL_ID, ROLE) values(4,'QA');
insert into DESIGNATION (LVL_ID, ROLE) values(5,'intern');

insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Thor',8,1);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Iron Man',1,2);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Hulk',1,3);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Captain Amrerica',1,2);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('War Machine',2,5);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Vision',2,4);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Falcon',4,6);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Antman',4,3);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Spiderman',2,7);
insert into EMPLOYEE (EMP_NAME, MANAGER_ID, DSGN_ID) values('Black Widow',3,6);