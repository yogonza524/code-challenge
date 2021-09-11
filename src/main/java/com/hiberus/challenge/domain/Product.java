package com.hiberus.challenge.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Product model")
public class Product {

  @NotNull
  @ApiModelProperty(value = "ID of product")
  private String id;

  @ApiModelProperty(value = "Name of product")
  private String name;

  @ApiModelProperty(value = "Description of product")
  private String description;
}
