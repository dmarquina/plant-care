package com.dmarquina.plantcare.dto.response;

import lombok.Data;

@Data
public class VerifyPrivilegeResponse {
  private Boolean hasPrivilege;

  public VerifyPrivilegeResponse(Boolean hasPrivilege) {
    this.hasPrivilege = hasPrivilege;
  }
}
