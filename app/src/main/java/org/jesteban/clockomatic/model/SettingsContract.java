package org.jesteban.clockomatic.model;





public interface  SettingsContract {
    String getSetting(String key);

    UndoAction setSetting(String key, String value);

    boolean existsSetting(String key);

    UndoAction removeSetting(String key);

}
