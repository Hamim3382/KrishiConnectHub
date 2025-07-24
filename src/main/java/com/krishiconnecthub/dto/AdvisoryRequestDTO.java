package com.krishiconnecthub.dto;

public class AdvisoryRequestDTO {
    private String cropType;
    private String soilType;
    private String season;
    private String problemDescription;
    private Long farmerId;

    // Getters and Setters
    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }
    public String getSoilType() { return soilType; }
    public void setSoilType(String soilType) { this.soilType = soilType; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }
    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }
}