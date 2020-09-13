CREATE TABLE backlogs (
    id int  NOT NULL,
    title varchar(255)  NULL,
    project_id int  NOT NULL,
    CONSTRAINT backlogs_pk PRIMARY KEY (id)
);

CREATE TABLE issues (
    id int  NOT NULL,
    issue_type varchar(255)  NOT NULL,
    title varchar(255)  NULL,
    parent_issue_id int  NULL,
    sprint_id int  NULL,
    backlog_id int  NULL,
    description varchar(255)  NULL,
    workflow_id int  NULL,
    issue_priority varchar(255)  NULL,
    creation_date date  NULL,
    executor_id int  NULL,
    reporter_id int  NULL,
    current_status_of_workflow varchar(255)  NULL,
    CONSTRAINT issues_pk PRIMARY KEY (id)
);

CREATE TABLE project_members (
    id int NOT NULL,
    users_id int  NOT NULL,
    projects_id int  NOT NULL,
    CONSTRAINT project_members_pk PRIMARY KEY (id)
);

CREATE TABLE projects (
    id int  NOT NULL,
    title varchar(255)  NULL,
    description varchar(255)  NULL,
    department_name varchar(255)  NULL,
    supervisor_id int  NULL,
    admin_id int  NULL,
    current_sprint_id int  NULL,
    CONSTRAINT projects_pk PRIMARY KEY (id)
);

CREATE TABLE sprints (
    id int  NOT NULL,
    project_id int  NOT NULL,
    title varchar(255)  NULL,
    start_date date  NULL,
    finish_date date  NULL,
    CONSTRAINT sprints_pk PRIMARY KEY (id)
);

CREATE TABLE users (
    id int  NOT NULL,
    first_name varchar(255)  NULL,
    last_name varchar(255)  NULL,
    patronymic varchar(255)  NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE workflow_statuses (
    id int  NOT NULL,
    workflow_id int  NOT NULL,
    status_name varchar(255)  NOT NULL,
    CONSTRAINT workflow_statuses_pk PRIMARY KEY (id)
);

CREATE TABLE workflows (
    id int  NOT NULL,
    name varchar(255) NULL,
    CONSTRAINT workflows_pk PRIMARY KEY (id)
);

ALTER TABLE backlogs ADD CONSTRAINT backlog_project
    FOREIGN KEY (project_id)
    REFERENCES projects (id)
;

ALTER TABLE issues ADD CONSTRAINT issue_backlogs
    FOREIGN KEY (backlog_id)
    REFERENCES backlogs (id)
;

ALTER TABLE issues ADD CONSTRAINT issue_issue
    FOREIGN KEY (parent_issue_id)
    REFERENCES issues (id)
;

ALTER TABLE issues ADD CONSTRAINT issue_sprint
    FOREIGN KEY (sprint_id)
    REFERENCES sprints (id)
;

ALTER TABLE issues ADD CONSTRAINT issue_user_executor
    FOREIGN KEY (executor_id)
    REFERENCES users (id)
;

ALTER TABLE issues ADD CONSTRAINT issue_user_reporter
    FOREIGN KEY (reporter_id)
    REFERENCES users (id)
;

ALTER TABLE issues ADD CONSTRAINT issue_workflow
    FOREIGN KEY (workflow_id)
    REFERENCES workflows (id)
;

ALTER TABLE project_members ADD CONSTRAINT project_members_projects
    FOREIGN KEY (projects_id)
    REFERENCES projects (id)
;

ALTER TABLE project_members ADD CONSTRAINT project_members_users
    FOREIGN KEY (users_id)
    REFERENCES users (id)
;

ALTER TABLE projects ADD CONSTRAINT projects_sprints
    FOREIGN KEY (current_sprint_id)
    REFERENCES sprints (id)
;

ALTER TABLE projects ADD CONSTRAINT projects_users_admin
    FOREIGN KEY (admin_id)
    REFERENCES users (id)
;

ALTER TABLE projects ADD CONSTRAINT projects_users_supervisor
    FOREIGN KEY (supervisor_id)
    REFERENCES users (id)
;

ALTER TABLE sprints ADD CONSTRAINT sprint_project
    FOREIGN KEY (project_id)
    REFERENCES projects (id)
;

ALTER TABLE workflow_statuses ADD CONSTRAINT wirkflow_statuses_workflow
    FOREIGN KEY (workflow_id)
    REFERENCES workflows (id)
;

CREATE SEQUENCE public.projects_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE public.backlogs_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE public.issues_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE public.project_members_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE public.sprints_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE public.users_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE public.workflow_statuses_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE SEQUENCE public.workflows_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;