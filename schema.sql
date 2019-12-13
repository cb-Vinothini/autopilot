CREATE TABLE workflows(
id bigint(19) not null auto_increment,
name varchar(100) not null collate utf8_unicode_ci,
description varchar(250) not null COLLATE utf8_unicode_ci,
type int(11) not null,
entity_type int(11) not null,
created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
modified_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_workflows_name (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE hooks(
id bigint(19) not null auto_increment,
name varchar(100) not null collate utf8_unicode_ci,
event_type int(11) not null,
workflow_id bigint(19) not null,
created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
modified_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_hooks_name (name),
CONSTRAINT fk_hooks_workflow_id FOREIGN KEY (workflow_id) REFERENCES workflows (id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE criterias(
id bigint(19) not null auto_increment,
name varchar(100) not null collate utf8_unicode_ci,
parameter varchar(100) not null collate utf8_unicode_ci,
operator int(11) not null collate utf8_unicode_ci,
value varchar(100) null,
entity_type varchar(100) not null,
created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
modified_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_criterias_name (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE criteria_groups(
id bigint(19) not null auto_increment,
name varchar(100) not null collate utf8_unicode_ci,
workflow_id bigint(19) not null,
criteria_id bigint(19) not null,
created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
modified_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_criteria_groups_name (name),
CONSTRAINT fk_criteria_groups_workflow_id FOREIGN KEY (workflow_id) REFERENCES workflows (id) ON DELETE CASCADE,
CONSTRAINT fk_criteria_groups_criteria_id FOREIGN KEY (criteria_id) REFERENCES criterias (id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE actions(
id bigint(19) not null auto_increment,
name varchar(100) not null collate utf8_unicode_ci,
api_name varchar(100) not null collate utf8_unicode_ci,
api_parameters mediumtext not null collate utf8_unicode_ci,
created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
modified_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_actions_name (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE action_groups(
id bigint(19) not null auto_increment,
name varchar(100) not null collate utf8_unicode_ci,
workflow_id bigint(19) not null,
action_id bigint(19) not null,
created_at timestamp not null DEFAULT CURRENT_TIMESTAMP,
modified_at timestamp not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_action_groups_name (name),
CONSTRAINT fk_action_groups_workflow_id FOREIGN KEY (workflow_id) REFERENCES workflows (id) ON DELETE CASCADE,
CONSTRAINT fk_action_groups_action_id FOREIGN KEY (action_id) REFERENCES actions (id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
