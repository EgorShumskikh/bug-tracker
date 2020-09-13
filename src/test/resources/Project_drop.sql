
ALTER TABLE backlogs
    DROP CONSTRAINT backlog_project;

ALTER TABLE issues
    DROP CONSTRAINT issue_backlogs;

ALTER TABLE issues
    DROP CONSTRAINT issue_issue;

ALTER TABLE issues
    DROP CONSTRAINT issue_sprint;

ALTER TABLE issues
    DROP CONSTRAINT issue_user_executor;

ALTER TABLE issues
    DROP CONSTRAINT issue_user_reporter;

ALTER TABLE issues
    DROP CONSTRAINT issue_workflow;

ALTER TABLE project_members
    DROP CONSTRAINT project_members_projects;

ALTER TABLE project_members
    DROP CONSTRAINT project_members_users;

ALTER TABLE projects
    DROP CONSTRAINT projects_sprints;

ALTER TABLE projects
    DROP CONSTRAINT projects_users_admin;

ALTER TABLE projects
    DROP CONSTRAINT projects_users_supervisor;

ALTER TABLE sprints
    DROP CONSTRAINT sprint_project;

ALTER TABLE workflow_statuses
    DROP CONSTRAINT wirkflow_statuses_workflow;


DROP TABLE backlogs;

DROP TABLE issues;

DROP TABLE project_members;

DROP TABLE projects;

DROP TABLE sprints;

DROP TABLE users;

DROP TABLE workflow_statuses;

DROP TABLE workflows;


DROP SEQUENCE public.projects_id_seq;

DROP SEQUENCE public.backlogs_id_seq;


DROP SEQUENCE public.issues_id_seq;


DROP SEQUENCE public.project_members_id_seq;


DROP SEQUENCE public.sprints_id_seq;


DROP SEQUENCE public.users_id_seq;


DROP SEQUENCE public.workflow_statuses_id_seq;


DROP SEQUENCE public.workflows_id_seq;
