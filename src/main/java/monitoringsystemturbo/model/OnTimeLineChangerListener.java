package monitoringsystemturbo.model;

import monitoringsystemturbo.model.timeline.Period;

import java.util.List;

public interface OnTimeLineChangerListener {
    void onTimelineChange(List<Period> periods, String appName);
}
