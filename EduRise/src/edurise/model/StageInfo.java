package edurise.model;

/** What the dashboard needs to know about one stage of a topic. */
public record StageInfo(int stage, int bestPercent, boolean unlocked) { }
