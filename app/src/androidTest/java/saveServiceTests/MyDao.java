package saveServiceTests;

import com.shuttl.location_pings.data.dao.GPSLocationsDao;
import com.shuttl.location_pings.data.model.entity.GPSLocation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyDao implements GPSLocationsDao {
    @Override
    public void addLocation(@NotNull GPSLocation location) {

    }

    @NotNull
    @Override
    public List<GPSLocation> locations() {
        return null;
    }

    @Override
    public List<GPSLocation> getLimitedLocations(int entries) {
        return null;
    }

    @NotNull
    @Override
    public List<GPSLocation> getBatchLocations(@NotNull String lastTime) {
        return null;
    }

    @Override
    public void clearLocations() {

    }

    @Override
    public void deleteOldestLocation() {

    }

    @Override
    public int getRowsCount() {
        return 0;
    }

    @Override
    public void deleteEntries(@NotNull String lastTime) {

    }
}
