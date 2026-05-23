package com.example.app.common;

public enum Faculty {
    FASS("Faculty of Arts and Social Sciences"), FoS("Faculty of Science"), FCCS("Faculty of Creative and Critical Studies"),
    FHSD("Faculty of Health and Social Development"), SoE("School of Engineering"), FoM("Faculty of Management"), 
    OSE("Okanagan School of Education"), CoGS("College of Graduate Studies"), SMP("Southern Medical Program"),
    SSW("School of Social Work"), NA("none");

    private final String action;

    Faculty(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
