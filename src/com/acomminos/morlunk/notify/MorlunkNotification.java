package com.acomminos.morlunk.notify;

/**
 * A class that represents the server-side Morlunk Co. notifications.
 * The user will receive a notification when an important event happens. e.g.
 * - User is given Paosos
 * - User is fined Paosos
 * - User is suspended/banned
 * Please, no promotional offers or spam.
 * An AlarmManager will check for the user's notifications, pinging Morlunk.com for data.
 * When clicked, these notifications will bring the user to their account page where they may see what's going on in greater detail.
 * @author andrew
 *
 */
public class MorlunkNotification {
	
	public enum MorlunkNotificationAction {
		ACTION_ACCOUNT,
		ACTION_NONE
	}
	private String notificationTitle;
	private String notificationDescription;
	private MorlunkNotificationAction notificationAction;
	
	public String getNotificationTitle() {
		return notificationTitle;
	}
	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}
	public String getNotificationDescription() {
		return notificationDescription;
	}
	public void setNotificationDescription(String notificationDescription) {
		this.notificationDescription = notificationDescription;
	}
	public MorlunkNotificationAction getNotificationAction() {
		return notificationAction;
	}
	public void setNotificationAction(MorlunkNotificationAction notificationAction) {
		this.notificationAction = notificationAction;
	}
}
