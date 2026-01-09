package com.fulfilment.application.monolith.warehouses.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

  private Long wareHouseId;
  // unique identifier
  private String businessUnitCode;

  private String location;

  private Integer capacity;

  private Integer stock;

  private ZonedDateTime creationAt;

  private ZonedDateTime archivedAt;
}
