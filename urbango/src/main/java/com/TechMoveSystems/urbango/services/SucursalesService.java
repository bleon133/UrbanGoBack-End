package com.TechMoveSystems.urbango.services;

import com.TechMoveSystems.urbango.admin.dto.BranchDtos.*;

import java.util.List;

public interface SucursalesService {
    List<BranchSummary> list();
    BranchDetail get(Integer id);
    BranchDetail create(CreateOrUpdateBranchRequest request, String photoPath);
    BranchDetail update(Integer id, CreateOrUpdateBranchRequest request, String photoPath);
    void delete(Integer id);
}
