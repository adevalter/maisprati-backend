package com.petshop.web.dto;

import java.util.List;

public class DashboardResponse {

    private long todayCount;
    private long inProgressCount;
    private long waitingCount;
    private long homeVisitCount;
    private long paidTodayCents;
    private long pendingPaymentCount;
    private List<AppointmentResponse> todayAppointments;
    private List<AppointmentResponse> inProgress;
    private List<UpcomingItemResponse> upcomingDue;

    public DashboardResponse(
            long todayCount,
            long inProgressCount,
            long waitingCount,
            long homeVisitCount,
            long paidTodayCents,
            long pendingPaymentCount,
            List<AppointmentResponse> todayAppointments,
            List<AppointmentResponse> inProgress,
            List<UpcomingItemResponse> upcomingDue) {
        this.todayCount = todayCount;
        this.inProgressCount = inProgressCount;
        this.waitingCount = waitingCount;
        this.homeVisitCount = homeVisitCount;
        this.paidTodayCents = paidTodayCents;
        this.pendingPaymentCount = pendingPaymentCount;
        this.todayAppointments = todayAppointments;
        this.inProgress = inProgress;
        this.upcomingDue = upcomingDue;
    }

    public long getTodayCount() { return todayCount; }
    public long getInProgressCount() { return inProgressCount; }
    public long getWaitingCount() { return waitingCount; }
    public long getHomeVisitCount() { return homeVisitCount; }
    public long getPaidTodayCents() { return paidTodayCents; }
    public long getPendingPaymentCount() { return pendingPaymentCount; }
    public List<AppointmentResponse> getTodayAppointments() { return todayAppointments; }
    public List<AppointmentResponse> getInProgress() { return inProgress; }
    public List<UpcomingItemResponse> getUpcomingDue() { return upcomingDue; }
}
