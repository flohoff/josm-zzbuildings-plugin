package org.openstreetmap.josm.plugins.plbuildings;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Rule;
import org.junit.Test;
import org.openstreetmap.josm.command.ChangePropertyCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.plugins.plbuildings.actions.BuildingsImportAction;
import org.openstreetmap.josm.plugins.plbuildings.validators.BuildingsWayValidator;
import org.openstreetmap.josm.testutils.JOSMTestRules;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openstreetmap.josm.plugins.plbuildings.ImportUtils.importOsmFile;

public class DuplicateImportTest {
    @Rule
    public JOSMTestRules rules = new JOSMTestRules().main();

    static {
        new MockUp<BuildingsImportAction>(){
            @Mock
            List<Command> updateTags(Way newBuilding, Way selectedBuilding) {
                return Collections.singletonList(
                    new ChangePropertyCommand(
                        selectedBuilding.getDataSet(),
                        Collections.singletonList(selectedBuilding),
                        newBuilding.getKeys()
                ));
            }
        };

    }
    @Test
    public void testSimpleDuplicateCheckNotDuplicate(){
        new MockUp<BuildingsImportAction>(){
            @Mock
            public DataSet getBuildingsAtCurrentLocation(){
                return importOsmFile(new File("test/data/duplicate_import/import_building.osm"), "");
            }
        };

        DataSet ds = importOsmFile(
            new File("test/data/duplicate_import/simple_replace_base.osm"),
            "");
        assertNotNull(ds);

        Way buildingToReplace = (Way) ds.getWays().stream().filter(way -> way.hasKey("building", "yes")).toArray()[0];
        ds.setSelected(buildingToReplace);

        BuildingsImportAction.performBuildingImport(ds);

        assertEquals(ds.getWays().stream().filter(BuildingsWayValidator::isBuildingWayValid).count(), 1);
    }

    @Test
    public void testSimpleDuplicateCheckDuplicateAllNodesEqualAndSameTags(){
        new MockUp<BuildingsImportAction>(){
            @Mock
            public DataSet getBuildingsAtCurrentLocation(){
                return importOsmFile(new File("test/data/duplicate_import/import_building.osm"), "");
            }
        };

        DataSet ds = importOsmFile(
            new File("test/data/duplicate_import/simple_duplicate_base.osm"),
            "");
        assertNotNull(ds);

        Way buildingToReplace = (Way) ds.getWays().stream().filter(way -> way.hasKey("building", "house")).toArray()[0];
        ds.setSelected(buildingToReplace);

        BuildingsImportAction.performBuildingImport(ds);

        assertEquals(ds.getWays().size(), 1);
        assertEquals(ds.getWays().stream().filter(way -> way.hasKey("building", "house")).count(), 1);
    }

    @Test
    public void testSimpleDuplicateCheckNotDuplicateAllNodesEqualAndDifferentTags(){
        new MockUp<BuildingsImportAction>(){
            @Mock
            public DataSet getBuildingsAtCurrentLocation(){
                return importOsmFile(new File("test/data/duplicate_import/import_building.osm"), "");
            }
        };

        DataSet ds = importOsmFile(
            new File("test/data/duplicate_import/simple_duplicate_different_tags_base.osm"),
            "");
        assertNotNull(ds);

        Way buildingToReplace = (Way) ds.getWays().stream()
            .filter(way -> way.hasKey("building","house"))
            .toArray()[0];
        ds.setSelected(buildingToReplace);

        BuildingsImportAction.performBuildingImport(ds);

        assertEquals(ds.getWays().size(), 1);
        assertEquals(ds.getWays().stream().filter(way -> way.hasKey("building", "detached")).count(), 1);
    }

    @Test
    public void testDuplicateBaseMoreNodesImportBuildingAllNodesEqual(){
        new MockUp<BuildingsImportAction>(){
            @Mock
            public DataSet getBuildingsAtCurrentLocation(){
                return importOsmFile(new File("test/data/duplicate_import/import_building.osm"), "");
            }
        };

        DataSet ds = importOsmFile(
            new File("test/data/duplicate_import/duplicate_more_nodes_base.osm"),
            "");
        assertNotNull(ds);

        Way buildingToReplace = (Way) ds.getWays().stream().filter(way -> way.hasKey("building", "yes")).toArray()[0];
        ds.setSelected(buildingToReplace);

        BuildingsImportAction.performBuildingImport(ds);

        assertEquals(ds.getWays().stream().filter(way -> way.hasKey("building", "house")).count(), 1);
    }
}