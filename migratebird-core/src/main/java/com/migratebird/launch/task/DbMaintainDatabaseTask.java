/**
 * Copyright 2014 Turgay Kivrak
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
package com.migratebird.launch.task;

import java.util.List;

/**
 * Base DbMaintain task
 *
*/
public abstract class DbMaintainDatabaseTask extends DbMaintainTask {

    protected List<? extends DbMaintainDatabase> databases;


    protected DbMaintainDatabaseTask() {
    }

    protected DbMaintainDatabaseTask(List<DbMaintainDatabase> databases) {
        this.databases = databases;
    }


    public void setDatabases(List<? extends DbMaintainDatabase> databases) {
        this.databases = databases;
    }
}