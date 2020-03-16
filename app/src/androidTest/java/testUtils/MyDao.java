package testUtils;


import androidx.room.Dao;
import androidx.room.Query;

import com.shuttl.location_pings.data.model.entity.GPSLocation;

import java.util.List;

@Dao
public interface MyDao {

    @Query("SELECT * FROM gps_locations LIMIT :numberOfEnteries")
    public List<GPSLocation> loadAllLocations(int numberOfEnteries);

}