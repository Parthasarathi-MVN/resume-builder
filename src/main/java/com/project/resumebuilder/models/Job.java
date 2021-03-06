package com.project.resumebuilder.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String company;
    private String designation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean isCurrentJob;


    //Get more intuition about this @ElementCollection annotation.
    @ElementCollection(targetClass = String.class)
    @CollectionTable(joinColumns = @JoinColumn(name = "job_id"))
    private List<String> responsibilities = new ArrayList<>();


    public List<String> getResponsibilities() {
        return responsibilities;
    }
    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public boolean isCurrentJob() {
        return isCurrentJob;
    }
    public void setCurrentJob(boolean isCurrentJob) {
        this.isCurrentJob = isCurrentJob;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    //custom function for getting only month and year. This is to display only month and year in the resume.
    public String getFormattedStartDate()
    {
        return startDate.format(DateTimeFormatter.ofPattern("MMM-yyyy"));
    }
    public String getFormattedEndDate()
    {
        return endDate.format(DateTimeFormatter.ofPattern("MMM-yyyy"));
    }



    

    
}
