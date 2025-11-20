package com.TechMoveSystems.urbango.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceResponse {
    private String id;
    private String initPoint;
    private String sandboxInitPoint;
}
