# RailWatchProxy
[![Build Status](https://travis-ci.org/Cyanelix/railwatchproxy.svg?branch=master)](https://travis-ci.org/Cyanelix/railwatchproxy) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.cyanelix.railwatch:proxy)](https://sonarqube.com/dashboard/index/com.cyanelix.railwatch:proxy) [![Quality Gate](https://sonarqube.com/api/badges/measure?key=com.cyanelix.railwatch:proxy&metric=coverage)](https://sonarqube.com/dashboard/index/com.cyanelix.railwatch:proxy) [![Quality Gate](https://sonarqube.com/api/badges/measure?key=com.cyanelix.railwatch:proxy&metric=code_smells)](https://sonarqube.com/dashboard/index/com.cyanelix.railwatch:proxy)


A simple project to check train times on a schedule, and report changes to estimated departure times via Firebase to an Android app.

Functionality is currently limited to registering an interest in direct trains between two stations between two times, for example station A to station B between 07:30 and 09:00, and notifications are sent whenever Nation Rail Enquiries' live departure board updates.

