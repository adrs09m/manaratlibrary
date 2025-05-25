package com.manarat.manaratlibrary;

import android.net.Uri;

public class StudentArtItem {
    private Uri imageUri;
    private String studentName;
    private String academicYear;
    private String classroom;

    public StudentArtItem(Uri imageUri, String studentName, String academicYear, String classroom) {
        this.imageUri = imageUri;
        this.studentName = studentName;
        this.academicYear = academicYear;
        this.classroom = classroom;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getClassroom() {
        return classroom;
    }
}