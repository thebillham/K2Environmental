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

package nz.co.k2.k2e.di.builder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import nz.co.k2.k2e.ui.jobs.jobmain.CheckFragmentProvider;
import nz.co.k2.k2e.ui.jobs.jobmain.InfoFragmentProvider;
import nz.co.k2.k2e.ui.jobs.jobmain.JobFragmentProvider;
import nz.co.k2.k2e.ui.jobs.jobmain.SamplesFragmentProvider;
import nz.co.k2.k2e.ui.jobs.jobmain.TasksFragmentProvider;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsFragmentProvider;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmFragmentProvider;
import nz.co.k2.k2e.ui.navdrawer.NavDrawerActivity;
import nz.co.k2.k2e.ui.navdrawer.NavDrawerModule;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {
            NavDrawerModule.class,
            WfmFragmentProvider.class,
            JobsFragmentProvider.class,
            JobFragmentProvider.class,
            CheckFragmentProvider.class,
            InfoFragmentProvider.class,
            SamplesFragmentProvider.class,
            TasksFragmentProvider.class
    })
    abstract NavDrawerActivity bindNavDrawerActivity();
//
//    @ContributesAndroidInjector(modules = LoginActivityModule.class)
//    abstract LoginActivity bindLoginActivity();
//
//    @ContributesAndroidInjector(modules = {
//            MainActivityModule.class,
//            AboutFragmentProvider.class,
//            RateUsDialogProvider.class})
//    abstract MainActivity bindMainActivity();
//
//    @ContributesAndroidInjector(modules = SplashActivityModule.class)
//    abstract SplashActivity bindSplashActivity();
}
