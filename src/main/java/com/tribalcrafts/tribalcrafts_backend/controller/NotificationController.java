package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.entity.Notification;
import com.tribalcrafts.tribalcrafts_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5174")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public List<Notification> getNotifications(@PathVariable Long userId) {
        System.out.println("Fetching notifications for user: " + userId);
        List<Notification> notifications = notificationService.getNotificationsByUser(userId);
        System.out.println("Found " + notifications.size() + " notifications");
        return notifications;
    }

    @GetMapping("/{userId}/unread/count")
    public Map<String, Long> getUnreadCount(@PathVariable Long userId) {
        Map<String, Long> response = new HashMap<>();
        response.put("count", notificationService.getUnreadCount(userId));
        return response;
    }

    @PutMapping("/{notificationId}/read")
    public Notification markAsRead(@PathVariable Long notificationId) {
        return notificationService.markAsRead(notificationId);
    }

    @PutMapping("/{userId}/read-all")
    public Map<String, String> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "All notifications marked as read");
        return response;
    }

    @DeleteMapping("/{notificationId}")
    public Map<String, String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification deleted successfully");
        return response;
    }

    @DeleteMapping("/{userId}/clear")
    public Map<String, String> clearAllNotifications(@PathVariable Long userId) {
        notificationService.clearAllNotifications(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "All notifications cleared");
        return response;
    }

    @PostMapping("/{userId}")
    public Notification addNotification(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        return notificationService.addNotification(
            userId,
            request.get("type"),
            request.get("title"),
            request.get("message"),
            request.get("link")
        );
    }
}