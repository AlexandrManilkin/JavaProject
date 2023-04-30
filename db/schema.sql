-- Здесь располагаются DDL-команды для создания схемы БД
-- Рекомендуется подробно описывать, для чего необходима каждая таблица
-- DLL-команды следует приводить в отформатированном виде, чтобы их было удобно читать

-- Таблица с группами студентов
create table groups (
    id             integer     auto_increment primary key,
    group_name     varchar(30) not null,
    formation_date date        not null,
    constraint uk_groups_group_formation unique(group_name, formation_date)
);

-- Таблица студентов
create table students (
    id           integer      auto_increment primary key,
    firstname    varchar(50)  not null,
    surname      varchar(100) not null,
    lastname     varchar(100),
    ser_passport varchar(4)   not null,
    num_passport varchar(6)   not null,
    group_id     integer      not null,
    constraint ch_students_ser check(char_length(ser_passport) = 4),
    constraint ch_students_num check(char_length(num_passport) = 6),
    constraint uk_students_ser_num unique(ser_passport, num_passport),
    constraint fk_students_group_id foreign key (group_id) references groups (id)
);

-- Таблица экзаменов
create table exams (
    id integer          auto_increment primary key,
    name varchar(200)   not null,
    exam_date timestamp not null,
    constraint uk_exams_name_date unique(name, exam_date)
);

-- Таблица с расписанием экзаменов студентов
create table student_exams (
    id           integer auto_increment primary key,
    student_id   integer not null,
    exam_id      integer not null,
    constraint uk_student_exams_student_exam unique(student_id, exam_id),
    constraint fk_student_exams_student foreign key (student_id) references students (id),
    constraint fk_student_exams_exam foreign key (exam_id) references exams (id)
);
