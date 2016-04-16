/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.database.model;

import java.util.List;

/**
 * Created by Snehankur on 11/8/2015.
 */
public class Room {
    private RoomDetails roomDetails;
    private RoomStats roomStats;
    private List<UserDetails> users;
    private List<RoomExpenses> roomExpenses;

    public RoomDetails getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(RoomDetails roomDetails) {
        this.roomDetails = roomDetails;
    }

    public RoomStats getRoomStats() {
        return roomStats;
    }

    public void setRoomStats(RoomStats roomStats) {
        this.roomStats = roomStats;
    }

    public List<UserDetails> getUsers() {
        return users;
    }

    public void setUsers(List<UserDetails> users) {
        this.users = users;
    }

    public List<RoomExpenses> getRoomExpenses() {
        return roomExpenses;
    }

    public void setRoomExpenses(List<RoomExpenses> roomExpenses) {
        this.roomExpenses = roomExpenses;
    }
}
