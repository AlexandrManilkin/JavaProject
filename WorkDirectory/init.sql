CREATE TABLE users (
    id integer auto_increment primary key,
    name varchar(30) NOT NULL,
    status varchar(10) NOT NULL,
    CONSTRAINT user_unique_name unique(name),
    CONSTRAINT user_statuses CHECK (status = 'admin' OR status = 'user')
);

CREATE TABLE tests(
	id integer auto_increment PRIMARY KEY,
	name varchar(30) NOT NULL,
	exercise_count integer NOT NULL
);

CREATE TABLE exercises(
	id integer auto_increment primary key,
	task varchar(100) NOT NULL,
	answer varchar(30) NOT NULL,
	answer_options_count integer NOT NULL,
	test_id integer NOT NULL,
	CONSTRAINT test_id_constr FOREIGN KEY (test_id) REFERENCES tests (id)
);

CREATE TABLE answer_options(
	id integer auto_increment primary key,
	text varchar(30) NOT NULL,
	exercise_id integer NOT NULL,
	CONSTRAINT exercise_id_constr FOREIGN KEY (exercise_id) REFERENCES exercises (id)
);

CREATE TABLE completed_tests(
	id integer auto_increment primary key,
	user_id integer NOT NULL,
	test_id integer NOT NULL,
	true_answers_count integer NOT NULL,
	CONSTRAINT compl_test_user_id_constr FOREIGN KEY (user_id) REFERENCES users(id),
	CONSTRAINT compl_test_test_id_constr FOREIGN KEY (test_id) REFERENCES tests(id)
);

CREATE TABLE users_answers(
	id integer auto_increment primary key,
	completed_test_id integer NOT NULL,
	answer integer NOT NULL,
	CONSTRAINT c_test_id_constr FOREIGN KEY (completed_test_id) REFERENCES completed_tests(id)
);

INSERT INTO users (name, status)
values ('admin', 'admin');