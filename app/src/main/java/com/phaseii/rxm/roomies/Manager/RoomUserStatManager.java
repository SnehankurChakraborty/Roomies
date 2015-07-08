package com.phaseii.rxm.roomies.Manager;

import android.content.Context;

import com.phaseii.rxm.roomies.dao.RoomUserStatDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomUserStatData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 8/23/2015.
 */
public class RoomUserStatManager {

    private boolean isDetailStored;
    private RoomiesDao roomiesDao;
    private Context mContext;

    public RoomUserStatManager(Context mContext) {
        this.mContext = mContext;
        roomiesDao = new RoomUserStatDaoImpl(mContext);
        isDetailStored = false;
    }

    public boolean storeRoomDetails(String username) {
        isDetailStored = false;
        Map<ServiceParam, Object> paramMap = new HashMap<>();

        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        projectionParams.add(QueryParam.USERALIAS);
        projectionParams.add(QueryParam.ROOMID);
        projectionParams.add(QueryParam.ROOM_ALIAS);
        projectionParams.add(QueryParam.NO_OF_PERSONS);
        projectionParams.add(QueryParam.MONTH_YEAR);
        projectionParams.add(QueryParam.RENT_MARGIN);
        projectionParams.add(QueryParam.MAID_MARGIN);
        projectionParams.add(QueryParam.ELECTRICITY_MARGIN);
        projectionParams.add(QueryParam.MISCELLANEOUS_MARGIN);
        projectionParams.add(QueryParam.TOTAL);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        selectionParams.add(QueryParam.USERNAME);
        paramMap.put(ServiceParam.SELECTION, selectionParams);

        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(username);
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        List<RoomUserStatData> roomUserStatDataList = (List<RoomUserStatData>) roomiesDao.getDetails(
                paramMap);

        if (roomUserStatDataList.size() > 0) {
            RoomiesHelper.cacheDBtoPreferences(mContext, roomUserStatDataList.get(0), null, null,
                    null, false);
            isDetailStored = true;
        }

        return isDetailStored;
    }
}
