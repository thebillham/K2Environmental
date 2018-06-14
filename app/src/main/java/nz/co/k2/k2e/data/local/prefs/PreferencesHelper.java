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

package nz.co.k2.k2e.data.local.prefs;

import java.util.Set;

public interface PreferencesHelper {

    String getCurrentUserEmail();

    void setCurrentUserEmail(String email);

    String getCurrentUserName();

    void setCurrentUserName(String userName);

    String getCurrentUserProfilePicUrl();

    void setCurrentUserProfilePicUrl(String profilePicUrl);

    Set<String> getCurrentUserMyJobNumbers();

    void setCurrentUserMyJobNumbers(Set<String> myJobNumbers);

    void deleteCurrentUserMyJobNumber(String myJobNumber);

    void insertCurrentUserMyJobNumber(String myJobNumber);
}
