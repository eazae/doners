package com.doners.donersbackend.application.dto.request.epilogue;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("EpilogueChangePatchDTO")
@NoArgsConstructor
@Getter
public class EpilogueChangePatchDTO {

    @NotBlank
    @NotNull
    @ApiModelProperty(name="에필로그 ID")
    private String epilogueId;

    @NotBlank
    @NotNull
    @ApiModelProperty(name="에필로그 제목")
    private String epilogueTitle;

    @NotBlank
    @NotNull
    @ApiModelProperty(name="에필로그 내용")
    private String epilogueDescription;
}