package gov.ca.emsa.pulse.common.domain.stats;

public class RequestStatistics {
	private Long requestCount;
	private Long requestSuccessCount;
	private Long requestFailureCount;
	private Long requestCancelledCount;
	private Long requestAvgCompletionSeconds;
	private Long requestSuccessAvgCompletionSeconds;
	private Long requestFailureAvgCompletionSeconds;
	private Long requestCancelledAvgCompletionSeconds;
	
	public Long getRequestCount() {
		return requestCount;
	}
	public void setRequestCount(Long requestCount) {
		this.requestCount = requestCount;
	}
	public Long getRequestSuccessCount() {
		return requestSuccessCount;
	}
	public void setRequestSuccessCount(Long requestSuccessCount) {
		this.requestSuccessCount = requestSuccessCount;
	}
	public Long getRequestFailureCount() {
		return requestFailureCount;
	}
	public void setRequestFailureCount(Long requestFailureCount) {
		this.requestFailureCount = requestFailureCount;
	}
	public Long getRequestCancelledCount() {
		return requestCancelledCount;
	}
	public void setRequestCancelledCount(Long requestCancelledCount) {
		this.requestCancelledCount = requestCancelledCount;
	}
	public Long getRequestAvgCompletionSeconds() {
		return requestAvgCompletionSeconds;
	}
	public void setRequestAvgCompletionSeconds(Long requestAvgCompletionSeconds) {
		this.requestAvgCompletionSeconds = requestAvgCompletionSeconds;
	}
	public Long getRequestSuccessAvgCompletionSeconds() {
		return requestSuccessAvgCompletionSeconds;
	}
	public void setRequestSuccessAvgCompletionSeconds(Long requestSuccessAvgCompletionSeconds) {
		this.requestSuccessAvgCompletionSeconds = requestSuccessAvgCompletionSeconds;
	}
	public Long getRequestFailureAvgCompletionSeconds() {
		return requestFailureAvgCompletionSeconds;
	}
	public void setRequestFailureAvgCompletionSeconds(Long requestFailureAvgCompletionSeconds) {
		this.requestFailureAvgCompletionSeconds = requestFailureAvgCompletionSeconds;
	}
	public Long getRequestCancelledAvgCompletionSeconds() {
		return requestCancelledAvgCompletionSeconds;
	}
	public void setRequestCancelledAvgCompletionSeconds(Long requestCancelledAvgCompletionSeconds) {
		this.requestCancelledAvgCompletionSeconds = requestCancelledAvgCompletionSeconds;
	}
	
}
