/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package nz.co.k2.k2e.data.local.db;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;

public interface DbHelper {
// TODO Replace with Entities
    // WFM
    Observable<List<WfmJob>> getAllWfmJobs();
    Single<WfmJob> getWfmJobByNumber(String jobNumber);
    Single<WfmJob> getWfmJobById(Long id);
    Single<Long> saveAllWfmJobs(List<WfmJob> wfmJobs);
    Single<Long> insertWfmJob(WfmJob wfmJob);
    Single<Boolean> isWfmListEmpty();

    // JOBS
    Observable<List<BaseJob>> getAllJobs();
    Single<Long> saveAllJobs(List<BaseJob> jobs);
    Single<Long> insertJob(BaseJob job);
    Single<BaseJob> getJobByJobNumber(String jobNumber);
    Single<BaseJob> getJobByUuid(String uuid);
    Single<Boolean> isJobListEmpty();

}
