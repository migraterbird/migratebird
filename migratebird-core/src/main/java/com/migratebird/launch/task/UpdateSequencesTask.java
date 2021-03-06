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
package com.migratebird.launch.task;

import static com.migratebird.config.MigratebirdProperties.PROPERTY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE;

import java.util.List;

import com.migratebird.MainFactory;
import com.migratebird.structure.sequence.SequenceUpdater;

/**
 * Task that updates all sequences and identity columns to a minimum value.
 *
*/
public class UpdateSequencesTask extends MigrateBirdDatabaseTask {

    protected Long lowestAcceptableSequenceValue;


    public UpdateSequencesTask() {
    }

    public UpdateSequencesTask(List<MigrateBirdDatabase> taskDatabases, Long lowestAcceptableSequenceValue) {
        super(taskDatabases);
        this.lowestAcceptableSequenceValue = lowestAcceptableSequenceValue;
    }


    @Override
    protected void addTaskConfiguration(TaskConfiguration taskConfiguration) {
        taskConfiguration.addDatabaseConfigurations(databases);
        taskConfiguration.addConfigurationIfSet(PROPERTY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE, lowestAcceptableSequenceValue);
    }

    @Override
    protected boolean doExecute(MainFactory mainFactory) {
        SequenceUpdater sequenceUpdater = mainFactory.createSequenceUpdater();
        sequenceUpdater.updateSequences();
        return true;
    }


    public void setLowestAcceptableSequenceValue(Long lowestAcceptableSequenceValue) {
        this.lowestAcceptableSequenceValue = lowestAcceptableSequenceValue;
    }
}
