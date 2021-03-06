/**
 * Copyright 2014 www.migratebird.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.migratebird.maven.plugin;

import com.migratebird.launch.task.ClearDatabaseTask;
import com.migratebird.launch.task.MigrateBirdDatabase;
import com.migratebird.launch.task.MigrateBirdTask;

import java.util.List;

/**
 * Task that removes all database items like tables, views etc from the database and empties the MIGRATEBIRD_SCRIPTS table.
 *
* @goal clearDatabase
 */
public class ClearDatabaseMojo extends BaseDatabaseMojo {

    @Override
    protected MigrateBirdTask createMigrateBirdTask(List<MigrateBirdDatabase> migrateBirdDatabases) {
        return new ClearDatabaseTask(migrateBirdDatabases);
    }
}
