package org.openstreetmap.josm.plugins.zzbuildings;

import org.junit.Rule;
import org.junit.Test;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.osm.AbstractPrimitive;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.plugins.zzbuildings.actions.BuildingsImportAction;
import org.openstreetmap.josm.testutils.JOSMTestRules;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.openstreetmap.josm.plugins.zzbuildings.ImportUtils.importOsmFile;
import static org.openstreetmap.josm.plugins.zzbuildings.validators.BuildingsWayValidator.isBuildingWayValid;

public class ReplaceOldBuildingImportTest {
    @Rule
    public JOSMTestRules rules = new JOSMTestRules().main();

    @Test
    public void testImportBuildingWithReplaceWithOneBuildingIsSelected(){
        DataSet importData = importOsmFile(new File("test/data/replace_building_1.osm"), "");
        assertNotNull(importData);

        Way buildingToImport = (Way) importData.getWays().toArray()[0];
        assertEquals(buildingToImport.getNodesCount() - 1, 4);

        DataSet ds = importOsmFile(new File("test/data/replace_multiple_buildings.osm"), "");
        assertNotNull(ds);


        Way buildingToReplace = (Way) ds.getWays().stream().filter(way -> way.getNodesCount() == 5).toArray()[0];
        ds.setSelected(buildingToReplace);

        BuildingsImportAction.performBuildingImport(ds, importData, buildingToReplace);

        assertEquals(buildingToReplace.getNodesCount() - 1, 4);
        assertTrue(isBuildingWayValid(buildingToReplace));
    }

    @Test
    public void testImportBuildingWithReplaceButMoreThanOneSoNullSoBuildingIsSelectedSoCancelImport(){
        DataSet importData = importOsmFile(new File("test/data/replace_building_1.osm"), "");
        assertNotNull(importData);

        DataSet ds = importOsmFile(new File("test/data/replace_multiple_buildings.osm"), "");
        assertNotNull(ds);
        ds.setSelected(ds.getWays());
        assertTrue(ds.getAllSelected().size() > 1);

        Set<Integer> versions = ds.getWays().stream().map(AbstractPrimitive::getVersion).collect(Collectors.toSet());

        BuildingsImportAction.performBuildingImport(ds, importData, null);

        assertTrue(ds.getWays().stream().allMatch(way -> versions.contains(way.getVersion())));
    }

    @Test
    public void testImportBuildingWithReplaceWithOneBuildingIsSelectedUndoRedo(){
        DataSet importData = importOsmFile(new File("test/data/replace_building_1.osm"), "");
        assertNotNull(importData);

        Way buildingToImport = (Way) importData.getWays().toArray()[0];
        assertEquals(buildingToImport.getNodesCount() - 1, 4);

        DataSet ds = importOsmFile(new File("test/data/replace_multiple_buildings.osm"), "");
        assertNotNull(ds);

        Way buildingToReplace = (Way) ds.getWays().stream().filter(way -> way.getNodesCount() == 5).toArray()[0];
        ds.setSelected(buildingToReplace);

        BuildingsImportAction.performBuildingImport(ds, importData, buildingToReplace);

        assertTrue(isBuildingWayValid(buildingToReplace));
        assertEquals(buildingToReplace.getNodesCount() - 1, 4);

        UndoRedoHandler.getInstance().undo(2);
        assertTrue(isBuildingWayValid(buildingToReplace));
        assertEquals(buildingToReplace.getNodesCount(), 5);

        UndoRedoHandler.getInstance().redo(2);
        assertTrue(isBuildingWayValid(buildingToReplace));
        assertEquals(buildingToReplace.getNodesCount() - 1, 4);
    }
}
