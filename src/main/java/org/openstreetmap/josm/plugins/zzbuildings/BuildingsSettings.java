package org.openstreetmap.josm.plugins.zzbuildings;

import org.openstreetmap.josm.data.preferences.DoubleProperty;
import org.openstreetmap.josm.data.preferences.StringProperty;

public class BuildingsSettings {

    public static final DoubleProperty BBOX_OFFSET = new DoubleProperty(
        "zzbuildings.bbox_offset",
        0.0000005
    );
    public static final StringProperty SERVER_URL = new StringProperty(
        "zzbuildings.server_url",
        "https://osm.zz.de/zzbuildings/v3/all/"
    );
    public static final DoubleProperty SEARCH_DISTANCE = new DoubleProperty(
        "zzbuildings.search_distance",
        3.0 // meters
    );

    public static final StringProperty IMPORT_STATS = new StringProperty(
        "zzbuildings.import_stats",
        "e30="  // "{}" (base64) – empty JSON
    );

}
