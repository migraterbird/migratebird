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
package com.migratebird.launch.ant;

import com.migratebird.launch.task.MigrateBirdTask;
import com.migratebird.launch.task.UpdateSequencesTask;

/**
 * Task that updates all sequences and identity columns to a minimum value.
 *
*/
public class UpdateSequencesAntTask extends BaseDatabaseAntTask {

    private Long lowestAcceptableSequenceValue;


    @Override
    protected MigrateBirdTask createMigrateBirdTask() {
        return new UpdateSequencesTask(getMigrateBirdDatabases(), lowestAcceptableSequenceValue);
    }


    /**
     * Threshold indicating the minimum value of sequences. If sequences are updated, all sequences having a lower value than this
     * one are set to this value. Defaults to 1000.
     *
     * @param lowestAcceptableSequenceValue The lowest sequence value
     */
    public void setLowestAcceptableSequenceValue(Long lowestAcceptableSequenceValue) {
        this.lowestAcceptableSequenceValue = lowestAcceptableSequenceValue;
    }


}
