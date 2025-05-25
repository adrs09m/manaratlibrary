package com.manarat.manaratlibrary;

import java.io.Serializable;
import java.util.Date;

public class BorrowRequest implements Serializable {
    private String bookId;
    private String userId;
    private String userName;
    private String userEmail;
    private String bookTitle;
    private Date requestDate;
    private Date approvalDate;
    private Date rejectionDate;
    private Date returnDate; // إضافة حقل تاريخ الإرجاع
    private String approvedBy;
    private String status;
    private Integer borrowDuration; // إضافة حقل مدة الاستعارة
    private String borrowerRating; // إضافة حقل تقييم المستعير
    private String returnStatus; // إضافة حقل حالة الإرجاع

    public BorrowRequest() {
    }

    // المُنشئ الأصلي بسبعة معاملات
    public BorrowRequest(String bookId, String userId, String userName, String userEmail, String bookTitle, Date requestDate, String status) {
        this.bookId = bookId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.bookTitle = bookTitle;
        this.requestDate = requestDate;
        this.status = status;
    }

    // المُنشئ الجديد بثمانية معاملات (يتضمن borrowDuration)
    public BorrowRequest(String bookId, String userId, String userName, String userEmail, String bookTitle, Date requestDate, String status, Integer borrowDuration) {
        this.bookId = bookId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.bookTitle = bookTitle;
        this.requestDate = requestDate;
        this.status = status;
        this.borrowDuration = borrowDuration;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Date getRejectionDate() {
        return rejectionDate;
    }

    public void setRejectionDate(Date rejectionDate) {
        this.rejectionDate = rejectionDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBorrowDuration() {
        return borrowDuration;
    }

    public void setBorrowDuration(Integer borrowDuration) {
        this.borrowDuration = borrowDuration;
    }

    public String getBorrowerRating() {
        return borrowerRating;
    }

    public void setBorrowerRating(String borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }
}