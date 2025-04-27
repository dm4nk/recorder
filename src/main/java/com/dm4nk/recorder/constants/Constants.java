package com.dm4nk.recorder.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String VIEWS_TABLE = "views";
    public static final String ACTIONS_TABLE = "actions";

    public static final String CREATE_ACTIONS_TABLE = "create table actions(user_id String, action String, event_time DateTime) engine=ReplacingMergeTree(event_time) order by button";
    public static final String CREATE_VIEWS_TABLE = "create table views(user_name String, page_visited_id UInt8, views UInt8, sign Int8) engine=CollapsingMergeTree(sign) order by (user_name, page_visited_id)";

    public static final String VIEW_ACTIONS_SQL = "select action, event_time from :tableName where user_id = :userId limit 1 by action";

    public static final String VIEW_VIEWS_BY_PAGE_ID_SQL = "select user_id, sum(views * sign) as views " +
            "from :tableName where user_id = :userId and page_visited_id = :pageVisitedId group by user_id HAVING sum(sign) > 0";
    public static final String VIEW_VIEWS_SQL = "select user_id, page_visited_id, sum(views * sign) as views " +
            "from :tableName where user_name = :userName group by user_name, page_visited_id HAVING sum(sign) > 0";

    public static final String DELETE_BY_USER_NAME_SQL = "delete from :tableName where user_id = :userId";
    public static final String OPTIMIZE_TABLE = "optimize table :tableName final";
    public static final String DROP_TABLE_SQL = "drop table if exists :tableName";
}
