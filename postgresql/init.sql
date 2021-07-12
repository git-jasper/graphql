CREATE TABLE TASK_DETAILS (
    task_id SERIAL PRIMARY KEY,
    description VARCHAR(100),
    interval_km INT,
    interval_months INT
);

CREATE TABLE TASKS (
    user_id INT,
    task_id INT REFERENCES TASK_DETAILS(task_id),
    name VARCHAR (30),
    serviced_at DATE,
    before_date DATE,
    before_km INT,
    PRIMARY KEY(user_id, task_id)
);

CREATE TABLE TASK_HISTORY (
    history_id SERIAL,
    user_id INT,
    task_id INT,
    service_date DATE,
    service_km INT,
    notes VARCHAR(100),
    PRIMARY KEY(history_id, user_id, task_id),
    FOREIGN KEY(user_id, task_id) REFERENCES TASKS(user_id, task_id)
);

INSERT INTO TASK_DETAILS(description, interval_km, interval_months)
VALUES ('a default task', 1000, 12);

INSERT INTO TASK_DETAILS(description, interval_km, interval_months)
VALUES ('another task', 5000, 24);