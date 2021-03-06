package org.cleverframe.quartz.vo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-7-29 14:41 <br/>
 */
public class QuartzTriggers implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Scheduler名称
     */
    private String schedName;

    /**
     * Trigger key
     */
    private String triggerName;

    /**
     * Trigger group名称
     */
    private String triggerGroup;

    /**
     * Job key
     */
    private String jobName;

    /**
     * Job group 名称
     */
    private String jobGroup;

    /**
     * Trigger 描述， .withDescription()方法传入的string
     */
    private String description;

    /**
     * 下一次触发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date nextFireTime;

    /**
     * 上一次触发时间，默认-1
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date prevFireTime;

    /**
     * Trigger 优先级，默认5
     */
    private Integer priority;

    /**
     * Trigger状态，PAUSED_BLOCKED:停止_阻塞; PAUSED:停止; WAITING:等待执行; ACQUIRED:已获得; EXECUTING:执行中; COMPLETE:完成; BLOCKED:阻塞; ERROR:错误; DELETED:已删除
     */
    private String triggerState;

    /**
     * Cron 或 Simple
     */
    private String triggerType;

    /**
     * Trigger开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date startTime;

    /**
     * Trigger结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endTime;

    /**
     * Trigger关联的Calendar name
     */
    private String calendarName;

    /**
     * misfire规则id
     */
    private Integer misfireInstr;

    /**
     * 存储Trigger的JobDataMap等
     */
    private Map<String, Object> jobData;

    /*===================== SimpleTrigger =====================*/
    /**
     * 需要重复次数
     */
    private Integer repeatCount;

    /**
     * 重复触发时间间隔(毫秒)
     */
    private Long repeatInterval;

    /**
     * 已经触发的次数
     */
    private Integer timesTriggered;

    /*===================== CronTrigger =====================*/
    /**
     * cron表达式
     */
    private String cronEx;

    /**
     * 时区ID
     */
    private String timeZoneId;

    /*--------------------------------------------------------------
     *          getter、setter
     * -------------------------------------------------------------*/

    /**
     * 获取 Scheduler名称
     */
    public String getSchedName() {
        return schedName;
    }

    /**
     * 设置 Scheduler名称
     */
    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    /**
     * 获取 Trigger key
     */
    public String getTriggerName() {
        return triggerName;
    }

    /**
     * 设置 Trigger key
     */
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    /**
     * 获取 Trigger group名称
     */
    public String getTriggerGroup() {
        return triggerGroup;
    }

    /**
     * 设置 Trigger group名称
     */
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    /**
     * 获取 Job key
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置 Job key
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取 Job group 名称
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * 设置 Job group 名称
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * 获取 Trigger 描述， .withDescription()方法传入的string
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置 Trigger 描述， .withDescription()方法传入的string
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取 下一次触发时间
     */
    public Date getNextFireTime() {
        return nextFireTime;
    }

    /**
     * 设置 下一次触发时间
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    /**
     * 获取 上一次触发时间，默认-1
     */
    public Date getPrevFireTime() {
        return prevFireTime;
    }

    /**
     * 设置 上一次触发时间，默认-1
     */
    public void setPrevFireTime(Date prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    /**
     * 获取 Trigger 优先级，默认5
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置 Trigger 优先级，默认5
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 获取 Trigger状态，PAUSED_BLOCKED:停止_阻塞; PAUSED:停止; WAITING:等待执行; ACQUIRED:已获得; EXECUTING:执行中; COMPLETE:完成; BLOCKED:阻塞; ERROR:错误; DELETED:已删除
     */
    public String getTriggerState() {
        return triggerState;
    }

    /**
     * 设置 Trigger状态，PAUSED_BLOCKED:停止_阻塞; PAUSED:停止; WAITING:等待执行; ACQUIRED:已获得; EXECUTING:执行中; COMPLETE:完成; BLOCKED:阻塞; ERROR:错误; DELETED:已删除
     */
    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    /**
     * 获取 Cron 或 Simple
     */
    public String getTriggerType() {
        return triggerType;
    }

    /**
     * 设置 Cron 或 Simple
     */
    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    /**
     * 获取 Trigger开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置 Trigger开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取 Trigger结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置 Trigger结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取 Trigger关联的Calendar name
     */
    public String getCalendarName() {
        return calendarName;
    }

    /**
     * 设置 Trigger关联的Calendar name
     */
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    /**
     * 获取 misfire规则id
     */
    public Integer getMisfireInstr() {
        return misfireInstr;
    }

    /**
     * 设置 misfire规则id
     */
    public void setMisfireInstr(Integer misfireInstr) {
        this.misfireInstr = misfireInstr;
    }

    /**
     * 获取 存储Trigger的JobDataMap等
     */
    public Map<String, Object> getJobData() {
        return jobData;
    }

    /**
     * 设置 存储Trigger的JobDataMap等
     */
    public void setJobData(Map<String, Object> jobData) {
        this.jobData = jobData;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public Integer getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(Integer timesTriggered) {
        this.timesTriggered = timesTriggered;
    }

    public String getCronEx() {
        return cronEx;
    }

    public void setCronEx(String cronEx) {
        this.cronEx = cronEx;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
}
