package me.dolia.lab.microserviceslab.book;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.scheduling.TriggerContext;

import java.time.Clock;
import java.util.Date;

@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class MyTrigger {

    private final long startTimeMs;
    private final int intervalMs;
    private final int times;
    private final Clock clock;
    private final Integer overlapMs;

    public Date nextExecutionTime(TriggerContext ignore) {
        var now = clock.millis();
        var calculatedTriggerTimeMs = startTimeMs;
        var executionCnt = 0;
        while (now > calculatedTriggerTimeMs && hasMoreExecutions(executionCnt)){
            executionCnt++;
            calculatedTriggerTimeMs = calculateNextTriggerTime(executionCnt);
        }
        if (executionCnt >= times) {
            return null;
        }
        var nextExecutionTime = new Date(calculatedTriggerTimeMs);
        return nextExecutionTime;
    }

    private long calculateNextTriggerTime(int executionCnt) {
        var nextTriggerTime = startTimeMs + intervalMs * executionCnt;
        if (isApplicableForOverlapping()){
            return nextTriggerTime - overlapMs;
        }
        return nextTriggerTime;
    }

    private boolean isApplicableForOverlapping() {
        return overlapMs != null;
    }

    private boolean hasMoreExecutions(int executionCnt) {
        return executionCnt < times;
    }
}