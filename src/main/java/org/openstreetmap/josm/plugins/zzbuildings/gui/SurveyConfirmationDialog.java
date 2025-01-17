package org.openstreetmap.josm.plugins.zzbuildings.gui;

import javax.swing.*;

import static org.openstreetmap.josm.tools.I18n.tr;

/**
 * Creates confirmation dialog to proceed with import data which contains "survey" value in tags.
 * It should prevent most accidental data breaking
 */
public class SurveyConfirmationDialog {

    /**
     * Shows confirmation dialog
     * @return true if user clicks "yes", else false ("no" button/canceled)
     */
    public static boolean show(){
        int result = JOptionPane.showConfirmDialog(
            null,
            tr("Detected \"survey\" value in tags.\nAre you sure you want to proceed updating this object?"),
            tr("Building import confirmation"),
            JOptionPane.YES_NO_OPTION
        );
        return result == JOptionPane.OK_OPTION;
    }
}
