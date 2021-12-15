package org.jesteban.clockomatic.model.work_schedule.conditionals;


import org.jesteban.clockomatic.helpers.InfoDayEntry;

import java.util.List;

public interface ConditionalContract {
    List<InfoDayEntry.PairedEntry> apply(InfoDayEntry.PairedEntry paired);
}
