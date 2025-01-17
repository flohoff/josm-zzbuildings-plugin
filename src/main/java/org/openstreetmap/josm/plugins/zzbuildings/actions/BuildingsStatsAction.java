package org.openstreetmap.josm.plugins.zzbuildings.actions;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.plugins.zzbuildings.models.BuildingsImportStats;
import org.openstreetmap.josm.plugins.zzbuildings.BuildingsPlugin;
import org.openstreetmap.josm.plugins.zzbuildings.gui.BuildingsImportStatsPanel;
import org.openstreetmap.josm.tools.ImageProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;

import static org.openstreetmap.josm.tools.I18n.tr;

public class BuildingsStatsAction extends JosmAction {

    public static final String DESCRIPTION = tr("Show buildings import stats");
    public static final String TITLE = tr("Buildings import stats");
    public BuildingsStatsAction(){
        super(
            TITLE,
            (ImageProvider) null,
            DESCRIPTION,
            null,
            true,
            String.format("%s:buildings_stats", BuildingsPlugin.info.name),
            false
        );
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        BuildingsImportStats buildingsStats = BuildingsImportStats.getInstance();
        LinkedHashMap<String, String> statsPanelData = new LinkedHashMap<>();
        statsPanelData.put(
            tr("New buildings"),
            Integer.toString(buildingsStats.getImportNewBuildingCounter())
        );
        statsPanelData.put(
            tr("Buildings with full replace"),
            Integer.toString(buildingsStats.getImportWithReplaceCounter())
        );
        statsPanelData.put(
            tr("Buildings with tags update"),
            Integer.toString(buildingsStats.getImportWithTagsUpdateCounter())
        );
        statsPanelData.put(
            tr("Total number of import actions"),
            Integer.toString(buildingsStats.getTotalImportActionCounter())
        );

        JOptionPane.showMessageDialog(
            null,
            new BuildingsImportStatsPanel(statsPanelData),
            TITLE,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
